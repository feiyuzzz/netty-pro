package com.feiyu.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * <p>
 * Netty 服务器
 * </p>
 *
 * @author Eddie
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        // 创建BossGroup 和 workerGroup
        // 说明:
        // 1. 创建两个线程组
        // 2. boss只是处理链接请求，真正的与客户端进行业务处理，会交给 workerGroup完成
        // 3. 两都是无限循环
        // 4. bossGroup 和 workerGroup 含有的子线程（NioEvenLoop)的个数
        // 默认实际 cpu 核数 * 2
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建服务器端对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 链式编程进行设置
            bootstrap.group(bossGroup, workerGroup)  //设置两线程组
                    .channel(NioServerSocketChannel.class) // 使用 NioServerSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列等待得到链接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保存活动链接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道测试对象（匿名对象）
                        // 给pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }); // 给我们的 workerGroup 的 EvenLoop 对应的管道设置处理器

            System.out.println("服务器 is ready ...");
            // 绑定一个端口并且同步，生成一个ChannelFuture对象
            // 启动服务器
            ChannelFuture cf = bootstrap.bind(6688).sync();
            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
