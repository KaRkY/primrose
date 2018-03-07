import { createSelector } from "reselect";

const location = state => state.location;

export default createSelector(
  location,
  location => {
    const page = parseInt((location.query && location.query.page) || 0, 10);
    const size = parseInt((location.query && location.query.size) || 10, 10);

    return {
      page,
      size,
      ...location.query,
    };
  }
);