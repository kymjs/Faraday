package com.kymjs.faradayapp;

/**
 * Created by ZhangTao on 3/8/17.
 */

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.kymjs.faraday.JSBridge;
import com.kymjs.faraday.JSONUtil;
import com.kymjs.faraday.JsCallback;

import org.json.JSONObject;

/**
 * 四个限制
 * 1. 必须实现JSBridge接口
 * 2. 要提供给web调用的方法必须加注解@JavascriptInterface
 * 3. 方法必须是static的
 * 4. 要提供给web的方法参数必须固定(context, jsonObject, JsCallback)
 */
public class DemoBridge implements JSBridge {

    public static final String BRIDGE_NAME = "DemoBridge";

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_SHOWLONG = "isShowLong";

    @JavascriptInterface
    public static void toast(Context context, JSONObject params, final JsCallback jsCallback) {
        String message = params.optString(KEY_MESSAGE);
        int isShowLong = params.optInt(KEY_SHOWLONG);
        Toast.makeText(context, message, isShowLong == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
        if (null != jsCallback) {
            jsCallback.apply(JSONUtil.getInstance()
                    .addStatus(RESULT_SUCCESS)
                    .addMessage("success message").build());
        }
    }
}