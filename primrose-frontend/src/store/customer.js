import axios from "../axios";
import convertError from "../util/convertError";
import createEntity from "./creators/createEntity";
import * as actions from "../actions";
import * as location from "./location";

const entity = createEntity({
  loadingAction: actions.customerLoad,
  fetchedAction: actions.customerLoadFinished,
  errorAction: actions.customerLoadError,
  rootSelector: state => state.customer,
});

export const apiLoad = ({
  dispatch,
  state,
  action
}) => {
    dispatch(actions.customerLoad());
    return axios.post("/customers", {
      jsonrpc: "2.0",
      method: "get",
      params: {
        customer: parseInt(location.getCurrentData(state).id, 10)
      },
      id: Date.now(),
    })
      .then(result => dispatch(actions.customerLoadFinished(result.data.result)))
      .catch(error => dispatch(actions.customerLoadError(convertError(error))));
};

export const getData = entity.selectors.getData;
export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;