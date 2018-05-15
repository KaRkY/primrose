import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

import NotificationConsumer from "../NotificationConsumer";

import AppBar from "@material-ui/core/AppBar";
import Button from "@material-ui/core/Button";
import Divider from "@material-ui/core/Divider";
import Drawer from "@material-ui/core/Drawer";
import IconButton from "@material-ui/core/IconButton";
import Snackbar from "@material-ui/core/Snackbar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";

import ChevronLeftIcon from "@material-ui/icons/ChevronLeft";
import ChevronRightIcon from "@material-ui/icons/ChevronRight";
import MenuIcon from "@material-ui/icons/Menu";
import CloseIcon from "@material-ui/icons/Close";


const propTypes = {
  classes: PropTypes.shape({
    root: PropTypes.string.isRequired,
    appBar: PropTypes.string.isRequired,
    appBarShift: PropTypes.string.isRequired,
    appBarShiftLeft: PropTypes.string.isRequired,
    appBarShiftRight: PropTypes.string.isRequired,
    appDrawerHeader: PropTypes.string.isRequired,
    appDrawerPaper: PropTypes.string.isRequired,
    appContent: PropTypes.string.isRequired,
    appContentLeft: PropTypes.string.isRequired,
    appContentRight: PropTypes.string.isRequired,
    appContentShift: PropTypes.string.isRequired,
    appContentShiftLeft: PropTypes.string.isRequired,
    appContentShiftRight: PropTypes.string.isRequired,
    appContentCenter: PropTypes.string.isRequired,
    grow: PropTypes.string.isRequired,
  }).isRequired,
  theme: PropTypes.object.isRequired,
  mobile: PropTypes.bool.isRequired,
  drawerOpen: PropTypes.bool.isRequired,
  toolbar: PropTypes.shape({
    title: PropTypes.oneOfType([
      PropTypes.string,
      PropTypes.element,
    ]).isRequired,
    position: PropTypes.oneOf(["static", "fixed"]),
    actions: PropTypes.element,
  }),
  navigation: PropTypes.element,
  content: PropTypes.element,
  onDrawerOpen: PropTypes.func.isRequired,
  onDrawerClose: PropTypes.func.isRequired,
};

const App = (props) => {

  const {
    classes,
    theme,
    mobile,
    drawerOpen,
    toolbar,
    navigation,
    content,
    onDrawerOpen,
    onDrawerClose,
  } = props;
  const anchor = theme.direction === "rtl" ? "Right" : "Left";

  const toolbarClasses = classNames(classes.appBar, {
    [classes.appBarShift]: !!navigation && drawerOpen && !mobile,
    [classes[`appBarShift${anchor}`]]: !!navigation && drawerOpen && !mobile,
  });

  const contentClasses = classNames(classes.appContent, classes[`appContent${anchor}`], {
    [classes.appContentShift]: !!navigation && drawerOpen && !mobile,
    [classes[`appContentShift${anchor}`]]: !!navigation && drawerOpen && !mobile,
  });

  return (
    <React.Fragment>
      <div className={classes.root}>
        {toolbar && (
          <AppBar
            position={toolbar.position}
            className={toolbarClasses}>
            <Toolbar>

              {navigation && !drawerOpen && <IconButton
                color="default"
                aria-label="open drawer"
                onClick={onDrawerOpen}
              >
                <MenuIcon />
              </IconButton>}

              <Typography variant="title" color="inherit">{toolbar.title}</Typography>
              <div className={classes.grow} />
              {toolbar.actions}
            </Toolbar>
          </AppBar>
        )}

        {navigation && (
          <Drawer
            variant={mobile ? "temporary" : "persistent"}
            anchor={anchor.toLowerCase()}
            open={drawerOpen}
            classes={{
              paper: classes.appDrawerPaper,
            }}
            onClose={onDrawerClose}
            ModalProps={{
              keepMounted: mobile, // Better open performance on mobile.
            }}
          >
            <div>
              <div className={classes.appDrawerHeader}>
                <IconButton onClick={onDrawerClose}>
                  {theme.direction === "rtl" ? <ChevronRightIcon /> : <ChevronLeftIcon />}
                </IconButton>
              </div>
              <Divider />
              {navigation}
            </div>
          </Drawer>
        )}

        {content && (
          <main className={contentClasses}>
            <div className={classes.appContentCenter}>
              {content}
            </div>
          </main>
        )}
      </div>


      <NotificationConsumer>{({
        push,
        close,
        open,
        current,
        key,
        exit,
      }) => {
        const snackbarActions = [];
        if (current && current.undo) {
          snackbarActions.push(<Button
            key="undo"
            color="secondary"
            size="small"
            onClick={current.undo}>UNDO</Button>);
        }
        snackbarActions.push(<IconButton
          key="close"
          aria-label="Close"
          color="inherit"
          className={classes.close}
          onClick={close}
        >
          <CloseIcon />
        </IconButton>);

        return (
          <Snackbar
            key={key}
            anchorOrigin={{
              vertical: "top",
              horizontal: "right",
            }}
            open={open}
            autoHideDuration={4000}
            onClose={close}
            onExited={exit}
            ContentProps={{
              "aria-describedby": "message-id",
            }}
            message={<span id="message-id">{current.text}</span>}
            action={snackbarActions}
          />
        );
      }}</NotificationConsumer>
    </React.Fragment>
  );
};

App.propTypes = propTypes;

export default App;