import withConfig from "../withConfig";
import Nav from "./Nav";

export const config = {
  propName: "items",
  ordered: true,

  Item: {
    ordered: true,
  }
};

export default withConfig(config)(Nav);