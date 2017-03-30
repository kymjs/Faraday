package com.kymjs.faraday;

/**
 * Created by ZhangTao on 3/8/17.
 */

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JSONUtil {
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String DATA = "data";
    private final Map<String, Object> mMainHashMap = new ConcurrentHashMap<>();
    private final Map<String, Object> mData = new ConcurrentHashMap<>();
    private final List<Object> mListData = new ArrayList<>();
    private Object mDataObject = null;

    private JSONUtil() {
    }

    public static JSONUtil getInstance() {
        return new JSONUtil();
    }

    public JSONUtil addStatus(int status) {
        this.put(this.mMainHashMap, STATUS, status + "");
        return this;
    }

    public JSONUtil addMessage(String message) {
        this.put(this.mMainHashMap, MESSAGE, message);
        return this;
    }

    public JSONUtil addExtraData(String key, Object value) {
        this.put(this.mData, key, value);
        return this;
    }

    public JSONUtil addExtraData(Object value) {
        this.mDataObject = value;
        return this;
    }

    public JSONUtil addListData(Object object) {
        this.mListData.add(object);
        return this;
    }

    private void put(Map<String, Object> map, String key, Object value) {
        if (key != null) {
            if (value == null) {
                value = "";
            }

            if (map != null) {
                map.put(key, value);
            }

        }
    }

    public String build() {
        String result;
        if (this.mMainHashMap.containsKey(STATUS)) {
            if (this.mDataObject == null) {
                this.put(this.mMainHashMap, DATA, this.mData);
            } else {
                this.put(this.mMainHashMap, DATA, this.mDataObject);
            }

            result = (new Gson()).toJson(this.mMainHashMap);
        } else if (this.mData.size() > 0) {
            result = (new Gson()).toJson(this.mData);
        } else {
            result = (new Gson()).toJson(this.mListData);
        }

        this.mMainHashMap.clear();
        this.mData.clear();
        this.mListData.clear();
        this.mDataObject = null;
        return result;
    }
}
