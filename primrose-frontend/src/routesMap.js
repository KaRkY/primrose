//import { redirect, NOT_FOUND } from "redux-first-router";
import * as actions from "./actions";
import * as customers from "./store/customers";
import * as contacts from "./store/contacts";
import * as createCustomer from "./store/createCustomer";
import * as createContact from "./store/createContact";
import * as deleteCustomers from "./store/deleteCustomers";
import * as deleteContacts from "./store/deleteContacts";

export default {
  [actions.dashboard]: {
    path: "/",
  },
  [actions.error]: {
    path: "/er",
  },
  [actions.customers]: {
    path: "/customers",
    thunk: (dispatch, getState, { action }) => customers.apiLoad({ dispatch, state: getState(), action }),
  },
  [actions.customersDelete]: {
    thunk: (dispatch, getState, { action }) => deleteCustomers.apiDelete({ dispatch, state: getState(), action }),
  },
  [actions.customerNew]: {
    path: "/customers/new",
  },
  [actions.customerCreate]: {
    thunk: (dispatch, getState, { action }) => createCustomer.apiCreate({ dispatch, state: getState(), action }),
  },
  [actions.customer]: {
    path: "/customers/:id",
  },
  [actions.customerEdit]: {
    path: "/customers/:id/edit",
  },

  [actions.contacts]: {
    path: "/contacts",
    thunk: (dispatch, getState, { action }) => contacts.apiLoad({ dispatch, state: getState(), action }),
  },
  [actions.contactsDelete]: {
    path: "/customers",
    thunk: (dispatch, getState, { action }) => deleteContacts.apiDelete({ dispatch, state: getState(), action }),
  },
  [actions.contactNew]: {
    path: "/contacts/new",
  },
  [actions.contactCreate]: {
    thunk: (dispatch, getState, { action }) => createContact.apiCreate({ dispatch, state: getState(), action }),
  },
  [actions.contact]: {
    path: "/contacts/:id",
  },
  [actions.contactEdit]: {
    path: "/contacts/:id/edit",
  },
};