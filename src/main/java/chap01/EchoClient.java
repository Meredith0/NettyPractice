package chap01;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

/**
 * @author : Meredith
 * @date : 2020-01-12 14:49
 * @description : 客户端的主类
 */
public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient (String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main (String[] args) throws Exception {
        // if (args.length != 2) {
        //     System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
        //     return;
        // }
        String host = "localhost";
        int port = Integer.parseInt("8001");
        new EchoClient(host, port).start();
    }

    public void start () throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)// 指定EventLoopGroup 以处理客户端事件
             .channel(NioSocketChannel.class)// 适用于NIO传输的Channel类型
             .remoteAddress(new InetSocketAddress(host, port))
             //在创建Channel时， 向ChannelPipeline中添加一个EchoClientHandler 实例
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel (SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(new EchoClientHandler());
                 }
             });
            ChannelFuture f = b.connect().sync();//连接到远程节点，阻塞等待直到连接完成
            f.channel().closeFuture().sync();//阻塞，直到Channel 关闭
        } finally {
            group.shutdownGracefully().sync();//关闭线程池并且释放所有的资源
        }
    }

}
