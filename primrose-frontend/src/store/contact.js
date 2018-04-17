import axios from "../axios";
import convertError from "../util/convertError";
import createEntity from "./creators/createEntity";
import * as actions from "../actions";
import * as location from "./location";

const entity = createEntity({
  loadingAction: actions.contactLoad,
  fetchedAction: actions.contactLoadFinished,
  errorAction: actions.contactLoadError,
  rootSelector: state => state.contact,
});

export const apiLoad = ({
  dispatch,
  state,
  action
}) => {
    dispatch(actions.contactLoad());
    return axios.post("/contacts", {
      jsonrpc: "2.0",
      method: "get",
      params: {
        contact: parseInt(location.getCurrentData(state).id, 10)
      },
      id: Date.now(),
    })
      .then(result => dispatch(actions.contactLoadFinished(result.data.result)))
      .catch(error => dispatch(actions.contactLoadError(convertError(error))));
};

export const getData = entity.selectors.getData;
export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;