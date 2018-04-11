import { createSelector } from "reselect";
import getCurrentQuery from "./getCurrentQuery";
import normalizeArray from "../util/normalizeArray";

export default createSelector(
  getCurrentQuery,
  query => {
    const selected = query && normalizeArray(query.selected);
    return selected && selected.map(val => {
      const num = Number(val);
      return Number.isNaN(num) ? val : num;
    });
  }
);