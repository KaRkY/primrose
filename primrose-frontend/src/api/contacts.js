import axios from "../axios";
import * as actions from "../actions";
import * as location from "../store/location";
import convertError from "../util/convertError";
import shouldReloadPageData from "../util/shouldReloadPageData";

const apiUrl = "/contacts";

export const create = props => {
  const { dispatch, action } = props;

  axios.post(apiUrl, {
    jsonrpc: "2.0",
    method: "create",
    params: {
      contact: action.payload
    },
    id: Date.now(),
  })
    .then(result => dispatch(actions.contactCreateFinished(result.data.result)))
    .catch(error => dispatch(actions.contactCreateError(convertError(error))));
};

export const remove = props => {
  const { dispatch, action } = props;

  axios.post(apiUrl, {
    jsonrpc: "2.0",
    method: "delete",
    params: Array.isArray(action.payload.contacts) ? {
      contactCodes: action.payload.contacts
    } : {
        contactCode: action.payload.contacts
      },
    id: Date.now(),
  })
    .then(result => dispatch(actions.contactsDeleteFinished(result.data.result)))
    .catch(error => dispatch(actions.contactsDeleteError(convertError(error))));
};

export const edit = props => {
  const { dispatch, state, action } = props;
  axios.post(apiUrl, {
    jsonrpc: "2.0",
    method: "edit",
    params: {
      contactCode: location.getCurrentData(state).contact,
      contact: action.payload,
    },
    id: Date.now(),
  })
    .then(result => dispatch(actions.contactEditFinished(result.data.result)))
    .catch(error => dispatch(actions.contactEditError(convertError(error))));
};

export const paged = props => {
  const { dispatch, state, action, isLoading, } = props;

  if (shouldReloadPageData(state, action, isLoading)) {
    dispatch(actions.contactsLoad());
    axios.post(apiUrl, {
      jsonrpc: "2.0",
      method: "search",
      params: {
        search: location.getCurrentPagination(state),
      },
      id: Date.now(),
    })
      .then(result => dispatch(actions.contactsLoadFinished(result.data.result)))
      .catch(error => dispatch(actions.contactsLoadError(convertError(error))));
  }
};

export const single = props => {
  const { dispatch, state } = props;

  dispatch(actions.contactLoad());
  axios.post(apiUrl, {
    jsonrpc: "2.0",
    method: "get",
    params: {
      contactCode: location.getCurrentData(state).contact,
    },
    id: Date.now(),
  })
    .then(result => dispatch(actions.contactLoadFinished(result.data.result)))
    .catch(error => dispatch(actions.contactLoadError(convertError(error))));
};