import withHandlers from "recompose/withHandlers";

export default withHandlers({
  onPaged: ({ pagination, handleList }) => property => (event, value) => handleList({ ...pagination, [property]: value, }),
});