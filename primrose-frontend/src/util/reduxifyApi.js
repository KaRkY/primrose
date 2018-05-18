import convertError from "./convertError";

export default ({
  load,
  finished,
  error,
  api,
  extractor = result => result.data,
}) => (dispatch, ...props) => {
  dispatch(load());
  api(...props)
    .then(result => dispatch(finished(extractor(result))))
    .catch(error => dispatch(error(convertError(error))));
}