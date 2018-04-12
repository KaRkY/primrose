import * as actions from "../actions";
import convertError from "../util/convertError";
import shouldReloadPageData from "../util/shouldReloadPageData";
import {
  load as loadCustomers,
  create as createCustomer,
  del as deleteCustomer,
} from "../api/customers";
import * as location from "../store/location";
import * as customers from "../store/customers";


export const load = async (dispatch, getState, { action }) => {
  const state = getState();
  const pagination = location.getCurrentPagination(state);

  if (shouldReloadPageData(getState, action, customers.isLoading)) {
    dispatch(actions.customersLoading());
    loadCustomers(pagination)
      .then(result => dispatch(actions.customersFetched(result.data)))
      .catch(error => dispatch(actions.customersError(convertError(error))));
  }
};

export const create = async (dispatch, getState, { action }) => {
  createCustomer(action.payload)
    .then(result => dispatch(actions.customer(result.data)))
    .catch(console.log);
};

export const del = async (dispatch, getState, {
  action
}) => {
  deleteCustomer(action.payload.customer)
    .then(result => {
      dispatch(actions.customers({
        query: location.getCurrentQuery(getState()),
        selected: undefined,
      }));
    })
    .catch(error => {
      console.log(error);
    });
};