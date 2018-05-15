export default theme => ({
  root: {
    background: theme.palette.action.disabledBackground,
    position: "absolute",
    height: "100%",
    width: "100%",
  },

  icon: {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
  },
});