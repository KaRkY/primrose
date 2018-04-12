import * as actions from "../actions";
import createPagedResource from "./createPagedResource";

export default createPagedResource({
  baseAction: actions.contacts,
  loadingAction: actions.contactsLoading,
  fetchedAction: actions.contactsFetched,
  errorAction: actions.contactsError,
});