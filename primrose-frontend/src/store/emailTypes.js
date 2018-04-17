import axios from "../axios";
import convertError from "../util/convertError";
import createMetaEntity from "./creators/createMetaEntity";
import * as actions from "../actions";

const entity = createMetaEntity({
  loadingAction: actions.emailTypesLoad,
  fetchedAction: actions.emailTypesLoadFinished,
  errorAction: actions.emailTypesLoadError,
  rootSelector: state => state.emailTypes,
});

export const apiLoad = ({
  dispatch,
  state,
  action
}) => {
  if (!getData(state) || getError(state)) {
    dispatch(actions.emailTypesLoad());
    return axios.post("/meta", {
      jsonrpc: "2.0",
      method: "email",
      id: Date.now(),
    })
      .then(result => dispatch(actions.emailTypesLoadFinished(result.data.result)))
      .catch(error => dispatch(actions.emailTypesLoadError(convertError(error))));
  } else {
    return Promise.resolve();
  }
};

export const getData = entity.selectors.getData;
export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;