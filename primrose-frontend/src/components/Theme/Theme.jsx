import renderProp from "../../util/renderProp";
import withTheme from "@material-ui/core/styles/withTheme";

const ThemeWrapper = ({ children, render, theme}) => renderProp({ children, render }, theme);

export default withTheme()(ThemeWrapper);