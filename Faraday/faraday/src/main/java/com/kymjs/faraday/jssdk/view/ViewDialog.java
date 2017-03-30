package com.kymjs.faraday.jssdk.view;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.kymjs.faraday.JSBridge;
import com.kymjs.faraday.JSONUtil;
import com.kymjs.faraday.JsCallback;

import org.json.JSONObject;

/**
 * Created by ZhangTao on 3/8/17.
 */

public class ViewDialog implements JSBridge {

    public static final String BRIDGE_NAME = "ViewDialog";

    @JavascriptInterface
    public static void toast(Context context, JSONObject params, final JsCallback jsCallback) {
        String message = params.optString("message");
        int isShowLong = params.optInt("isShowLong");
        Toast.makeText(context, message, isShowLong == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
        if (null != jsCallback) {
            jsCallback.apply(JSONUtil.getInstance()
                    .addStatus(RESULT_SUCCESS)
                    .addMessage("success").build());
        }
    }

    @JavascriptInterface
    public static void dialog(Context context, JSONObject params, final JsCallback jsCallback) {
    }
}