//import { redirect, NOT_FOUND } from "redux-first-router";
import customersThunk from "./thunks/customers";
import customersDeleteThunk from "./thunks/customersDelete";
import contactsThunk from "./thunks/contacts";
import contactsDeleteThunk from "./thunks/contactsDelete";

export default {
  DASHBOARD: {
    path: "/",
  },
  ERROR: {
    path: "/er",
  },
  CUSTOMERS: {
    path: "/customers",
    thunk: customersThunk,
  },
  CUSTOMERS_DELETE: {
    path: "/customers",
    thunk: customersDeleteThunk,
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
    path: "/contacts",
    thunk: contactsThunk,
  },
  CONTACTS_DELETE: {
    path: "/customers",
    thunk: contactsDeleteThunk,
  },
  CONTACT_NEW: {
    path: "/contacts/new",
  },
  CONTACT: {
    path: "/contacts/:id",
  },
  CONTACT_EDIT: {
    path: "/contacts/:id/edit",
  },
};