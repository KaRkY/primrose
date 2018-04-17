import axios from "../axios";
import convertError from "../util/convertError";
import createExecuteLifecycle from "./creators/createExecuteLifecycle";
import * as actions from "../actions";

const entity = createExecuteLifecycle({
  baseAction: actions.customerCreate,
  createdAction: actions.customerCreateFinished,
  errorAction: actions.customerCreateError,
  rootSelector: state => state.createCustomer,
});

export const apiCreate = ({
  dispatch,
  state,
  action
}) => {
  axios.post("/customers", {
    jsonrpc: "2.0",
    method: "create",
    params: {
      customer: action.payload
    },
    id: Date.now(),
  })
    .then(result => dispatch(actions.customerCreateFinished(result.data.result)))
    .catch(error => dispatch(actions.customerCreateError(convertError(error))));
};

export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;