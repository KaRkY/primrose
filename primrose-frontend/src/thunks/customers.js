import * as actions from "../actions";
import convertError from "../util/convertError";
import shouldReloadPageData from "../util/shouldReloadPageData";
import {
  load as loadCustomers,
  create as createCustomer,
  del as deleteCustomer,
} from "../api/customers";
import getCurrentQuery from "../selectors/getCurrentQuery";
import getCurrentPage from "../selectors/getCurrentPage";
import getCurrentSize from "../selectors/getCurrentSize";
import getCurrentSortProperty from "../selectors/getCurrentSortProperty";
import getCurrentSortDirection from "../selectors/getCurrentSortDirection";


export const load = async (dispatch, getState, {
  action
}) => {
  if (shouldReloadPageData(getState, action)) {
    const state = getState();
    dispatch(actions.customersLoading());
    loadCustomers({
        page: getCurrentPage(state),
        size: getCurrentSize(state),
        sortProperty: getCurrentSortProperty(state),
        sortDirection: getCurrentSortDirection(state),
      })
      .then(result => dispatch(actions.customersFetched(result.data)))
      .catch(error => dispatch(actions.customersError(convertError(error))));
  }
};

export const create = async (dispatch, getState, {
  action
}) => {
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
        query: getCurrentQuery(getState()),
        selected: undefined,
      }));
    })
    .catch(error => {
      console.log(error);
    });
};