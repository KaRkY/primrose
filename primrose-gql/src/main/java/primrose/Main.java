package primrose;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.jndi.JndiLocatorDelegate;

@SpringBootApplication
public class Main extends SpringBootServletInitializer {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final JndiLocatorDelegate jndi = JndiLocatorDelegate.createDefaultResourceRefLocator();

  public static void main(final String[] args) {
    SpringApplication.run(Main.class, args);
//
//    final String schema = "type Query{hello(id: String): [Account]} type Account{id: String, name: String}";
//
//    final SchemaParser schemaParser = new SchemaParser();
//    final TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);
//
//    final TypeRuntimeWiring queryTypeWiring = TypeRuntimeWiring
//      .newTypeWiring("Query")
//      .dataFetcher("hello", env -> Arrays.asList(
//        ImmutableAccount.builder()
//        .id(env.getArgument("id"))
//        .name("test0")
//        .build(),
//        ImmutableAccount.builder()
//        .id("test1")
//        .name("test1")
//        .build(),
//        ImmutableAccount.builder()
//        .id("test2")
//        .name("test2")
//        .build()))
//      .build();
//
//    final RuntimeWiring runtimeWiring = RuntimeWiring
//      .newRuntimeWiring()
//      .wiringFactory(new WiringFactory() {
//        @Override
//        public DataFetcher<?> getDefaultDataFetcher(final FieldWiringEnvironment environment) {
//          return new PropertyDataFetcher<>(environment.getFieldDefinition().getName());
//        }
//      })
//      .type(queryTypeWiring)
//      .build();
//
//    final SchemaGenerator schemaGenerator = new SchemaGenerator();
//    final GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
//
//    final GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
//    final ExecutionResult executionResult = build.execute("{hello(id: \"11\"){ id, name}}");
//
//    System.out.println(executionResult.toSpecification());
  }

  @Override
  protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {

    try {
      final String profile = jndi.lookup("spring.profile.active", String.class);
      builder.profiles(profile);
    } catch (final NamingException e) {
      // If lookup is missing do nothing
      logger.info("Ignoring missing jndi profile name.");
    }

    return builder.sources(Main.class);
  }

}
