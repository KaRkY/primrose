package primrose.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

import primrose.spring.CachedSQLLoader;
import primrose.spring.ClasspathResourceLoader;
import primrose.spring.FileSQLLoader;
import primrose.spring.SQLLoader;

@Configuration
@EnableScheduling
@EnableCaching
public class RootConfiguration {

  @Bean
  public Module guava() {
    return new GuavaModule();
  }

  @Bean
  public SQLLoader sqlLoader() {
    return new CachedSQLLoader(new FileSQLLoader(new ClasspathResourceLoader()));
  }
}
