import { createSelector } from "reselect";
import getPreviousQuery from "./getPreviousQuery";

export default createSelector(
  getPreviousQuery,
  query => parseInt((query && query.size) || 5, 10)
);