package primrose.resolvers;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

@Component
public class Mutation implements GraphQLMutationResolver {

  public long deleteCustomer(long id) {
    System.out.println(id);
    return id;
  }
}
