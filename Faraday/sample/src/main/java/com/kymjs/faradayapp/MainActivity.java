package com.kymjs.faradayapp;

import android.os.Build;
import android.os.Bundle;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.kymjs.faraday.FaradayBridge;
import com.kymjs.faraday.jssdk.view.ViewDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注册声明好的JSBridge
        //一定要在webview加载之前注册,否则无法生效
        FaradayBridge.getInstance().register(new DemoBridge());
        FaradayBridge.getInstance().register(ViewDialog.BRIDGE_NAME, new ViewDialog());

        //开调试(API 19)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        WebView webView = (WebView) findViewById(R.id.webview);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当不使用webview时
        //静态变量持有map，不clean会造成内存泄漏
        FaradayBridge.getInstance().clear();
    }
}
