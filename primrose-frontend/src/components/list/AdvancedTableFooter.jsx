import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { withStyles } from "material-ui/styles";

import {
  TableRow,
  TableFooter,
  TablePagination,
} from "material-ui/Table";

const styles = theme => ({
  panelRow: {
    backgroundColor: theme.palette.background.default,
  },
});

const enhance = compose(
  withHandlers({
    onPageChange: ({ onPageChange }) => (event, page) => onPageChange && onPageChange(event, page),
    onPageSizeChange: ({ onPageSizeChange }) => (event) => onPageSizeChange && onPageSizeChange(event, event.target.value),
  }),
  withStyles(styles)
);

const AdvancedTableFooter = ({
  classes,
  columns,
  pageSize,
  totalSize,
  pageNumber,
  detailed,
  selectable,
  rowsPerPageOptions,
  onPageChange,
  onPageSizeChange,
}) => (
    <TableFooter>
      <TableRow>
        <TablePagination
          colSpan={columns.length + (selectable ? 1 : 0) + (detailed ? 1 : 0)}
          count={totalSize}
          rowsPerPage={pageSize}
          page={pageNumber}
          backIconButtonProps={{
            "aria-label": "Previous Page",
          }}
          nextIconButtonProps={{
            "aria-label": "Next Page",
          }}
          onChangePage={onPageChange}
          onChangeRowsPerPage={onPageSizeChange}
          rowsPerPageOptions={rowsPerPageOptions}
        />
      </TableRow>
    </TableFooter>
  );

const ComposedAdvancedTableFooter = enhance(AdvancedTableFooter);

export default ComposedAdvancedTableFooter;