import * as actions from "./actions";
import * as location from "./store/location";

import * as customersApi from "./api/customers";
import * as contactsApi from "./api/contacts";

import meta from "./store/meta";
import * as metaApi from "./api/meta";
import axios from "./axios";
import text from "./pages/Dashboard/text.md";

import convertError from "./util/convertError";
import shouldReloadPageData from "./util/shouldReloadPageData";

export default {
  [actions.dashboardPage]: {
    path: "/",
  },
  [actions.customerListPage]: {
    path: "/customers",
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      if (shouldReloadPageData(state, action)) {
        dispatch(actions.customerListLoad());
        customersApi.list(location.getCurrentPagination(state))
          .then(result => dispatch(actions.customerListFinished(result.data.result)))
          .catch(error => dispatch(actions.customerListError(convertError(error))));
      }

      if (!meta.customerTypes.getData(state) || meta.customerTypes.getError(state)) {
        dispatch(actions.customerTypesLoad());
        metaApi.customerTypes()
          .then(result => dispatch(actions.customerTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.customerTypesError(convertError(error))));
      }

      if (!meta.customerRelationTypes.getData(state) || meta.customerRelationTypes.getError(state)) {
        dispatch(actions.customerRelationTypesLoad());
        metaApi.customerRelationTypes()
          .then(result => dispatch(actions.customerRelationTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.customerRelationTypesError(convertError(error))));
      }
    },
  },
  [actions.customerViewPage]: {
    path: "/customers/:customer",
    fromPath: param => param,
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      dispatch(actions.customerViewLoad());
      customersApi.view(location.getCurrentData(state).customer)
        .then(result => dispatch(actions.customerViewFinished(result.data.result)))
        .catch(error => dispatch(actions.customerViewError(convertError(error))));

      if (!meta.customerTypes.getData(state) || meta.customerTypes.getError(state)) {
        dispatch(actions.customerTypesLoad());
        metaApi.customerTypes()
          .then(result => dispatch(actions.customerTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.customerTypesError(convertError(error))));
      }

      if (!meta.customerRelationTypes.getData(state) || meta.customerRelationTypes.getError(state)) {
        dispatch(actions.customerRelationTypesLoad());
        metaApi.customerRelationTypes()
          .then(result => dispatch(actions.customerRelationTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.customerRelationTypesError(convertError(error))));
      }

      if (!meta.emailTypes.getData(state) || meta.emailTypes.getError(state)) {
        dispatch(actions.emailTypesLoad());
        metaApi.emailTypes()
          .then(result => dispatch(actions.emailTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.emailTypesError(convertError(error))));
      }

      if (!meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state)) {
        dispatch(actions.phoneNumberTypesLoad());
        metaApi.phoneNumberTypes()
          .then(result => dispatch(actions.phoneNumberTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.phoneNumberTypesError(convertError(error))));
      }
    },
  },
  [actions.customerUpdatePage]: {
    path: "/customers/:customer/update",
    fromPath: param => param,
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      dispatch(actions.customerUpdateLoad());
      customersApi.view(location.getCurrentData(state).customer)
        .then(result => dispatch(actions.customerUpdateFinished(result.data.result)))
        .catch(error => dispatch(actions.customerUpdateError(convertError(error))));

      if (!meta.customerTypes.getData(state) || meta.customerTypes.getError(state)) {
        dispatch(actions.customerTypesLoad());
        metaApi.customerTypes()
          .then(result => dispatch(actions.customerTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.customerTypesError(convertError(error))));
      }

      if (!meta.customerRelationTypes.getData(state) || meta.customerRelationTypes.getError(state)) {
        dispatch(actions.customerRelationTypesLoad());
        metaApi.customerRelationTypes()
          .then(result => dispatch(actions.customerRelationTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.customerRelationTypesError(convertError(error))));
      }

      if (!meta.emailTypes.getData(state) || meta.emailTypes.getError(state)) {
        dispatch(actions.emailTypesLoad());
        metaApi.emailTypes()
          .then(result => dispatch(actions.emailTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.emailTypesError(convertError(error))));
      }

      if (!meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state)) {
        dispatch(actions.phoneNumberTypesLoad());
        metaApi.phoneNumberTypes()
          .then(result => dispatch(actions.phoneNumberTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.phoneNumberTypesError(convertError(error))));
      }
    },
  },
  [actions.customerNewPage]: {
    path: "/customers/new",
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      if (!meta.customerTypes.getData(state) || meta.customerTypes.getError(state)) {
        dispatch(actions.customerTypesLoad());
        metaApi.customerTypes()
          .then(result => dispatch(actions.customerTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.customerTypesError(convertError(error))));
      }

      if (!meta.customerRelationTypes.getData(state) || meta.customerRelationTypes.getError(state)) {
        dispatch(actions.customerRelationTypesLoad());
        metaApi.customerRelationTypes()
          .then(result => dispatch(actions.customerRelationTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.customerRelationTypesError(convertError(error))));
      }

      if (!meta.emailTypes.getData(state) || meta.emailTypes.getError(state)) {
        dispatch(actions.emailTypesLoad());
        metaApi.emailTypes()
          .then(result => dispatch(actions.emailTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.emailTypesError(convertError(error))));
      }

      if (!meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state)) {
        dispatch(actions.phoneNumberTypesLoad());
        metaApi.phoneNumberTypes()
          .then(result => dispatch(actions.phoneNumberTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.phoneNumberTypesError(convertError(error))));
      }
    },
  },

  [actions.contactListPage]: {
    path: "/contacts",
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      if (shouldReloadPageData(state, action)) {
        dispatch(actions.contactListLoad());
        contactsApi.list(location.getCurrentPagination(state))
          .then(result => dispatch(actions.contactListFinished(result.data.result)))
          .catch(error => dispatch(actions.contactListError(convertError(error))));
      }
    },
  },
  [actions.contactViewPage]: {
    path: "/contacts/:contact",
    fromPath: param => param,
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      dispatch(actions.contactViewLoad());
      contactsApi.view(location.getCurrentData(state).contact)
        .then(result => dispatch(actions.contactViewFinished(result.data.result)))
        .catch(error => dispatch(actions.contactViewError(convertError(error))));

      if (!meta.emailTypes.getData(state) || meta.emailTypes.getError(state)) {
        dispatch(actions.emailTypesLoad());
        metaApi.emailTypes()
          .then(result => dispatch(actions.emailTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.emailTypesError(convertError(error))));
      }

      if (!meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state)) {
        dispatch(actions.phoneNumberTypesLoad());
        metaApi.phoneNumberTypes()
          .then(result => dispatch(actions.phoneNumberTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.phoneNumberTypesError(convertError(error))));
      }
    },
  },
  [actions.contactUpdatePage]: {
    path: "/contacts/:contact/update",
    fromPath: param => param,
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      dispatch(actions.contactUpdateLoad());
      contactsApi.view(location.getCurrentData(state).contact)
        .then(result => dispatch(actions.contactUpdateFinished(result.data.result)))
        .catch(error => dispatch(actions.contactUpdateError(convertError(error))));

      if (!meta.emailTypes.getData(state) || meta.emailTypes.getError(state)) {
        dispatch(actions.emailTypesLoad());
        metaApi.emailTypes()
          .then(result => dispatch(actions.emailTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.emailTypesError(convertError(error))));
      }

      if (!meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state)) {
        dispatch(actions.phoneNumberTypesLoad());
        metaApi.phoneNumberTypes()
          .then(result => dispatch(actions.phoneNumberTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.phoneNumberTypesError(convertError(error))));
      }
    },
  },
  [actions.contactNewPage]: {
    path: "/contacts/new",
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      if (!meta.emailTypes.getData(state) || meta.emailTypes.getError(state)) {
        dispatch(actions.emailTypesLoad());
        metaApi.emailTypes()
          .then(result => dispatch(actions.emailTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.emailTypesError(convertError(error))));
      }

      if (!meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state)) {
        dispatch(actions.phoneNumberTypesLoad());
        metaApi.phoneNumberTypes()
          .then(result => dispatch(actions.phoneNumberTypesFinished(result.data.result)))
          .catch(error => dispatch(actions.phoneNumberTypesError(convertError(error))));
      }
    },
  },
};