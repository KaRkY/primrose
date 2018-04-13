export default (sort, column) => {
  if (sort && sort.column === column) {
    switch (sort.direction) {
      case "asc": return "desc";
      case "desc": return undefined;
      case undefined: return "asc";
      default: return "asc";
    }
  } else {
    return "asc";
  }
};