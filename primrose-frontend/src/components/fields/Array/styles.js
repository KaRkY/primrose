export default theme => ({
  root: {
    display: "flex",
    flexDirection: "column",
    flexWrap: "nowrap",

    "& > *:not(:first-child)": {
      marginTop: theme.spacing.unit * 2,
    }
  },

  elements: {
    display: "flex",
    flexDirection: "column",
    flexWrap: "nowrap",

    "& > *:not(:first-child)": {
      marginTop: theme.spacing.unit * 2,
    }
  },

  element: {
    display: "flex",
    flexDirection: "row",
    flexWrap: "nowrap",
    alignItems: "center",

    "& > *:not(:first-child)": {
      marginLeft: theme.spacing.unit * 2,
    },

    "& > *:first-child": {
      flex: "1 1 auto"
    }
  },
});