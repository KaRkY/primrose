import { createSelector } from "reselect";

const root = state => state.location;
const query = createSelector(root, root => root.query);
const type = createSelector(root, root => root.type);

export default {
  root,
  type,
  query,
};