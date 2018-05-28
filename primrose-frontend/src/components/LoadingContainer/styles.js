export default theme => ({
  root: {
    position: "relative",
  },

  loading: {
    background: theme.palette.action.disabledBackground,
    position: "absolute",
    //height: "100%",
    //width: "100%",
    top: 0,
    bottom: 0,
    left: 0,
    right: 0,
  },

  icon: {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
  },
});