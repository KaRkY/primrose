import withStateHandlers from "recompose/withStateHandlers";

export default withStateHandlers(
  () => ({ panelAnchorEl: null }),
  {
    onPanelOpen: ({ panelAnchorEl, ...props }) => panelAnchorEl => ({ panelAnchorEl, ...props }),
    onPanelClose: ({ panelAnchorEl, ...props }) => () => ({ panelAnchorEl: null, ...props }),
  }
);