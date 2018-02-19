import RenderComp from "../RenderComp";

import { graphql } from "react-apollo";
import gql from "graphql-tag";

export const loadCustomer = gql`
query loadCustomer($id: ID!) {
  customer(id: $id) {
    id
    type
    relationType
    fullName
    displayName
    email
    phone
    description
  }
}
`;

const hoc = graphql(loadCustomer, {
  options: ({ id }) => ({
    fetchPolicy: "network-only",
    notifyOnNetworkStatusChange: true,
    variables: {
      id
    },

  }),
  props: ({ data }) => ({
    customer: data.customer,
    networkStatus: data.networkStatus,
    totalSize: data.customersCount,
    error: data.error,
  }),
});

const LoadCustomer = hoc(RenderComp);
export default LoadCustomer;