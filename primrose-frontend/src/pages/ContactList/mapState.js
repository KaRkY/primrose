import * as location from "../../store/location";
import * as contactList from "../../store/contactList";

export default (state, props) => ({
  contacts: contactList.getData(state),
  pagination: location.getCurrentPagination(state),
  totalSize: contactList.getCount(state),
  query: location.getCurrentQuery(state),
});