package com.feiyu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * <p>
 * 自定义一个Handler 继承 ChannelInboundHandlerAdapter
 * </p>
 *
 * @author Eddie
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 读取数据（这里可以读取客户端发送的数据）
    // 1. ChannelHandlerContext ctx ： 上下文对象，含有 管道pipeline， 通道，地址
    // 2. Object msg : 就是客户端发送的数据 默认 Object
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("sever ctx = " + ctx);
        // 将 msg 转换成 ByteBuf
        // ByteBuf 是 netty 提供的，不是 NIO 的 ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());

    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // write + flush 将数据写入到缓冲并刷新
        // 一般我们发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello 客户端~",CharsetUtil.UTF_8));
    }

    // 处理异常，一般就是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();
    }
}
