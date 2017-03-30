package com.kymjs.faraday;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by ZhangTao on 3/7/17.
 */

public interface JSBridge {
    int RESULT_SUCCESS = 200;
    int RESULT_FAIL = 500;

    boolean router(Context context, String methodName, JSONObject param, final JSCallback jsCallback);
}
