import React from "react";
import compose from "recompose/compose";
import { graphql } from "react-apollo";
import gql from "graphql-tag";
import { withStyles } from "material-ui/styles";

import Typography from "material-ui/Typography";

const contentStyle = theme => ({

});

const query = gql`
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

const enhance = compose(
  graphql(query, {
    options: ({ params }) => ({
      variables: {
        id: params.id
      }
    }),
  }),
  withStyles(contentStyle)
);

const Content = (props) => (
  <Typography component="pre" variant="body2">{JSON.stringify(props, null, 2)}</Typography>
);

export default enhance(Content);