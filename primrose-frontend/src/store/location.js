import {
  createSelector
} from "reselect";
import normalizeArray from "../util/normalizeArray";

const getPaginationFromQuery = (query = {}) => {
  const result = {};

  if (query.page) {
    result.page = parseInt(query.page, 10);
  } else {
    result.page = 0;
  }

  if (query.size) {
    result.size = parseInt(query.size, 10);
  } else {
    result.size = 5;
  }


  if (query.sortProperty) {
    result.sort = {
      property: query.sortProperty,
      direction: query.sortDirection,
    };
  }

  if (query.selected) {
    result.selected = normalizeArray(query.selected);
  }

  if (query.search) {
    result.search = query.search;
  }

  if("searchOpen" in query) {
    result.searchOpen = null;
  }

  return result;
};

export const getLocation = state => state.location;
export const getCurrentData = createSelector(getLocation, location => location.payload);
export const getCurrentQuery = createSelector(getLocation, location => location.query);
export const getPreviousQuery = createSelector(getLocation, location => location.prev.query);
export const getCurrentPagination = createSelector(getCurrentQuery, getPaginationFromQuery);
export const getPreviousPagination = createSelector(getPreviousQuery, getPaginationFromQuery);
export const getPageType = createSelector(getLocation, location => location.type);
export const getCurrentPathname = createSelector(getLocation, location => location.pathname);
export const getPreviousPathname = createSelector(getLocation, location => location.prev.pathname);