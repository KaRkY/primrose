import React from "react";
import { FieldArray as RFFiledArray } from "react-final-form-arrays";

import Grid from "@material-ui/core/Grid";
import IconButton from "@material-ui/core/IconButton";
import Paper from "@material-ui/core/Paper";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import withStyles from "@material-ui/core/styles/withStyles";

import AddIcon from "@material-ui/icons/Add";


export const style = theme => ({
  root: {
    //width: "100%",
  },

  grid: {
    padding: 3 * theme.spacing.unit,
  },

  grow: {
    flex: "1 1 auto",
  },
});

const FieldArray = ({ classes, children, label, name, push, initialValue }) => (
  <Paper className={classes.root} elevation={2}>
    <Toolbar>
      <Typography variant="title">{label}</Typography>
      <div className={classes.grow} />
      <IconButton variant="raised" onClick={() => push(name, initialValue || {})}>
        <AddIcon />
      </IconButton>
    </Toolbar>
    <Grid className={classes.grid} container spacing={16}>
      <RFFiledArray name={name} children={children} />
    </Grid>
  </Paper>
);

export default withStyles(style)(FieldArray);