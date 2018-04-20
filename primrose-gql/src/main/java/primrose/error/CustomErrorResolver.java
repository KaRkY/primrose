package primrose.error;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.Errors;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.ErrorResolver;

public class CustomErrorResolver implements ErrorResolver {

   @Override
   public JsonError resolveError(Throwable t, Method method, List<JsonNode> arguments) {
      if (t instanceof ArgumentValidationException) {
         Errors errors = ((ArgumentValidationException) t).getErrors();
         Map<String, Object> hashMap = new HashMap<>();

         if (errors.hasGlobalErrors()) {
            hashMap.put("objectErrors", errors
               .getGlobalErrors()
               .stream()
               .map(error -> new ObjectError(error.getObjectName(), error.getCode(), error.getDefaultMessage()))
               .toArray());
         }

         if (errors.hasFieldErrors()) {
            hashMap.put("fieldErrors", errors
               .getFieldErrors()
               .stream()
               .map(error -> new FieldError(error.getObjectName(), error.getField(), error.getCode(), error.getDefaultMessage(), error.getRejectedValue()))
               .toArray());
         }

         return new JsonError(-32600, "Invalid Request", hashMap);
      }
      return null;
   }

}
