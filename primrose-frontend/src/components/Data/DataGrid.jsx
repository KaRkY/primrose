import React from "react";
import PropTypes from "prop-types";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import difference from "lodash/difference";
import nextDirection from "../../util/nextDirection";

import Tooltip from "material-ui/Tooltip";
import Fade from "material-ui/transitions/Fade";
import Checkbox from "material-ui/Checkbox";
import IconButton from "material-ui/IconButton";
import KeyboardArrowRight from "@material-ui/icons/KeyboardArrowRight";
import KeyboardArrowDown from "@material-ui/icons/KeyboardArrowDown";
import Table, {
  TableBody,
  TableCell,
  TableRow,
  TableFooter,
  TablePagination,
  TableHead,
  TableSortLabel,
} from "material-ui/Table";

const styles = theme => ({
  "data-grid-table": {

  },

  "data-grid-head": {

  },

  "data-grid-row": {

  },

  "data-grid-checkbox": {
    width: 1,
  },

  "data-grid-open-panel": {
    width: 1,
  },

  "data-grid-actions": {
    width: 1,
    whiteSpace: "nowrap",
  },

  "data-grid-cell": {
    whiteSpace: "nowrap",
    overflow: "hidden",
    textOverflow: "ellipsis",
  },

  "data-grid-cell-max": {
    whiteSpace: "nowrap",
    overflow: "hidden",
    textOverflow: "ellipsis",
    width: "100%",
  },

  "data-grid-panel": {
    backgroundColor: theme.palette.background.default,
  },
});

const enhance = compose(
  withStyles(styles)
);

const extractId = (getId, obj) => {
  if (typeof getId === "function") {
    return getId(obj);
  } else {
    if (typeof getId === "string") {
      return obj[getId];
    } else {
      return obj.name;
    }
  }
}

const DataGrid = ({
  classes,
  rows = [],
  columns,
  getRowId,
  pagination,
  sorting,
  filtering,
  selecting,
  detailed,
  rowActions,
}) => {
  const numRows = rows.length;
  const unselectedRows = difference(rows.map(row => extractId(getRowId, row)), selecting ? selecting.rowIds : []);
  const numUnselected = unselectedRows.length;
  const emptyRows = (pagination ? pagination.size : numRows) - numRows;
  const colSpan = columns.length + (selecting ? 1 : 0) + (detailed ? 1 : 0) + (rowActions ? 1 : 0);

  const result = (
    <Table className={classes["data-grid-table"]}>
      <TableHead className={classes["data-grid-head"]}>
        <TableRow>
          {detailed && <TableCell padding="checkbox" />}
          {selecting && (
            <TableCell padding="checkbox">
              <Checkbox
                indeterminate={numUnselected > 0 && numUnselected < numRows}
                checked={numUnselected === 0 && numRows > 0}
                onChange={(event, checked) => selecting.onSelectRowsChange(event, rows.map(row => extractId(getRowId, row)), checked)}
              />
            </TableCell>
          )}
          {columns.map(column => (
            <TableCell
              key={column.name}
              className={column.grow ? classes["data-grid-cell-max"] : classes["data-grid-cell"]}
              numeric={column.numeric}
              padding={column.disablePadding ? "none" : "default"}
              style={column.width && { width: column.width}}
              sortDirection={(sorting && sorting.sort) ? (sorting.sort.property === column.name ? sorting.sort.direction : false) : undefined}>
              {sorting && column.sortable
                ? (
                  <Tooltip
                    title="Sort"
                    placement={column.numeric ? "bottom-end" : "bottom-start"}
                    enterDelay={300}
                  >
                    <TableSortLabel
                      active={(sorting.sort && sorting.sort.property) === column.name}
                      direction={sorting.sort && sorting.sort.direction}
                      onClick={event => sorting.onSortChange(event, column.name, nextDirection(sorting.sort, column.name))}
                    >
                      {column.title || column.name}
                    </TableSortLabel>
                  </Tooltip>
                )
                : column.title || column.name}
            </TableCell>
          ))}
          {rowActions && <TableCell padding="checkbox">Actions</TableCell>}
        </TableRow>
      </TableHead>

      <TableBody>
        {rows.map(row => {
          const currentRowId = extractId(getRowId, row);
          const selectedRowIds = (selecting && selecting.rowIds) || [];
          const detailedRowIds = (detailed && detailed.rowIds) || [];
          const isSelected = selectedRowIds.find(el => el === currentRowId) !== undefined ? true : false;
          const isPanelOpen = detailedRowIds.find(el => el === currentRowId)  !== undefined ? true : false;

          return (
            <React.Fragment key={currentRowId}>
              <TableRow hover className={classes["data-grid-row"]}>
                {detailed && (
                  <TableCell className={classes["data-grid-open-panel"]} padding="checkbox">
                    <IconButton onClick={event => detailed.onOpenRowsChange(event, [currentRowId], !isPanelOpen)}>
                      {isPanelOpen ? <KeyboardArrowDown /> : <KeyboardArrowRight />}
                    </IconButton>
                  </TableCell>
                )}
                {selecting && (
                  <TableCell className={classes["data-grid-checkbox"]} padding="checkbox">
                    <Checkbox
                      checked={isSelected}
                      onChange={(event, checked) => selecting.onSelectRowsChange(event, [currentRowId], checked)}
                    />
                  </TableCell>
                )}
                {columns.map(column => {
                  const format = column.formatter ? column.formatter : value => value;
                  const value = format(column.getCellValue ? column.getCellValue(row) : row[column.name]);
                  return (
                    <TableCell
                      key={column.name}
                      className={column.grow ? classes["data-grid-cell-max"] : classes["data-grid-cell"]}
                      numeric={column.numeric}
                      padding={column.disablePadding ? "none" : "default"}
                      style={column.width && { width: column.width}}
                      data-header={column.title || column.name}>
                      <Tooltip
                        title={value || ""}
                        enterDelay={300}
                      >
                        <span>{value}</span>
                      </Tooltip>
                    </TableCell>
                  );
                })}
                {rowActions && <TableCell padding="checkbox" numeric className={classes["data-grid-actions"]}>{rowActions(row)}</TableCell>}
              </TableRow>
              {detailed && (
                <Fade in={isPanelOpen} unmountOnExit>
                  <TableRow>
                    <TableCell className={classes["data-grid-panel"]} colSpan={colSpan}>
                      {detailed.children(row)}
                    </TableCell>
                  </TableRow>
                </Fade>
              )}
            </React.Fragment>
          );
        })}

        {emptyRows > 0 && (
          <TableRow style={{ height: 49 * emptyRows }}>
            <TableCell colSpan={colSpan} />
          </TableRow>
        )}
      </TableBody>

      {pagination && (
        <TableFooter>
          <TableRow>
            <TablePagination
              colSpan={colSpan}
              count={pagination.totalSize}
              rowsPerPage={pagination.size}
              page={pagination.page}
              backIconButtonProps={{
                "aria-label": "Previous Page",
              }}
              nextIconButtonProps={{
                "aria-label": "Next Page",
              }}
              onChangePage={pagination.onPageChange}
              onChangeRowsPerPage={(event, size) => pagination.onPageSizeChange(event, event.target.value)}
              rowsPerPageOptions={pagination.rowsPerPageOptions || [5, 10, 25]}
            />
          </TableRow>
        </TableFooter>
      )}
    </Table>
  );
  return result;
};

const ComposedDataGrid = enhance(DataGrid);
ComposedDataGrid.propTypes = {
  rows: PropTypes.array.isRequired,

  columns: PropTypes.arrayOf(PropTypes.shape({
    name: PropTypes.string.isRequired,
    title: PropTypes.string,
    numeric: PropTypes.bool,
    grow: PropTypes.bool,
    sortable: PropTypes.bool,
    getCellValue: PropTypes.func,
    formatter: PropTypes.func,
    width: PropTypes.string,
  })).isRequired,

  getRowId: PropTypes.func,

  pagination: PropTypes.shape({
    page: PropTypes.number.isRequired,
    size: PropTypes.number.isRequired,
    totalSize: PropTypes.number.isRequired,
    rowsPerPageOptions: PropTypes.arrayOf(PropTypes.number),
    onPageChange: PropTypes.func.isRequired,
    onPageSizeChange: PropTypes.func.isRequired,
  }),

  sorting: PropTypes.shape({
    sort: PropTypes.shape({
      property: PropTypes.string.isRequired,
      direction: PropTypes.oneOf(["asc", "desc"]).isRequired,
    }),
    onSortChange: PropTypes.func.isRequired,
  }),

  filtering: PropTypes.shape({
    onFilterChange: PropTypes.func.isRequired
  }),

  selecting: PropTypes.shape({
    rowIds: PropTypes.array.isRequired,
    onSelectRowsChange: PropTypes.func.isRequired,
  }),

  detailed: PropTypes.shape({
    rowIds: PropTypes.array.isRequired,
    onOpenRowsChange: PropTypes.func.isRequired,
    children: PropTypes.func.isRequired,
  }),

  rowActions: PropTypes.func.isRequired,
};

export default ComposedDataGrid;