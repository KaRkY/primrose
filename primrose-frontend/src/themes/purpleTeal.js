import { createMuiTheme } from "@material-ui/core/styles";

export default createMuiTheme({
  palette: {
    type: "dark",
    primary: {
      light: "#d05ce3",
      main: "#9c27b0",
      dark: "#6a0080",
      contrastText: "#ffffff",
    },
    secondary: {
      light: "#6effe8",
      main: "#1de9b6",
      dark: "#00b686",
      contrastText: "#000000",
    },
    error: {
      light: "#ff8a50",
      main: "#ff5722",
      dark: "#c41c00",
      contrastText: "#000000",
    },
  },
  drawer: {
    width: 250,
  },
});