package primrose.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.googlecode.jsonrpc4j.ErrorResolver;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
   private static Converter<String, String> CASE_CONVERT = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN);

   @Bean
   public MessageCodesResolver messageCode() {
      return new MessageCodesResolver() {

         @Override
         public String[] resolveMessageCodes(String errorCode, String objectName, String field, Class<?> fieldType) {
            String newCode = CASE_CONVERT.convert(errorCode);
            return new String[] { String.join(".", newCode, objectName, field) };
         }

         @Override
         public String[] resolveMessageCodes(String errorCode, String objectName) {
            String newCode = CASE_CONVERT.convert(errorCode);
            return new String[] { String.join(".", newCode, objectName) };
         }
      };
   }

   @Bean
   public static AutoJsonRpcServiceImplExporter exporter(ErrorResolver resolver) {
      AutoJsonRpcServiceImplExporter exporter = new AutoJsonRpcServiceImplExporter();
      exporter.setErrorResolver(resolver);
      exporter.setShouldLogInvocationErrors(false);
      exporter.setRethrowExceptions(false);
      exporter.setRegisterTraceInterceptor(true);
      return exporter;
   }
}
