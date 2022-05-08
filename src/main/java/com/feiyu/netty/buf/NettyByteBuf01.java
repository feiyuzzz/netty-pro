package com.feiyu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author Eddie
 */
public class NettyByteBuf01 {

    public static void main(String[] args) {
        // 创建一个 ByteBuf
        // 说明
        // 1. 创建一个 对象，该对象包含一个数组arr，是一个byte[10]
        // 2. 在 netty 的 buf 中，不需要使用 flip 进行反转，
        // 底层维护了 readerIndex 和 writerIndex
        // 3. 通过 readerIndex 和 writerIndex 和 capacity，将 buffer 分成三个区域
        // 0 -- readerIndex  已近读取区域
        // readerIndex --- writerIndex 可读区域
        // writerIndex --- capacity，可写区域
        ByteBuf buf = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buf.writeByte(i);
        }

        System.out.println("buf capacity =" + buf.capacity());

        for (int i = 0; i < buf.capacity(); i++) {
            System.out.println(buf.readByte());
        }
    }
}
