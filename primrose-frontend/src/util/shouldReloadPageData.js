import * as location from "../store/location";
import isEqual from "lodash/isEqual";
import omit from "lodash/omit";

export default (state, action) => {
  const cur = location.getCurrentPagination(state);
  const prev = location.getPreviousPagination(state);
  const currentPathname = location.getCurrentPathname(state);
  const previousPathname = location.getPreviousPathname(state);


  console.log("equal", isEqual(
    omit(cur, "selected"), 
    omit(prev, "selected"),),
    cur,
    prev);


  if (!(action.payload && action.payload.force)) {
    if (
      prev.page === cur.page && 
      prev.size === cur.size &&
      prev.search === cur.search &&
      currentPathname === previousPathname) {
        if(prev.sort === undefined && cur.sort === undefined) {
          return false;
        } else {
          if(
            prev.sort && 
            cur.sort &&
            prev.sort.property === cur.sort.property &&
            prev.sort.direction === cur.sort.direction 
          ) {
            return false;
          } else {
            return true;
          }
        }
      };
  }

  return true;
};