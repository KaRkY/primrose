package primrose.spring;

import org.springframework.stereotype.Component;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;

import primrose.error.ArgumentValidationException;

@Component
public class ValidationSupport {

  private Validator            validator;
  private MessageCodesResolver messageCodesResolver;

  public ValidationSupport(Validator validator, MessageCodesResolver messageCodesResolver) {
    this.validator = validator;
    this.messageCodesResolver = messageCodesResolver;
  }

  // TODO: Make nullable an nonNullable validation
  public void validate(String paramName, Object value) {
    DirectFieldBindingResult bindingResult = new DirectFieldBindingResult(value, paramName);
    bindingResult.setMessageCodesResolver(messageCodesResolver);
    validator.validate(value, bindingResult);
    if (bindingResult.hasErrors()) { throw new ArgumentValidationException(bindingResult); }
  }
}
