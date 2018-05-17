import * as customerView from "../../store/customerView";

export default (state, props) => ({
  customer: customerView.getData(state),
});