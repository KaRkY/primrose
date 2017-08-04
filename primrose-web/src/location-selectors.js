import { createSelector } from "reselect";

const root = state => state.location;
const query = createSelector(root, root => root.query);
const type = createSelector(root, root => root.type);
const querySize = createSelector(query, query => query && query.size);
const queryPage = createSelector(query, query => query && query.page);
const querySearch = createSelector(query, query => query && query.search);

export default {
  root,
  type,
  query: {
    size: querySize,
    page: queryPage,
    search: querySearch,
  },
};