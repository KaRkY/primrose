import * as actions from "./actions";

import customers from "./store/customers";
import contacts from "./store/contacts";
import meta from "./store/meta";

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
      customers.paged.api({ dispatch, state: getState(), action }),
      meta.customerTypes.api({ dispatch, state: getState(), action }),
      meta.customerRelationTypes.api({ dispatch, state: getState(), action }),
    ]),
  },
  [actions.customerNew]: {
    path: "/customers/new",
    thunk: (dispatch, getState, { action }) => Promise.all([
      meta.customerTypes.api({ dispatch, state: getState(), action }),
      meta.customerRelationTypes.api({ dispatch, state: getState(), action }),
      meta.emailTypes.api({ dispatch, state: getState(), action }),
      meta.phoneNumberTypes.api({ dispatch, state: getState(), action }),
    ]),
  },
  [actions.customer]: {
    path: "/customers/:customer",
    thunk: (dispatch, getState, { action }) => Promise.all([
      customers.single.api({ dispatch, state: getState(), action }),
      meta.customerTypes.api({ dispatch, state: getState(), action }),
      meta.customerRelationTypes.api({ dispatch, state: getState(), action }),
      meta.emailTypes.api({ dispatch, state: getState(), action }),
      meta.phoneNumberTypes.api({ dispatch, state: getState(), action }),
    ]),
  },
  [actions.customerEdit]: {
    path: "/customers/:customer/edit",
    thunk: (dispatch, getState, { action }) => Promise.all([
      customers.single.api({ dispatch, state: getState(), action }),
      meta.customerTypes.api({ dispatch, state: getState(), action }),
      meta.customerRelationTypes.api({ dispatch, state: getState(), action }),
      meta.emailTypes.api({ dispatch, state: getState(), action }),
      meta.phoneNumberTypes.api({ dispatch, state: getState(), action }),
    ]),
  },

  [actions.accounts]: {
    path: "/customers/:customer/accounts"
  },
  
  [actions.accountNew]: {
    path: "/customers/:customer/accounts/new"
  },

  [actions.account]: {
    path: "/customers/:customer/accounts/:account"
  },

  [actions.accountEdit]: {
    path: "/customers/:customer/accounts/:account/edit"
  },

  [actions.contacts]: {
    path: "/contacts",
    thunk: (dispatch, getState, { action }) => Promise.all([
      contacts.paged.api({ dispatch, state: getState(), action })
    ]),
  },
  [actions.contactNew]: {
    path: "/contacts/new",
    thunk: (dispatch, getState, { action }) => Promise.all([
      meta.emailTypes.api({ dispatch, state: getState(), action }),
      meta.phoneNumberTypes.api({ dispatch, state: getState(), action }),
    ]),
  },
  [actions.contact]: {
    path: "/contacts/:contact",
    thunk: (dispatch, getState, { action }) => Promise.all([
      contacts.single.api({ dispatch, state: getState(), action }),
      meta.emailTypes.api({ dispatch, state: getState(), action }),
      meta.phoneNumberTypes.api({ dispatch, state: getState(), action }),
    ]),
  },
  [actions.contactEdit]: {
    path: "/contacts/:contact/edit",
    thunk: (dispatch, getState, { action }) => Promise.all([
      contacts.single.api({ dispatch, state: getState(), action }),
      meta.emailTypes.api({ dispatch, state: getState(), action }),
      meta.phoneNumberTypes.api({ dispatch, state: getState(), action }),
    ]),
  },

  [actions.customersDelete]: {
    thunk: (dispatch, getState, { action }) => customers.delete.api({ dispatch, state: getState(), action }),
  },

  [actions.customerCreate]: {
    thunk: (dispatch, getState, { action }) => customers.create.api({ dispatch, state: getState(), action }),
  },

  [actions.accountsDelete]: {
          
  },

  [actions.accountCreate]: {

  },

  [actions.contactCreate]: {
    thunk: (dispatch, getState, { action }) => contacts.create.api({ dispatch, state: getState(), action }),
  },

  [actions.contactsDelete]: {
    thunk: (dispatch, getState, { action }) => contacts.delete.api({ dispatch, state: getState(), action }),
  },
};