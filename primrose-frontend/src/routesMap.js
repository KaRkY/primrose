//import { redirect, NOT_FOUND } from "redux-first-router";
import getQuery from "./selectors/getQuery";
import isCustomersPage from "./selectors/isCustomersPage";
import getPageId from "./util/getPageId";

export default {
  HOME: {
    path: "/",
  },
  CUSTOMERS: {
    path: "/customers",
    thunk: async (dispatch, getState) => {
      const query = getQuery(getState());

      if (isCustomersPage(getState())) return;

      dispatch({
        type: "CUSTOMERS_FETCHED",
        payload: {
          entities: {},
          result: {
            customers: [],
            customersCount: 0
          },
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