package com.feiyu.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 自己的业务处理器
 * </p>
 *
 * @author Eddie
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 定义一个channel组，管理所有的Channel
    // GlobalEventExecutor.INSTANCE 是一个全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // handlerAdded 表示链接建立，一旦链接建立 第一个被执行
    // 将当前channel加入到全局 channelGroup 中
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户加入聊天的信息推送给其他在线的客户端
        //  channelGroup.writeAndFlush() 回见 channelGroup 会将组中的 channel 遍历，并发送
        channelGroup.writeAndFlush("[客户端] " + channel.remoteAddress() + " 加入聊天\n");
        channelGroup.add(channel);
    }

    // 表示 channel 处于活动状态，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了~" + sdf.format(new Date()) + "\n");
    }

    // 当 channel 处于非活动状态，提示 xx 下线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线~"+sdf.format(new Date()) + "\n");
    }

    // 断开链接会被触发，将 xx 客户离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端] " + channel.remoteAddress() + " 离开了 " + sdf.format(new Date()) + "\n");
        System.out.println("channelGroup size" + channelGroup.size());
    }

    // 读取信息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取到当前的 channel
        Channel channel = ctx.channel();
        // 遍历 channelGroup , 根据不同的情况，回送不同的消息
        channelGroup.forEach(channel1 -> {
            if (channel != channel1) {
                channel1.writeAndFlush("[客户] " + channel.remoteAddress() + " 发送消息" + msg + sdf.format(new Date()) + "\n");
            } else {
                // 回显自己的消息
                channel.writeAndFlush("[自己]发送了消息：" + msg + " "+sdf.format(new Date()) + "\n");
            }
        });
    }

    // 异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
