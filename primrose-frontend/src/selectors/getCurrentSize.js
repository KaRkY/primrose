import { createSelector } from "reselect";
import getCurrentQuery from "./getCurrentQuery";

export default createSelector(
  getCurrentQuery,
  query => parseInt((query && query.size) || 5, 10)
);