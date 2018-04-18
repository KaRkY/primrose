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
  editedAction,
  errorAction,
  rootSelector,
  apiUrl,
  apiParameters,
  ...rest
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
          method: "edit",
          params: apiParameters({
            dispatch,
            state,
            action
          }),
          id: Date.now(),
        })
        .then(result => dispatch(editedAction(result.data.result)))
        .catch(error => dispatch(errorAction(convertError(error))));
    },

    ...rest
  };
};