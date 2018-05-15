import * as contactView from "../../store/contactView";

export default (state, props) => ({
  contact: contactView.getData(state),
});