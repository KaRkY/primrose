import App from "./App";
import styles from "./styles";
import enhance from "./enhance";
import withStyles from "@material-ui/core/styles/withStyles";

export default withStyles(styles)(enhance(App));