import axios from "../axiosInstance";
import convertError from "../util/convertError";
import createExecuteLifecycle from "./createExecuteLifecycle";
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
  axios.post("/contacts", action.payload)
    .then(result => dispatch(actions.contactCreateFinished(result.data)))
    .catch(error => dispatch(actions.contactCreateError(convertError(error))));
};

export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;