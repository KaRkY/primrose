import { combineReducers } from "redux";
import { createSelector } from "reselect";
import { handleActions } from "redux-actions";
import * as actions from "../actions";

const data = handleActions({
  [actions.customerViewFinished]: (state, action) => action.payload,
  [actions.customerViewError]: () => null,
}, null);

const loading = handleActions({
  [actions.customerViewLoad]: (state, action) => !!!action.payload,
  [actions.customerViewFinished]: () => false,
  [actions.customerViewError]: () => false,
}, false);

const error = handleActions({
  [actions.customerViewLoad]: () => null,
  [actions.customerViewFinished]: () => null,
  [actions.customerViewError]: (state, action) => action.payload,
}, null);

export default combineReducers({
  data,
  loading,
  error,
});

export const rootSelector = state => state.customerView;
export const getData = createSelector(rootSelector, root => root.data);
export const getError = createSelector(rootSelector, root => root.error);
export const isLoading = createSelector(rootSelector, root => root.loading);