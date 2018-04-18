import axios from "../../axios";
import convertError from "../../util/convertError";
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
  loadingAction,
  fetchedAction,
  errorAction,
  rootSelector,
  apiUrl,
  apiEntity,
  ...rest
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

  const reducer = combineReducers({
    data,
    loading,
    error,
  });

  const selectors = {
    getData: createSelector(rootSelector, root => root.data),
    getError: createSelector(rootSelector, root => root.error),
    isLoading: createSelector(rootSelector, root => root.loading),
  };

  return {
    reducer,
    ...selectors,

    api: ({
      dispatch,
      state,
      action
    }) => {
      if (!selectors.getData(state) || selectors.getError(state)) {
        dispatch(loadingAction());
        return axios.post(apiUrl, {
          jsonrpc: "2.0",
          method: apiEntity,
          id: Date.now(),
        })
          .then(result => dispatch(fetchedAction(result.data.result)))
          .catch(error => dispatch(errorAction(convertError(error))));
      } else {
        return Promise.resolve();
      }
    },

    ...rest
  };
};