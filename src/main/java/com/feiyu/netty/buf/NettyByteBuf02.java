package com.feiyu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @author Eddie
 */
public class NettyByteBuf02 {

    public static void main(String[] args) {
        // 创建 buf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello,world！", Charset.defaultCharset());
        // 使用相关方法
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();
            // 将 content 转换成字符串
            System.out.println(new String(content,Charset.defaultCharset()));

            System.out.println("byteBuf = " + byteBuf );

            System.out.println(byteBuf.arrayOffset()); // 开始 0
            System.out.println(byteBuf.readerIndex()); // 已经读取到 buf 的那个位置
            System.out.println(byteBuf.writerIndex()); // 写到哪个位置
            System.out.println(byteBuf.capacity()); // buf 的容量

            System.out.println(byteBuf.readableBytes()); // buf 中可以读的字节长度

            byteBuf.readByte(); // 读取一个 byte

            System.out.println(byteBuf.readableBytes());
        }
    }
}
