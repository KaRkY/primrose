import isLoading from "../../store/isLoading";
import getError from "../../store/getError";
import * as location from "../../store/location";
import * as page from "../../store/page";
import * as title from "../../store/title";

export default (state, props) => ({
  title: title.getTitle(state),
  page: page.getPage(state),
  pageType: location.getPageType(state),
  isLoading: isLoading(state),
  error: getError(state),
  pathname: location.getCurrentPathname(state),
});