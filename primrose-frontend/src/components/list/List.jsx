import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { withStyles } from "material-ui/styles";

import Paper from "material-ui/Paper";
import Button from "material-ui/Button";
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
import { IconButton } from "material-ui";
import KeyboardArrowRight from "material-ui-icons/KeyboardArrowRight";

const styles = theme => ({
  root: {
    position: "relative",
  },

  grow: {
    flex: "1 1 auto",
  },

  panelRow: {
    backgroundColor: theme.palette.background.default,
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
  withHandlers({
    onPageChange: ({ onPageChange }) => (event, page) => onPageChange && onPageChange(page),
    onChangeRowsPerPage: ({ onChangeRowsPerPage }) => (event) => onChangeRowsPerPage && onChangeRowsPerPage(event.target.value),
    onSelectRows: ({ onSelectRows }) => (rows) => (event, checked) => onSelectRows && onSelectRows(rows, checked),
    onDelete: ({ onDelete }) => (event) => onDelete && onDelete(),
  }),
  withStyles(styles)
);

const createSortHandler = (onSortHandler, property) => () => onSortHandler && onSortHandler(property);
const empty = [];
const getList = (list, loading) => list || empty;
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
  deleting,
  sortDirection,
  isSortedColumn,
  rowsPerPageOptions,
  onSelectRows,
  onSortChange,
  onPageChange,
  onRowsPerPageChange,
  onDelete,
  rowId }) => {
  const numRows = data.length;
  const emptyRows = pageSize - numRows;
  const numSelected = (selectedRows && selectedRows.length);

  return (
    <Paper className={classes.root}>
      <Toolbar>
        <Typography variant="title">{title}</Typography>
        <div className={classes.grow} />
        {numSelected > 0 && (
          <Tooltip id="appbar-theme" title="Delete selected customers" enterDelay={300}>
            <Button disabled={deleting} variant="raised" color="secondary" onClick={onDelete}>
              Delete
              {deleting && <CircularProgress color={"inherit"} size={24} />}
            </Button>
          </Tooltip>
        )}
      </Toolbar>
      <Table style={{ "table-layout": "fixed" }}>
        <colgroup>
          <col style={{ width: 48 }} />
          {selectable && (<col style={{ width: 58 }} />)}
          {columns.map(column => (
            <col key={column.id} style={column.numeric && { width: 58 }} />
          ))}
        </colgroup>
        <TableHead>
          <TableRow>
            <TableCell padding="checkbox" />
            {selectable && (
              <TableCell padding="checkbox">
                <Checkbox
                  indeterminate={numSelected > 0 && numSelected < numRows}
                  checked={numSelected === numRows}
                  onChange={onSelectRows(data.map(row => extractId(rowId, row)))}
                />
              </TableCell>
            )}
            {columns.map(column => (
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
          {data.map(row => (
            <React.Fragment>
              <TableRow hover key={extractId(rowId, row)}>
                <TableCell padding="checkbox">
                  <IconButton>
                    <KeyboardArrowRight />
                  </IconButton>
                </TableCell>
                {selectable && (
                  <TableCell padding="checkbox">
                    <Checkbox
                      checked={(selectedRows && selectedRows.find(el => el === extractId(rowId, row))) ? true : false}
                      onChange={onSelectRows([extractId(rowId, row)])}
                    />
                  </TableCell>
                )}
                {columns.map(column => (
                  <TableCell key={column.id}
                    numeric={column.numeric}
                    padding={column.disablePadding ? "none" : "default"}>
                    {row[column.id]}
                  </TableCell>
                ))}
              </TableRow>
            </React.Fragment>
          ))}

          {emptyRows > 0 && (
            <TableRow style={{ height: 49 * emptyRows }}>
              <TableCell colSpan={columns.length + (selectable ? 2 : 1)} />
            </TableRow>
          )}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              colSpan={columns.length + (selectable ? 2 : 1)}
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
              onChangeRowsPerPage={onRowsPerPageChange}
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