package com.ccreanga.anonymizer;

import java.util.HashMap;
import java.util.Map;

public class Store {

    public static Map<String,Map> store = new HashMap<>();

    public static void put(String storeName, Object key, Object value){
        Map map = store.get(storeName);
        if (map==null) {
            map = new HashMap();
            store.put(storeName, map);
        }
        map.put(key,value);


    }

    public static Object get(String storeName, Object key){
        Map map = store.get(storeName);
        return map==null?null:map.get(key);
    }

    public static void main(String[] args) {
        Store.put("store1","key","value");
        System.out.println(Store.get("store1","key"));
        System.out.println(Store.get("store1","key1"));
        System.out.println(Store.get("store2","key"));
    }

}
