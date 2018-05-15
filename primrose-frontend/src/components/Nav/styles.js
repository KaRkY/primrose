export default theme => ({
  activeMenuItem: {
    backgroundColor: theme.palette.primary.main,
    "& $activeMenuItemText, & $activeMenuItemIcon, & + * $activeMenuItemLoading": {
      color: theme.palette.primary.contrastText,
    },
    "&:hover": {
      backgroundColor: theme.palette.primary.light,
    },
  },
  activeMenuItemText: {},
  activeMenuItemIcon: {},
  activeMenuItemLoading: {
    color: theme.palette.text.primary,
  },
  nested: {
    paddingLeft: theme.spacing.unit * 4,
  },
});