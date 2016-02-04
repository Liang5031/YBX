package com.ybx.guider.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ybx.guider.responses.LoginResponse;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class LoginRequest extends Request<LoginResponse> {

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
//            String xmlString = new String(response.data,
//                    HttpHeaderParser.parseCharset(response.headers));
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            XmlPullParser xmlPullParser = factory.newPullParser();
//            xmlPullParser.setInput(new StringReader(xmlString));
//            return Response.success(xmlPullParser, HttpHeaderParser.parseCacheHeaders(response));

            LoginResponse res = new LoginResponse(response);
            return Response.success(res, HttpHeaderParser.parseCacheHeaders(response));

        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(LoginResponse response) {
        mListener.onResponse(response);
    }
}
