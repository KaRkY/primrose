//import { redirect, NOT_FOUND } from "redux-first-router";
import getQuery from "./selectors/getQuery";
import isCustomersPage from "./selectors/customers/isCustomersPage";
import isContactsPage from "./selectors/contacts/isContactsPage";
import getPageId from "./util/getPageId";

export default {
  DASHBOARD: {
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

  CONTACTS: {
    path: "/contacts"
  },
  CONTACTS_NEW: {
    path: "/contacts/new",
    thunk: async (dispatch, getState) => {
      const query = getQuery(getState());

      if (isContactsPage(getState())) return;

      dispatch({
        type: "CONTACTS_FETCHED",
        payload: {
          entities: {},
          result: {
            contacts: [],
            contactsCount: 0
          },
          pageId: getPageId(query),
        }
      });
    },
  },
};