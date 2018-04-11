import { createSelector } from "reselect";
import getCurrentQuery from "./getCurrentQuery";

export default createSelector(
  getCurrentQuery,
  query => parseInt((query && query.page) || 0, 10)
);