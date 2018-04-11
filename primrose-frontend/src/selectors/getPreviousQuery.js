import { createSelector } from "reselect";

const location = state => state.location.prev;

export default createSelector(
  location,
  location => {
    if(!location.query) return;

    const page = parseInt(location.query.page, 10);
    const size = parseInt(location.query.size, 10);

    return {
      ...location.query,
      page,
      size,
    };
  }
);