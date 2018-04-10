import withConfig from "../withConfig";
import Wizard from "./Wizard";

export const config = {
  propName: "steps",
  ordered: true,

  Step: {
    renderChildren: true,
  }
};

export default withConfig(config)(Wizard);