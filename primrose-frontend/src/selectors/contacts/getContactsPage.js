import { createSelector } from "reselect";
import getQuery from "../getQuery";
import getPageId from "../../util/getPageId";

const contacts = state => state.contacts;
const contactsData = createSelector(contacts, contacts => contacts.byId);
const contactsPage = createSelector(
  contacts,
  getQuery,
  (contacts, query) => {
  const page = contacts.pages[getPageId(query)];
  return (page && page.result) || [];
});

export default createSelector(
  contactsData,
  contactsPage,
  (contactsData, contactsPage) => contactsPage.map(id => contactsData[id])
);