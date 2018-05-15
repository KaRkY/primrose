import * as actions from "../../actions";

export default dispatch => ({
  handleView: payload => dispatch(actions.contactViewPage(payload)),
});