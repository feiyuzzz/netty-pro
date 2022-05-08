package com.feiyu.netty.codec;

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
        StudentPojo.Student student = (StudentPojo.Student) msg;
        System.out.println("客户端发送的数据 id= " + student.getId() + " name = " + student.getName());
    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // write + flush 将数据写入到缓冲并刷新
        // 一般我们发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("喵3", CharsetUtil.UTF_8));
    }

    // 处理异常，一般就是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
