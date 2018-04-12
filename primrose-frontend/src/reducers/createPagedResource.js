import {
  combineReducers
} from "redux";
import {
  handleActions
} from "redux-actions";

export default ({
  baseAction,
  loadingAction,
  fetchedAction,
  errorAction,
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

  return combineReducers({
    data,
    count,
    loading,
    error,
  });
};