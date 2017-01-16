package com.ccreanga.anonymizer;

import java.util.HashMap;
import java.util.Map;

public class Store {

    public static Map<String, Map> store = new HashMap<>();

    public static void put(String storeName, String key, Object value) {
        Map map = store.get(storeName);
        if (map == null) {
            map = new HashMap();
            store.put(storeName, map);
        }
        map.put(key, value);

    }

    public static Object get(String storeName, Object key) {
        Map map = store.get(storeName);
        return map == null ? null : map.get(key);
    }
}
