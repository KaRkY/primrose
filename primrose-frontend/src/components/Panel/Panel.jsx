import React from "react";

import Paper from "@material-ui/core/Paper";
import Toolbar from "@material-ui/core/Toolbar";

const propTypes = {

};

const Panel = ({
  toolbar,
  content,
  footer,
}) => {
  return (
    <Paper>
      {toolbar && 
        <Toolbar>{toolbar}</Toolbar>
      }

      {footer && 
        <div>{footer}</div>
      }
    </Paper>
  );
};
Panel.propTypes = propTypes;
export default Panel;