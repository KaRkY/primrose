import { createSelector } from "reselect";

export default createSelector(
  state => state.customers,
  customers => customers.totalCount,
);