import * as location from "../../store/location";
import * as page from "../../store/page";
import * as title from "../../store/title";

export default (state, props) => ({
  title: title.getTitle(state),
  page: page.getComponent(state),
  pageType: location.getPageType(state),
  isLoading: page.isLoading(state),
  error: page.getError(state),
  pathname: location.getCurrentPathname(state),
});