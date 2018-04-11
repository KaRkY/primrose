import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import withWidth, { isWidthDown } from "material-ui/utils/withWidth";

import Grid from "material-ui/Grid";
import Toolbar from "material-ui/Toolbar";
import Paper from "material-ui/Paper";
import Typography from "material-ui/Typography";

import AceField from "../Form/AceField";
import Wizard from "../Form/ChildConfigWizard";
import TextField from "../Form/TextField";
import CheckboxField from "../Form/CheckboxField";

const contentStyle = theme => ({
  grid: {
    padding: 3 * theme.spacing.unit,
  },
});

const enhance = compose(
  withWidth(),
  withStyles(contentStyle)
);

const Content = ({ classes, width, style }) => {
  const editorHeaight = isWidthDown("md", width) ? "200px" : isWidthDown("lg", width) ? "400px" : "600px";
  return (
    <Wizard onSubmit={console.log}>
      <Wizard.Step label="Basic data">
        <Paper elevation={2}>
          <Toolbar>
            <Typography variant="title">Basic data</Typography>
          </Toolbar>
          <Grid className={classes.grid} container spacing={16}>
            <Grid item xs={12}>
              <TextField
                name="name"
                label="Execution name"
                fullWidth
              />
            </Grid>
            <Grid item xs={12}>
              <CheckboxField name="auto_queue" label="Auto queue" />
            </Grid>
          </Grid>
        </Paper>
      </Wizard.Step>
      <Wizard.Step label="Search script">
        <Paper elevation={2}>
          <Toolbar>
            <Typography variant="title">Search script</Typography>
          </Toolbar>
          <Grid className={classes.grid} container spacing={16}>
            <Grid item xs={12}>
              <AceField
                name="code"
                height={editorHeaight} />
            </Grid>
          </Grid>
        </Paper>
      </Wizard.Step>
      <Wizard.Step label="Filter script">
        <Paper elevation={2}>
          <Toolbar>
            <Typography variant="title">Filter script</Typography>
          </Toolbar>
          <Grid className={classes.grid} container spacing={16}>
            <Grid item xs={12}>
              <AceField
                name="code"
                height={editorHeaight} />
            </Grid>
          </Grid>
        </Paper>
      </Wizard.Step>
      <Wizard.Step label="Alteration script">
        <Paper elevation={2}>
          <Toolbar>
            <Typography variant="title">Alteration script</Typography>
          </Toolbar>
          <Grid className={classes.grid} container spacing={16}>
            <Grid item xs={12}>
              <AceField
                name="code"
                height={editorHeaight} />
            </Grid>
          </Grid>
        </Paper>
      </Wizard.Step>
    </Wizard>
  );
};

export default enhance(Content);