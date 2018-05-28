import withStateHandlers from "recompose/withStateHandlers";
import compose from "recompose/compose";

export default compose(
  withStateHandlers(
    ({ }) => ({ loading: true }),
    {
      toggleLoading: state => event => ({ ...state, loading: !state.loading }),
    }
  ),
);