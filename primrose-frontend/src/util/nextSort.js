export default (sort, column) => {
  let direction;
  if (sort && sort.column === column) {
    switch (sort.direction) {
      case "asc": direction = "desc"; break;
      case "desc": direction = undefined; break;
      case undefined: direction = "asc"; break;
      default: direction = "asc"; break;
    }
  } else {
    direction = "asc";
  }

  return {
    column,
    direction,
  };
};