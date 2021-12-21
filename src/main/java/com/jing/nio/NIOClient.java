package com.jing.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description: NIO非阻塞网络编程编 【客户端】
 * @Author: Wu
 * @Date: 2021/12/21 11:28
 * @Version: 1.0
 */
public class NIOClient {

    public static void main(String[] args) throws Exception {


        //创建一个网络通道
        SocketChannel socketChannel= SocketChannel.open();

        //设置该通道为非阻塞
        socketChannel.configureBlocking(false);

        //提供服务器端的 ip和port
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        //通道连接服务器
        if (!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("因连接需要时间，客户端不会阻塞，可以做其他工作...");
            }
        }

        //连接成功，发送数据
        String str = "hello,Wu....";
        //Wraps a byte array into a buffer. 自动设置buffer大小为str字节数组大小，并赋值。
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        //发送数据，将buffer数据写入channel
        socketChannel.write(buffer);

    }


}
