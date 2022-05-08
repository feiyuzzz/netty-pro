package com.feiyu.netty.codec2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

/**
 * <p>
 * 客户端 Handler
 * </p>
 *
 * @author Eddie
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int random = new Random().nextInt(3);
        MessagePojo.Message message = null;
        if(0 == random){
            message =  MessagePojo.Message.newBuilder().setDataType(MessagePojo.Message.DataType.StudentType).setStudent(MessagePojo.Student.newBuilder().setId(20).setName("小李").build()).build();
        }else{
            message = MessagePojo.Message.newBuilder().setDataType(MessagePojo.Message.DataType.WorkerType).setWorker(MessagePojo.Worker.newBuilder().setAge(30).setName("李四").build()).build();
        }
        ctx.writeAndFlush(message);
    }

    // 处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
