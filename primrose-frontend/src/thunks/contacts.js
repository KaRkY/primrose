import * as actions from "../actions";
import convertError from "../util/convertError";
import isContactsLoading from "../selectors/contacts/isLoading";
import { load as loadContacts } from "../api/contacts";
import getCurrentQuery from "../selectors/getCurrentQuery";
import getCurrentPage from "../selectors/getCurrentPage";
import getPreviousPage from "../selectors/getPreviousPage";
import getCurrentSize from "../selectors/getCurrentSize";
import getPreviousSize from "../selectors/getPreviousSize";
import getCurrentSortProperty from "../selectors/getCurrentSortProperty";
import getCurrentSortDirection from "../selectors/getCurrentSortDirection";
import getCurrentPathname from "../selectors/getCurrentPathname";
import getPreviousPathname from "../selectors/getPreviousPathname";


export const load = async (dispatch, getState) => {
  const currentPage = getCurrentPage(getState());
  const previousPage = getPreviousPage(getState());
  const currentSize = getCurrentSize(getState());
  const previousSize = getPreviousSize(getState());
  const currentPathname = getCurrentPathname(getState());
  const previousPathname = getPreviousPathname(getState());
  const currentSortProperty = getCurrentSortProperty(getState());
  const currentSortDirection = getCurrentSortDirection(getState());

  if(isContactsLoading(getState())) return;
  if(currentPage === previousPage && currentSize === previousSize && currentPathname === previousPathname) return;
  dispatch({ type: "CONTACTS_FETCH"});
  loadContacts({
    page: currentPage,
    size: currentSize,
    sortProperty: currentSortProperty,
    sortDirection: currentSortDirection,
  })
  .then(result => dispatch(actions.contactsFetched(result.data)))
  .catch(error => dispatch(actions.contactsError(convertError(error))));
};

export const del = async (dispatch, getState, { action }) => {
  console.log(action);

  dispatch(actions.contacts({
    query: getCurrentQuery(getState()),
    selected: undefined,
  }));
};