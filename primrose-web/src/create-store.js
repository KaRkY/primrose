import { applyMiddleware, createStore, combineReducers } from "redux";
import { createLogger } from "redux-logger";
import { connectRoutes, redirect } from "redux-first-router";
import createHistory from "history/createBrowserHistory";
import createSagaMiddleware from "redux-saga";
import { composeWithDevTools } from "redux-devtools-extension";
import queryString from "query-string";
import rootSaga from "./app/saga";
import appReducer from "./app/reducer";
import entitiesReducer from "./entities-reducer";
import settingsReducer from "./settings-reducer";
import routeMap from "./route-map";

import AppActions from "./app/actions";
import DashboardActions from "./app/dashboard/actions";

const composeEnhancers = composeWithDevTools({
  // Specify name here, actionsBlacklist, actionsCreators and other options if needed
});

export default () => {
  const history = createHistory();
  const { reducer, middleware, enhancer, initialDispatch } = connectRoutes(history, routeMap, {
    querySerializer: queryString,
    initialDispatch: false,
    onBeforeChange: (dispatch, getState, action) => {
      if(action.type === AppActions.types.ROOT_SCENE_REQUESTED) {
        return dispatch(redirect(DashboardActions.creators.requestDashboardScene()));
      }
    }
  });
  const sagaMiddleware = createSagaMiddleware();
  const middlewares = [];

  middlewares.push(middleware);
  middlewares.push(sagaMiddleware);

  if (process.env.NODE_ENV !== "production") {
    middlewares.push(createLogger({
      collapsed: true,
      diff: true
    }));
  }

  const store = createStore(
    combineReducers({
      location: reducer,
      app: appReducer,
      entities: entitiesReducer,
      settings: settingsReducer,
    }),
    composeEnhancers(
      enhancer,
      applyMiddleware(...middlewares),
    ));

  sagaMiddleware.run(rootSaga);
  initialDispatch();

  return store;
};