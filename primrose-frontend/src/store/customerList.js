import { combineReducers } from "redux";
import { createSelector } from "reselect";
import { handleActions } from "redux-actions";
import * as actions from "../actions";

const data = handleActions({
  [actions.customerListFinished]: (state, action) => action.payload.data,
  [actions.customerListError]: () => null,
}, null);

const count = handleActions({
  [actions.customerListFinished]: (state, action) => action.payload.count,
  [actions.customerListError]: () => null,
}, 0);

const loading = handleActions({
  [actions.customerListLoad]: (state, action) => !!!action.payload,
  [actions.customerListFinished]: () => false,
  [actions.customerListError]: () => false,
}, false);

const error = handleActions({
  [actions.customerListLoad]: () => null,
  [actions.customerListFinished]: () => null,
  [actions.customerListError]: (state, action) => action.payload,
}, null);

export default combineReducers({
  data,
  count,
  loading,
  error,
});

export const rootSelector = state => state.customerList;
export const getCount = createSelector(rootSelector, root => root.count);
export const getData = createSelector(rootSelector, root => root.data);
export const getError = createSelector(rootSelector, root => root.error);
export const isLoading = createSelector(rootSelector, root => root.loading);