package com.kymjs.faraday;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ZhangTao on 3/7/17.
 */
public class FaradayBridge {

    private Gson mGson;
    private Map<String, Method> nativeMethodMap = new HashMap<>();

    private static final String SCHEME = "faraday";
    private final static String RETURN_RESULT_FORMAT = "{\"code\": %d, \"result\": %s}";
    private static final FaradayBridge INSTANCE = new FaradayBridge();

    public static FaradayBridge getInstance() {
        return INSTANCE;
    }

    private FaradayBridge() {
    }

    public void addNativeMethod(String name, Class<? extends JSBridge> jsBridge) {
        Method[] methods = jsBridge.getDeclaredMethods();
        for (Method method : methods) {
            JavascriptInterface jsInterface = method.getAnnotation(JavascriptInterface.class);
            if (jsInterface != null) {
                nativeMethodMap.put(getBridgeModuleName(name, method.getName()), method);
            }
        }
    }

    private Method getMethod(String bridge, String methodName) {
        return nativeMethodMap.get(getBridgeModuleName(bridge, methodName));
    }

    public static String getBridgeModuleName(String bridge, String methodName) {
        return String.format("%s.%s", bridge, methodName);
    }

    public String call(WebView webView, String jsonStr) {
        String methodName = "";
        String bridge = "";
        String param = "{}";
        String result = "";
        String sid = "";
        if (!TextUtils.isEmpty(jsonStr)) {
            if (jsonStr.startsWith(SCHEME)) {
                Uri uri = Uri.parse(jsonStr);
                bridge = uri.getHost();
                param = uri.getQuery();
                sid = getPort(jsonStr);
                String path = uri.getPath();
                if (!TextUtils.isEmpty(path)) {
                    methodName = path.replace("/", "").trim();
                }
            }
            try {
                Method method = getMethod(bridge, methodName);

                Object[] values = new Object[3];
                values[0] = webView.getContext();
                values[1] = new JSONObject(param);
                values[2] = new JsCallback(webView, sid);

                if (method == null) {
                    result = getReturn(JSBridge.RESULT_FAIL, "not found method(" + methodName + ") with valid parameters");
                } else {
                    result = getReturn(JSBridge.RESULT_SUCCESS, method.invoke(null, values));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            result = getReturn(JSBridge.RESULT_FAIL, "call data empty");
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

    private String getReturn(int stateCode, Object result) {
        String insertRes;
        if (result == null) {
            insertRes = "{}";
        } else if (result instanceof String) {
            result = ((String) result).replace("\"", "\\\"");
            insertRes = "\"" + result + "\"";
        } else if (!(result instanceof Integer)
                && !(result instanceof Long)
                && !(result instanceof Boolean)
                && !(result instanceof Float)
                && !(result instanceof Double)
                && !(result instanceof JSONObject)) {
            if (mGson == null) {
                mGson = new Gson();
            }
            insertRes = mGson.toJson(result);
        } else {
            insertRes = String.valueOf(result);
        }
        return String.format(Locale.getDefault(), RETURN_RESULT_FORMAT, stateCode, insertRes);
    }
}