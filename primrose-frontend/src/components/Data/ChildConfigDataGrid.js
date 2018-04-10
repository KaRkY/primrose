import withConfig from "../withConfig";
import DataGrid from "./DataGrid";

export const config = {
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
};

export default withConfig(config)(DataGrid);