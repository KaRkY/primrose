import React from "react";

import Typography from "@material-ui/core/Typography";

const CustomerView = ({
  customer,
}) => (
  <Typography component="pre" variant="body2">{JSON.stringify(customer, null, 2)}</Typography>
  );

export default CustomerView;