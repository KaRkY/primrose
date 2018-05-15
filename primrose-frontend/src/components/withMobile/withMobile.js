import withWidth, { isWidthDown } from "@material-ui/core/withWidth";

import compose from "recompose/compose";
import mapProps from "recompose/mapProps";

export default () => compose(
  withWidth(),
  mapProps(({ width, ...rest }) => ({ mobile: isWidthDown("md", width), ...rest })),
);