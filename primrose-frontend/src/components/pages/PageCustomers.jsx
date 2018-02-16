import React from "react";
import compose from "recompose/compose";
import withProps from "recompose/withProps";
import withStateHandlers from "recompose/withStateHandlers";
import withHandlers from "recompose/withHandlers";
import { withStyles } from "material-ui/styles";

import { graphql } from "react-apollo";
import gql from "graphql-tag";
import get from "lodash/get";
import difference from "lodash/difference";
import union from "lodash/union";

import List from "../list/List";
import Paper from "material-ui/Paper";
import Typography from "material-ui/Typography";

const contentStyle = theme => ({

});

const loadCustomers = gql`
query loadCustomers($pageable: Pageable, $sort: [PropertySort]) {
  customers(pageable: $pageable, sort: $sort) {
    id
    fullName
    displayName
    email
    phone
  }
  customersCount
}
`;

const deleteCustomers = gql`
mutation deleteCustomers($ids: [ID]!) {
	deleteCustomers(ids: $ids)
}
`;

const enhance = compose(
  withHandlers({
    onPageChange: ({ router, response: { name, params, location: { query } } }) => (event, page) => {
      router.history.navigate(router.history.toHref({
        pathname: router.addons.pathname(name, params),
        query: Object.assign({}, query, { page }),
      }));
    },
    onPageSizeChange: ({ router, response: { name, params, location: { query } } }) => (event, size) => {
      router.history.navigate(router.history.toHref({
        pathname: router.addons.pathname(name, params),
        query: Object.assign({}, query, { size }),
      }));
    },
    onSortChange: ({ router, response: { name, params, location: { query } } }) => (event, property) => {
      router.history.navigate(router.history.toHref({
        pathname: router.addons.pathname(name, params),
        query: Object.assign({}, query, parseSort(query, property)),
      }));
    },
  }),

  withStateHandlers(
    () => ({ selectedRows: [], deleting: false, }),
    {
      selectRow: ({ selectedRows, ...rest }) => (event, value, checked) => ({
        selectedRows: checked ? union(selectedRows, value) : difference(selectedRows, value),
        ...rest
      }),
      clearSelection: ({ selectedRows, ...rest }) => () => ({ selectedRows: [], ...rest }),
      onDeleting: ({ deleting, ...rest }) => (value) => ({ deleting: value, ...rest }),
    }
  ),

  withProps((props) => ({
    pageSize: parseInt(get(props, "response.location.query.size", 10), 10),
    pageNumber: parseInt(get(props, "response.location.query.page", 0), 10),
    sortProperty: get(props, "response.location.query.sortProperty"),
    sortDirection: get(props, "response.location.query.sortDirection"),
  })),

  graphql(loadCustomers, {
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
  }),

  graphql(deleteCustomers, {
    props: ({ mutate, ownProps }) => ({
      deleteCustomers: () => {
        ownProps.onDeleting(true);
        return mutate({
          variables: {
            ids: ownProps.selectedRows
          },

          refetchQueries: ["loadCustomers"],
        })
          .then(result => result.data.deleteCustomers)
          .then(result => {
            ownProps.selectRow(result, false);
            ownProps.onDeleting(false);
            return result;
          });
      },
    }),
    options: { refetchQueries: [{ query: loadCustomers }] }
  }),

  withProps((props) => ({ loadPage: props.networkStatus === 1, loading: [1, 2, 4, 6].indexOf(props.networkStatus) > -1 })),

  withStyles(contentStyle)
);

const getRowId = row => row.id;

const Content = ({
  customers,
  pageNumber,
  pageSize,
  totalSize,
  sortProperty,
  sortDirection,
  loading,
  deleting,
  selectRow,
  clearSelection,
  selectedRows,
  deleteCustomers,
  onPageChange,
  onPageSizeChange,
  onSortChange }) => (
    <List title="Customers"
      columns={[
        { id: "id", label: "Id", numeric: true, disablePadding: true },
        { id: "fullName", label: "Full name" },
        { id: "displayName", label: "Display name" },
        { id: "phone", label: "Phone" },
        { id: "email", label: "Email" },
      ]}
      selectable
      detailed
      loading={loading}
      deleting={deleting}
      rowId={getRowId}
      rows={customers || []}
      totalSize={totalSize || 0}
      pageNumber={pageNumber}
      pageSize={pageSize}
      selectedRows={selectedRows}
      sortColumn={sortProperty}
      sortDirection={sortDirection && sortDirection.toLowerCase()}
      onSelectRows={selectRow}
      onPageChange={onPageChange}
      onPageSizeChange={onPageSizeChange}
      onSortChange={onSortChange}
      onDelete={deleteCustomers}
      renderPanel={(row) => (
        <Paper>
          <Typography type="body2" component="pre">{JSON.stringify(row, null, 2)}</Typography>
        </Paper>
      )}
    />
  );

export default enhance(Content);

const parseSort = (query, property) => {
  const prop = query && query.sortProperty;
  const dir = query && query.sortDirection && query.sortDirection.toUpperCase();

  let sortProperty;
  let sortDirection;
  if (prop === property) {
    sortProperty = prop;
    switch (dir) {
      case "ASC":
        sortDirection = "DESC";
        break;

      case "DESC":
        sortDirection = undefined;
        sortProperty = undefined;
        break;

      default:
        sortDirection = "ASC";
        break;
    }
  } else {
    sortProperty = property;
    sortDirection = "ASC";
  }

  return {
    sortProperty,
    sortDirection
  };
};

const parseDirection = (dir) => {
  if (dir && (dir.toUpperCase() === "ASC" || dir.toUpperCase() === "DESC" || dir.toUpperCase() === "DEFAULT")) {
    return dir.toUpperCase();
  }
};