import { createSelector } from "reselect";

const root = state => state.settings;

const accountsSearchDefaultPagging = createSelector(
  root,
  root => root.defaults.query[root.app.accounts.search.paging]
);

export default {
  root,
  accountsSearchDefaultPagging
};