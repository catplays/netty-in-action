package com.catplay.netty.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author wangyong
 * @Date 2020-04-02
 */
public class IdUtil {
    private final static AtomicLong idx = new AtomicLong();
    private final static AtomicInteger idx2 = new AtomicInteger();

    public static long nextId() {
        long curr =  idx.incrementAndGet();
        if (curr >= Long.MAX_VALUE) {
            System.out.println(curr);
            idx.set(0);
        }
        return curr;
    }

}
