package com.feiyu.netty.heartbreath;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *    带心跳的服务器
 * </p>
 *
 * @author Eddie
 */
public class MyServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //  加入 netty 提供 IdleStatusHandler
                            // 1 IdleStatusHandler 是 netty 提供的处理状态处理器
                            // 2 long readerTime : 表示多次时间没有读，就会发送一个心跳检测包检测是否链接
                            // 3 long writerIdlerTime ： 表示多次时间没有写操作了，就会发送一个心跳检测包检测是否链接
                            // 4 long allIdleTime ： 表示多次时间既没有也没有写，就会发送一个心跳检测包检测是否链接
                            // 文档说明
                            // * Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                            // * read, write, or both operation for a while.
                            // 6 当 IdleStateEvent 触发后，就会传递给管道的下一个handler处理
                            // 通过调用（触发）下一个handler的 userEventTiggered，在该方法中去处理
                            // IdleStateHandler(读空闲，写空闲，读写空闲)
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的handler（自定义）
                            pipeline.addLast(new MyServerHandler());
                        }
                    });
            // 启动服务器
            ChannelFuture cf = bootstrap.bind(7000).sync();
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
