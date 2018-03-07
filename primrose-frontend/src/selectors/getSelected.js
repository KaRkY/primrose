import { createSelector } from "reselect";
import getQuery from "./getQuery";
import normalizeArray from "../util/normalizeArray";

export default createSelector(
  getQuery,
  query => normalizeArray(query.selected)
);