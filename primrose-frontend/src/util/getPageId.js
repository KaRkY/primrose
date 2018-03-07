export default query => {
  const page = parseInt((query && query.page) || 0, 10);
  const size = parseInt((query && query.size) || 10, 10);
  const sortDirection = query && query.sortDirection;
  const sortProperty = query && query.sortProperty;

  return JSON.stringify({
    page,
    size,
    sortDirection,
    sortProperty,
  });
};