import React from "react";
import ReactDOM from "react-dom";
import createHistory from "history/createBrowserHistory";
import configureStore from "./configureStore";

import defaultTheme, * as other from "./themes";

import Main from "./Main";
import { Provider } from "react-redux";
import { MuiThemeProvider } from "material-ui/styles";
import MomentUtils from "material-ui-pickers/utils/moment-utils";
import MuiPickersUtilsProvider from "material-ui-pickers/utils/MuiPickersUtilsProvider";

const root = document.getElementById("root");
const history = createHistory();
const store = configureStore(history);

const themes = {
  default: defaultTheme,
  ...other,
};

const render = (Main) => {
  ReactDOM.render((
    <MuiThemeProvider theme={themes[/*"indigoLightBlue"*/ "purpleTeal"]}>
      <MuiPickersUtilsProvider utils={MomentUtils}>
        <Provider store={store}>
          <Main />
        </Provider>
      </MuiPickersUtilsProvider>
    </MuiThemeProvider>
  ), root);
};

render(Main);
//registerServiceWorker(); // Lahko povzro�a probleme zaenkrat onemogo�eni