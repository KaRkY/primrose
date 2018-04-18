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
  baseAction,
  createdAction,
  errorAction,
  rootSelector,
  apiUrl,
  apiParameters,
  ...rest
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

  const reducer = combineReducers({
    loading,
    error,
  });

  const selectors = {
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
      axios.post(apiUrl, {
          jsonrpc: "2.0",
          method: "delete",
          params: apiParameters({
            dispatch,
            state,
            action
          }),
          id: Date.now(),
        })
        .then(result => dispatch(createdAction(result.data.result)))
        .catch(error => dispatch(errorAction(convertError(error))));
    },

    ...rest
  };
};