package com.ligen;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

//import io.netty.handler.codec.http.HttpChunkAggregator;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by ligen on 2016/12/10.
 */
public class DispatcherServletChannelInitializer extends ChannelInitializer<SocketChannel> {

    private DispatcherServlet dispatcherServlet;

    public DispatcherServletChannelInitializer() throws Exception {

        MockServletContext servletContext = new MockServletContext();
        MockServletConfig servletConfig = new MockServletConfig(servletContext);
        servletConfig.addInitParameter("contextConfigLocation", "classpath:applicationContext.xml");
        servletContext.addInitParameter("contextConfigLocation", "classpath:applicationContext.xml");

        XmlWebApplicationContext wac = new XmlWebApplicationContext();
        wac.setServletContext(servletContext);
        wac.setServletConfig(servletConfig);

        wac.setConfigLocation("classpath:dispatcher-servlet.xml");
        wac.refresh();

        dispatcherServlet = new DispatcherServlet(wac);
        dispatcherServlet.init(servletConfig);
        System.out.println("servlet inited");

    }

    @Override
    public void initChannel(SocketChannel channel) throws Exception {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = channel.pipeline();

        // Uncomment the following line if you want HTTPS
        //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
        //engine.setUseClientMode(false);
        //pipeline.addLast("ssl", new SslHandler(engine));

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
        pipeline.addLast("handler", new ServletNettyHandler(this.dispatcherServlet));
    }

    @Configuration
    @EnableWebMvc
    @ComponentScan(basePackages="org.springframework.sandbox.mvc")
    static class WebConfig extends WebMvcConfigurerAdapter {

    }
}
