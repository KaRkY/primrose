import * as markdownExample from "../../store/markdownExample";

export default (state, props) => ({
  customer: markdownExample.getData(state),
});