import get from "lodash/get";

import PageHome from "./components/pages/PageHome";
import PageCustomers from "./components/pages/PageCustomers";
import PageCustomer from "./components/pages/PageCustomer";
import PageNotFound from "./components/pages/PageNotFound";

export default [{
  name: "Home",
  path: "",
  match: {
    response: ({ set }) => {
      set.body(PageHome);
      set.title("Home");
    }
  },
}, {
  name: "Customers",
  path: "customers",
  match: {
    response: function({ set,  route }) {
      const {name, params, location: { query }} = route;

      set.data({
        pageSize: parseInt(query.size || 10, 10),
        pageNumber: parseInt(query.page || 0, 10),
        sortProperty: query.sortProperty,
        sortDirection: query.sortDirection,
        onPageChange: (router, page) => {
          router.history.navigate(router.history.toHref({
            pathname: router.addons.pathname(name, params),
            query: Object.assign({}, query, { page }),
          }));
        },
        onPageSizeChange: (router, size) => {
          router.history.navigate(router.history.toHref({
            pathname: router.addons.pathname(name, params),
            query: Object.assign({}, query, { size }),
          }));
        },
        onSortChange: (router, property) => {
          router.history.navigate(router.history.toHref({
            pathname: router.addons.pathname(name, params),
            query: Object.assign({}, query, parseSort(query, property)),
          }));
        },
      });
      set.body(PageCustomers);
      set.title("Customers");
    }
  },
  children: [{
    name: "Customer",
    path: ":id",
    match: {
      response: ({ set, resolved }) => {
        set.body(PageCustomer);
        set.title("Customer");
      }
    },
  }]
},{
  name: "Not Found",
  path: "(.*)",
  match: {
    response: ({ set }) => {
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