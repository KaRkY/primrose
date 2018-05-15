import { combineReducers } from "redux";
import { createSelector } from "reselect";
import { handleActions } from "redux-actions";
import * as actions from "../actions";

const data = handleActions({
  [actions.contactViewFinished]: (state, action) => action.payload,
  [actions.contactViewError]: () => null,
}, null);

const loading = handleActions({
  [actions.contactViewLoad]: (state, action) => !!!action.payload,
  [actions.contactViewFinished]: () => false,
  [actions.contactViewError]: () => false,
}, false);

const error = handleActions({
  [actions.contactViewLoad]: () => null,
  [actions.contactViewFinished]: () => null,
  [actions.contactViewError]: (state, action) => action.payload,
}, null);

export default combineReducers({
  data,
  loading,
  error,
});

export const rootSelector = state => state.contactView;
export const getData = createSelector(rootSelector, root => root.data);
export const getError = createSelector(rootSelector, root => root.error);
export const isLoading = createSelector(rootSelector, root => root.loading);