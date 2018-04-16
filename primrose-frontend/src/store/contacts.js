import axios from "../axiosInstance";
import shouldReloadPageData from "../util/shouldReloadPageData";
import convertError from "../util/convertError";
import createEntity from "./createEntity";
import * as actions from "../actions";
import * as location from "./location";

const entity = createEntity({
  baseAction: actions.contacts,
  loadingAction: actions.contactsLoad,
  fetchedAction: actions.contactsLoadFinished,
  errorAction: actions.contactsLoadError,
  rootSelector: state => state.contacts,
});

export const apiLoad = ({
  dispatch,
  state,
  action
}) => {
  const pagination = location.getCurrentPagination(state);

  if (shouldReloadPageData(state, action, isLoading)) {
    dispatch(actions.contactsLoad());
    return axios.post("/contacts", {
      jsonrpc: "2.0",
      method: "search",
      params: {
        search: pagination
      },
      id: Date.now(),
    })
      .then(result => dispatch(actions.contactsLoadFinished(result.data.result)))
      .catch(error => dispatch(actions.contactsLoadError(convertError(error))));
  } else {
    return Promise.resolve();
  }
};

export const getCount = entity.selectors.getCount;
export const getData = entity.selectors.getData;
export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;