package primrose.controllers;

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
import graphql.execution.batched.BatchedExecutionStrategy;
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
  public Map<String, Object> schema() {
    return query(IntrospectionQuery.INTROSPECTION_QUERY, null, null);
  }

  @GetMapping(params = { "query" })
  @ResponseBody
  public Map<String, Object> getQuery(
    @RequestParam("query") final String query,
    @RequestParam("variables") final String variables,
    @RequestParam("operationName") final String operationName) {
    return query(query, deserializeVariablesObject(variables), operationName);
  }

  @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @ResponseBody
  public Map<String, Object> postQuery(
    @RequestParam("query") final String query,
    @RequestParam("variables") final String variables,
    @RequestParam("operationName") final String operationName) {
    return query(query, deserializeVariablesObject(variables), operationName);
  }

  @SuppressWarnings("unchecked")
  @PostMapping
  @ResponseBody
  public Map<String, Object> postBodyQuery(
    @RequestBody final String body) {
    try {
      // final GraphQL graph = GraphQL.newGraphQL(schema).build();
      final Map<String, Object> req = objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {
      });

      String query = null;
      if (req.get("query") != null) {
        query = req.get("query").toString();

        Map<String, Object> variables = null;
        if (req.get("variables") != null) {
          variables = (Map<String, Object>) req.get("variables");
        }

        String operationName = null;
        if (req.get("operationName") != null) {
          operationName = req.get("operationName").toString();
        }
        return query(query, variables, operationName);
      } else {
        throw new RuntimeException();
      }

    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Map<String, Object> query(final String query, final Map<String, Object> variables, final String operationName) {
    final ExecutionInput.Builder executionBuilder = ExecutionInput.newExecutionInput();
    if (query != null) {
      executionBuilder.query(query);
      if (variables != null) {
        executionBuilder.variables(variables);
      }
      if (operationName != null) {
        executionBuilder.operationName(operationName);
      }

      return GraphQL
        .newGraphQL(schema)
        .queryExecutionStrategy(new BatchedExecutionStrategy())
        .build()
        .execute(executionBuilder.build())
        .toSpecification();
    } else {
      throw new RuntimeException();
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
      return null;
    }
  }
}
