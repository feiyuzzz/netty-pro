package com.feiyu.netty.codec2;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <p>
 * 自定义一个Handler 继承 ChannelInboundHandlerAdapter
 * </p>
 *
 * @author Eddie
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MessagePojo.Message> {

    // 根据dataType来输出信息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePojo.Message msg) throws Exception {
        MessagePojo.Message.DataType dataType = msg.getDataType();
        if(dataType == MessagePojo.Message.DataType.StudentType){
            MessagePojo.Student stu = msg.getStudent();
            System.out.println("学生{id="+stu.getId()+", name="+stu.getName()+"}");
        }else if (dataType == MessagePojo.Message.DataType.WorkerType){
            MessagePojo.Worker wor = msg.getWorker();
            System.out.println("工人{name="+wor.getName()+", age="+wor.getAge()+"}");
        }else{
            System.out.println("传输类型不正确！");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
