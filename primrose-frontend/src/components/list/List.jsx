import React from "react";
import PropTypes from "prop-types";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";

import Paper from "material-ui/Paper";
import Toolbar from "material-ui/Toolbar";
import Typography from "material-ui/Typography";
import Grid from "material-ui/Grid";
import Tooltip from "material-ui/Tooltip";
import Checkbox from "material-ui/Checkbox";
import Table, {
  TableBody,
  TableCell,
  TableFooter,
  TableHead,
  TablePagination,
  TableRow,
  TableSortLabel,
} from "material-ui/Table";

const propTypes = {
};

const styles = theme => ({
  //body: theme.mixins.gutters({})
});

const enhance = compose(
  withStyles(styles)
);

const createSortHandler = (onSortHandler, property) => () => onSortHandler && onSortHandler(property);
const empty = [];
const getList = (list) => list || empty;
const extractId = (getId, obj) => {
  if (typeof getId === "function") {
    return getId(obj);
  } else {
    if (typeof getId === "String") {
      return obj[getId];
    } else {
      return obj.id;
    }
  }
}

const createPageChange = (onPageChange) => (event, page) => onPageChange(page);
const createChangeRowsPerPage = (onChangeRowsPerPage) => (event) => onChangeRowsPerPage(event.target.value);

const List = ({
  classes,
  title,
  columns,
  data,
  count,
  rowsPerPage,
  page,
  selectable,
  order,
  orderBy,
  onSortHandler,
  onChangePage,
  onChangeRowsPerPage,
  rowsPerPageOptions,
  getRowId }) => (
    <Paper className={classes.test}>
      <Toolbar>
        <Typography variant="title">{title}</Typography>
      </Toolbar>
      <div className={classes.body}>
        <Table>
          <TableHead>
            <TableRow>
              {selectable && (
                <TableCell padding="checkbox"><Checkbox /></TableCell>
              )}
              {getList(columns).map(column => (
                <TableCell key={column.id}
                  numeric={column.numeric}
                  padding={column.disablePadding ? "none" : "default"}
                  sortDirection={orderBy === column.id ? order : false}>
                  <Tooltip
                    title="Sort"
                    placement={column.numeric ? "bottom-end" : "bottom-start"}
                    enterDelay={300}
                  >
                    <TableSortLabel
                      active={orderBy === column.id}
                      direction={order}
                      onClick={createSortHandler(onSortHandler, column.id)}
                    >
                      {column.label}
                    </TableSortLabel>
                  </Tooltip>
                </TableCell>
              ))}
            </TableRow>
          </TableHead>

          <TableBody>
            {getList(data).map(row => (
              <TableRow hover key={extractId(getRowId, row)}>
                {selectable && (
                  <TableCell padding="checkbox"><Checkbox /></TableCell>
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

            {rowsPerPage - getList(data).length > 0 && (
              <TableRow style={{ height: 49 * (rowsPerPage - getList(data).length) }}>
                <TableCell colSpan={6} />
              </TableRow>
            )}
          </TableBody>
          <TableFooter>
            <TableRow>
              <TablePagination
                colSpan={6}
                count={count}
                rowsPerPage={rowsPerPage}
                page={page}
                backIconButtonProps={{
                  "aria-label": "Previous Page",
                }}
                nextIconButtonProps={{
                  "aria-label": "Next Page",
                }}
                onChangePage={createPageChange(onChangePage)}
                onChangeRowsPerPage={createChangeRowsPerPage(onChangeRowsPerPage)}
                rowsPerPageOptions={rowsPerPageOptions}
              />
            </TableRow>
          </TableFooter>
        </Table>
      </div>
    </Paper>
  );

const ComposedList = enhance(List);
ComposedList.propTypes = propTypes;

export default ComposedList;