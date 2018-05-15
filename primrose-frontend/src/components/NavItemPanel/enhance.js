import withStateHandlers from "recompose/withStateHandlers";

export default withStateHandlers(
  ({ open }) => ({ panelOpen: open }),
  {
    onToggleOpen: ({ panelOpen }) => () => ({ panelOpen: !panelOpen }),
  }
);