import withHandlers from "recompose/withHandlers";
import withStateHandlers from "recompose/withStateHandlers";
import compose from "recompose/compose";

export default compose(
  withHandlers({
    onPaged: ({ pagination, handleList }) => property => (event, value) => {
      const newValue = { ...pagination, [property]: value, };
      if(value === undefined) {
        delete newValue[property];
      }
      handleList(newValue);
    },
  }),

  withStateHandlers(
    ({ pagination }) => ({ searchTerm: pagination.search || "" }),
    {
      onChangeSearchTerm: () => event => ({ searchTerm: event.target.value}),
      onClearSearchTerm: () => event => ({ searchTerm: ""}),
    }
  ),
);