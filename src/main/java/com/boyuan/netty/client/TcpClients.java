package com.boyuan.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TcpClients {

    public  void connect(int port, String host) throws Exception{
        //配置客户端NIO 线程组
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();
        try {
            client.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TcpClientHandler());
                        }
                    });

            //绑定端口, 异步连接操作
            ChannelFuture future = client.connect(host, port);
            future.addListener(new ConnectionListener()); //服务重连
            //等待客户端连接端口关闭
            future.channel().closeFuture().sync();
        } finally {
            //优雅关闭 线程组
            //group.shutdownGracefully();
        }
    }
    public static void main(String[] args) {
        int port = 443;
        TcpClients client = new TcpClients();
        try {
            client.connect(8888, "127.0.0.1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
