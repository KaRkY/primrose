import * as location from "../store/location";

export default (state, action, isLoading) => {
  const cur = location.getCurrentPagination(state);
  const prev = location.getPreviousPagination(state);
  const currentPathname = location.getCurrentPathname(state);
  const previousPathname = location.getPreviousPathname(state);

  if (!(action.payload && action.payload.force)) {
    if (
      prev.page === cur.page && 
      prev.size === cur.size &&
      prev.query === cur.query &&
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
    if (isLoading(state)) return false;
  }

  return true;
};