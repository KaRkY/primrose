import * as actions from "../actions";
import createEntity from "./createEntity";

const entity = createEntity({
  baseAction: actions.contacts,
  loadingAction: actions.contactsLoading,
  fetchedAction: actions.contactsFetched,
  errorAction: actions.contactsError,
  rootSelector: state => state.contacts,
});

export const getCount = entity.selectors.getCount;
export const getData = entity.selectors.getData;
export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;