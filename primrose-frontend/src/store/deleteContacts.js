import axios from "../axiosInstance";
import convertError from "../util/convertError";
import createExecuteLifecycle from "./createExecuteLifecycle";
import * as actions from "../actions";

const entity = createExecuteLifecycle({
  baseAction: actions.contactsDelete,
  createdAction: actions.contactsDeleteFinished,
  errorAction: actions.contactsDeleteError,
  rootSelector: state => state.deleteContacts,
});

export const apiDelete = ({
  dispatch,
  state,
  action
}) => {
  const contacts = action.payload.contacts;
  if (Array.isArray(contacts)) {
    axios.delete("/contacts", {
        jsonrpc: "2.0",
        method: "delete",
        params: {
          contact: contacts
        },
        id: Date.now(),
      })
      .then(result => dispatch(actions.contactsDeleteFinished(result.data.result)))
      .catch(error => dispatch(actions.contactsDeleteError(convertError(error))));
  } else {
    axios.delete("/contacts", {
        jsonrpc: "2.0",
        method: "delete",
        params: {
          contacts: contacts
        },
        id: Date.now(),
      })
      .then(result => dispatch(actions.contactsDeleteFinished(result.data.result)))
      .catch(error => dispatch(actions.contactsDeleteError(convertError(error))));
  }
};

export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;