import React from "react";
import compose from "recompose/compose";
import { withAxios } from "react-axios";
import { withStyles } from "material-ui/styles";

import isBlank from "../../util/isBlank";

import { Post } from "react-axios";
import gql from "graphql-tag";

import { Formik } from "formik";
import Paper from "material-ui/Paper";
import Grid from "material-ui/Grid";
import Button from "material-ui/Button";
import Typography from "material-ui/Typography";
import Input from "material-ui/Input";
import Select from "material-ui/Select";
import MenuItem from "material-ui/Menu/MenuItem";
import FormControl from "material-ui/Form/FormControl";
import FormHelperText from "material-ui/Form/FormHelperText";
import InputLabel from "material-ui/Input/InputLabel";
import Toolbar from "material-ui/Toolbar";

const contentStyle = theme => ({
  root: theme.mixins.gutters({
  }),

  formAction: {
    margin: theme.spacing.unit,
  },
});


const enhance = compose(
  withAxios,
  withStyles(contentStyle)
);

export const loadTypes = gql`
  query loadTypes {
    customerTypes
    customerRelationTypes
  }
`;

export const createCustomer = gql`
  mutation createCustomer($customer: CustomerInput!) {
    createCustomer(customer: $customer)
  }
`;

const Content = ({
  classes,
  axios,
  router,
}) => (
    <Post data={{
      query: loadTypes.loc.source.body,
    }}>{(typesErrors, typesResponse, isTypesLoading, reloadTypes) => (
      <Paper className={classes.root}>
        <Formik
          initialValues={{
            type: "",
            relationType: "",
            fullName: "",
            displayName: "",
            email: "",
            phone: "",
            description: "",
          }}

          validate={(values) => {
            let errors = {};

            if (!values.type) {
              errors.type = "Required";
            }

            if (!values.relationType) {
              errors.relationType = "Required";
            }

            if (!values.fullName) {
              errors.fullName = "Required";
            }

            if (!values.email) {
              errors.email = "Required";
            }

            return errors;
          }}

          onSubmit={(
            values,
            { setSubmitting, setErrors }
          ) => {
            axios.request({
              method: "post",
              data: {
                query: createCustomer.loc.source.body,
                variables: {
                  customer: Object.keys(values).reduce((acc, key) => {
                    if (!isBlank(values[key])) {
                      acc[key] = values[key];
                    }
                    return acc;
                  }, {}),
                }
              }
            })
              .then(response => {
                setSubmitting(false);
                router.history.navigate(router.history.toHref({
                  pathname: router.addons.pathname("Customers"),
                }));
              })
              .catch(error => {
                setErrors({
                  global: "Request failed",
                });
                setSubmitting(false);
              });
          }}

          render={({
            values,
            errors,
            touched,
            handleReset,
            handleChange,
            handleBlur,
            handleSubmit,
            isSubmitting,
          }) => (

              <form onSubmit={handleSubmit}>
                <Grid container>
                  {errors.global && (
                    <Grid item xs={12}>
                      <Typography>{errors.global}</Typography>
                    </Grid>
                  )}

                  <Grid item xs={6}>
                    <FormControl fullWidth error={touched.fullName && errors.fullName ? true : false}>
                      <InputLabel htmlFor="fullName">Full name</InputLabel>
                      <Input id="fullName" name="fullName" value={values.fullName} onChange={handleChange} onBlur={handleBlur} />
                      {touched.fullName && errors.fullName && <FormHelperText>{errors.fullName}</FormHelperText>}
                    </FormControl>
                  </Grid>

                  <Grid item xs={6}>
                    <FormControl fullWidth error={touched.displayName && errors.displayName ? true : false}>
                      <InputLabel htmlFor="displayName">Display name</InputLabel>
                      <Input id="displayName" name="displayName" value={values.displayName} onChange={handleChange} onBlur={handleBlur} />
                      {touched.displayName && errors.displayName && <FormHelperText>{errors.displayName}</FormHelperText>}
                    </FormControl>
                  </Grid>

                  <Grid item xs={6}>
                    <FormControl fullWidth error={touched.type && errors.type ? true : false}>
                      <InputLabel htmlFor="type">{isTypesLoading ? "Loading..." : "Type"}</InputLabel>
                      <Select
                        value={values.type}
                        disabled={isTypesLoading}
                        onChange={handleChange}
                        inputProps={{
                          id: "type",
                          name: "type",
                        }}
                      >
                        {(!isTypesLoading) && typesResponse && typesResponse.data &&
                          typesResponse.data.data.customerTypes.map(type => (
                            <MenuItem key={type} value={type}>{type}</MenuItem>
                          ))}
                      </Select>
                      {touched.type && errors.type && <FormHelperText>{errors.type}</FormHelperText>}
                    </FormControl>
                  </Grid>

                  <Grid item xs={6}>
                    <FormControl fullWidth error={touched.relationType && errors.relationType ? true : false}>
                      <InputLabel htmlFor="type">{isTypesLoading ? "Loading..." : "Relation type"}</InputLabel>
                      <Select
                        value={values.relationType}
                        disabled={isTypesLoading}
                        onChange={handleChange}
                        inputProps={{
                          id: "relationType",
                          name: "relationType",
                        }}
                      >
                        {(!isTypesLoading) && typesResponse && typesResponse.data &&
                          typesResponse.data.data.customerRelationTypes.map(relationType => (
                            <MenuItem key={relationType} value={relationType}>{relationType}</MenuItem>
                          ))}
                      </Select>
                      {touched.relationType && errors.relationType && <FormHelperText>{errors.relationType}</FormHelperText>}
                    </FormControl>
                  </Grid>

                  <Grid item xs={6}>
                    <FormControl fullWidth error={touched.email && errors.email ? true : false}>
                      <InputLabel htmlFor="email">Email</InputLabel>
                      <Input id="email" name="email" value={values.email} onChange={handleChange} onBlur={handleBlur} />
                      {touched.email && errors.email && <FormHelperText>{errors.email}</FormHelperText>}
                    </FormControl>
                  </Grid>

                  <Grid item xs={6}>
                    <FormControl fullWidth error={touched.phone && errors.phone ? true : false}>
                      <InputLabel htmlFor="phone">Phone</InputLabel>
                      <Input id="phone" name="phone" value={values.phone} onChange={handleChange} onBlur={handleBlur} />
                      {touched.phone && errors.phone && <FormHelperText>{errors.phone}</FormHelperText>}
                    </FormControl>
                  </Grid>

                  <Grid item xs={12}>
                    <FormControl fullWidth error={touched.description && errors.description ? true : false}>
                      <InputLabel htmlFor="description">Description</InputLabel>
                      <Input multiline rows={6} id="description" name="description" value={values.description} onChange={handleChange} onBlur={handleBlur} />
                      {touched.description && errors.description && <FormHelperText>{errors.description}</FormHelperText>}
                    </FormControl>
                  </Grid>

                  <Grid item xs={12}>
                    <Toolbar>
                      <Button className={classes.formAction} type="submit" variant="raised" color="primary" disabled={isSubmitting}>Save</Button>
                      <Button className={classes.formAction} type="reset" onClick={handleReset}>Reset</Button>
                    </Toolbar>
                  </Grid>
                </Grid>
              </form>

            )}
        />
      </Paper>
    )}</Post>
  );

export default enhance(Content);