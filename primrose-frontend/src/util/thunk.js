import * as actions from "../actions";
import convertError from "./convertError";

export default loaders => (dispatch, getState, { action }) => {
  const state = getState();

  dispatch(actions.pageLoad());
  const promises = loaders.map(element => element({ dispatch, state, action }));
  Promise
    .all(promises)
    .then(result => dispatch(actions.pageFinished(result)))
    .catch(error => dispatch(actions.pageError(convertError(error))));
}