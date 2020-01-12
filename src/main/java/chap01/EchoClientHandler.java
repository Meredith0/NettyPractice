package chap01;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author : Meredith
 * @date : 2020-01-12 14:40
 * @description : 客户端的ChannelHandler
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    //将在一个连接建立时被调用
    @Override
    public void channelActive (ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    // 每当接收数据时，都会调用这个方法。需要注意的是，由服务器发送的消息可能会被分块接收。
    // 也就是说，如果服务器发送了5 字节，那么不能保证这5字节会被一次性接收
    @Override
    protected void channelRead0 (ChannelHandlerContext ctx, ByteBuf msg) {
        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
