import React from "react";
import { Form } from "react-final-form";
import setFieldData from "final-form-set-field-data";
import AutoSaveField from "../AutoSaveField";

import compose from "recompose/compose";
import withProps from "recompose/withProps";
import withStateHandlers from "recompose/withStateHandlers";

import Stepper from "@material-ui/core/Stepper";
import Step from "@material-ui/core/Step";
import StepLabel from "@material-ui/core/StepLabel";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import withStyles from "@material-ui/core/styles/withStyles";

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
      autoSave: (state) => (values) => {
        const newValues = [...state.formValues];
        newValues[state.activeStep] = values;
        return {
          ...state,
          formValues: newValues,
        };
      },

      next: (state, { steps = [] }) => () => {
        const { activeStep } = state;
        let { skipped } = state;
        if (skipped.has(activeStep)) {
          skipped = new Set(state.skipped.values());
          skipped.delete(activeStep);
        }

        const newValues = [...state.formValues];
        newValues.push({});
        return {
          activeStep: activeStep + 1,
          skipped,
          formValues: newValues,
        };
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
  withProps(({ activeStep, steps, onSubmit, formValues }) => ({
    isFirstPage: () => activeStep === 0,
    isLastPage: () => (activeStep + 1) >= steps.length,
    isOptional: () => steps[activeStep].optional,
    save: () => onSubmit(formValues),
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
  save,
  autoSave,
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
        mutators={{ setFieldData }}
        onSubmit={save}>
        {({
          handleSubmit,
          submitting,
          values,
          reset,
          pristine,
          invalid,
          mutators,
        }) => (
            <React.Fragment>
              <AutoSaveField setFieldData={mutators.setFieldData} save={autoSave} />
              <div className={classes.content}>{steps[activeStep].component}</div>
              <Grid className={classes.buttons} item container spacing={16}>
                <Grid item>
                  <Button variant="raised" onClick={back} disabled={isFirstPage()}>Back</Button>
                </Grid>
                <Grid item>
                  {isLastPage() ?
                    <Button
                      variant="raised"
                      color="primary"
                      disabled={invalid || submitting}
                      onClick={save}>
                      Save
                    </Button> :
                    <Button
                      variant="raised"
                      type="submit"
                      disabled={invalid || submitting}
                      onClick={next}>
                      Next
                  </Button>
                  }
                </Grid>
                {isOptional() &&
                  <Grid item>
                    <Button variant="raised" onClick={skip}>Skip</Button>
                  </Grid>
                }
              </Grid>
            </React.Fragment>
          )}
      </Form>
    </React.Fragment>
  );
};

const EnhancedWizard = enhance(Wizard);

export default EnhancedWizard;