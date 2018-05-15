import App from "./App";
import enhance from "./enhance";
import styles from "./styles";
import withStyles from "@material-ui/core/styles/withStyles";

export default withStyles(styles)(enhance(App));