import * as location from "../../store/location";
import * as contactUpdate from "../../store/contactUpdate";
import meta from "../../store/meta";

export default (state, props) => ({
  contact: contactUpdate.getData(state),
  contactCode: location.getCurrentData(state).contact,
  emailTypes: meta.emailTypes.getData(state),
  phoneNumberTypes: meta.phoneNumberTypes.getData(state),
});