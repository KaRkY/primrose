import axios from "../axios";
import convertError from "../util/convertError";
import createExecuteLifecycle from "./creators/createExecuteLifecycle";
import * as actions from "../actions";

const entity = createExecuteLifecycle({
  baseAction: actions.contactCreate,
  createdAction: actions.contactCreateFinished,
  errorAction: actions.contactCreateError,
  rootSelector: state => state.createContact,
});

export const apiCreate = ({
  dispatch,
  state,
  action
}) => {
  axios.post("/contacts", {
    jsonrpc: "2.0",
    method: "create",
    params: {
      contact: action.payload
    },
    id: Date.now(),
  })
    .then(result => dispatch(actions.contactCreateFinished(result.data.result)))
    .catch(error => dispatch(actions.contactCreateError(convertError(error))));
};

export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;