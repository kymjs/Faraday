package com.kymjs.faraday.jssdk.http;

import android.content.Context;

import com.kymjs.faraday.JSBridge;
import com.kymjs.faraday.JSONUtil;
import com.kymjs.faraday.JsCallback;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by ZhangTao on 3/8/17.
 */
public class RxVolleyHttpEngine implements IHttp {

    public static final String URL = "url";

    @Override
    public void get(Context context, JSONObject params, JsCallback callback) {
        RxVolley.Builder builder = new RxVolley.Builder();
        Iterator<String> iterator = params.keys();
        HttpParams httpParams = null;
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.equals(URL)) {
                builder.url(params.optString(URL));
            } else {
                if (httpParams == null) {
                    httpParams = new HttpParams();
                }
                httpParams.put(key, params.optString(key));
            }
        }
        if (httpParams != null) {
            builder.params(httpParams);
        }
        builder.httpMethod(RxVolley.Method.GET)
                .callback(new RequestCallback(callback))
                .doTask();
    }

    @Override
    public void post(Context context, JSONObject params, JsCallback callback) {
        RxVolley.Builder builder = new RxVolley.Builder();
        Iterator<String> iterator = params.keys();
        HttpParams httpParams = null;
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.equals(URL)) {
                builder.url(params.optString(URL));
            } else {
                if (httpParams == null) {
                    httpParams = new HttpParams();
                }
                httpParams.put(key, params.optString(key));
            }
        }
        if (httpParams != null) {
            builder.params(httpParams);
        }
        builder.httpMethod(RxVolley.Method.POST)
                .callback(new RequestCallback(callback))
                .doTask();
    }

    private class RequestCallback extends HttpCallback {

        private JsCallback mJsCallback;

        RequestCallback(JsCallback callback) {
            this.mJsCallback = callback;
        }

        @Override
        public void onSuccess(Map<String, String> headers, byte[] t) {
            super.onSuccess(headers, t);
            JSONUtil jsonUtil = JSONUtil.getInstance();
            jsonUtil.addStatus(JSBridge.RESULT_SUCCESS)
                    .addMessage("success");
            try {
                jsonUtil.addExtraData(new JSONObject(new String(t)));
            } catch (JSONException e) {
                jsonUtil.addExtraData(new String(t));
            }
            mJsCallback.apply(jsonUtil.build());
        }

        @Override
        public void onFailure(int errorNo, String strMsg) {
            super.onFailure(errorNo, strMsg);
            mJsCallback.apply(JSONUtil.getInstance()
                    .addStatus(errorNo)
                    .addMessage(strMsg)
                    .build());
        }
    }
}
