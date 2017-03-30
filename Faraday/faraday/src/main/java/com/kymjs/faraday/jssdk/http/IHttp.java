package com.kymjs.faraday.jssdk.http;

import android.content.Context;

import com.kymjs.faraday.JsCallback;

import org.json.JSONObject;

/**
 * Created by ZhangTao on 3/8/17.
 */

public interface IHttp {

    void get(Context context, JSONObject params, JsCallback callback);

    void post(Context context, JSONObject params, JsCallback callback);
}
