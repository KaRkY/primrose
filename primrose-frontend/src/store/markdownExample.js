import { combineReducers } from "redux";
import { createSelector } from "reselect";
import { handleActions } from "redux-actions";
import * as actions from "../actions";

const data = handleActions({
  [actions.markdownExampleFinished]: (state, action) => action.payload,
  [actions.markdownExampleError]: () => null,
}, null);

const loading = handleActions({
  [actions.markdownExampleLoad]: (state, action) => !!!action.payload,
  [actions.markdownExampleFinished]: () => false,
  [actions.markdownExampleError]: () => false,
}, false);

const error = handleActions({
  [actions.markdownExampleLoad]: () => null,
  [actions.markdownExampleFinished]: () => null,
  [actions.markdownExampleError]: (state, action) => action.payload,
}, null);

export default combineReducers({
  data,
  loading,
  error,
});

export const rootSelector = state => state.markdownExample;
export const getData = createSelector(rootSelector, root => root.data);
export const getError = createSelector(rootSelector, root => root.error);
export const isLoading = createSelector(rootSelector, root => root.loading);