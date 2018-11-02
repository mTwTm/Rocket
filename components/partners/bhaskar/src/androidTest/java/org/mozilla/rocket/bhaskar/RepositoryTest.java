package org.mozilla.rocket.bhaskar;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.Pair;
import android.util.Log;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mozilla.cachedrequestloader.CachedRequestLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@RunWith(AndroidJUnit4.class)
public class RepositoryTest {

    private static final int SOCKET_TAG = 1234;
    private int count = 0;

    @Test
    public void testLoadAndCache() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Log.e("mTwTm", "Start");
        Repository repository = new Repository(InstrumentationRegistry.getContext(), 521, 30, null, SOCKET_TAG, itemPojoList -> {
            Log.e("mTwTm", Arrays.toString(itemPojoList.toArray()));
            countDownLatch.countDown();
        });
        countDownLatch.await();
    }
}
