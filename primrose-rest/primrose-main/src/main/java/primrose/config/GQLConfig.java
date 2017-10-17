package primrose.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.FieldWiringEnvironment;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;
import graphql.schema.idl.WiringFactory;
import primrose.graphql.GQLDataFetcher;
import primrose.graphql.PropertyDataFetcher;

@Configuration
public class GQLConfig {

  private final ApplicationContext context;
  private final List<DataFetcher<?>> dataFetchers;

  public GQLConfig(
    final ApplicationContext context,
    final List<DataFetcher<?>> dataFetchers) {
    this.context = context;
    this.dataFetchers = dataFetchers;
  }

  @Bean
  public GraphQLSchema schema() throws IOException {
    final Resource[] resources = context.getResources("classpath*:**/*.graphqls");

    final StringBuilder builder = new StringBuilder();
    for (final Resource resource : resources) {
      try (InputStream in = resource.getInputStream();
        Reader rin = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader brin = new BufferedReader(rin)) {
        String line = brin.readLine();
        while (line != null) {
          builder.append(line).append("\n");
          line = brin.readLine();
        }
      }
    }

    System.out.println(builder.toString());
    final SchemaParser schemaParser = new SchemaParser();
    final TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(builder.toString());

    final SchemaGenerator schemaGenerator = new SchemaGenerator();
    return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring());
  }

  @Bean
  public RuntimeWiring runtimeWiring() {
    final Map<String, TypeRuntimeWiring.Builder> builders = new HashMap<>();

    for (final DataFetcher<?> dataFetcher : dataFetchers) {
      final GQLDataFetcher annotation = AnnotationUtils.findAnnotation(dataFetcher.getClass(), GQLDataFetcher.class);

      builders
        .computeIfAbsent(annotation.type(), TypeRuntimeWiring::newTypeWiring)
        .dataFetcher(annotation.property(), dataFetcher);
    }

    final RuntimeWiring.Builder builder = RuntimeWiring
      .newRuntimeWiring()
      .wiringFactory(new WiringFactory() {
        @Override
        public DataFetcher<?> getDefaultDataFetcher(final FieldWiringEnvironment environment) {
          return new PropertyDataFetcher<>(environment.getFieldDefinition().getName());
        }
      });

    builders.forEach((key, value) -> builder.type(value.build()));
    return builder.build();
  }
}
