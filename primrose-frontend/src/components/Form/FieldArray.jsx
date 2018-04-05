import React from "react";
import { withStyles } from "material-ui/styles";
import AddIcon from "material-ui-icons/Add";
import Toolbar from "material-ui/Toolbar";
import Typography from "material-ui/Typography";
import IconButton from "material-ui/IconButton";
import Grid from "material-ui/Grid";
import Paper from "material-ui/Paper";
import { FieldArray as RFFiledArray } from "react-final-form-arrays";


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

const FieldArray = ({ classes, children, label, name, push }) => (
  <Paper className={classes.root} elevation={2}>
    <Toolbar>
      <Typography variant="title">{label}</Typography>
      <div className={classes.grow} />
      <IconButton variant="raised" onClick={() => push(name)}>
        <AddIcon />
      </IconButton>
    </Toolbar>
    <Grid className={classes.grid} container spacing={16}>
      <RFFiledArray name={name} children={children} />
    </Grid>
  </Paper>
);

export default withStyles(style)(FieldArray);