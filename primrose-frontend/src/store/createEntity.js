import {
  combineReducers
} from "redux";
import {
  handleActions
} from "redux-actions";
import {
  createSelector
} from "reselect";

export default ({
  baseAction,
  loadingAction,
  fetchedAction,
  errorAction,
  rootSelector,
}) => {
  const data = handleActions({
    [fetchedAction]: (state, action) => action.payload.data,
    [errorAction]: () => null,
  }, null);

  const count = handleActions({
    [fetchedAction]: (state, action) => action.payload.count,
    [errorAction]: () => null,
  }, 0);

  const loading = handleActions({
    [loadingAction]: () => true,
    [fetchedAction]: () => false,
    [errorAction]: () => false,
  }, false);

  const error = handleActions({
    [baseAction]: () => null,
    [fetchedAction]: () => null,
    [errorAction]: (state, action) => action.payload,
  }, null);

  return {
    reducer: combineReducers({
      data,
      count,
      loading,
      error,
    }),

    selectors: {
      getCount: createSelector(rootSelector, root => root.count),
      getData: createSelector(rootSelector, root => root.data),
      getError: createSelector(rootSelector, root => root.error),
      isLoading: createSelector(rootSelector, root => root.loading),
    }
  };
};