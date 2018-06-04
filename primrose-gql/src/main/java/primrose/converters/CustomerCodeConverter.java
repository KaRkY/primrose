package primrose.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import primrose.rest.customer.CustomerCode;

@Component
public class CustomerCodeConverter implements Converter<String, CustomerCode> {

  @Override
  public CustomerCode convert(String source) {
    return CustomerCode.of(source);
  }

}
