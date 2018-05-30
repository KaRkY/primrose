export default theme => ({
  root: {
    display: "flex",
    flexDirection: "column",
    flexWrap: "nowrap",

    "& > *:not(:first-child)": {
      marginTop: theme.spacing.unit * 2,
    }
  },

  types: {
    display: "flex",
    flexDirection: "column",
    flexWrap: "wrap",

    "& > *:not(:first-child)": {
      marginTop: theme.spacing.unit * 2,
    },

    "& > *": {
      flex: "1 1 auto",
    },

    [theme.breakpoints.up("md")]: {
      flexDirection: "row",

      "& > *:not(:first-child)": {
        marginTop: 0,
        marginLeft: theme.spacing.unit * 2,
      },
    }
  }
});