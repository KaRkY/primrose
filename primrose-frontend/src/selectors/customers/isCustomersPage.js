import { createSelector } from "reselect";
import getQuery from "../getQuery";
import getPageId from "../../util/getPageId";

const customers = state => state.customers;
export default createSelector(
  customers,
  getQuery,
  (customers, query) => {
  const page = customers.pages[getPageId(query)];
  return !!page && page.loaded;
});