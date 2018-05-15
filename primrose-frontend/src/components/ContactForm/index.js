import ContactForm from "./ContactForm";
import styles from "./styles";
import enhance from "./enhance";
import withStyles from "@material-ui/core/styles/withStyles";

export default withStyles(styles)(enhance(ContactForm));