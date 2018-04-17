//import { redirect, NOT_FOUND } from "redux-first-router";
import * as actions from "./actions";
import * as customers from "./store/customers";
import * as customer from "./store/customer";
import * as customerTypes from "./store/customerTypes";
import * as customerRelationTypes from "./store/customerRelationTypes";
import * as emailTypes from "./store/emailTypes";
import * as phoneNumberTypes from "./store/phoneNumberTypes";
import * as contacts from "./store/contacts";
import * as contact from "./store/contact";
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
    thunk: (dispatch, getState, { action }) => Promise.all([
      customers.apiLoad({ dispatch, state: getState(), action }),
      customerTypes.apiLoad({ dispatch, state: getState(), action }),
      customerRelationTypes.apiLoad({ dispatch, state: getState(), action }),
    ]),
  },
  [actions.customersDelete]: {
    thunk: (dispatch, getState, { action }) => deleteCustomers.apiDelete({ dispatch, state: getState(), action }),
  },
  [actions.customerNew]: {
    path: "/customers/new",
    thunk: (dispatch, getState, { action }) => Promise.all([
      customerTypes.apiLoad({ dispatch, state: getState(), action }),
      customerRelationTypes.apiLoad({ dispatch, state: getState(), action }),
      emailTypes.apiLoad({ dispatch, state: getState(), action }),
      phoneNumberTypes.apiLoad({ dispatch, state: getState(), action }),
    ]),
  },
  [actions.customerCreate]: {
    thunk: (dispatch, getState, { action }) => createCustomer.apiCreate({ dispatch, state: getState(), action }),
  },
  [actions.customer]: {
    path: "/customers/:id",
    thunk: (dispatch, getState, { action }) => Promise.all([
      customer.apiLoad({ dispatch, state: getState(), action }),
      customerTypes.apiLoad({ dispatch, state: getState(), action }),
      customerRelationTypes.apiLoad({ dispatch, state: getState(), action }),
      emailTypes.apiLoad({ dispatch, state: getState(), action }),
      phoneNumberTypes.apiLoad({ dispatch, state: getState(), action }),
    ]),
  },
  [actions.customerEdit]: {
    path: "/customers/:id/edit",
    thunk: (dispatch, getState, { action }) => Promise.all([
      customer.apiLoad({ dispatch, state: getState(), action }),
      customerTypes.apiLoad({ dispatch, state: getState(), action }),
      customerRelationTypes.apiLoad({ dispatch, state: getState(), action }),
      emailTypes.apiLoad({ dispatch, state: getState(), action }),
      phoneNumberTypes.apiLoad({ dispatch, state: getState(), action }),
    ]),
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
    thunk: (dispatch, getState, { action }) => Promise.all([
      emailTypes.apiLoad({ dispatch, state: getState(), action }),
      phoneNumberTypes.apiLoad({ dispatch, state: getState(), action }),
    ]),
  },
  [actions.contactCreate]: {
    thunk: (dispatch, getState, { action }) => createContact.apiCreate({ dispatch, state: getState(), action }),
  },
  [actions.contact]: {
    path: "/contacts/:id",
    thunk: (dispatch, getState, { action }) => Promise.all([
      contact.apiLoad({ dispatch, state: getState(), action }),
      emailTypes.apiLoad({ dispatch, state: getState(), action }),
      phoneNumberTypes.apiLoad({ dispatch, state: getState(), action }),
    ]),
  },
  [actions.contactEdit]: {
    path: "/contacts/:id/edit",
    thunk: (dispatch, getState, { action }) => Promise.all([
      contact.apiLoad({ dispatch, state: getState(), action }),
      emailTypes.apiLoad({ dispatch, state: getState(), action }),
      phoneNumberTypes.apiLoad({ dispatch, state: getState(), action }),
    ]),
  },
};