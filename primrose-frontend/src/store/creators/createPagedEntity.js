import {
  combineReducers
} from "redux";
import {
  handleActions
} from "redux-actions";
import {
  createSelector
} from "reselect";
import axios from "../../axios";
import shouldReloadPageData from "../../util/shouldReloadPageData";
import convertError from "../../util/convertError";

export default ({
  loadingAction,
  fetchedAction,
  errorAction,
  rootSelector,
  apiUrl,
  apiParameters,
  ...rest
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
    [loadingAction]: () => null,
    [fetchedAction]: () => null,
    [errorAction]: (state, action) => action.payload,
  }, null);

  const reducer = combineReducers({
    data,
    count,
    loading,
    error,
  });

  const selectors = {
    getCount: createSelector(rootSelector, root => root.count),
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
      if (shouldReloadPageData(state, action, selectors.isLoading)) {
        dispatch(loadingAction());
        axios.post(apiUrl, {
            jsonrpc: "2.0",
            method: "search",
            params: apiParameters({
              dispatch,
              state,
              action
            }),
            id: Date.now(),
          })
          .then(result => dispatch(fetchedAction(result.data.result)))
          .catch(error => dispatch(errorAction(convertError(error))));
      }
    },

    ...rest
  };
};