import axios from "../axios";
import * as actions from "../actions";
import * as location from "../store/location";
import convertError from "../util/convertError";
import shouldReloadPageData from "../util/shouldReloadPageData";

const apiUrl = "/accounts";

export const create = props => {
  const { dispatch, action } = props;

  axios.post(apiUrl, {
    jsonrpc: "2.0",
    method: "create",
    params: {
      account: action.payload
    },
    id: Date.now(),
  })
    .then(result => dispatch(actions.accountCreateFinished(result.data.result)))
    .catch(error => dispatch(actions.accountCreateError(convertError(error))));
};

export const deactivate = props => {
  const { dispatch, action } = props;

  axios.post(apiUrl, {
    jsonrpc: "2.0",
    method: "delete",
    params: Array.isArray(action.payload) ? {
      accountIds: action.payload
    } : {
        accountId: action.payload
      },
    id: Date.now(),
  })
    .then(result => dispatch(actions.accountsDeactivateFinished(result.data.result)))
    .catch(error => dispatch(actions.accountsDeactivateError(convertError(error))));
};

export const edit = props => {
  const { dispatch, state, action } = props;

  axios.post(apiUrl, {
    jsonrpc: "2.0",
    method: "update",
    params: {
      customerId: parseInt(location.getCurrentData(state).customer, 10),
      accountId: parseInt(location.getCurrentData(state).account, 10),
      account: action.payload,
    },
    id: Date.now(),
  })
    .then(result => dispatch(actions.accountEdit(result.data.result)))
    .catch(error => dispatch(actions.accountEditError(convertError(error))));
};

export const paged = props => {
  const { dispatch, state, action, isLoading, } = props;

  if (shouldReloadPageData(state, action, isLoading)) {
    dispatch(actions.accountsLoad());
    axios.post(apiUrl, {
      jsonrpc: "2.0",
      method: "list",
      params: {
        pagination: location.getCurrentPagination(state),
      },
      id: Date.now(),
    })
      .then(result => dispatch(actions.accountsLoadFinished(result.data.result)))
      .catch(error => dispatch(actions.accountsLoadError(convertError(error))));
  }
};

export const single = props => {
  const { dispatch, state } = props;

  dispatch(actions.accountLoad());
  axios.post(apiUrl, {
    jsonrpc: "2.0",
    method: "get",
    params: {
      accountId: parseInt(location.getCurrentData(state).account, 10)
    },
    id: Date.now(),
  })
    .then(result => dispatch(actions.accountLoadFinished(result.data.result)))
    .catch(error => dispatch(actions.accountLoadError(convertError(error))));
};