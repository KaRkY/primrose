import dynamic from "../dynamic";
import Wizard from "./Wizard";

export default dynamic({
  propName: "steps",
  ordered: true,

  Step: {
    renderChildren: true,
  }
})(Wizard);