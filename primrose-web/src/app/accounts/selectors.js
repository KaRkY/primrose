import { createSelector } from "reselect";
import queryString from "query-string";
import EntitiesSelectors from "@/entities-selectors";
import LocationSelectors from "@/location-selectors";
import SettingsSelectors from "@/settings-selectors";

const root = state => state.app.accounts;
const search = createSelector(root, state => state.search);
const pages = createSelector(search, search => search.pages);
const query = createSelector(
  search,
  search => ({
    size: search.current.size,
    page: search.current.page,
    search: search.current.search
  })
);

const page = createSelector(
  pages, 
  query, 
  (pages, query) => pages && pages[queryString.stringify(query)]);

const lastUpdated = createSelector(page, page => page && page.lastUpdated);
const results = createSelector(page, page => page && page.results);
const loading = createSelector(search, search => search.loading);
const currentPageData = createSelector(results, EntitiesSelectors.accounts, (results, entities) => results.map(id => entities[id]));

export default {
  root,
  search: {
    currentPageData,
    query,
    loading,
    lastUpdated,
  }
};