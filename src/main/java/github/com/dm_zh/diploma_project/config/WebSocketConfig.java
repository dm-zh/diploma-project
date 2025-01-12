package github.com.dm_zh.diploma_project.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.handler.SuccessAuthorizationListener;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;

@Configuration
public class WebSocketConfig {

  @Value("${socket.host}")
  private String host;

  @Value("${socket.port}")
  private int port;

  private SocketIOServer server;

  @Bean
  public SocketIOServer socketIOServer() throws Exception {
    com.corundumstudio.socketio.Configuration config =
        new com.corundumstudio.socketio.Configuration();
    config.setHostname(host);
    config.setPort(port);


    SocketIOServer socketIOServer = new SocketIOServer(config);
    this.server = socketIOServer;
    return socketIOServer;
  }

  @PreDestroy
  public void stopSocketIOServer() {
    this.server.stop();
  }


}
