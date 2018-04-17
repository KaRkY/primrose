import axios from "../axios";
import convertError from "../util/convertError";
import createMetaEntity from "./creators/createMetaEntity";
import * as actions from "../actions";

const entity = createMetaEntity({
  loadingAction: actions.customerRelationTypesLoad,
  fetchedAction: actions.customerRelationTypesLoadFinished,
  errorAction: actions.customerRelationTypesLoadError,
  rootSelector: state => state.customerRelationTypes,
});

export const apiLoad = ({
  dispatch,
  state,
  action
}) => {
  if (!getData(state) || getError(state)) {
    dispatch(actions.customerRelationTypesLoad());
    return axios.post("/meta", {
      jsonrpc: "2.0",
      method: "customerRelation",
      id: Date.now(),
    })
      .then(result => dispatch(actions.customerRelationTypesLoadFinished(result.data.result)))
      .catch(error => dispatch(actions.customerRelationTypesLoadError(convertError(error))));
  } else {
    return Promise.resolve();
  }
};

export const getData = entity.selectors.getData;
export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;