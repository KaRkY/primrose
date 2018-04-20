import { handleActions } from "redux-actions";
import { createSelector } from "reselect";

export default ({
  baseAction,
  createdAction,
  errorAction,
  rootSelector,
}) => {
  const loading = handleActions({
    [baseAction]: () => true,
    [createdAction]: () => false,
    [errorAction]: () => false,
  }, false);

  const error = handleActions({
    [baseAction]: () => null,
    [createdAction]: () => null,
    [errorAction]: (state, action) => action.payload,
  }, null);

  const reducers = {
    loading,
    error,
  };

  const selectors = {
    getError: createSelector(rootSelector, root => root.error),
    isLoading: createSelector(rootSelector, root => root.loading),
  };

  return {
    reducers,
    selectors,
  };
};