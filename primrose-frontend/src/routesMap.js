import * as actions from "./actions";
import * as location from "./store/location";

import * as customersApi from "./api/customers";
import * as contactsApi from "./api/contacts";

import meta from "./store/meta";
import * as metaApi from "./api/meta";
import axios from "./axios";
import text from "./pages/Dashboard/text.md";

import convertError from "./util/convertError";
import reduxifyApi from "./util/reduxifyApi";
import shouldReloadPageData from "./util/shouldReloadPageData";

const customerList = reduxifyApi({
  load: actions.customerListLoad, 
  finished: actions.customerListFinished, 
  error: actions.customerListError,
  api: customersApi.list,
  extractor: result => result.data.result,
});

const customerView = reduxifyApi({
  load: actions.customerViewLoad, 
  finished: actions.customerViewFinished, 
  error: actions.customerViewError,
  api: customersApi.view,
  extractor: result => result.data.result,
});

const customerUpdate = reduxifyApi({
  load: actions.customerUpdateLoad, 
  finished: actions.customerUpdateFinished, 
  error: actions.customerUpdateError,
  api: customersApi.view,
  extractor: result => result.data.result,
});

const contactList = reduxifyApi({
  load: actions.contactListLoad, 
  finished: actions.contactListFinished, 
  error: actions.contactListError,
  api: contactsApi.list,
  extractor: result => result.data.result,
});

const contactView = reduxifyApi({
  load: actions.contactViewLoad, 
  finished: actions.contactViewFinished, 
  error: actions.contactViewError,
  api: contactsApi.view,
  extractor: result => result.data.result,
});

const contactUpdate = reduxifyApi({
  load: actions.contactUpdateLoad, 
  finished: actions.contactUpdateFinished, 
  error: actions.contactUpdateError,
  api: contactsApi.view,
  extractor: result => result.data.result,
});

const metaCustomerTypes = reduxifyApi({
  load: actions.customerTypesLoad, 
  finished: actions.customerTypesFinished, 
  error: actions.customerTypesError,
  api: metaApi.customerTypes,
  extractor: result => result.data.result,
});

const metaCustomerRelationTypes = reduxifyApi({
  load: actions.customerRelationTypesLoad, 
  finished: actions.customerRelationTypesFinished, 
  error: actions.customerRelationTypesError,
  api: metaApi.customerRelationTypes,
  extractor: result => result.data.result,
});

const metaEmailTypes = reduxifyApi({
  load: actions.emailTypesLoad, 
  finished: actions.emailTypesFinished, 
  error: actions.emailTypesError,
  api: metaApi.emailTypes,
  extractor: result => result.data.result,
});

const metaPhoneNumberTypes = reduxifyApi({
  load: actions.phoneNumberTypesLoad, 
  finished: actions.phoneNumberTypesFinished, 
  error: actions.phoneNumberTypesError,
  api: metaApi.phoneNumberTypes,
  extractor: result => result.data.result,
});



export default {
  [actions.dashboardPage]: {
    path: "/",
    thunk: (dispatch, getState, { action }) => {
      dispatch(actions.customerViewLoad());
      axios
      .get(text)
      .then(result => dispatch(actions.customerViewFinished(result.data)))
        .catch(error => dispatch(actions.customerViewError(convertError(error))));
    }
  },
  [actions.customerListPage]: {
    path: "/customers",
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      if (shouldReloadPageData(state, action)) {
        customerList(dispatch, location.getCurrentPagination(state))
      }

      if (!meta.customerTypes.getData(state) || meta.customerTypes.getError(state)) {
        metaCustomerTypes(dispatch);
      }

      if (!meta.customerRelationTypes.getData(state) || meta.customerRelationTypes.getError(state)) {
        metaCustomerRelationTypes(dispatch);
      }
    },
  },
  [actions.customerNewPage]: {
    path: "/customers/new",
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      if (!meta.customerTypes.getData(state) || meta.customerTypes.getError(state)) {
        metaCustomerTypes(dispatch);
      }

      if (!meta.customerRelationTypes.getData(state) || meta.customerRelationTypes.getError(state)) {
        metaCustomerRelationTypes(dispatch);
      }

      if (!meta.emailTypes.getData(state) || meta.emailTypes.getError(state)) {
        metaEmailTypes(dispatch);
      }

      if (!meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state)) {
        metaPhoneNumberTypes(dispatch);
      }
    },
  },
  [actions.customerViewPage]: {
    path: "/customers/:customer",
    fromPath: param => param,
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      customerView(dispatch, location.getCurrentData(state).customer);

      if (!meta.customerTypes.getData(state) || meta.customerTypes.getError(state)) {
        metaCustomerTypes(dispatch);
      }

      if (!meta.customerRelationTypes.getData(state) || meta.customerRelationTypes.getError(state)) {
        metaCustomerRelationTypes(dispatch);
      }

      if (!meta.emailTypes.getData(state) || meta.emailTypes.getError(state)) {
        metaEmailTypes(dispatch);
      }

      if (!meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state)) {
        metaPhoneNumberTypes(dispatch);
      }
    },
  },
  [actions.customerUpdatePage]: {
    path: "/customers/:customer/update",
    fromPath: param => param,
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      customerUpdate(dispatch, location.getCurrentData(state).customer);

      if (!meta.customerTypes.getData(state) || meta.customerTypes.getError(state)) {
        metaCustomerTypes(dispatch);
      }

      if (!meta.customerRelationTypes.getData(state) || meta.customerRelationTypes.getError(state)) {
        metaCustomerRelationTypes(dispatch);
      }

      if (!meta.emailTypes.getData(state) || meta.emailTypes.getError(state)) {
        metaEmailTypes(dispatch);
      }

      if (!meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state)) {
        metaPhoneNumberTypes(dispatch);
      }
    },
  },

  [actions.contactListPage]: {
    path: "/contacts",
    thunk: (dispatch, getState, { action }) => {
      const state = getState();


      if (shouldReloadPageData(state, action)) {
        contactList(dispatch, location.getCurrentPagination(state))
      }
    },
  },
  [actions.contactNewPage]: {
    path: "/contacts/new",
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      if (!meta.emailTypes.getData(state) || meta.emailTypes.getError(state)) {
        metaEmailTypes(dispatch);
      }

      if (!meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state)) {
        metaPhoneNumberTypes(dispatch);
      }
    },
  },
  [actions.contactViewPage]: {
    path: "/contacts/:contact",
    fromPath: param => param,
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      contactView(dispatch, location.getCurrentData(state).contact);

      if (!meta.emailTypes.getData(state) || meta.emailTypes.getError(state)) {
        metaEmailTypes(dispatch);
      }

      if (!meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state)) {
        metaPhoneNumberTypes(dispatch);
      }
    },
  },
  [actions.contactUpdatePage]: {
    path: "/contacts/:contact/update",
    fromPath: param => param,
    thunk: (dispatch, getState, { action }) => {
      const state = getState();

      contactUpdate(dispatch, location.getCurrentData(state).contact);

      if (!meta.emailTypes.getData(state) || meta.emailTypes.getError(state)) {
        metaEmailTypes(dispatch);
      }

      if (!meta.phoneNumberTypes.getData(state) || meta.phoneNumberTypes.getError(state)) {
        metaPhoneNumberTypes(dispatch);
      }
    },
  },
};