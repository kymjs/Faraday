package com.kymjs.faraday;

import android.webkit.WebView;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * Created by ZhangTao on 3/7/17.
 */

public class JSCallback {

    private static final String CALLBACK_JS_FORMAT = "javascript:JSBridge.onComplete(%s, %s);";

    private WeakReference<WebView> mWebViewRef;
    private String sid;

    public JSCallback(WebView webView, String sid) {
        mWebViewRef = new WeakReference<>(webView);
        this.sid = sid;
    }

    public void apply(String callback) {
        if (mWebViewRef.get() == null) {
            return;
        }
        mWebViewRef.get().loadUrl(String.format(Locale.getDefault(), CALLBACK_JS_FORMAT, sid, callback));
    }
}
