package primrose.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import primrose.immutables.ImmutableInputAccount;
import primrose.immutables.ImmutableInputAccountAddress;
import primrose.immutables.ImmutableInputAccountContact;
import primrose.model.input.BaseInputAccount;
import primrose.model.input.BaseInputAccountAddress;
import primrose.model.input.BaseInputAccountContact;
import primrose.spring.jwt.JwtProperties;

@Configuration
@EnableScheduling
@EnableCaching
@EnableConfigurationProperties({ JwtProperties.class })
public class RootConfiguration {

  @Bean
  public Module module() {
    final SimpleModule simpleModule = new SimpleModule();

    simpleModule.addAbstractTypeMapping(BaseInputAccountAddress.class, ImmutableInputAccountAddress.class);
    simpleModule.addAbstractTypeMapping(BaseInputAccountContact.class, ImmutableInputAccountContact.class);
    simpleModule.addAbstractTypeMapping(BaseInputAccount.class, ImmutableInputAccount.class);

    return simpleModule;
  }
}
