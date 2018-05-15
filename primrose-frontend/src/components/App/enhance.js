import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import withStateHandlers from "recompose/withStateHandlers";
import withMobile from "../withMobile";
import withTheme from "@material-ui/core/styles/withTheme";

export default compose(
  withMobile(),
  withTheme(),
  withStateHandlers(
    ({ mobile }) => ({ drawerOpen: !mobile }),
    {
      onDrawerOpen: ({ drawerOpen, ...props }) => () => ({ drawerOpen: true, ...props }),
      onDrawerClose: ({ drawerOpen, ...props }) => () => ({ drawerOpen: false, ...props }),
    }
  ),
  withStateHandlers(
    () => ({ open: false, current: {}, key: null, queue: [] }),
    {
      push: ({ queue, ...props }) => item => ({ ...props, queue: [...queue, { ...item, key: new Date().getTime() }] }),
      process: ({ queue, ...props }) => () => {
        if (queue.length > 0) {
          const [first, ...rest] = queue;
          return ({ ...props, open: true, current: first, queue: rest });
        } else {
          return ({ ...props, open: false, current: {}, queue });
        }
      },
      close: ({ open, ...props }) => (event, reason) => ({ ...props, open: reason === "clickaway" ? open : false }),
    }),
  withHandlers({
    push: ({ push, open, process }) => item => {
      push(item);
      if (!open) {
        process();
      }
    },
  }),
  
);