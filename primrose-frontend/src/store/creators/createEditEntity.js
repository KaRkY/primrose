import { handleActions } from "redux-actions";
import { createSelector } from "reselect";

export default ({
  baseAction,
  editedAction,
  errorAction,
  rootSelector,
}) => {
  const loading = handleActions({
    [baseAction]: () => true,
    [editedAction]: () => false,
    [errorAction]: () => false,
  }, false);

  const error = handleActions({
    [baseAction]: () => null,
    [editedAction]: () => null,
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