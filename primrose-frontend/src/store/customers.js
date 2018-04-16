import axios from "../axios";
import shouldReloadPageData from "../util/shouldReloadPageData";
import convertError from "../util/convertError";
import createEntity from "./createEntity";
import * as actions from "../actions";
import * as location from "./location";

const entity = createEntity({
  baseAction: actions.customers,
  loadingAction: actions.customersLoad,
  fetchedAction: actions.customersLoadFinished,
  errorAction: actions.customersLoadError,
  rootSelector: state => state.customers,
});

export const apiLoad = ({
  dispatch,
  state,
  action
}) => {
  const pagination = location.getCurrentPagination(state);

  if (shouldReloadPageData(state, action, isLoading)) {
    dispatch(actions.customersLoad());
    return axios.post("/customers", {
      jsonrpc: "2.0",
      method: "search",
      params: {
        search: pagination
      },
      id: Date.now(),
    })
      .then(result => dispatch(actions.customersLoadFinished(result.data.result)))
      .catch(error => dispatch(actions.customersLoadError(convertError(error))));
  } else {
    return Promise.resolve();
  }
};

export const getCount = entity.selectors.getCount;
export const getData = entity.selectors.getData;
export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;