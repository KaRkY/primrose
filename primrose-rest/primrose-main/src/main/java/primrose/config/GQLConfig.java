package primrose.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import primrose.graphql.scalars.GraphQLLocalDateTime;
import primrose.immutables.graphql.PropertyDataFetcher;

@Configuration
public class GQLConfig {

  private final ApplicationContext context;
  private final GQLDataFetchers    dataFetchers;

  public GQLConfig(
    final ApplicationContext context,
    final GQLDataFetchers dataFetchers) {
    this.context = context;
    this.dataFetchers = dataFetchers;
  }

  @Bean
  public RuntimeWiring runtimeWiring() {
    return RuntimeWiring
      .newRuntimeWiring()
      .wiringFactory(new WiringFactory() {
        @Override
        public DataFetcher<?> getDefaultDataFetcher(final FieldWiringEnvironment environment) {
          return new PropertyDataFetcher<>(environment.getFieldDefinition().getName());
        }
      })
      .scalar(new GraphQLLocalDateTime())
      .type(TypeRuntimeWiring
        .newTypeWiring("Query")
        .dataFetcher("accounts", dataFetchers.queryAccounts())
        .build())
      .type(TypeRuntimeWiring
        .newTypeWiring("Account")
        .dataFetcher("addresses", dataFetchers.accountsAddresses())
        .dataFetcher("contacts", dataFetchers.accountsContacts())
        .build())
      .type(TypeRuntimeWiring
        .newTypeWiring("Mutation")
        .dataFetcher("createAccount", dataFetchers.mutationCreateAccount())
        .dataFetcher("importAccounts", dataFetchers.mutationImportAccounts()))
      .build();
  }

  @Bean
  public GraphQLSchema schema() throws IOException {
    final Resource[] resources = context.getResources("classpath*:**/*.graphqls");

    final SchemaParser schemaParser = new SchemaParser();
    final TypeDefinitionRegistry typeDefinitionRegistry = new TypeDefinitionRegistry();
    for (final Resource resource : resources) {
      try (InputStream in = resource.getInputStream();
        Reader rin = new InputStreamReader(in, StandardCharsets.UTF_8)) {
        typeDefinitionRegistry.merge(schemaParser.parse(rin));
      }
    }

    final SchemaGenerator schemaGenerator = new SchemaGenerator();
    return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring());
  }
}
