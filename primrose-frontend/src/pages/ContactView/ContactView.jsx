import React from "react";

import Typography from "@material-ui/core/Typography";

const ContactView = ({
  contact,
}) => (
  <Typography component="pre" variant="body2">{JSON.stringify(contact, null, 2)}</Typography>
  );

export default ContactView;