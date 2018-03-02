import React from "react";
import compose from "recompose/compose";
import { withAxios } from "react-axios";
import { withStyles } from "material-ui/styles";

import { Post } from "react-axios";
import gql from "graphql-tag";

import Paper from "material-ui/Paper";
import CustomerForm from "../Customer/CustomerForm";

const contentStyle = theme => ({
  root: theme.mixins.gutters({
  }),
});


const enhance = compose(
  withAxios,
  withStyles(contentStyle)
);

export const loadTypes = gql`
  query loadTypes {
    customerTypes
    customerRelationTypes
  }
`;

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

export const createCustomer = gql`
  mutation createCustomer($customer: CustomerInput!) {
    createCustomer(customer: $customer)
  }
`;

/*
  Display loading indicator
 */
const Content = ({
  classes,
  axios,
  router,
  id,
}) => (
    <Post data={{
      query: loadTypes.loc.source.body,
    }}>{(typesErrors, typesResponse, isTypesLoading, reloadTypes) => (
      <Post data={{
        query: loadCustomer.loc.source.body,
        variables: {
          id
        }
      }}>{(customerError, customerResponse, isCustomerLoading, reloadCustomer) => (
        <Paper className={classes.root}>
          <CustomerForm
            edit
            customer={(customerResponse && customerResponse.data && customerResponse.data.data.customer) || {}}
            customerTypes={{
              isLoading: isTypesLoading,
              values: (typesResponse && typesResponse.data && typesResponse.data.data.customerTypes) || []
            }}
            customerRelationTypes={{
              isLoading: isTypesLoading,
              values: (typesResponse && typesResponse.data && typesResponse.data.data.customerRelationTypes) || []
            }}
            onSave={customer => {
              console.log(customer);
            }}
          />
        </Paper>
      )}</Post>
    )}</Post>
  );

export default enhance(Content);