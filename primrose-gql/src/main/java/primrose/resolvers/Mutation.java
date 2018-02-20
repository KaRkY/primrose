package primrose.resolvers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

import primrose.mutation.CustomerMutation;

@Component
public class Mutation implements GraphQLMutationResolver {

  private CustomerMutation customerMutation;

  public Mutation(CustomerMutation customerMutation) {
    this.customerMutation = customerMutation;
  }

  public List<Long> deleteCustomers(List<Long> ids) {
    customerMutation.deleteCustomers(ids);
    return ids;
  }
}