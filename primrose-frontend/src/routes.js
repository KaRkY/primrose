import difference from "lodash/difference";
import union from "lodash/union";
import normalizeArray from "./util/normalizeArray";

import PageHome from "./components/pages/PageHome";
import PageCustomers from "./components/pages/PageCustomers";
import PageCustomer from "./components/pages/PageCustomer";
import PageNewCustomer from "./components/pages/PageNewCustomer";
import PageNotFound from "./components/pages/PageNotFound";

export default [{
  name: "Home",
  path: "",
  match: {
    response: ({
      set
    }) => {
      set.body(PageHome);
      set.title("Home");
    }
  },
}, {
  name: "Customers",
  path: "customers",
  match: {
    response: function ({
      set,
      route
    }) {
      const {
        name,
        params,
        location: {
          query
        }
      } = route;

      const normalizedSelectedRows = normalizeArray(query.selected);

      set.data({
        pageSize: parseInt(query.size || 10, 10),
        pageNumber: parseInt(query.page || 0, 10),
        sortProperty: query.sortProperty,
        sortDirection: query.sortDirection,
        selectedRows: normalizedSelectedRows,
        onPageChange: (router, page) => {
          router.history.navigate(router.history.toHref({
            pathname: router.addons.pathname(name, params),
            query: {
              ...query,
              page,
              selected: undefined,
            },
          }));
        },
        onPageSizeChange: (router, size) => {
          router.history.navigate(router.history.toHref({
            pathname: router.addons.pathname(name, params),
            query: {
              ...query,
              size,
              selected: undefined,
            },
          }));
        },
        onSortChange: (router, property) => {
          router.history.navigate(router.history.toHref({
            pathname: router.addons.pathname(name, params),
            query: {
              ...query,
              ...parseSort(query, property),
              selected: undefined,
            },
          }));
        },
        onSelectedRowsChange: (router, selectedRows, checked) => {
          router.history.replace(router.history.toHref({
            pathname: router.addons.pathname(name, params),
            query: {
              ...query,
              selected: (checked ? union(normalizedSelectedRows, selectedRows) : difference(normalizedSelectedRows, selectedRows))
            },
          }));
        },
        onNewCustomer: (router) => {
          router.history.navigate(router.history.toHref({
            pathname: router.addons.pathname("NewCustomer"),
          }));
        },
      });
      set.body(PageCustomers);
      set.title("Customers");
    }
  },
  children: [{
    name: "NewCustomer",
    path: "new",
    match: {
      response: ({
        set,
        resolved
      }) => {
        set.body(PageNewCustomer);
        set.title("New Customer");
      }
    },
  }, {
    name: "Customer",
    path: ":id",
    match: {
      response: ({
        set,
        resolved
      }) => {
        set.body(PageCustomer);
        set.title("Customer");
      }
    },
  }]
}, {
  name: "Not Found",
  path: "(.*)",
  match: {
    response: ({
      set
    }) => {
      set.body(PageNotFound);
      set.title("Not Found");
    }
  },
}];

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