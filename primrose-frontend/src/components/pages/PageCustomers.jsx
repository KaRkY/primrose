import React from "react";
import compose from "recompose/compose";
import { graphql } from "react-apollo";
import gql from "graphql-tag";
import { withStyles } from "material-ui/styles";

import {
  SortingState,
  PagingState,
  IntegratedPaging,
  IntegratedSorting,
} from "@devexpress/dx-react-grid";
import {
  Grid,
  Table,
  TableHeaderRow,
  PagingPanel,
} from "@devexpress/dx-react-grid-material-ui";
import Paper from "material-ui/Paper";

const contentStyle = theme => ({

});

const query = gql`
query loadCustomers($pageable: Pageable) {
  customers(pageable: $pageable) {
    pageNumber
    pageSize
    totalSize
    data {
      id
      fullName
      displayName
    }
  }
}
`;

const enhance = compose(
  graphql(query, {
    options: ({ params, query }) => ({
      variables: {
        pageable: {
          pageNumber: (query && query.page) || 0,
          pageSize: (query && query.size) || 10
        }
      },

    }),
    props: ({ data }) => ({
      customers: data.customers && data.customers.data,
      page: data.customers && data.customers.pageNumber,
      size: data.customers && data.customers.pageSize,
      total: data.customers && data.customers.totalSize,
      loading: data.loading,
      error: data.error,
    }),
  }),
  withStyles(contentStyle)
);

const getRowId = row => row.id;

const Content = ({ customers, page, size, total }) => (
  <Paper>
    <Grid
      rows={customers || []}
      columns={[
        { name: "id", title: "Id" },
        { name: "fullName", title: "Full name"}]}
      getRowId={getRowId}
    >

      <Table/>
    </Grid>
  </Paper>
);

export default enhance(Content);