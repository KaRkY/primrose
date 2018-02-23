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

const renderApp = () => <App />

export default (client) => ({ router }) => {
  ReactDOM.render((
    <ApolloProvider client={client}>
      <MuiThemeProvider theme={themes["purpleTeal"]}>
        <CuriProvider router={router}>{renderApp}</CuriProvider>
      </MuiThemeProvider>
    </ApolloProvider>
  ), root);
};