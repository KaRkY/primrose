import * as actions from "../actions";
import createEntity from "./createEntity";

const entity = createEntity({
  baseAction: actions.customers,
  loadingAction: actions.customersLoading,
  fetchedAction: actions.customersFetched,
  errorAction: actions.customersError,
  rootSelector: state => state.customers,
});

export const getCount = entity.selectors.getCount;
export const getData = entity.selectors.getData;
export const getError = entity.selectors.getError;
export const isLoading = entity.selectors.isLoading;
export default entity.reducer;