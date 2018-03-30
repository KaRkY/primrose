import { createSelector } from "reselect";
import isCustomersPage from "./isCustomersPage";

export default createSelector(
  isCustomersPage,
  isLoading => isLoading);