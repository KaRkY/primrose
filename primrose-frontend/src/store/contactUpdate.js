import { combineReducers } from "redux";
import { createSelector } from "reselect";
import { handleActions } from "redux-actions";
import * as actions from "../actions";

const data = handleActions({
  [actions.contactUpdateFinished]: (state, action) => action.payload,
  [actions.contactUpdateError]: () => null,
}, null);

const loading = handleActions({
  [actions.contactUpdateLoad]: (state, action) => !!!action.payload,
  [actions.contactUpdateFinished]: () => false,
  [actions.contactUpdateError]: () => false,
}, false);

const error = handleActions({
  [actions.contactUpdateLoad]: () => null,
  [actions.contactUpdateFinished]: () => null,
  [actions.contactUpdateError]: (state, action) => action.payload,
}, null);

export default combineReducers({
  data,
  loading,
  error,
});

export const rootSelector = state => state.contactUpdate;
export const getData = createSelector(rootSelector, root => root.data);
export const getError = createSelector(rootSelector, root => root.error);
export const isLoading = createSelector(rootSelector, root => root.loading);