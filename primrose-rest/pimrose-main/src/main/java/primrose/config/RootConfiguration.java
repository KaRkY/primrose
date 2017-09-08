package primrose.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

@Configuration
@EnableScheduling
public class RootConfiguration {

  @Bean
  public Module guava() {
    return new GuavaModule();
  }
}
