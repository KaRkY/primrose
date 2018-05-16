import withStateHandlers from "recompose/withStateHandlers";
import compose from "recompose/compose";

export default compose(
  withStateHandlers(
    ({ searchTerm }) => ({ searchTerm: searchTerm || "", searchOpen: false }),
    {
      onChangeSearchTerm: state => event => ({ ...state, searchTerm: event.target.value }),
      onClearSearchTerm: state => event => ({ ...state, searchTerm: "" }),
      onSearchOpen: state => event => ({ ...state, searchOpen: true }),
      onSearchClose: state => event => ({ ...state, searchOpen: false }),
    }
  ),
);