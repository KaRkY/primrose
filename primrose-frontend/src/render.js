import React from "react";
import ReactDOM from "react-dom";
import defaultTheme, * as other from "./themes";

import App from "./components/app/App";
import { MuiThemeProvider } from "material-ui/styles";
import { CuriProvider } from "@curi/react";
import { ApolloProvider } from "react-apollo";


const themes = {
  default: defaultTheme,
  ...other,
};


export default (client) => ({ router }) => {
  ReactDOM.render((
    <ApolloProvider client={client}>
      <MuiThemeProvider theme={themes["purpleTeal"]}>
        <CuriProvider router={router}>{({ response }) => (
          <App body={response.body} data={response.data} title={response.title} params={response.params} query={response.location.query} />
      )}</CuriProvider>
      </MuiThemeProvider>
    </ApolloProvider>
  ), root);
};