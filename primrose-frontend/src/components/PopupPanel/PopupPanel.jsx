import React from "react";

import Popover from "@material-ui/core/Popover";
import Button from "@material-ui/core/Button";

const PopupPanel = ({
  panelAnchorEl,
  onPanelOpen,
  onPanelClose,
  component,
  children,
  className,
}) => {
  const Component = component || Button;

  return (
    <React.Fragment>
      <Component 
        aria-owns={panelAnchorEl ? "fade-menu" : null}
        aria-haspopup={true}
        onClick={event => onPanelOpen(event.currentTarget)}
      />
      <Popover
        open={Boolean(panelAnchorEl)}
        anchorEl={panelAnchorEl}
        onClose={onPanelClose}
        classes={{ paper: className }}
        anchorOrigin={{
          vertical: "bottom",
          horizontal: "center",
        }}
        transformOrigin={{
          vertical: "top",
          horizontal: "center",
        }}
      >
        {children}
      </Popover>
    </React.Fragment>
  );
};

export default PopupPanel;