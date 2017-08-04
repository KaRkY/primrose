package primrose.util;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SearchRequestValidator implements Validator {

  @Override
  public boolean supports(final Class<?> clazz) {
    return SearchRequest.class.equals(clazz);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    final SearchRequest searchRequest = (SearchRequest) target;

    if (searchRequest.getPage() != null && searchRequest.getSize() == null) {
      errors.rejectValue("size", "empty", "When page is specified size is mendatory.");
    }
  }

}
