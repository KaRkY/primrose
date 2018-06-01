export default theme => console.log(theme) || ({
  root: {
    display: "flex",
    flexDirection: "column",
    flexWrap: "nowrap",

    "& > *:not(:first-child)": {
      marginTop: theme.spacing.unit * 4,
    }
  },

  horizontal: {
    display: "flex",
    flexDirection: "column",
    flexWrap: "nowrap",
    justifyContent: "space-between",

    "& > *:not(:first-child)": {
      marginTop: theme.spacing.unit * 2,
    },

    "& > *": {
      flex: "0 1 45%",
    },

    [theme.breakpoints.up("md")]: {
      flexDirection: "row",

      "& > *:not(:first-child)": {
        marginTop: 0,
        marginLeft: theme.spacing.unit * 2,
      },
    }
  },

  actions: {
    display: "flex",
    flexDirection: "row",
    flexWrap: "nowrap",

    "& > *:not(:first-child)": {
      marginLeft: theme.spacing.unit * 2,
    }
  },

  panel: theme.mixins.gutters({
    paddingBottom: theme.spacing.unit * 3,
  })
});