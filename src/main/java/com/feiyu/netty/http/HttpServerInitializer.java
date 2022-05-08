package com.feiyu.netty.http;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.ServerSocket;

/**
 * <p>
 *   自定义 HttpServer 初始化
 * </p>
 *
 * @author Eddie
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        // 向管道加入处理器

        // 得到管道
        ChannelPipeline pipeline = ch.pipeline();

        // 加入netty提供的HttpServerCodec
        // 1. netty 提供的一个基于http的编解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        // 2. 增加一个自定义的handler
        pipeline.addLast("MyHttpServerHandler",new HttpServerHandler());
    }
}
