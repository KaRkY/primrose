import { combineReducers } from "redux";
import { createSelector } from "reselect";
import { handleActions } from "redux-actions";
import * as actions from "../actions";

const data = handleActions({
  [actions.contactListFinished]: (state, action) => action.payload.data,
  [actions.contactListError]: () => null,
}, null);

const count = handleActions({
  [actions.contactListFinished]: (state, action) => action.payload.count,
  [actions.contactListError]: () => null,
}, 0);

const loading = handleActions({
  [actions.contactListLoad]: (state, action) => !!!action.payload,
  [actions.contactListFinished]: () => false,
  [actions.contactListError]: () => false,
}, false);

const error = handleActions({
  [actions.contactListLoad]: () => null,
  [actions.contactListFinished]: () => null,
  [actions.contactListError]: (state, action) => action.payload,
}, null);

export default combineReducers({
  data,
  count,
  loading,
  error,
});

export const rootSelector = state => state.contactList;
export const getCount = createSelector(rootSelector, root => root.count);
export const getData = createSelector(rootSelector, root => root.data);
export const getError = createSelector(rootSelector, root => root.error);
export const isLoading = createSelector(rootSelector, root => root.loading);