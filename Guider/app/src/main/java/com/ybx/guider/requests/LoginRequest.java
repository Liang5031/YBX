package com.ybx.guider.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ybx.guider.responses.LoginResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.UnsupportedEncodingException;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class LoginRequest extends Request<LoginResponse> {
    private String mUrl;
    private final Response.Listener<LoginResponse> mListener;

    public LoginRequest(int method, String url, Response.Listener<LoginResponse> listener,
                        Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public LoginRequest(String url, Response.Listener<LoginResponse> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Response<LoginResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            LoginResponse res = new LoginResponse(response);
            return Response.success(res, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(LoginResponse response) {
        mListener.onResponse(response);
    }
}
