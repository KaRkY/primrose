import { createSelector } from "reselect";

const root = state => state.entities;
const accounts = createSelector(root, root => root.account);

export default {
  root,
  accounts,
};