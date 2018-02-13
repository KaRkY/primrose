import React from "react";
import compose from "recompose/compose";
import { graphql } from "react-apollo";
import gql from "graphql-tag";
import { Curious } from "@curi/react";
import { withStyles } from "material-ui/styles";

import List from "../list/List";

const contentStyle = theme => ({

});

const query = gql`
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

const parseDirection = (dir) => {
  if (dir && (dir.toUpperCase() === "ASC" || dir.toUpperCase() === "DESC" || dir.toUpperCase() === "DEFAULT")) {
    return dir.toUpperCase();
  }
};

const enhance = compose(
  graphql(query, {
    options: ({ params, query }) => ({
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
    props: ({ data, ownProps }) => ({
      customers: data.customers && data.customers.data,
      currentPage: data.customers && data.customers.pageNumber,
      currentSize: data.customers && data.customers.pageSize,
      size: parseInt(data.variables.pageable.pageSize),
      page: parseInt(data.variables.pageable.pageNumber),
      sort: data.variables.sort && data.variables.sort[0],
      total: data.customers && data.customers.totalSize,
      loading: data.loading,
      error: data.error,
    }),
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

const Content = ({ customers, page, size, total, sort }) => (
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
      getRowId={getRowId}
      data={customers}
      count={total || 0}
      page={page}
      orderBy={sort && sort.propertyName}
      order={sort && sort.direction.toLowerCase()}
      onChangePage={pageChange(router, response)}
      onChangeRowsPerPage={pageSizeChange(router, response)}
      onSortHandler={sortChange(router, response)}
      rowsPerPage={size}
    />
  )}</Curious>
);

export default enhance(Content);