export default theme => ({
  root: {
    position: "relative",
    display: "flex",
    width: "100%",
    height: "100vh",
  },

  appBar: {
    transition: theme.transitions.create(["margin", "width"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  appBarShift: {
    width: `calc(100% - ${theme.drawer.width}px)`,
    transition: theme.transitions.create(["margin", "width"], {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  appBarShiftLeft: {
    marginLeft: theme.drawer.width,
  },
  appBarShiftRight: {
    marginRight: theme.drawer.width,
  },

  appDrawerHeader: {
    display: "flex",
    alignItems: "center",
    justifyContent: "flex-end",
    padding: "0 8px",
    ...theme.mixins.toolbar
  },

  appDrawerPaper: {
    width: theme.drawer.width,
  },

  appContent: {
    boxSizing: "border-box",
    width: "100%",
    flexGrow: 1,
    backgroundColor: theme.palette.background.default,
    padding: theme.spacing.unit * 3,
    transition: theme.transitions.create("margin", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    minHeight: "calc(100vh - 56px)",
    marginTop: 56,
    [theme.breakpoints.up("sm")]: {
      minHeight: "calc(100vh - 64px)",
      marginTop: 64,
    },
  },

  appContentLeft: {
    marginLeft: 0,
  },
  appContentRight: {
    marginRight: 0,
  },
  appContentShift: {
    transition: theme.transitions.create("margin", {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  appContentShiftLeft: {
    marginLeft: theme.drawer.width,
    width: `calc(100% - ${theme.drawer.width}px)`,
  },
  appContentShiftRight: {
    marginRight: theme.drawer.width,
    width: `calc(100% - ${theme.drawer.width}px)`,
  },
  appContentCenter: {
    [theme.breakpoints.up("sm")]: {
      //"max-width": 1170,
      position: "relative",
      margin: "0 auto",
      minHeight: "100%",
    },
  },

  grow: {
    flex: "1 1 auto",
  },
});