import { createMuiTheme } from "material-ui/styles";
import indigo from "material-ui/colors/indigo";
import lightBlue from "material-ui/colors/lightBlue";
import defaultTheme from "./default";

export default createMuiTheme({
  palette: {
    type: "light",
    primary: {
      light: indigo[300],
      main: indigo[500],
      dark: indigo[700],
      contrastText: defaultTheme.palette.getContrastText(indigo[500]),
    },
    secondary: {
      light: lightBlue.A200,
      main: lightBlue.A400,
      dark: lightBlue.A700,
      contrastText: defaultTheme.palette.getContrastText(lightBlue.A400),
    },
  },
  status: {
    danger: "orange",
  },
  drawer: {
    width: 250,
  },
});