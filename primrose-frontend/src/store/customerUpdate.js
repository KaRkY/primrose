import { combineReducers } from "redux";
import { createSelector } from "reselect";
import { handleActions } from "redux-actions";
import * as actions from "../actions";

const data = handleActions({
  [actions.customerUpdateFinished]: (state, action) => action.payload,
  [actions.customerUpdateError]: () => null,
}, null);

const loading = handleActions({
  [actions.customerUpdateLoad]: (state, action) => !!!action.payload,
  [actions.customerUpdateFinished]: () => false,
  [actions.customerUpdateError]: () => false,
}, false);

const error = handleActions({
  [actions.customerUpdateLoad]: () => null,
  [actions.customerUpdateFinished]: () => null,
  [actions.customerUpdateError]: (state, action) => action.payload,
}, null);

export default combineReducers({
  data,
  loading,
  error,
});

export const rootSelector = state => state.customerUpdate;
export const getData = createSelector(rootSelector, root => root.data);
export const getError = createSelector(rootSelector, root => root.error);
export const isLoading = createSelector(rootSelector, root => root.loading);