package com.silence.netty;

// import io.netty.bootstrap.ServerBootstrap;
// import io.netty.buffer.Unpooled;
// import io.netty.channel.ChannelHandlerContext;
// import io.netty.channel.ChannelInitializer;
// import io.netty.channel.ChannelOption;
// import io.netty.channel.SimpleChannelInboundHandler;
// import io.netty.channel.nio.NioEventLoopGroup;
// import io.netty.channel.socket.SocketChannel;
// import io.netty.channel.socket.nio.NioServerSocketChannel;
// import io.netty.handler.codec.http.DefaultFullHttpResponse;
// import io.netty.handler.codec.http.FullHttpRequest;
// import io.netty.handler.codec.http.HttpHeaderNames;
// import io.netty.handler.codec.http.HttpHeaderValues;
// import io.netty.handler.codec.http.HttpHeaders;
// import io.netty.handler.codec.http.HttpObjectAggregator;
// import io.netty.handler.codec.http.HttpRequestDecoder;
// import io.netty.handler.codec.http.HttpResponseDecoder;
// import io.netty.handler.codec.http.HttpResponseStatus;
// import io.netty.handler.codec.http.HttpVersion;
// import io.netty.util.AsciiString;

/**
 * netty start
 */
public class Start {
  // public static class HttpServerOne{
  //   private final int port;
  //   private ServerBootstrap bootstrap;

  //   public HttpServerOne(int _port){
  //     port = _port;
  //   }

  //   public void start() throws InterruptedException{
  //     bootstrap = new ServerBootstrap(); 
  //     final NioEventLoopGroup group = new NioEventLoopGroup();
  //     bootstrap.group(group)
  //       .channel(NioServerSocketChannel.class)
  //       .childHandler(new ChannelInitializer<SocketChannel>() {

  //         @Override
  //         protected void initChannel(SocketChannel arg0) throws Exception {
  //           System.out.println("initChannel ch: " + arg0);
  //           arg0.pipeline()
  //               .addLast("decoder", new HttpRequestDecoder())
  //               .addLast("encoder", new HttpResponseDecoder())
  //               .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
  //               .addLast("handler", new Start.HttpHandler())
  //               ;
  //         }
          
  //       })
  //       .option(ChannelOption.SO_BACKLOG, 128)
  //       .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
      
  //     bootstrap.bind(port).sync();
  //   }

  // }

  // public static class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
  //   private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

  //   @Override
  //   protected void channelRead0(ChannelHandlerContext arg0, FullHttpRequest arg1) throws Exception {
  //     System.out.println("class: " + arg1.getClass().getName());

  //     DefaultFullHttpResponse response = new DefaultFullHttpResponse(
  //       HttpVersion.HTTP_1_1, 
  //       HttpResponseStatus.OK, 
  //       Unpooled.wrappedBuffer("test".getBytes())
  //     );
  //     HttpHeaders headers = response.headers();
  //     headers.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
  //     headers.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
  //     headers.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

  //     arg0.write(response);
  //   }


  //   @Override
  //   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
  //       System.out.println("channelReadComplete");
  //       super.channelReadComplete(ctx);
  //       ctx.flush(); // 4
  //   }

  //   @Override
  //   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
  //       System.out.println("exceptionCaught");
  //       if(null != cause) cause.printStackTrace();
  //       if(null != ctx) ctx.close();
  //   }

  // }
}
