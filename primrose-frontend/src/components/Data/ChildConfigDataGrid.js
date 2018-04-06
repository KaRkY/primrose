import dynamic from "../dynamic";
import DataGrid from "./DataGrid";

export default dynamic({
  Columns: {
    key: "columns",
    ordered: true,
    ignoreProps: true,
  },

  Column: {
    key: "column",
  },

  Pagination: {
    key: "pagination",
  },

  Sorting: {
    key: "sorting",
  },

  Filtering: {
    key: "filtering",
  },

  Selecting: {
    key: "selecting",
  },

  Detailed: {
    key: "detailed",
    renderChildren: true,
  },

  RowActions: {
    key: "rowActions",
    ignoreProps: true,
    renderChildren: true,
  }
})(DataGrid);