import * as location from "../../store/location";
import * as customerList from "../../store/customerList";
import meta from "../../store/meta";

export default (state, props) => ({
  customers: customerList.getData(state),
  customerTypes: meta.customerTypes.getData(state),
  customerRelationTypes: meta.customerRelationTypes.getData(state),
  pagination: location.getCurrentPagination(state),
  totalSize: customerList.getCount(state),
});