import createReduxPromiseListener from "redux-promise-listener";

export const listener = createReduxPromiseListener();

export default ({ start, resolve, reject }) => {
  return listener.createAsyncFunction({
    start: start.toString(),
    resolve: resolve.toString(),
    reject: reject.toString(),
    setPayload: (action, payload) => start(payload),
  })
};