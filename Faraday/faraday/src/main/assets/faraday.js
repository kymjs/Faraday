(function (win, lib) {
    var doc = win.document;
    var hasOwnProperty = Object.prototype.hasOwnProperty;
    var Faraday = win.Faraday || (win.Faraday = {});
    var inc = 1;
    var LOCAL_PROTOCOL = 'faraday';
    var CB_PROTOCOL = 'cb_faraday';
    var CALLBACK_PREFIX = 'callback_';

    var Core = {
        call: function (obj, method, params, callback, timeout) {
            var sid;

            if (typeof callback !== 'function') {
                callback = null;
            }

            sid = Private.getSid();

            Private.registerCall(sid, callback);
            Private.callMethod(obj, method, params, sid);
        },

        onComplete: function (sid, data) {
            Private.onComplete(sid, data);
        }
    };

    var Private = {
        params: {},
        chunks: {},
        calls: {},

        getSid: function () {
            return Math.floor(Math.random() * (1 << 50)) + '' + inc++;
        },

        buildParam: function (obj) {
            if (obj && typeof obj === 'object') {
                return JSON.stringify(obj);
            } else {
                return obj || '';
            }
        },

        parseData: function (str) {
            var rst;
            if (str && typeof str === 'string') {
                try {
                    rst = JSON.parse(str);
                } catch (e) {
                    rst = {
                        status: {
                            code: 1,
                            msg: 'PARAM_PARSE_ERROR'
                        }
                    };
                }
            } else {
                rst = str || {};
            }

            return rst;
        },

        //根据sid注册calls的回调函数
        registerCall: function (sid, callback) {
            if (callback) {
                this.calls[CALLBACK_PREFIX + sid] = callback;
            }
        },

        //根据sid删除calls对应的回调函数，并返回call对象
        unregisterCall: function (sid) {
            var callbackId = CALLBACK_PREFIX + sid;
            var call = {};

            if (this.calls[callbackId]) {
                call.callback = this.calls[callbackId];
                delete this.calls[callbackId];
            }

            return call;
        },

        callMethod: function (obj, method, params, sid) {
            params = Private.buildParam(params);

            var uri = LOCAL_PROTOCOL + '://' + obj + ':' + sid + '/' + method + '?' + params;

            var value = CB_PROTOCOL + ':';
            window.prompt(uri, value);
        },

        onComplete: function (sid, data) {
            var callObj = this.unregisterCall(sid);
            var callback = callObj.callback;

            data = this.parseData(data);

            callback && callback(data);
        }
    };

    for (var key in Core) {
        if (!hasOwnProperty.call(Faraday, key)) {
            Faraday[key] = Core[key];
        }
    }
})(window);