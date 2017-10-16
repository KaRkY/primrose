package primrose.graphql;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.introspection.IntrospectionQuery;
import graphql.schema.GraphQLSchema;

@Controller
@RequestMapping(path = "/graphql")
public class GQLController {

  private final GraphQLSchema schema;
  private final ObjectMapper objectMapper;

  public GQLController(
    final Optional<GraphQLSchema> schema,
    final ObjectMapper objectMapper) {
    this.schema = schema.orElse(null);
    this.objectMapper = objectMapper;
  }

  @GetMapping("/schema.json")
  @ResponseBody
  public Map<String, Object> schema(
    @RequestParam("query") final String query,
    @RequestParam("variables") final String variables,
    @RequestParam("operationName") final String operationName) {
    final GraphQL graph = GraphQL.newGraphQL(schema).build();

    return graph.execute(ExecutionInput.newExecutionInput()
      .query(IntrospectionQuery.INTROSPECTION_QUERY)
      .build())
      .toSpecification();
  }

  @GetMapping(params = { "query" })
  @ResponseBody
  public Map<String, Object> getQuery(
    @RequestParam("query") final String query,
    @RequestParam("variables") final String variables,
    @RequestParam("operationName") final String operationName) {
    final GraphQL graph = GraphQL.newGraphQL(schema).build();

    return graph.execute(ExecutionInput.newExecutionInput()
      .query(query)
      .operationName(operationName)
      .variables(deserializeVariablesObject(variables))
      .build())
      .toSpecification();
  }

  @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @ResponseBody
  public Map<String, Object> postQuery(
    @RequestParam("query") final String query,
    @RequestParam("variables") final String variables,
    @RequestParam("operationName") final String operationName) {
    final GraphQL graph = GraphQL.newGraphQL(schema).build();

    return graph.execute(ExecutionInput.newExecutionInput()
      .query(query)
      .operationName(operationName)
      .variables(deserializeVariablesObject(variables))
      .build())
      .toSpecification();
  }

  @PostMapping
  @ResponseBody
  public Map<String, Object> postBodyQuery(
    @RequestBody final String body) {
    try {
      //final GraphQL graph = GraphQL.newGraphQL(schema).build();
      final Map<String, Object> req = objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {
      });

      req.forEach((key, value) -> System.out.println(String.format("key = %s, value = %s", key, value.getClass())));

      return null;
    } catch (final IOException e) {
      System.out.println(e);
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> deserializeVariablesObject(final Object variables) {
    if (variables instanceof Map) {
      return (Map<String, Object>) variables;
    } else if (variables instanceof String) {
      try {
        return objectMapper.readValue((String) variables, new TypeReference<Map<String, Object>>() {
        });
      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      throw new RuntimeException("variables should be either an object or a string");
    }
  }
}
