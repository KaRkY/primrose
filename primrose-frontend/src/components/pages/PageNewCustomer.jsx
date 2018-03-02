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

export const createCustomer = gql`
  mutation createCustomer($customer: CustomerInput!) {
    createCustomer(customer: $customer)
  }
`;


const Content = ({
  classes,
  axios,
  router,
}) => (
    <Post data={{
      query: loadTypes.loc.source.body,
    }}>{(typesErrors, typesResponse, isTypesLoading, reloadTypes) => (
      <Paper className={classes.root}>
        <CustomerForm
          customerTypes={{
            isLoading: isTypesLoading,
            values: (typesResponse && typesResponse.data && typesResponse.data.data.customerTypes) || []
          }}
          customerRelationTypes={{
            isLoading: isTypesLoading,
            values: (typesResponse && typesResponse.data && typesResponse.data.data.customerRelationTypes) || []
          }}
          onSave={customer => {
            return axios
              .request({
                method: "post",
                data: {
                  query: createCustomer.loc.source.body,
                  variables: {
                    customer,
                  }
                }
              })
              .then(response => {
                router.history.navigate(router.history.toHref({
                  pathname: router.addons.pathname("Customers"),
                }));
              });
          }}
        />
      </Paper>
    )}</Post>
  );

export default enhance(Content);