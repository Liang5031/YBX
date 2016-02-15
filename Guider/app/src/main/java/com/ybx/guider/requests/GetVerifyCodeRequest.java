package com.ybx.guider.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ybx.guider.responses.GetVerifyCodeResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.UnsupportedEncodingException;

/**
 * Created by chenl on 2016/2/15.
 */
public class GetVerifyCodeRequest extends Request<GetVerifyCodeResponse> {
    private String mUrl;
    private final Response.Listener<GetVerifyCodeResponse> mListener;

    public GetVerifyCodeRequest(int method, String url, Response.Listener<GetVerifyCodeResponse> listener,
                        Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public GetVerifyCodeRequest(String url, Response.Listener<GetVerifyCodeResponse> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Response<GetVerifyCodeResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            GetVerifyCodeResponse res = new GetVerifyCodeResponse(response);
            return Response.success(res, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(GetVerifyCodeResponse response) {
        mListener.onResponse(response);
    }
}
