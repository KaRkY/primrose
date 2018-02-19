import RenderComp from "../RenderComp";

import { graphql } from "react-apollo";
import gql from "graphql-tag";

export const loadCustomers = gql`
query loadCustomers($pageable: Pageable, $sort: [PropertySort]) {
  customers(pageable: $pageable, sort: $sort) {
    id
    type
    relationType
    fullName
    displayName
  }
  customersCount
}
`;

const parseDirection = (dir) => {
  if (dir && (dir.toUpperCase() === "ASC" || dir.toUpperCase() === "DESC" || dir.toUpperCase() === "DEFAULT")) {
    return dir.toUpperCase();
  }
};

const hoc = graphql(loadCustomers, {
  options: ({ pageNumber, pageSize, sortProperty, sortDirection }) => ({
    fetchPolicy: "network-only",
    notifyOnNetworkStatusChange: true,
    variables: {
      pageable: {
        pageNumber: pageNumber,
        pageSize
      },
      sort: (sortProperty && [{
        propertyName: sortProperty,
        direction: parseDirection(sortDirection)
      }]) || []
    },

  }),
  props: ({ data }) => ({
    customers: data.customers,
    networkStatus: data.networkStatus,
    totalSize: data.customersCount,
    error: data.error,
  }),
});

const LoadCustomers = hoc(RenderComp);
export default LoadCustomers;