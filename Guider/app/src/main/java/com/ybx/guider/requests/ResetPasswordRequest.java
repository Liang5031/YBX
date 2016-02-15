package com.ybx.guider.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ybx.guider.responses.ResetPasswordResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.UnsupportedEncodingException;

/**
 * Created by chenl on 2016/2/15.
 */
public class ResetPasswordRequest extends Request<ResetPasswordResponse> {
    private String mUrl;
    private final Response.Listener<ResetPasswordResponse> mListener;

    public ResetPasswordRequest(int method, String url, Response.Listener<ResetPasswordResponse> listener,
                                Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public ResetPasswordRequest(String url, Response.Listener<ResetPasswordResponse> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Response<ResetPasswordResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            ResetPasswordResponse res = new ResetPasswordResponse(response);
            return Response.success(res, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ResetPasswordResponse response) {
        mListener.onResponse(response);
    }
}
