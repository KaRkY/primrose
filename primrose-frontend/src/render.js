import React from "react";
import ReactDOM from "react-dom";
import defaultTheme, * as other from "./themes";
import axios from "axios";

import Main from "./Main";
import { MuiThemeProvider } from "material-ui/styles";
import { CuriProvider } from "@curi/react";
import { AxiosProvider } from "react-axios";


const themes = {
  default: defaultTheme,
  ...other,
};

const axiosInstance = axios.create({
  baseURL: "http://localhost:9080/graphql/",
  timeout: 5000,
});

export default () => ({ router }) => {
  ReactDOM.render((
    <MuiThemeProvider theme={themes["purpleTeal"]}>
      <AxiosProvider instance={axiosInstance}>
        <CuriProvider router={router}>
          {(props) => <Main {...props}/>}
        </CuriProvider>
      </AxiosProvider>
    </MuiThemeProvider>
  ), root);
};