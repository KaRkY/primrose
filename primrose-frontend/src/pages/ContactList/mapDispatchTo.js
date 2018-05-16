import * as actions from "../../actions";

export default dispatch => ({
  handleList: payload => dispatch(actions.contactListPage(payload)),
  handleView: (event, payload) => dispatch(actions.contactViewPage(payload)),
  handleNew: () => dispatch(actions.contactNewPage()),
  handleUpdate: (event, payload) => dispatch(actions.contactUpdatePage(payload)),
});