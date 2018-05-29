import renderProp from "../../util/renderProp";
import withWidth, { isWidthDown } from "@material-ui/core/withWidth";

const MobileWrapper = ({ children, render, width, ...rest}) => renderProp({ children, render }, isWidthDown("md", width));

export default withWidth()(MobileWrapper);