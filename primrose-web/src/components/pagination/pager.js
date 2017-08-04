import React from "react";
import classnames from "classnames";
import range from "lodash/range";

const renderItem = (key, active, number) => (
  <li key={key}>
    <a className={classnames({
      "pagination-link": true,
      "is-current": active
    })}>{number}</a></li>
);

const renderEllipsis = (key) => (
  <li key={key}><span className="pagination-ellipsis">&hellip;</span></li>
);

const generatePaginationArray = (size, page, step) => {
  if (size < step * 2 + 6) {
    return range(1, size);
  } else if (page < step * 2 + 2) {
    return range(1, step * 2 + 4)
      .concat(["ellipsis", size]);
  } else if (page > size - step * 2 - 1) {
    return [1, "ellipsis"]
      .concat(range(size - step * 2 - 2, size + 1));
  } else {
    return [1, "ellipsis"]
      .concat(range(page - step, page + step + 1))
      .concat(["ellipsis", size]);
  }
}

export default ({ children, size, page, step }) => (
  <ul className="pagination-list">{
    generatePaginationArray(size, page, step)
      .map((item, index) => {
        if (item === "ellipsis") {
          return renderEllipsis(item + index);
        } else {
          return renderItem(item, page === item, item);
        }
      })
  }</ul>
);