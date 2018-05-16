import * as actions from "../../actions";

export default dispatch => ({
  handleList: payload => dispatch(actions.customerListPage(payload)),
  handleView: (event, payload) => dispatch(actions.customerViewPage(payload)),
  handleNew: () => dispatch(actions.customerNewPage()),
  handleUpdate: (event, payload) => dispatch(actions.customerUpdatePage(payload)),
});