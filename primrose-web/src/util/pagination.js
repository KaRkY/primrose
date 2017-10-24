export const toQuery = pagination => ({
  page: pagination.page,
  size: pagination.rowsPerPage,
  sort: pagination.sortBy ? `${pagination.descending ? "-" : ""}${pagination.sortBy}` : undefined,
});

export const fromQuery = (query) => {
  const pagination = {};
  pagination.page = query.page;
  pagination.rowsPerPage = query.size;
  if (query.sort) {
    if (query.sort.startsWith("-")) {
      pagination.descending = true;
      pagination.sortBy = query.sort.slice(1);
    } else {
      pagination.descending = false;
      if (query.sort.startsWith("+")) {
        pagination.sortBy = query.sort.slice(1);
      } else {
        pagination.sortBy = query.sort;
      }
    }
  }

  return pagination;
};
