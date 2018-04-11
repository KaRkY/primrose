import { createSelector } from "reselect";
import getCurrentQuery from "./getCurrentQuery";

export default createSelector(
  getCurrentQuery,
  query => query && query.sortProperty
);