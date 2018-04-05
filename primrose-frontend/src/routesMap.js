//import { redirect, NOT_FOUND } from "redux-first-router";
import axios from "./axios";
import gql from "graphql-tag";
import { normalize, schema } from "normalizr";
import getQuery from "./selectors/getQuery";
import isCustomersPage from "./selectors/isCustomersPage";
import getPageId from "./util/getPageId";

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

const customer = new schema.Entity("customers", {
});

const parseDirection = (dir) => {
  if (dir && (dir.toUpperCase() === "ASC" || dir.toUpperCase() === "DESC" || dir.toUpperCase() === "DEFAULT")) {
    return dir.toUpperCase();
  }
};

export default {
  HOME: {
    path: "/",
  },
  CUSTOMERS: {
    path: "/customers",
    thunk: async (dispatch, getState) => {
      const query = getQuery(getState());
      const { page = 0, size = 10, sortProperty, sortDirection } = query;

      if(isCustomersPage(getState())) return;

      const { data } = await axios.request({
        method: "post",
        data: {
          query: loadCustomers.loc.source.body,
          variables: {
            pageable: {
              pageNumber: page,
              pageSize: size,
            },
            sort: (sortProperty && [{
              propertyName: sortProperty,
              direction: parseDirection(sortDirection)
            }]) || []
          }
        },
      });

      const normalizedCustomers = normalize(data.data, {customers: [customer]});

      dispatch({
        type: "CUSTOMERS_FETCHED",
        payload: {
          ...normalizedCustomers,
          pageId: getPageId(query),
        }
      });
    },
  },
  CUSTOMER_NEW: {
    path: "/customers/new",
  },
  CUSTOMER: {
    path: "/customers/:id",
  },
  CUSTOMER_EDIT: {
    path: "/customers/:id/edit",
  },
};