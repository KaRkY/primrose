import * as actions from "../actions";
import createPagedResource from "./createPagedResource";

export default createPagedResource({
  baseAction: actions.customers,
  loadingAction: actions.customersLoading,
  fetchedAction: actions.customersFetched,
  errorAction: actions.customersError,
});