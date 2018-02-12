import React from "react";
import PropTypes from "prop-types";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import Drawer from "material-ui/Drawer";
import Divider from "material-ui/Divider";
import IconButton from "material-ui/IconButton";
import ChevronLeftIcon from "material-ui-icons/ChevronLeft";
import ChevronRightIcon from "material-ui-icons/ChevronRight";

const propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  mobile: PropTypes.bool.isRequired,
};

const styles = theme => ({
  drawerHeader: {
    display: "flex",
    alignItems: "center",
    justifyContent: "flex-end",
    padding: "0 8px",
    ...theme.mixins.toolbar
  },
  drawerPaper: {
    width: 250,
    height: "100vh",
    [theme.breakpoints.up("md")]: {
      width: theme.drawer.width,
      height: "100vh",
    },
  },
});

const enhance = compose(
  withStyles(styles, { withTheme: true })
);

const AppDrawer = ({ classes, theme, mobile, children, open, onClose }) => {
  const anchor = theme.direction === "rtl" ? "right" : "left";
  return (
    <Drawer
      variant={mobile ? "temporary" : "persistent"}
      anchor={anchor}
      open={open}
      classes={{
        paper: classes.drawerPaper,
      }}
      onClose={onClose}
      ModalProps={{
        keepMounted: mobile, // Better open performance on mobile.
      }}
    >
      <div>
        <div className={classes.drawerHeader}>
          <IconButton onClick={onClose}>
            {theme.direction === "rtl" ? <ChevronRightIcon /> : <ChevronLeftIcon />}
          </IconButton>
        </div>
        <Divider />
        {children}
      </div>
    </Drawer>
  );
};

const StyledAppDrawer = enhance(AppDrawer);
StyledAppDrawer.propTypes = propTypes;

export default StyledAppDrawer;