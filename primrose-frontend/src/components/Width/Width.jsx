import renderProp from "../../util/renderProp";
import withWidth from "@material-ui/core/withWidth";

const WidthWrapper = ({ children, render, width}) => renderProp({ children, render }, width);

export default withWidth()(WidthWrapper);