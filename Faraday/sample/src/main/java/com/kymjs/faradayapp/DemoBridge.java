package com.kymjs.faradayapp;

/**
 * Created by ZhangTao on 3/8/17.
 */

import android.content.Context;
import android.widget.Toast;

import com.kymjs.faraday.JSBridge;
import com.kymjs.faraday.JSCallback;
import com.kymjs.faraday.JSONUtil;

import org.json.JSONObject;

/**
 * 四个限制
 */
public class DemoBridge implements JSBridge {

    public static final String BRIDGE_NAME = "DemoBridge";

    private static final String METHOD_TOAST = "toast";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_SHOWLONG = "isShowLong";


    @Override
    public boolean router(Context context, String methodName, JSONObject param, JSCallback jsCallback) {
        if (METHOD_TOAST.equalsIgnoreCase(methodName)) {
            toast(context, param, jsCallback);
        }
        return false;
    }

    public static void toast(Context context, JSONObject params, final JSCallback jsCallback) {
        String message = params.optString(KEY_MESSAGE);
        int isShowLong = params.optInt(KEY_SHOWLONG);
        Toast.makeText(context, message, isShowLong == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
        if (null != jsCallback) {
            jsCallback.apply(JSONUtil.getInstance()
                    .addStatus(RESULT_SUCCESS)
                    .build());
        }
    }
}