package org.mozilla.rocket.bhaskar;

public class ItemPojo {
    public String id;
    public String language;
    public String title;
    public String category;
    public String subcategory;
    public String keywords;
    public String description;
    public String[] tags;
    public String detailUrl;
    public String articleFrom;
    public long publishTime;
    public String coverPic;
    public String province;
    public String city;
    public String summary;

    @Override
    public String toString() {
        return id;
    }
}
