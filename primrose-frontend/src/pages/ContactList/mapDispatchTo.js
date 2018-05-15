import * as actions from "../../actions";

export default dispatch => ({
  handlePaged: payload => dispatch(actions.contactListPage(payload)),
  handleSingle: payload => dispatch(actions.contactViewPage(payload)),
  handleNew: payload => dispatch(actions.contactNewPage(payload)),
  handleUpdate: payload => dispatch(actions.contactUpdatePage(payload)),
});