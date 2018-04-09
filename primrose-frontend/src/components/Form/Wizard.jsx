import React from "react";
import PropTypes from "prop-types";
import { Form } from "react-final-form";
import compose from "recompose/compose";
import withStateHandlers from "recompose/withStateHandlers";
import withProps from "recompose/withProps";
import { withStyles } from "material-ui/styles";

import Stepper, { Step, StepLabel } from "material-ui/Stepper";
import Typography from "material-ui/Typography";
import Button from "material-ui/Button";
import Grid from "material-ui/Grid";

export const style = theme => ({
  content: {
    padding: 3 * theme.spacing.unit,
  },
  buttons: theme.mixins.gutters({
  }),

  resetButton: {
    marginLeft: "auto",
  }
});

const enhance = compose(
  withStyles(style),
  withStateHandlers(({ steps }) => ({
    activeStep: 0,
    skipped: new Set(),
    formValues: [{}],
  }), {
      next: (state, { steps = [], onSubmit = () => null }) => (values, ...rest) => {
        console.log(rest);
        const { activeStep } = state;
        let { skipped } = state;
        if (skipped.has(activeStep)) {
          skipped = new Set(state.skipped.values());
          skipped.delete(activeStep);
        }

        const newValues = [...state.formValues];
        newValues[activeStep] = values;

        if (state.activeStep >= steps.length - 1) {
          onSubmit(newValues);
          return state;
        } else {
          newValues.push({});
          return {
            activeStep: activeStep + 1,
            skipped,
            formValues: newValues,
          };
        }
      },

      back: (state, props) => () => {
        if (state.activeStep === 0) {
          throw new Error("Negative steps are not supported.");
        }

        let newValues;
        if (props.deleteOnBack) {
          newValues = [...state.formValues];
          newValues.pop();
        } else {
          newValues = state.formValues;
        }

        return {
          ...state,
          activeStep: state.activeStep - 1,
          formValues: newValues,
        };
      },

      skip: (state, props) => () => {
        const { activeStep } = state;
        if (!props.steps[activeStep].optional) {
          // You probably want to guard against something like this,
          // it should never occur unless someone"s actively trying to break something.
          throw new Error("You can't skip a step that isn't optional.");
        }
        const skipped = new Set(state.skipped.values());
        skipped.add(activeStep);
        const newValues = [...state.formValues];
        newValues[activeStep] = null;
        newValues.push({});
        return {
          activeStep: state.activeStep + 1,
          skipped,
          formValues: newValues,
        };
      },
    }
  ),
  withProps(({ activeStep, steps, onSubmit, next }) => ({
    isFirstPage: () => activeStep === 0,
    isLastPage: () => (activeStep + 1) >= steps.length,
    isOptional: () => steps[activeStep].optional,
  })),
);

const Wizard = ({
  classes,
  steps,
  skipped,
  activeStep,
  formValues,
  next,
  back,
  skip,
  validate,
  isFirstPage,
  isLastPage,
  isOptional,
}) => {
  return (
    <React.Fragment>
      <Stepper activeStep={activeStep}>
        {steps.map((step, index) => {
          const props = {};
          const labelProps = {};
          if (step.optional) {
            labelProps.optional = <Typography variant="caption">Optional</Typography>;
          }
          if (skipped.has(index)) {
            props.completed = false;
          }
          return (
            <Step key={step.label} {...props}>
              <StepLabel {...labelProps}>{step.label}</StepLabel>
            </Step>
          );
        })}
      </Stepper>
      <Form
        key={activeStep}
        initialValues={formValues[activeStep]}
        validate={steps[activeStep].validate}
        onSubmit={next}>
        {({ handleSubmit, submitting, values, reset }) => (
          <form onSubmit={handleSubmit} onReset={reset}>
            <div className={classes.content}>{steps[activeStep].children}</div>
            <Grid className={classes.buttons} item container spacing={16}>
              <Grid item>
                <Button name="back" variant="raised" onClick={back} disabled={isFirstPage()}>Back</Button>
              </Grid>
              <Grid item>
                {isLastPage() ?
                  <Button name="save" variant="raised" color="primary" type="submit" disabled={submitting}>Save</Button> :
                  <Button name="next" variant="raised" type="submit">Next</Button>}
              </Grid>
              {isOptional() &&
                <Grid item>
                  <Button variant="raised" onClick={skip}>Skip</Button>
                </Grid>
              }
              <Grid className={classes.resetButton} item>
                <Button variant="raised" type="reset">Reset</Button>
              </Grid>
            </Grid>
          </form>
        )}
      </Form>
    </React.Fragment>
  );
};

const EnhancedWizard = enhance(Wizard);

export default EnhancedWizard;