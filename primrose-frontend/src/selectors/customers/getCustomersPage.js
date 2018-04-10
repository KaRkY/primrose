import { createSelector } from "reselect";
import getQuery from "../getQuery";
import getPageId from "../../util/getPageId";

const customers = state => state.customers;
const customersData = createSelector(customers, customers => customers.byId);
const customersPage = createSelector(
  customers,
  getQuery,
  (customers, query) => {
  const page = customers.pages[getPageId(query)];
  return (page && page.result) || [];
});

export default createSelector(
  customersData,
  customersPage,
  (customersData, customersPage) => customersPage.map(id => customersData[id])
);