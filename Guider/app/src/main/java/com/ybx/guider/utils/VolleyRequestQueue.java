package com.ybx.guider.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by chenl on 2016/2/3.
 */
public class VolleyRequestQueue {
    private static VolleyRequestQueue mInstance = null;
    private RequestQueue mRequestQueue;
    private Network mNetwork;
    private Cache mCache;
    private static Context mCtx;

    private VolleyRequestQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
//            /* Default cache size is 5M */
//            mCache = new DiskBasedCache(mCtx.getApplicationContext().getCacheDir());
//            /* Set up the network to use HttpURLConnection as the HTTP client. */
//            mNetwork = new BasicNetwork(new HurlStack());
//            /* Instantiate the RequestQueue with the cache and network. */
//            mRequestQueue = new RequestQueue(mCache, mNetwork);
//            /* Start the queue */
//            mRequestQueue.start();

            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_OUTPUT_STREAM, " Request URL: " + req.getUrl());
        }
        getRequestQueue().add(req);

    }
}
