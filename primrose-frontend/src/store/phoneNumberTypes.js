import axios from "../axios";
import convertError from "../util/convertError";
import createMetaEntity from "./creators/createMetaEntity";
import * as actions from "../actions";

const entity = createMetaEntity({
  loadingAction: actions.phoneNumberTypesLoad,
  fetchedAction: actions.phoneNumberTypesLoadFinished,
  errorAction: actions.phoneNumberTypesLoadError,
  rootSelector: state => state.phoneNumberTypes,
});

export const apiLoad = ({
  dispatch,
  state,
  action
}) => {
  if (!getData(state) || getError(state)) {
    dispatch(actions.phoneNumberTypesLoad());
    return axios.post("/meta", {
      jsonrpc: "2.0",
      method: "phoneNumber",
      id: Date.now(),
    })
      .then(result => dispatch(actions.phoneNumberTypesLoadFinished(result.data.result)))
      .catch(error => dispatch(actions.phoneNumberTypesLoadError(convertError(error))));
  } else {
    return Promise.resolve();
  }
};

export const getData = entity.selectors.getData;
export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;