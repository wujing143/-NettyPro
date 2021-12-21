package com.jing.nio;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: NIO非阻塞网络编程编 【服务端】
 * 写一个 NIO 入门案例，实现服务器端和客户端之间的数据简单通讯（非阻塞）
 * @Author: Wu
 * @Date: 2021/12/21 9:30
 * @Version: 1.0
 */
public class NIOServer {
    public static void main(String[] args) throws Exception {

        //创建ServerSocketChannel -->  ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //创建 Selector（选择器）
        Selector  selector = Selector.open();

        //绑定一个端口6666，在服务器监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));// 真实类型：WindowsSelectorImpl

        //设置为非阻塞通道
        serverSocketChannel.configureBlocking(false);

        //吧 serverSocketChannel 注册到 selector中，关心事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("注册后的selectionkey 数量=" + selector.keys().size()); // 1

        //循环等待客户端连接
        while (true) {

            if (selector.select(1000) == 0) { //等待1秒，selector选择器还未监听到任何事件发生，返回
                System.out.println("服务器等待一秒，无法连接...");
                continue; //返回
            }

            //如有事件（>0）,就返回 selectionKey 集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys 数量 = " + selectionKeys.size());
            // 遍历 Set<SelectionKey>,使用迭代器遍历
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                //获取到一个selectionKey
                SelectionKey key = iterator.next();

                //根据key，找到对应的通道发生的事件做相应处理
                if (key.isAcceptable()) { //如果是 OP_ACCEPT ,有新的客户端连接


                    //因为已确认有连接，所有通道使用accept()，进行阻塞。【BIO是一直傻傻的阻塞式等待连接】
                    //该客户端需要一个SocketChannel[通道]
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false); //设置非阻塞
                    System.out.println("已被客户端连接，生成一个socketChannel：" + socketChannel.hashCode());
                    //将socketChannel注册到 selector，关注事件为OP_READ，并关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接后 ，注册的selectionkey 数量=" + selector.keys().size()); //2,3,4..
                }

                if (key.isReadable()) {// 发生 OP_ACCEPT
                    //通过key 反向获取到对应的channel，也就是 上面创建的 socketChannel对象
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    channel.close();
                    System.out.println("form 客户端的数据：" + new String(buffer.array()));
                }
                //手动从集合中删除已处理过的key，防止重复操作
                iterator.remove();
            }

        }

    }

}
