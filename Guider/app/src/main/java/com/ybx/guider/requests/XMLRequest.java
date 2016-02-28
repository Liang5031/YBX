package com.ybx.guider.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ybx.guider.responses.XMLResponse;
//import com.ybx.guider.responses.LoginResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.UnsupportedEncodingException;

/**
 * Created by chenlia1 on 2016/2/4.
 */
public class XMLRequest extends Request<XMLResponse> {
    private String mUrl;
    private final Response.Listener<XMLResponse> mListener;

    public XMLRequest(int method, String url, Response.Listener<XMLResponse> listener,
                      Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public XMLRequest(String url, Response.Listener<XMLResponse> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Response<XMLResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            XMLResponse res = new XMLResponse(response);
            return Response.success(res, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(XMLResponse response) {
        mListener.onResponse(response);
    }
}
