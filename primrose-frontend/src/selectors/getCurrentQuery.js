import { createSelector } from "reselect";

const location = state => state.location;

export default createSelector(
  location,
  location => location.query
);