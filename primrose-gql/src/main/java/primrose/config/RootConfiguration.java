package primrose.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.slugify.Slugify;
import com.googlecode.jsonrpc4j.AnnotationsErrorResolver;
import com.googlecode.jsonrpc4j.DefaultErrorResolver;
import com.googlecode.jsonrpc4j.ErrorResolver;
import com.googlecode.jsonrpc4j.MultipleErrorResolver;

import primrose.error.CustomErrorResolver;

@Configuration
public class RootConfiguration {

   @Bean
   public static ErrorResolver resolver() {
      return new MultipleErrorResolver(
         new CustomErrorResolver(),
         AnnotationsErrorResolver.INSTANCE,
         DefaultErrorResolver.INSTANCE);
   }

   @Bean
   public Slugify slugify() {
      Slugify slugify = new Slugify();
      slugify.withTransliterator(true);
      slugify.withLowerCase(true);
      return slugify;
   }
}
