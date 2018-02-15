import React from "react";
import compose from "recompose/compose";
import lifecycle from "recompose/lifecycle";
import withProps from "recompose/withProps";
import withStateHandlers from "recompose/withStateHandlers";
import { graphql } from "react-apollo";
import gql from "graphql-tag";
import { Curious } from "@curi/react";
import { withStyles } from "material-ui/styles";
import NProgress from "nprogress";

import List from "../list/List";

const contentStyle = theme => ({

});

const loadCustomers = gql`
query loadCustomers($pageable: Pageable, $sort: [PropertySort]) {
  customers(pageable: $pageable, sort: $sort) {
    pageNumber
    pageSize
    totalSize
    data {
      id
      fullName
      displayName
      email
      phone
    }
  }
}
`;

const deleteCustomers = gql`
mutation deleteCustomers($ids: [ID]!) {
	deleteCustomers(ids: $ids)
}
`;

window.loadCustomers = loadCustomers;

const parseDirection = (dir) => {
  if (dir && (dir.toUpperCase() === "ASC" || dir.toUpperCase() === "DESC" || dir.toUpperCase() === "DEFAULT")) {
    return dir.toUpperCase();
  }
};

const enhance = compose(
  withStateHandlers(() => ({
    selectedRows: [],
    deleting: false,
  }), {
      selectRow: ({ selectedRows, ...rest }) => (value, checked) => {
        if (checked) {
          return {
            selectedRows: value,
            ...rest
          };
        } else {
          return {
            selectedRows: selectedRows.filter(sr => value.find(vr => vr === sr) ? false : true),
            ...rest
          };
        }
      },
      clearSelection: ({ selectedRows, ...rest }) => () => ({ selectedRows: [], ...rest }),
      onDeleting: ({ deleting, ...rest }) => (value) => ({ deleting: value, ...rest }),
    }),
  graphql(loadCustomers, {
    options: ({ params, query }) => ({
      fetchPolicy: "network-only",
      notifyOnNetworkStatusChange: true,
      variables: {
        pageable: {
          pageNumber: (query && query.page) || 0,
          pageSize: (query && query.size) || 10
        },
        sort: (query && query.sortProperty && [{
          propertyName: query.sortProperty,
          direction: parseDirection(query.sortDirection)
        }]) || []
      },

    }),
    props: ({ data, ownProps, ...rest }) => ({
      customers: data.customers && data.customers.data,
      currentPage: data.customers && data.customers.pageNumber,
      currentSize: data.customers && data.customers.pageSize,
      networkStatus: data.networkStatus,
      size: parseInt(data.variables.pageable.pageSize, 10),
      page: parseInt(data.variables.pageable.pageNumber, 10),
      sort: data.variables.sort && data.variables.sort[0],
      total: data.customers && data.customers.totalSize,
      loading: data.loading,
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
          })
      },
    }),
    options: { refetchQueries: [{ query: loadCustomers }] }
  }),
  withProps(({ networkStatus }) => ({
    loadPage: networkStatus === 1,
    loading: [1, 2, 4, 6].indexOf(networkStatus) > -1
  })),
  lifecycle({
    componentDidMount() {
      if (this.props.loadPage) {
        NProgress.start();
      }
    },

    componentWillReceiveProps(nextProps) {
      if (this.props.loadPage && !nextProps.loadPage) {
        NProgress.done();
      }
    }
  }),
  withStyles(contentStyle)
);

const pageSizeChange = (router, response) => (size) => {
  const location = router.history.toHref({
    pathname: router.addons.pathname(response.name, response.params),
    query: Object.assign({}, response.location.query, { size }),
  });
  router.history.navigate(location);
};

const pageChange = (router, response) => (page) => {
  const location = router.history.toHref({
    pathname: router.addons.pathname(response.name, response.params),
    query: Object.assign({}, response.location.query, { page }),
  });
  router.history.navigate(location);
};

const sortChange = (router, response) => (property) => {
  const prop = response.location.query && response.location.query.sortProperty;
  const dir = response.location.query
    && response.location.query.sortDirection
    && response.location.query.sortDirection.toUpperCase();

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

  const location = router.history.toHref({
    pathname: router.addons.pathname(response.name, response.params),
    query: Object.assign({}, response.location.query, { sortProperty, sortDirection }),
  });
  router.history.navigate(location);
};

const getRowId = row => row.id;

const Content = ({ customers, page, size, total, sort, loading, deleting, selectRow, clearSelection, selectedRows, deleteCustomers }) => (
  <Curious>{({ router, response, navigation }) => (
    <List title="Customers"
      columns={[
        { id: "id", label: "Id", numeric: true, disablePadding: true },
        { id: "fullName", label: "Full name" },
        { id: "displayName", label: "Display name" },
        { id: "phone", label: "Phone" },
        { id: "email", label: "Email" },
      ]}
      selectable
      loading={loading}
      deleting={console.log(deleting) || deleting}
      rowId={getRowId}
      data={customers || []}
      totalSize={total || 0}
      pageNumber={page}
      pageSize={size}
      selectedRows={selectedRows}
      isSortedColumn={column => sort && (column.id === sort.propertyName)}
      sortDirection={sort && sort.direction.toLowerCase()}
      onSelectRows={selectRow}
      onPageChange={pageChange(router, response)}
      onRowsPerPageChange={pageSizeChange(router, response)}
      onSortChange={sortChange(router, response)}
      onDelete={deleteCustomers}
    />
  )}</Curious>
);

export default enhance(Content);