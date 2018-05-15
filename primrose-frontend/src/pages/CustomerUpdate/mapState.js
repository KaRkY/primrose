import * as location from "../../store/location";
import * as customerUpdate from "../../store/customerUpdate";
import meta from "../../store/meta";

export default (state, props) => ({
  customer: customerUpdate.getData(state),
  customerCode: location.getCurrentData(state).customer,
  customerTypes: meta.customerTypes.getData(state),
  customerRelationTypes: meta.customerRelationTypes.getData(state),
  emailTypes: meta.emailTypes.getData(state),
  phoneNumberTypes: meta.phoneNumberTypes.getData(state),
});