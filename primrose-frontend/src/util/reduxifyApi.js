import convertError from "./convertError";

export default ({
  load,
  finished,
  error,
  api,
  extractor = result => result.data,
  reload = () => true,
  data = () => undefined,
}) => thunk => {
  if(reload(thunk.state, thunk.action)) {
    thunk.dispatch(load());
    return api(data(thunk.state, thunk.action))
      .then(result => {
        const transformed = extractor(result);
        thunk.dispatch(finished(transformed));

        return transformed;
      })
      .catch(error => {
        thunk.dispatch(error(convertError(error)));
        throw error;
      });
  } else {
    return Promise.resolve();
  }
}