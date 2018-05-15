import Main from "./Main";
import { connect } from "react-redux";
import withStyles from "@material-ui/core/styles/withStyles";

import styles from "./styles";
import mapDispatchTo from "./mapDispatchTo";
import mapState from "./mapState";


export default connect(mapState, mapDispatchTo)(withStyles(styles)(Main));