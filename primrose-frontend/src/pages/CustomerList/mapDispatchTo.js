import * as actions from "../../actions";

export default dispatch => ({
  handlePaged: payload => dispatch(actions.customerListPage(payload)),
  handleSingle: payload => dispatch(actions.customerViewPage(payload)),
  handleNew: payload => dispatch(actions.customerNewPage(payload)),
  handleUpdate: payload => dispatch(actions.customerUpdatePage(payload)),
});