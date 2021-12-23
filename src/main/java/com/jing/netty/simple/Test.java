package com.jing.netty.simple;

import io.netty.util.NettyRuntime;

public class Test {
    public static void main(String[] args) {
        //系统内核数：4
        System.out.println("系统内核数："+NettyRuntime.availableProcessors());
    }
}
