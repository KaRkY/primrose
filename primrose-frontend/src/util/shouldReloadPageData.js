import getCurrentPage from "../selectors/getCurrentPage";
import getPreviousPage from "../selectors/getPreviousPage";
import getCurrentSize from "../selectors/getCurrentSize";
import getPreviousSize from "../selectors/getPreviousSize";
import getCurrentPathname from "../selectors/getCurrentPathname";
import getPreviousPathname from "../selectors/getPreviousPathname";
import isCustomersLoading from "../selectors/customers/isLoading";

export default (getState, action) => {
  const state = getState();
  const currentPage = getCurrentPage(state);
  const previousPage = getPreviousPage(state);
  const currentSize = getCurrentSize(state);
  const previousSize = getPreviousSize(state);
  const currentPathname = getCurrentPathname(state);
  const previousPathname = getPreviousPathname(state);

  if (!(action.payload && action.payload.force)) {
    if (currentPage === previousPage && currentSize === previousSize && currentPathname === previousPathname) return false;
    if (isCustomersLoading(state)) return false;
  }

  return true;
};