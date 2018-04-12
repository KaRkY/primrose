//import { redirect, NOT_FOUND } from "redux-first-router";
import * as actions from "./actions";
import {
  load as customersLoadThunk,
  create as customerCreateThunk,
  del as customerDeleteThunk
} from "./thunks/customers";
import {
  load as contactsLoadThunk,
  del as contactsDeleteThunk
} from "./thunks/contacts";

export default {
  [actions.dashboard]: {
    path: "/",
  },
  [actions.error]: {
    path: "/er",
  },
  [actions.customers]: {
    path: "/customers",
    thunk: customersLoadThunk,
  },
  [actions.customerDelete]: {
    thunk: customerDeleteThunk,
  },
  [actions.customerNew]: {
    path: "/customers/new",
  },
  [actions.customerCreate]: {
    thunk: customerCreateThunk,
  },
  [actions.customer]: {
    path: "/customers/:id",
  },
  [actions.customerEdit]: {
    path: "/customers/:id/edit",
  },

  [actions.contacts]: {
    path: "/contacts",
    thunk: contactsLoadThunk,
  },
  [actions.contactDelete]: {
    path: "/customers",
    thunk: contactsDeleteThunk,
  },
  [actions.contactNew]: {
    path: "/contacts/new",
  },
  [actions.contact]: {
    path: "/contacts/:id",
  },
  [actions.contactEdit]: {
    path: "/contacts/:id/edit",
  },
};