import withConfig from "./withConfig";
import PropTypes from "prop-types";
import App from "./App";

export const config = {
  Toolbar: {
    key: "toolbar",
    propTypes: {
      title: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.element,
      ]).isRequired,
      position: PropTypes.oneOf(["static", "fixed"]),
    },
    renderChildren: true,
    mapProps: {
      children: "actions"
    }
  },

  Navigation: {
    key: "navigation",
    renderChildren: true,
  },

  Content: {
    key: "content",
    renderChildren: true,
  },
};

export default withConfig(config)(App);