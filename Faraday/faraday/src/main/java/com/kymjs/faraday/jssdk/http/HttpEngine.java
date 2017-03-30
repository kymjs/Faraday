package com.kymjs.faraday.jssdk.http;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.kymjs.faraday.JSBridge;
import com.kymjs.faraday.JsCallback;

import org.json.JSONObject;

/**
 * Created by ZhangTao on 3/8/17.
 */
public class HttpEngine implements JSBridge {

    public static final String BRIDGE_NAME = "HttpEngine";

    public static IHttp sHttpEngine = new RxVolleyHttpEngine();

    @JavascriptInterface
    public static void get(Context context, JSONObject params, JsCallback callback) {
        if (sHttpEngine != null) {
            sHttpEngine.get(context, params, callback);
        }
    }

    @JavascriptInterface
    public static void post(Context context, JSONObject params, JsCallback callback) {
        if (sHttpEngine != null) {
            sHttpEngine.post(context, params, callback);
        }
    }
}
