import "./styles/main.scss";

import React from "react";
import { render } from "react-dom";
import { Provider } from "react-redux";

import createStore from "./create-store";
import Root from "./app/scenes/root";

let store = createStore();

render((
<Provider store={store}>
  <Root/>
</Provider>
), document.querySelector("#app"))
