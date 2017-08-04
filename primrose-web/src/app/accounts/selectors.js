import { createSelector } from "reselect";
import queryString from "query-string";
import EntitiesSelectors from "@/entities-selectors";
import LocationSelectors from "@/location-selectors";
import SettingsSelectors from "@/settings-selectors";

const root = state => state.app.accounts;
const search = createSelector(root, state => state.search);
const pages = createSelector(search, search => search.pages);
const query = createSelector(
  LocationSelectors.query,
  SettingsSelectors.accountsSearchDefaultPagging,
  (query, defaultQuery) => query || defaultQuery
);
const headers = createSelector(
  search,
  search => search.headers
);

const page = createSelector(
  pages, 
  query, 
  (pages, query) => pages && pages[queryString.stringify(query)]);

const lastUpdated = createSelector(page, page => page && page.lastUpdated);
const results = createSelector(page, page => page && page.results);
const loading = createSelector(search, search => search.loading);
const isFirst = createSelector(query, headers, (query, headers) => parseInt(query.page) === 1);
const isLast = createSelector(query, headers, (query, headers) => parseInt(headers["search-number-of-pages"]) === query.page);
const currentPageNumber = createSelector(headers, headers => parseInt(headers["search-page-number"]) || 0);
const numberOfPages = createSelector(headers, headers => parseInt(headers["search-number-of-pages"]) || 0);
const currentPageData = createSelector(results, EntitiesSelectors.accounts, (results, entities) => results && results.map(id => entities[id]));

export default {
  root,
  search: {
    currentPageData,
    query,
    loading,
    lastUpdated,
    isFirst,
    isLast,
    currentPageNumber,
    numberOfPages
  }
};