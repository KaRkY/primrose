import axios from "../axiosInstance";
import shouldReloadPageData from "../util/shouldReloadPageData";
import convertError from "../util/convertError";
import createEntity from "./createEntity";
import * as actions from "../actions";
import * as location from "./location";

const entity = createEntity({
  baseAction: actions.customers,
  loadingAction: actions.customersLoading,
  fetchedAction: actions.customersFetched,
  errorAction: actions.customersError,
  rootSelector: state => state.customers,
});

export const apiLoad = ({
  dispatch,
  state,
  action
}) => {
  const pagination = location.getCurrentPagination(state);

  if (shouldReloadPageData(state, action, isLoading)) {
    dispatch(actions.customersLoading());
    return axios.get("/customers", { params: pagination })
      .then(result => dispatch(actions.customersFetched(result.data)))
      .catch(error => dispatch(actions.customersError(convertError(error))));
  } else {
    return Promise.resolve();
  }
};

export const apiCreate = ({
  dispatch,
  state,
  action
}) => {
  axios.post("/customers", action.payload)
    .then(result => dispatch(actions.customer(result.data)))
    .catch(console.log);
};

export const getCount = entity.selectors.getCount;
export const getData = entity.selectors.getData;
export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;