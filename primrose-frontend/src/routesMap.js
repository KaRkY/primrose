import * as actions from "./actions";

import customers from "./store/customers";
import * as customersApi from "./api/customers";
import contacts from "./store/contacts";
import * as contactsApi from "./api/contacts";
import meta from "./store/meta";
import * as metaApi from "./api/meta";

export default {
  [actions.dashboardPage]: {
    path: "/",
  },
  [actions.errorPage]: {
    path: "/er",
  },
  [actions.customersPage]: {
    path: "/customers",
    thunk: (dispatch, getState, { action }) => Promise.all([
      customersApi.paged({ dispatch, state: getState(), action, isLoading: customers.paged.isLoading }),
      metaApi.customerTypes({ dispatch, state: getState(), action, ...meta.customerTypes }),
      metaApi.customerRelationTypes({ dispatch, state: getState(), action, ...meta.customerRelationTypes }),
    ]),
  },
  [actions.customerPageNew]: {
    path: "/customers/new",
    thunk: (dispatch, getState, { action }) => Promise.all([
      metaApi.customerTypes({ dispatch, state: getState(), action, ...meta.customerTypes }),
      metaApi.customerRelationTypes({ dispatch, state: getState(), action, ...meta.customerRelationTypes }),
      metaApi.emailTypes({ dispatch, state: getState(), action, ...meta.emailTypes }),
      metaApi.phoneNumberTypes({ dispatch, state: getState(), action, ...meta.phoneNumberTypes }),
    ]),
  },
  [actions.customerPage]: {
    path: "/customers/:customer",
    thunk: (dispatch, getState, { action }) => Promise.all([
      customersApi.single({ dispatch, state: getState(), action }),
      metaApi.customerTypes({ dispatch, state: getState(), action, ...meta.customerTypes }),
      metaApi.customerRelationTypes({ dispatch, state: getState(), action, ...meta.customerRelationTypes }),
      metaApi.emailTypes({ dispatch, state: getState(), action, ...meta.emailTypes }),
      metaApi.phoneNumberTypes({ dispatch, state: getState(), action, ...meta.phoneNumberTypes }),
    ]),
  },
  [actions.customerPageEdit]: {
    path: "/customers/:customer/edit",
    thunk: (dispatch, getState, { action }) => Promise.all([
      customersApi.single({ dispatch, state: getState(), action }),
      metaApi.customerTypes({ dispatch, state: getState(), action, ...meta.customerTypes }),
      metaApi.customerRelationTypes({ dispatch, state: getState(), action, ...meta.customerRelationTypes }),
      metaApi.emailTypes({ dispatch, state: getState(), action, ...meta.emailTypes }),
      metaApi.phoneNumberTypes({ dispatch, state: getState(), action, ...meta.phoneNumberTypes }),
    ]),
  },

  [actions.accountsPage]: {
    path: "/customers/:customer/accounts"
  },

  [actions.accountPageNew]: {
    path: "/customers/:customer/accounts/new"
  },

  [actions.accountPage]: {
    path: "/customers/:customer/accounts/:account"
  },

  [actions.accountPageEdit]: {
    path: "/customers/:customer/accounts/:account/edit"
  },

  [actions.contactsPage]: {
    path: "/contacts",
    thunk: (dispatch, getState, { action }) => Promise.all([
      contactsApi.paged({ dispatch, state: getState(), action, isLoading: contacts.paged.isLoading })
    ]),
  },
  [actions.contactPageNew]: {
    path: "/contacts/new",
    thunk: (dispatch, getState, { action }) => Promise.all([
      metaApi.emailTypes({ dispatch, state: getState(), action, ...meta.emailTypes }),
      metaApi.phoneNumberTypes({ dispatch, state: getState(), action, ...meta.phoneNumberTypes }),
    ]),
  },
  [actions.contactPage]: {
    path: "/contacts/:contact",
    thunk: (dispatch, getState, { action }) => Promise.all([
      contactsApi.single({ dispatch, state: getState(), action }),
      metaApi.emailTypes({ dispatch, state: getState(), action, ...meta.emailTypes }),
      metaApi.phoneNumberTypes({ dispatch, state: getState(), action, ...meta.phoneNumberTypes }),
    ]),
  },
  [actions.contactPageEdit]: {
    path: "/contacts/:contact/edit",
    thunk: (dispatch, getState, { action }) => Promise.all([
      contactsApi.single({ dispatch, state: getState(), action }),
      metaApi.emailTypes({ dispatch, state: getState(), action, ...meta.emailTypes }),
      metaApi.phoneNumberTypes({ dispatch, state: getState(), action, ...meta.phoneNumberTypes }),
    ]),
  },

  [actions.customerCreate]: {
    thunk: (dispatch, getState, { action }) => customersApi.create({ dispatch, state: getState(), action }),
  },

  [actions.customerEdit]: {
    thunk: (dispatch, getState, { action }) => customersApi.edit({ dispatch, state: getState(), action }),
  },

  [actions.customersDeactivate]: {
    thunk: (dispatch, getState, { action }) => customersApi.deactivate({ dispatch, state: getState(), action }),
  },

  [actions.accountsDeactivate]: {

  },

  [actions.accountCreate]: {

  },

  [actions.contactCreate]: {
    thunk: (dispatch, getState, { action }) => contactsApi.create({ dispatch, state: getState(), action }),
  },

  [actions.contactEdit]: {
    thunk: (dispatch, getState, { action }) => contactsApi.edit({ dispatch, state: getState(), action }),
  },

  [actions.contactsDeactivate]: {
    thunk: (dispatch, getState, { action }) => contactsApi.deactivate({ dispatch, state: getState(), action }),
  },
};