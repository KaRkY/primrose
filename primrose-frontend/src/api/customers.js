import axios from "../axios";
import * as actions from "../actions";
import * as location from "../store/location";
import convertError from "../util/convertError";
import shouldReloadPageData from "../util/shouldReloadPageData";

const apiUrl = "/customers";

export const create = props => {
  const {
    dispatch,
    action
  } = props;

  axios.post(apiUrl, {
      jsonrpc: "2.0",
      method: "create",
      params: {
        customer: action.payload
      },
      id: Date.now(),
    })
    .then(result => dispatch(actions.customerCreateFinished(result.data.result)))
    .catch(error => dispatch(actions.customerCreateError(convertError(error))));
};

export const remove = props => {
  const {
    dispatch,
    action
  } = props;

  axios.post(apiUrl, {
      jsonrpc: "2.0",
      method: "delete",
      params: Array.isArray(action.payload.customers) ? {
        customerCodes: action.payload.customers
      } : {
        customerCode: action.payload.customers
      },
      id: Date.now(),
    })
    .then(result => dispatch(actions.customersDeleteFinished(result.data.result)))
    .catch(error => dispatch(actions.customersDeleteError(convertError(error))));
};

export const edit = props => {
  const {
    dispatch,
    state,
    action
  } = props;

  axios.post(apiUrl, {
      jsonrpc: "2.0",
      method: "edit",
      params: {
        customerCode: location.getCurrentData(state).customer,
        customer: action.payload,
      },
      id: Date.now(),
    })
    .then(result => dispatch(actions.customerEditFinished(result.data.result)))
    .catch(error => dispatch(actions.customerEditError(convertError(error))));
};

export const paged = props => {
  const {
    dispatch,
    state,
    action,
    isLoading,
  } = props;

  if (shouldReloadPageData(state, action, isLoading)) {
    dispatch(actions.customersLoad());
    axios.post(apiUrl, {
        jsonrpc: "2.0",
        method: "search",
        params: {
          search: location.getCurrentPagination(state),
        },
        id: Date.now(),
      })
      .then(result => dispatch(actions.customersLoadFinished(result.data.result)))
      .catch(error => dispatch(actions.customersLoadError(convertError(error))));
  }
};

export const single = props => {
  const {
    dispatch,
    state
  } = props;

  dispatch(actions.customerLoad());
  axios.post(apiUrl, {
      jsonrpc: "2.0",
      method: "get",
      params: {
        customerCode: location.getCurrentData(state).customer,
      },
      id: Date.now(),
    })
    .then(result => dispatch(actions.customerLoadFinished(result.data.result)))
    .catch(error => dispatch(actions.customerLoadError(convertError(error))));
};