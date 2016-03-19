package com.ybx.guider.requests;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ybx.guider.responses.XMLResponse;
import com.ybx.guider.utils.URLUtils;
//import com.ybx.guider.responses.LoginResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.UnsupportedEncodingException;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class XMLRequest<T extends XMLResponse> extends Request<T> {
    private String mUrl;
    private final Response.Listener<T> mListener;
    private T mXmlResponse;
    public XMLRequest(int method, String url, Response.Listener<T> listener,
                      Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public XMLRequest(String url, Response.Listener<T> listener, Response.ErrorListener errorListener, T response) {
        this(Method.GET, url, listener, errorListener);
        mXmlResponse = response;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            mXmlResponse.parse(response);
            return Response.success(mXmlResponse, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}
