import { createMuiTheme } from "@material-ui/core/styles";
import purple from "@material-ui/core/colors/purple";
import green from "@material-ui/core/colors/green";
import defaultTheme from "./default";

export default createMuiTheme({
  palette: {
    type: "dark",
    primary: {
      light: purple[300],
      main: purple[500],
      dark: purple[700],
      contrastText: defaultTheme.palette.getContrastText(purple[500]),
    },
    secondary: {
      light: green.A200,
      main: green.A400,
      dark: green.A700,
      contrastText: defaultTheme.palette.getContrastText(green.A400),
    },
  },
  status: {
    danger: "orange",
  },
  drawer: {
    width: 250,
  },
});