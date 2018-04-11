import { createSelector } from "reselect";

const location = state => state.location.prev;
export default createSelector(
  location,
  location => location && location.pathname
);