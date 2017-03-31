package com.kymjs.faradayapp;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.kymjs.faraday.FaradayBridge;
import com.kymjs.faraday.jssdk.http.HttpEngine;
import com.kymjs.faraday.jssdk.view.ViewDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注册声明好的JSBridge
        //一定要在webview加载之前注册,否则无法生效
        FaradayBridge.getInstance().addNativeMethod(DemoBridge.BRIDGE_NAME, DemoBridge.class);
        FaradayBridge.getInstance().addNativeMethod(ViewDialog.BRIDGE_NAME, ViewDialog.class);
        FaradayBridge.getInstance().addNativeMethod(HttpEngine.BRIDGE_NAME, HttpEngine.class);

        //开调试(API 19)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        WebView webView = (WebView) findViewById(R.id.webview);
        //移除默认内置接口,防止远程代码执行
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.removeJavascriptInterface("accessibility");
            webView.removeJavascriptInterface("accessibilityTraversal");
        }

        webView.setWebChromeClient(new InjectedChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");
    }

    public class InjectedChromeClient extends WebChromeClient {

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            result.confirm(FaradayBridge.getInstance().call(view, message));
            return true;
        }
    }
}
