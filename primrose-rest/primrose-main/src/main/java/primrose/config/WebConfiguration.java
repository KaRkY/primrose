package primrose.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import primrose.accounts.AccountSearchWebArgumentResolver;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  @Override
  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new AccountSearchWebArgumentResolver());
  }

}
