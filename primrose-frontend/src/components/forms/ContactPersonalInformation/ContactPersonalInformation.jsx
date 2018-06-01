import React from "react";
import classnames from "classnames";

import Typography from "@material-ui/core/Typography";

import TextField from "../../fields/Text";

const ContactPersonalInformation = ({ classes, className, types, relationTypes }) => (
  <div className={classnames(classes.root, className)}>
    <Typography variant="title">Contact information</Typography>

    <TextField name="fullName" label="Full name" />

    <TextField
      multiline
      rows={5}
      rowsMax={5}
      name="description"
      label="Description"
    />
  </div>
);

export default ContactPersonalInformation;