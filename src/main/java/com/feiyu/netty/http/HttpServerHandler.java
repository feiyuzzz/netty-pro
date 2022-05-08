package com.feiyu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * <p>
 * Handler
 * 1. SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter的一个子类
 * 2. 客户端和服务器相互通讯的数据封装成 HttpObject
 * </p>
 *
 * @author Eddie
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    // channelRead0 读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // 判断 msg 是不是 http 请求
        if (msg instanceof HttpRequest) {
            // 每个客户端都有对应的 pipline 以及自己的 Handler
            System.out.println("pipeline hashCode = " + ctx.pipeline().hashCode()
                    + "HttpServerHandler hashCode = " + this.hashCode());
            System.out.println("msg 类型 = " + msg.getClass());
            System.out.println("客户端地址 = " + ctx.channel().remoteAddress());

            // 解决重复请求问题
            // 获取到 httpRequest
            HttpRequest httpRequest = (HttpRequest) msg;
            // 获取uri
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico，不做响应");
                return;
            }

            // 回复信息给浏览器【http 协议】
            ByteBuf content = Unpooled.copiedBuffer("hello，我是服务器", Charset.defaultCharset());
            // 构建一个Http协议响应
            DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将构建好的 fullHttpResponse 返回
            ctx.writeAndFlush(fullHttpResponse);
        }
    }

}
