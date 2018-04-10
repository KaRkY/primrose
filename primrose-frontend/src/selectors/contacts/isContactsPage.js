import { createSelector } from "reselect";
import getQuery from "../getQuery";
import getPageId from "../../util/getPageId";

const contacts = state => state.contacts;
export default createSelector(
  contacts,
  getQuery,
  (contacts, query) => {
  const page = contacts.pages[getPageId(query)];
  return !!page && page.loaded;
});