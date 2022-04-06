[![OSL](https://kymjs.com/qiniu/image/logo3.png)](https://kymjs.com/works/)
=================


## Faraday

A set of perfect H5 hybrid jsbridge solution of Android, which is simple and convenient for development and debugging

* Compatible with Android 12

* Support androidx package

* No privacy related issues

## Getting Started

latest version numbers: [![](https://jitpack.io/v/kymjs/Faraday.svg)](https://jitpack.io/#kymjs/Faraday)

```
// root build.gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

// module build.gradle
dependencies {
    implementation 'com.github.kymjs:faraday:2.0.0'
}
```  

#### register JSBridge

```
FaradayBridge.getInstance().register(new DemoBridge());
```

#### customs WebView

```
public class InjectedChromeClient extends WebChromeClient {
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
      //  只需要在 onJsPrompt 方法中通知Faraday即可
      result.confirm(FaradayBridge.getInstance().call(view, message));
        return true;
    }
}
```

#### native call js

使用简单，且方便自定义封装，对 H5 侧开发更直观  

```
webview.loadUrl("javascript://window.hello()");
```



## Requirements

Faraday can be included in any Android application.  

Faraday supports Android 4.0, API14 (HONEYCOMB_MR1) and later.  

## License

Licensed under the Apache License Version 2.0.  [The "License"](http://www.apache.org/licenses/LICENSE-2.0)  
