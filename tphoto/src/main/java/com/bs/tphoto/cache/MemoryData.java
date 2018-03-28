package com.bs.tphoto.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MemoryData {

    public static ConcurrentMap USERS_PRIVATEKEY = null;
    public static ConcurrentMap USERS_KEY = null;

    static {
        USERS_PRIVATEKEY = new ConcurrentHashMap<String,Object>();
        USERS_KEY = new ConcurrentHashMap<String,Object>();
    }

}
