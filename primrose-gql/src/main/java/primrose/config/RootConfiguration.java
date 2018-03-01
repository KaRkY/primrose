package primrose.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import graphql.execution.ExecutionStrategy;
import graphql.execution.batched.BatchedExecutionStrategy;

@Configuration
@EnableScheduling
@EnableCaching
public class RootConfiguration {

  @Bean
  public Module module() {
    final SimpleModule simpleModule = new SimpleModule();

    return simpleModule;
  }

  @Bean
  public ExecutionStrategy queryExecutionStrategy() {
    return new BatchedExecutionStrategy();
  }
}
