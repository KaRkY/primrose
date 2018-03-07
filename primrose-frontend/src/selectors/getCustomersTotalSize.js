import { createSelector } from "reselect";
import getQuery from "./getQuery";

export default createSelector(
  state => state.customers,
  customers => customers.totalCount,
);