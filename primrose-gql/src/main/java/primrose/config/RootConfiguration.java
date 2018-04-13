package primrose.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;

@Configuration
public class RootConfiguration {

  @Bean
  public AutoJsonRpcServiceImplExporter exporter() {
    return new AutoJsonRpcServiceImplExporter();
  }

}
