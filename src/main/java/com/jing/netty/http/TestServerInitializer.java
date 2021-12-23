package com.jing.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        //向管道加入处理器

        //得到管道【双向链表】
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个netty 提供的httpServerCodec    编解码器codec =[coder - decoder]
        //HttpServerCodec 说明
        //1. HttpServerCodec 是netty 提供的处理http的 编-解码器的Handler
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        //2. 增加一个自定义的Handler
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());

        System.out.println("ok~~~~");

    }
}
