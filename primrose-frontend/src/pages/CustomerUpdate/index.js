import CustomerUpdate from "./CustomerUpdate";
import styles from "./styles";
import withStyles from "@material-ui/core/styles/withStyles";
import mapDispatchTo from "./mapDispatchTo";
import mapState from "./mapState";
import { connect } from "react-redux";


export default connect(mapState, mapDispatchTo)(withStyles(styles)(CustomerUpdate));