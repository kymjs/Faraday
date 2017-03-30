package com.kymjs.faraday;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ZhangTao on 3/7/17.
 */
public class FaradayBridge {

    private static final String SCHEME = "faraday";
    private final static String RETURN_RESULT_FORMAT = "{\"code\": %d, \"result\": %s}";
    private static final FaradayBridge INSTANCE = new FaradayBridge();

    private Map<String, JSBridge> jsBridgeMap = new HashMap<>();

    public static FaradayBridge getInstance() {
        return INSTANCE;
    }

    private FaradayBridge() {
    }

    public void register(JSBridge object) {
        register(object.getClass().getSimpleName(), object);
    }

    public void register(String className, JSBridge jsBridge) {
        jsBridgeMap.put(className, jsBridge);
    }

    public boolean unRegister(String methodName) {
        return jsBridgeMap.remove(methodName) != null;
    }

    public void clear() {
        jsBridgeMap.clear();
    }

    public String call(WebView webView, String jsonStr) {
        String result = "";
        if (!TextUtils.isEmpty(jsonStr)) {
            if (jsonStr.startsWith(SCHEME)) {
                Uri uri = Uri.parse(jsonStr);
                String jsBridgeName = uri.getHost();
                String param = uri.getQuery();
                String sid = getPort(jsonStr);
                String path = uri.getPath();
                String methodName = "";

                if (!TextUtils.isEmpty(path)) {
                    methodName = path.replace("/", "").trim();
                }

                JSBridge jsBridge = jsBridgeMap.get(jsBridgeName);
                if (jsBridge == null) {
                    result = String.format(Locale.getDefault(), RETURN_RESULT_FORMAT,
                            JSBridge.RESULT_FAIL, "not found method(" + methodName + ") with valid parameters");
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(param);
                    } catch (JSONException e) {
                        jsonObject = new JSONObject();
                    }
                    JSCallback jsCallback = new JSCallback(webView, sid);
                    result = String.format(Locale.getDefault(), RETURN_RESULT_FORMAT, JSBridge.RESULT_SUCCESS,
                            jsBridge.router(webView.getContext(), methodName, jsonObject, jsCallback));
                }
            } else {
                result = String.format(Locale.getDefault(), RETURN_RESULT_FORMAT,
                        JSBridge.RESULT_FAIL, jsonStr + " is not faraday scheme");
            }
        } else {
            result = String.format(Locale.getDefault(), RETURN_RESULT_FORMAT, JSBridge.RESULT_FAIL, "call data empty");
        }

        return result;
    }

    private String getPort(String url) {
        if (!TextUtils.isEmpty(url)) {
            String[] arrays = url.split(":");
            if (arrays.length >= 3) {
                String portWithQuery = arrays[2];
                arrays = portWithQuery.split("/");
                if (arrays.length > 1) {
                    return arrays[0];
                }
            }
        }
        return null;
    }
}