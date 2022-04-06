[![OSL](https://kymjs.com/qiniu/image/logo3.png)](https://kymjs.com/works/)
=================

一套完美的 H5 hybrid jsbridge 解决方案，精简且极大方便开发调试  

* 兼容 Android 12
* 支持 androidx
* 无隐私权限相关问题


## 使用方式  

#### 引入

当前最新版本为，请将版本号替换为最新版：
[![](https://jitpack.io/v/kymjs/Faraday.svg)](https://jitpack.io/#kymjs/Faraday)

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

#### 注入JSBridge

```
FaradayBridge.getInstance().register(new DemoBridge());
```

#### 解耦WebView，支持自定义WebView

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

#### 原生调用js

使用简单，且方便自定义封装，对 H5 侧开发更直观  

```
webview.loadUrl("javascript://window.hello()");
```


## 开源协议
```
 Copyright (C) 2015, 张涛
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 ```
