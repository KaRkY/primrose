import ContactList from "./ContactList";
import mapDispatchTo from "./mapDispatchTo";
import mapState from "./mapState";
import enhance from "./enhance";
import { connect } from "react-redux";


export default connect(mapState, mapDispatchTo)(enhance(ContactList));