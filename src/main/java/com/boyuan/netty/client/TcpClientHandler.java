package com.boyuan.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

public class TcpClientHandler extends ChannelInboundHandlerAdapter {
    TcpClients timeClient = new TcpClients();
    private final ByteBuf firstMSG;

    public TcpClientHandler() {
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMSG = Unpooled.buffer(req.length);
        firstMSG.writeBytes(req);
    }

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(firstMSG);
//    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("Receive Message: " + body);

    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {  //断线重连
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                System.err.println("服务端链接不上，开始重连操作...");
                try {
                    timeClient.connect(8888, "127.0.0.1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3, TimeUnit.SECONDS);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { //重复发送数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    ByteBuf bf = Unpooled.copiedBuffer("QUERY TIME ORDER".getBytes());//(("你好服务端：" + Math.random()).getBytes());
                    ctx.writeAndFlush(bf);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}
