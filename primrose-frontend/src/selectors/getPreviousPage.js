import { createSelector } from "reselect";
import getPreviousQuery from "./getPreviousQuery";

export default createSelector(
  getPreviousQuery,
  query => parseInt((query && query.page) || 0, 10)
);