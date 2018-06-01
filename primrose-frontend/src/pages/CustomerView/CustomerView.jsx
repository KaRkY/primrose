import React from "react";
import * as customerView from "../../store/customerView";

import Typography from "@material-ui/core/Typography";

import Connect from "../../components/Connect";

const CustomerView = ({
  customer,
}) => (
    <Connect mapStateToProps={state => ({
      customer: customerView.getData(state),
    })}>
      {state => (
        <Typography component="pre" variant="body2">{JSON.stringify(state.customer, null, 2)}</Typography>
      )}
    </Connect>
  );

export default CustomerView;