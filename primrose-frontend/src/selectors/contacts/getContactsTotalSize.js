import { createSelector } from "reselect";

export default createSelector(
  state => state.contacts,
  contacts => contacts.totalCount,
);