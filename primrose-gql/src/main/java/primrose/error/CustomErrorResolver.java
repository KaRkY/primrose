package primrose.error;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.ErrorResolver;

public class CustomErrorResolver implements ErrorResolver {

  @Override
  public JsonError resolveError(Throwable t, Method method, List<JsonNode> arguments) {
    if (t instanceof ArgumentValidationException) {
      Map<String,Object> hashMap = new HashMap<>();
      hashMap.put("fields", ((ArgumentValidationException) t).getFields());
      return new JsonError(-32600, "Missing field", hashMap);
    }
    return null;
  }

}
