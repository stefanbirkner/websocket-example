import java.io.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.*;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.server.*;
import org.eclipse.jetty.websocket.server.config.*;

class WebSocketServer
{
  public static void main(
    String... args
  ) throws Exception {
    var server = new WebSocketServer();
    server.stopOnShutdown();
    server.start();
  }

  final Server server;
  
  WebSocketServer(
  ) {
    this.server = new Server();
  }

  void start(
  ) throws Exception {
    var connector = new ServerConnector(server);
    server.addConnector(connector);

    var handler = createServletAndHandler();
    server.setHandler(handler);

    server.start();
    System.out.println(
      "Server started at port " + connector.getLocalPort()
    );
  }

  private ServletContextHandler createServletAndHandler(
  ) {
    var servlet = new JettyWebSocketServlet() {
      @Override
      protected void configure(
        JettyWebSocketServletFactory factory
      ) {
        factory.addMapping("/", (req, res) -> new EchoSocket());
      }
    };
    var handler = new ServletContextHandler();
    handler.addServlet(
      new ServletHolder(servlet),
      "/ws"
    );
    JettyWebSocketServletContainerInitializer.configure(handler, null);
    return handler;
  }

  void stopOnShutdown(
  ) {
    Runtime.getRuntime().addShutdownHook(
      new Thread(this::safeStop)
    );
  }

  private void safeStop(
  ) {
    System.out.println("Shutting down WebSocketServer");
    try {
      server.stop();
      System.out.println("Exiting WebSocketServer");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @WebSocket
  public static class EchoSocket
  {
    private Session session;

    @OnWebSocketConnect
    public void onWebSocketConnect(
      Session session
    ) {
      this.session = session;
    }

    @OnWebSocketMessage
    public void onWebSocketText(
      String message
    ) throws IOException {
        if (session != null && session.isOpen())
        {
          session.getRemote().sendString(message);
        }
    }
  }
}