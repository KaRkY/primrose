import axios from "../axios";
import convertError from "../util/convertError";
import createMetaEntity from "./creators/createMetaEntity";
import * as actions from "../actions";

const entity = createMetaEntity({
  loadingAction: actions.customerTypesLoad,
  fetchedAction: actions.customerTypesLoadFinished,
  errorAction: actions.customerTypesLoadError,
  rootSelector: state => state.customerTypes,
});

export const apiLoad = ({
  dispatch,
  state,
  action
}) => {
  if (!getData(state) || getError(state)) {
    dispatch(actions.customerTypesLoad());
    return axios.post("/meta", {
      jsonrpc: "2.0",
      method: "customer",
      id: Date.now(),
    })
      .then(result => dispatch(actions.customerTypesLoadFinished(result.data.result)))
      .catch(error => dispatch(actions.customerTypesLoadError(convertError(error))));
  } else {
    return Promise.resolve();
  }
};

export const getData = entity.selectors.getData;
export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;