package org.mozilla.rocket.bhaskar;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.cachedrequestloader.CachedRequestLoader;
import org.mozilla.cachedrequestloader.ResponseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Repository {
    private CachedRequestLoader mainCachedRequestLoader;
    private static final String SUBSCRIPTION_KEY_NAME = "bhaskar";
    private static final String SUBSCRIPTION_URL = "http://appfeed.bhaskar.com/webfeed/apidata/firefox?pageSize=%d&channel_slno=%d&pageNumber=%d";
    private static final int FIRST_PAGE = 1;
    private int channel;
    private int pageSize;
    private OnDataChangedListener onDataChangedListener;
    private List<ItemPojo> itemPojoList;

    public Repository(Context context, int channel, int pageSize, String userAgent, int socketTag, OnDataChangedListener onDataChangedListener) {
        this.channel = channel;
        this.pageSize = pageSize;
        itemPojoList = new ArrayList<>();
        mainCachedRequestLoader = new CachedRequestLoader(context, getSubscriptionKey(FIRST_PAGE), getSubscriptionUrl(FIRST_PAGE), userAgent, socketTag);
        this.onDataChangedListener = onDataChangedListener;
        ResponseData responseData = mainCachedRequestLoader.getStringLiveData();
        responseData.observeForever(integerStringPair -> {
            if (integerStringPair == null) {
                return;
            }
            if (integerStringPair.first != null && ResponseData.SOURCE_NETWORK == integerStringPair.first) {
                try {
                    itemPojoList.addAll(parseData(integerStringPair.second));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onDataChangedListener.onDataChanged(itemPojoList);
            }
        });
    }

    private List<ItemPojo> parseData(String response) throws JSONException {
        List<ItemPojo> ret = new ArrayList<>();
        // TODO: 11/2/18 It takes 0.1s - 0.2s to create JsonObject, do we want to improve this? 
        JSONObject root = new JSONObject(response);
        JSONObject data = root.getJSONObject("data");
        JSONArray rows = data.getJSONArray("rows");
        for (int i = 0 ; i < rows.length() ; i++) {
            JSONObject row = rows.getJSONObject(i);
            ItemPojo itemPojo = new ItemPojo();
            itemPojo.id = row.getString("id");
            itemPojo.articleFrom = row.getString("articleFrom");
            itemPojo.category = row.getString("category");
            itemPojo.city = row.getString("city");
            ret.add(itemPojo);
        }
        return ret;
    }

    private String getSubscriptionKey(int page) {
        return SUBSCRIPTION_KEY_NAME + pageSize + "/" + page;
    }

    private String getSubscriptionUrl(int pageNumber) {
        return String.format(Locale.US, SUBSCRIPTION_URL, pageSize, channel, pageNumber);
    }

    public interface OnDataChangedListener {
        void onDataChanged(List<ItemPojo> itemPojoList);
    }
}