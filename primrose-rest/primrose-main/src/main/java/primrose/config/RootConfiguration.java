package primrose.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import primrose.spring.jwt.JwtProperties;

@Configuration
@EnableScheduling
@EnableCaching
@EnableConfigurationProperties({ JwtProperties.class })
public class RootConfiguration {

}
