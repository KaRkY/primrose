import React from "react";
import ReactDOM from "react-dom";
import Browser from "@hickory/browser";
import curi from "@curi/core";
import routes from "./routes";
import queryString from "query-string";
import NProgress from "nprogress";
import createTitleSideEffect from "@curi/side-effect-title";
import createActiveAddon from "@curi/addon-active";
import { ApolloClient } from "apollo-client";
import { HttpLink } from "apollo-link-http";
import { InMemoryCache } from "apollo-cache-inmemory";
import registerServiceWorker from "./registerServiceWorker";

NProgress.configure({ showSpinner: false });

const setTitle = createTitleSideEffect({
  suffix: "Primrose",
  delimiter: "|"
});

const history = Browser({
  query: {
    parse: queryString.parse,
    stringify: query => queryString.stringify(query, { strict: false, encode: false }),
  }
});
const router = curi(history, routes, {
  sideEffects: [{ fn: setTitle }],
  addons: [createActiveAddon()],
});

const client = new ApolloClient({
  link: new HttpLink({uri: "http://localhost:9080/graphql/"}),
  cache: new InMemoryCache()
});
const root = document.getElementById("root");

let render = () => {
  const exec = require("./render").default;
  router.respond(exec(client));
};

if (module.hot) {
  // Support hot reloading of components
  // and display an overlay for runtime errors
  const renderApp = render;
  const renderError = (error) => {
    const RedBox = require("redbox-react").default;
    ReactDOM.render( <
      RedBox error = {
        error
      }
      />,
      root,
    );
  };

  // In development, we wrap the rendering function to catch errors,
  // and if something breaks, log the error and render it to the screen
  render = () => {
    try {
      renderApp();
    } catch (error) {
      console.error(error);
      renderError(error);
    }
  };

  // Whenever the App component file or one of its dependencies
  // is changed, re-import the updated component and re-render it
  module.hot.accept("./render", () => {
    setTimeout(render);
  });
}

render();
registerServiceWorker();