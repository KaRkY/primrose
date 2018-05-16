import withHandlers from "recompose/withHandlers";
import compose from "recompose/compose";

export default compose(
  withHandlers({
    onPaged: ({ pagination, handleList }) => property => (event, value) => {
      const newValue = { ...pagination, [property]: value, };
      if(value === undefined) {
        delete newValue[property];
      }
      if(property === "search") {
        newValue.page = 0;
      }
      handleList(newValue);
    },
  }),
);