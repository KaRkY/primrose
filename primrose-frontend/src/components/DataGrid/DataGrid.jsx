import React from "react";
import PropTypes from "prop-types";
import difference from "lodash/difference";
import nextDirection from "../../util/nextDirection";

import Checkbox from "@material-ui/core/Checkbox";
import Fade from "@material-ui/core/Fade";
import IconButton from "@material-ui/core/IconButton";
import Paper from "@material-ui/core/Paper";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableFooter from "@material-ui/core/TableFooter";
import TableHead from "@material-ui/core/TableHead";
import TablePagination from "@material-ui/core/TablePagination";
import TableRow from "@material-ui/core/TableRow";
import TableSortLabel from "@material-ui/core/TableSortLabel";
import Tooltip from "@material-ui/core/Tooltip";

import DataGridHeader from "../DataGridHeader";

import KeyboardArrowDownIcon from "@material-ui/icons/KeyboardArrowDown";
import KeyboardArrowRightIcon from "@material-ui/icons/KeyboardArrowRight";
import DeleteIcon from "@material-ui/icons/Delete";
import EditIcon from "@material-ui/icons/Edit";
import SendIcon from "@material-ui/icons/Send";
import OpenInNewIcon from "@material-ui/icons/OpenInNew";

const propTypes = {
  rows: PropTypes.array.isRequired,
  getRowId: PropTypes.oneOfType([PropTypes.func, PropTypes.string]),

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
    onChange: PropTypes.func.isRequired,
  }),

  filtering: PropTypes.shape({
    panel: PropTypes.element.isRequired,
    value: PropTypes.any,
    icon: PropTypes.element.isRequired,
    onChange: PropTypes.func.isRequired,
    open: PropTypes.bool.isRequired,
    onOpen: PropTypes.func.isRequired,
    onClose: PropTypes.func.isRequired,
  }),

  searching: PropTypes.shape({
    icon: PropTypes.element,
    text: PropTypes.string,
    tooltip: PropTypes.string.isRequired,
    onSearch: PropTypes.func.isRequired,
    placeholder: PropTypes.string,
  }),

  selecting: PropTypes.shape({
    rowIds: PropTypes.array.isRequired,
    onSelectRowsChange: PropTypes.func.isRequired,
  }),

  adding: PropTypes.shape({
    icon: PropTypes.element,
    text: PropTypes.string.isRequired,
    onEvent: PropTypes.func.isRequired,
  }),

  sending: PropTypes.shape({
    icon: PropTypes.element,
    text: PropTypes.string.isRequired,
    onEvent: PropTypes.func.isRequired,
  }),

  opening: PropTypes.shape({
    icon: PropTypes.element,
    text: PropTypes.string.isRequired,
    onEvent: PropTypes.func.isRequired,
  }),

  editing: PropTypes.shape({
    icon: PropTypes.element,
    text: PropTypes.string.isRequired,
    onEvent: PropTypes.func.isRequired,
  }),

  removing: PropTypes.shape({
    icon: PropTypes.element,
    text: PropTypes.string.isRequired,
    onEvent: PropTypes.func.isRequired,
  }),

  detailed: PropTypes.shape({
    rowIds: PropTypes.array.isRequired,
    onOpenRowsChange: PropTypes.func.isRequired,
    panel: PropTypes.func.isRequired,
  }),
};

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

const getRows = (obj, getRowId, rows, checked) => {
  if (Array.isArray(rows)) {
    const rowIds = rows.map(row => extractId(getRowId, row));
    if (checked) {
      return [...obj.rowIds, ...rowIds];
    } else {
      return obj.rowIds.filter(id => rowIds.indexOf(id) < 0);
    }
  } else {
    const rowId = extractId(getRowId, rows);
    if (checked) {
      return [...obj.rowIds, rowId];
    } else {
      return obj.rowIds.filter(id => id !== rowId);
    }
  }
};

const defaultIcon = (obj, defaultIcon) => (obj && obj.icon) || defaultIcon;

const DataGrid = ({
  classes,
  rows = [],
  columns,
  getRowId,
  pagination,
  sorting,
  filtering,
  searching,
  selecting,
  opening,
  adding,
  sending,
  editing,
  removing,
  detailed,
}) => {
  const numRows = rows.length;
  const unselectedRows = difference(rows.map(row => extractId(getRowId, row)), selecting ? selecting.rowIds : []);
  const numUnselected = unselectedRows.length;
  const hasSelectedRows = selecting && selecting.rowIds && selecting.rowIds.length > 0;
  const emptyRows = (pagination ? pagination.size : numRows) - numRows;
  const rowActions = !!opening || !!sending || !!editing || !!removing || false;
  const colSpan = columns.length + (selecting ? 1 : 0) + (detailed ? 1 : 0) + (rowActions ? 1 : 0);

  const result = (
    <Paper>
      <DataGridHeader
        searchTerm={searching && searching.text}
        searching={searching}
        selecting={selecting}
        adding={adding}
        sending={sending}
        removing={removing}
        hasSelected={hasSelectedRows}
      />
      <Table className={classes.table}>
        <TableHead className={classes.head}>
          <TableRow>
            {detailed && <TableCell padding="checkbox" />}
            {selecting && (
              <TableCell padding="checkbox">
                <Checkbox
                  indeterminate={numUnselected > 0 && numUnselected < numRows}
                  checked={numUnselected === 0 && numRows > 0}
                  onChange={(event, checked) => selecting.onSelectRowsChange(event, getRows(selecting, getRowId, rows, checked))}
                />
              </TableCell>
            )}
            {columns.map(column => (
              <TableCell
                key={column.name}
                className={column.grow ? classes.cellMax : classes.cell}
                numeric={column.numeric}
                padding={column.disablePadding ? "none" : "default"}
                style={column.width && { width: column.width }}
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
                        onClick={event => {
                          const property = column.name;
                          const direction = nextDirection(sorting.sort, column.name);
                          sorting.onChange(event, direction && { property, direction })
                        }}
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
            const currentRowId = extractId(getRowId, row).toString();
            const selectedRowIds = (selecting && selecting.rowIds) || [];
            const detailedRowIds = (detailed && detailed.rowIds) || [];
            const isSelected = selectedRowIds.find(el => el.toString() === currentRowId) !== undefined ? true : false;
            const isPanelOpen = detailedRowIds.find(el => el.toString() === currentRowId) !== undefined ? true : false;

            return (
              <React.Fragment key={currentRowId}>
                <TableRow hover className={classes.row}>
                  {detailed && (
                    <TableCell className={classes.openPanel} padding="checkbox">
                      <IconButton onClick={event => detailed.onOpenRowsChange(event, getRows(detailed, getRowId, row, !isPanelOpen))}>
                        {isPanelOpen ? <KeyboardArrowDownIcon /> : <KeyboardArrowRightIcon />}
                      </IconButton>
                    </TableCell>
                  )}
                  {selecting && (
                    <TableCell className={classes.checkbox} padding="checkbox">
                      <Checkbox
                        checked={isSelected}
                        onChange={(event, checked) => selecting.onSelectRowsChange(event, getRows(selecting, getRowId, row, checked))}
                      />
                    </TableCell>
                  )}
                  {columns.map(column => {
                    const format = column.formatter ? column.formatter : value => value;
                    const value = format(column.getCellValue ? column.getCellValue(row) : row[column.name]);
                    return (
                      <TableCell
                        key={column.name}
                        className={column.grow ? classes.cellMax : classes.cell}
                        numeric={column.numeric}
                        padding={column.disablePadding ? "none" : "default"}
                        style={column.width && { width: column.width }}
                        data-header={column.title || column.name}>
                        <span>{value}</span>
                      </TableCell>
                    );
                  })}
                  {rowActions && <TableCell padding="checkbox" numeric className={classes.rowActions}>
                    {opening &&
                      <Tooltip
                        title={opening.text}
                        placement="bottom"
                        enterDelay={300}
                      >
                        <IconButton onClick={event => opening.onEvent(event, currentRowId)}>
                          {defaultIcon(opening.icon, <OpenInNewIcon />)}
                        </IconButton>
                      </Tooltip>
                    }
                    {editing &&
                      <Tooltip
                        title={editing.text}
                        placement="bottom"
                        enterDelay={300}
                      >
                        <IconButton onClick={event => editing.onEvent(event, currentRowId)}>
                          {defaultIcon(editing.icon, <EditIcon />)}
                        </IconButton>
                      </Tooltip>
                    }
                    {removing &&
                      <Tooltip
                        title={removing.text}
                        placement="bottom"
                        enterDelay={300}
                      >
                        <IconButton onClick={event => removing.onEvent(event, currentRowId)}>
                          {defaultIcon(removing.icon, <DeleteIcon />)}
                        </IconButton>
                      </Tooltip>
                    }
                    {sending &&
                      <Tooltip
                        title={sending.text}
                        placement="bottom"
                        enterDelay={300}
                      >
                        <IconButton onClick={event => sending.onEvent(event, currentRowId)}>
                          {defaultIcon(sending.icon, <SendIcon />)}
                        </IconButton>
                      </Tooltip>
                    }
                  </TableCell>}
                </TableRow>
                {detailed && (
                  <Fade in={isPanelOpen} unmountOnExit>
                    <TableRow>
                      <TableCell className={classes.panel} colSpan={colSpan}>
                        {detailed.panel(row)}
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
    </Paper>
  );
  return result;
};
DataGrid.propTypes = propTypes;
export default DataGrid;