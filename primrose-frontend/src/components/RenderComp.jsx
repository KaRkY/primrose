import extractRenderMethod from "../util/extractRenderMethod";

const RenderComp = ({ children, component, render, ...props }) => {
  return extractRenderMethod({ children, component, render })(props);
};
export default RenderComp;