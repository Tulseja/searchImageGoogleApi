package com.doitunzo.imageSearch.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;



public class CustomHttpRequest<T> extends Request<T>
{
    private String mRequestBody;
    private Map<String, String> mRequestHeader;
    private Class<T> mModelClass;
    private final Gson mGson = new Gson();
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE_JSON = String.format("application/json; " +
            "charset=%s", PROTOCOL_CHARSET);
    private static final String PROTOCOL_CONTENT_TYPE_URLENCODED = String.format
            ("application/x-www-form-urlencoded; charset=%s", PROTOCOL_CHARSET);
    private InternalListener mInternalListener;
    private int mRequestId;

    private CustomHttpRequest()
    {
        super(Method.GET, null, null);
    }


    public CustomHttpRequest(int method, @NonNull String url, int requestID, @Nullable
            Map<String, String> header, @Nullable String body, int timeOut, @Nullable Class<T>
                                     modelClass, HttpResponseListener listener)
    {
        super(method, url, listener == null ? null : new InternalListener(listener, requestID));
        mInternalListener = (InternalListener) this.getErrorListener();
        mRequestId = requestID;
        mRequestBody = body;
        mRequestHeader = header;
        mModelClass = modelClass;
        setRetryPolicy(new DefaultRetryPolicy(timeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    public CustomHttpRequest(int method, @NonNull String url, int requestID, @Nullable
            Map<String, String> header, @Nullable String body, @Nullable Class<T> modelClass,
                             HttpResponseListener listener) {
        this(method, url, requestID, header, body, 12000, modelClass, listener);
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            String responseString = new String(response.data, HttpHeaderParser.parseCharset
                    (response.headers, PROTOCOL_CHARSET));
            Log.d("AK",responseString);
            if (mModelClass == null)
            {
                return Response.success((T) responseString, HttpHeaderParser.parseCacheHeaders
                        (response));
            } else
            {
                return Response.success(mGson.fromJson(responseString, mModelClass),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        } catch (Exception e)
        {
            return Response.error(new ParseError(e));
        }
    }


    @Override
    protected void deliverResponse(T response)
    {
        if (mInternalListener != null)
        {
            mInternalListener.onResponse(response);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        return mRequestHeader != null ? mRequestHeader : super.getHeaders();
    }

    @Override
    public String getBodyContentType()
    {
        switch (mRequestId)
        {
            default:
                return PROTOCOL_CONTENT_TYPE_JSON;

        }

    }

    @Override
    public byte[] getBody()
    {
        try
        {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee)
        {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }


    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError)
    {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null)
        {
            volleyError = new VolleyError(new String(volleyError.networkResponse.data));
        }
        return volleyError;
    }
}
