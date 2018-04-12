import {
  createSelector
} from "reselect";
import normalizeArray from "../util/normalizeArray";

const getPaginationFromQuery = (query = {}) => {
  let sort;
  if (query.sortProperty) {
    sort = {
      property: query.sortProperty,
      direction: query.sortDirection,
    };
  }

  let selected;
  if (query.selected) {
    selected = normalizeArray(query.selected)
      .map(val => {
        const num = Number(val);
        return Number.isNaN(num) ? val : num;
      });
  }

  return {
    page: parseInt(query.page || 0, 10),
    size: parseInt(query.size || 5, 10),
    sort,
    selected,
    query: query.query,
  };
};

export const getLocation = state => state.location;
export const getCurrentQuery = createSelector(getLocation, location => location.query);
export const getPreviousQuery = createSelector(getLocation, location => location.prev.query);
export const getCurrentPagination = createSelector(getCurrentQuery, getPaginationFromQuery);
export const getPreviousPagination = createSelector(getPreviousQuery, getPaginationFromQuery);
export const getPageType = createSelector(getLocation, location => location.type);
export const getCurrentPathname = createSelector(getLocation, location => location.pathname);
export const getPreviousPathname = createSelector(getLocation, location => location.prev.pathname);