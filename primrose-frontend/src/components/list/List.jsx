import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import Paper from "material-ui/Paper";
import Toolbar from "material-ui/Toolbar";
import Typography from "material-ui/Typography";
import Tooltip from "material-ui/Tooltip";
import Checkbox from "material-ui/Checkbox";
import Fade from "material-ui/transitions/Fade";
import { CircularProgress } from "material-ui/Progress";
import Table, {
  TableBody,
  TableCell,
  TableFooter,
  TableHead,
  TablePagination,
  TableRow,
  TableSortLabel,
} from "material-ui/Table";

const styles = theme => ({
  root: {
    position: "relative",
  },

  loadingContainer: {
    position: "absolute",
    top: 0,
    left: 0,
    width: "100%",
    height: "100%",
    background: theme.palette.action.disabledBackground,
  },

  loadingIcon: {
    position: "absolute",
    margin: "auto",
    top: 0,
    bottom: 0,
    left: 0,
    right: 0,
  }
});

const enhance = compose(
  withStyles(styles)
);

const createSortHandler = (onSortHandler, property) => () => onSortHandler && onSortHandler(property);
const empty = [];
const getList = (list, loading) => {
  if (list) {
    if (loading) {
      return empty;
    } else {
      return list;
    }
  } else {
    return empty;
  }
};
const extractId = (getId, obj) => {
  if (typeof getId === "function") {
    return getId(obj);
  } else {
    if (typeof getId === "string") {
      return obj[getId];
    } else {
      return obj.id;
    }
  }
}

const createPageChange = (onPageChange) => (event, page) => onPageChange(page);
const createChangeRowsPerPage = (onChangeRowsPerPage) => (event) => onChangeRowsPerPage(event.target.value);
const createSelectRow = (onSelectRow, rowId) => (event, checked) => onSelectRow && onSelectRow(rowId, checked);
const createSelectAllRows = (onSelectRow, rows) => (event, checked) => onSelectRow && onSelectRow(rows, checked);

const List = ({
  classes,
  title,
  columns,
  data,
  totalSize,
  pageSize,
  pageNumber,
  selectable,
  selectedRows,
  loading,
  sortDirection,
  isSortedColumn,
  rowsPerPageOptions,
  onSelectRow,
  onSortChange,
  onPageChange,
  onRowsPerPageChange,
  rowId }) => {
  const numRows = getList(data, loading).length;
  const emptyRows = pageSize - numRows;
  const rowData = getList(data, loading);
  const numSelected = (selectedRows && selectedRows.length);

  return (
    <Paper className={classes.root}>
      <Toolbar>
        <Typography variant="title">{title}</Typography>
      </Toolbar>
      <Table>
        <TableHead>
          <TableRow>
            {selectable && (
              <TableCell padding="checkbox">
                <Checkbox
                  indeterminate={numSelected > 0 && numSelected < numRows}
                  checked={numSelected === numRows}
                  onChange={createSelectAllRows(onSelectRow, rowData.map(row => extractId(rowId, row)))}
                />
              </TableCell>
            )}
            {getList(columns).map(column => (
              <TableCell key={column.id}
                numeric={column.numeric}
                padding={column.disablePadding ? "none" : "default"}
                sortDirection={isSortedColumn(column) ? sortDirection : false}>
                <Tooltip
                  title="Sort"
                  placement={column.numeric ? "bottom-end" : "bottom-start"}
                  enterDelay={300}
                >
                  <TableSortLabel
                    active={isSortedColumn(column)}
                    direction={sortDirection}
                    onClick={createSortHandler(onSortChange, column.id)}
                  >
                    {column.label}
                  </TableSortLabel>
                </Tooltip>
              </TableCell>
            ))}
          </TableRow>
        </TableHead>

        <TableBody>
          {rowData.map(row => (
            <TableRow hover key={extractId(rowId, row)}>
              {selectable && (
                <TableCell padding="checkbox">
                  <Checkbox
                    checked={(selectedRows && selectedRows.find(el => el === extractId(rowId, row))) ? true : false}
                    onChange={createSelectRow(onSelectRow, extractId(rowId, row))}
                  />
                </TableCell>
              )}
              {getList(columns).map(column => (
                <TableCell key={column.id}
                  numeric={column.numeric}
                  padding={column.disablePadding ? "none" : "default"}>
                  {row[column.id]}
                </TableCell>
              ))}
            </TableRow>
          ))}

          {emptyRows > 0 && (
            <TableRow style={{ height: 49 * emptyRows }}>
              <TableCell colSpan={6} />
            </TableRow>
          )}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              colSpan={6}
              count={totalSize}
              rowsPerPage={pageSize}
              page={pageNumber}
              backIconButtonProps={{
                "aria-label": "Previous Page",
              }}
              nextIconButtonProps={{
                "aria-label": "Next Page",
              }}
              onChangePage={createPageChange(onPageChange)}
              onChangeRowsPerPage={createChangeRowsPerPage(onRowsPerPageChange)}
              rowsPerPageOptions={rowsPerPageOptions}
            />
          </TableRow>
        </TableFooter>
      </Table>
      <Fade in={loading} unmountOnExit>
        <div className={classes.loadingContainer}>
          <CircularProgress className={classes.loadingIcon} />
        </div>
      </Fade>
    </Paper>
  );
};

const ComposedList = enhance(List);

export default ComposedList;