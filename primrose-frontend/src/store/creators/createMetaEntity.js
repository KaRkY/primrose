import {
  handleActions
} from "redux-actions";
import {
  createSelector
} from "reselect";

export default ({
  loadingAction,
  fetchedAction,
  errorAction,
  rootSelector,
}) => {
  const data = handleActions({
    [fetchedAction]: (state, action) => action.payload.reduce((acc, element) => {
      acc[element.slug] = element.name;
      return acc;
    }, {}),
    [errorAction]: () => null,
  }, null);

  const loading = handleActions({
    [loadingAction]: () => true,
    [fetchedAction]: () => false,
    [errorAction]: () => false,
  }, false);

  const error = handleActions({
    [loadingAction]: () => null,
    [fetchedAction]: () => null,
    [errorAction]: (state, action) => action.payload,
  }, null);

  const reducers = {
    data,
    loading,
    error,
  };

  const selectors = {
    getData: createSelector(rootSelector, root => root.data),
    getError: createSelector(rootSelector, root => root.error),
    isLoading: createSelector(rootSelector, root => root.loading),
  };

  return {
    reducers,
    selectors,
  };
};