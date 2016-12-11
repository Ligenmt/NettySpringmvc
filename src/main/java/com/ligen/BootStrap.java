package com.ligen;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * Created by ligen on 2016/12/10.
 */
public class BootStrap {

    private final int port;

    public BootStrap(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        ServerBootstrap server = new ServerBootstrap();

        try {
            server.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                    .channel(NioServerSocketChannel.class).
                    localAddress(port)
                    .childHandler(new DispatcherServletChannelInitializer());
            System.out.println("bootstrap start");
            server.bind().sync().channel().closeFuture().sync();
        }
        finally {
            System.out.println("shutdown");
        }

    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new BootStrap(port).run();
    }
}
