import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import findByType from "../../util/findByType";
import extractRenderMethod from "../../util/extractRenderMethod";
import get from "lodash/get";
import identity from "lodash/identity";
import difference from "lodash/difference";

import Tooltip from "material-ui/Tooltip";
import Fade from "material-ui/transitions/Fade";
import Checkbox from "material-ui/Checkbox";
import IconButton from "material-ui/IconButton";
import KeyboardArrowRight from "material-ui-icons/KeyboardArrowRight";
import KeyboardArrowDown from "material-ui-icons/KeyboardArrowDown";
import Table, {
  TableBody,
  TableCell,
  TableRow,
  TableFooter,
  TablePagination,
  TableHead,
  TableSortLabel,
} from "material-ui/Table";

const styles = theme => console.log(theme) || ({
  "data-grid-table": {
    tableLayout: "fixed",
  },

  "data-grid-head": {

  },

  "data-grid-row": {

  },

  "data-grid-cell": {
    whiteSpace: "nowrap",
    overflow: "hidden",
    textOverflow: "ellipsis",
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
      return obj.id;
    }
  }
}

const Columns = () => null;
Columns.displayName = "Columns";

const Pagination = () => null;
Pagination.displayName = "Pagination";

const Sortable = () => null;
Sortable.displayName = "Sortable";

const Selectable = () => null;
Selectable.displayName = "Selectable";

const RenderPanel = () => null;
RenderPanel.displayName = "RenderPanel";

const RenderCell = () => null;
RenderCell.displayName = "RenderCell";

const RowActions = () => null;
RowActions.displayName = "RowActions";

const DataGrid = ({
  classes,
  rows = [],
  rowId,
  children }) => {

  const columnsComponent = findByType(children, Columns)[0];
  const paginationComponent = findByType(children, Pagination)[0];
  const sortableComponent = findByType(children, Sortable)[0];
  const selectableComponent = findByType(children, Selectable)[0];
  const renderPanelComponent = findByType(children, RenderPanel)[0];
  const renderCellComponent = findByType(children, RenderCell)[0];
  const rowActionsComponent = findByType(children, RowActions)[0];

  const columns = get(columnsComponent, "props.children", []);

  const isPaginated = paginationComponent ? true : false;
  const pageSize = get(paginationComponent, "props.pageSize", 10);
  const pageNumber = get(paginationComponent, "props.pageNumber", 0);
  const specifiedSize = get(paginationComponent, "props.totalSize", 0);
  // To prevent page change until data is loaded
  const totalSize = specifiedSize ? specifiedSize : (pageSize * (pageNumber + 1));
  const rowsPerPageOptions = get(paginationComponent, "props.rowsPerPageOptions", [5, 10, 25]);
  const onPageChange = get(paginationComponent, "props.onPageChange", identity);
  const onPageSizeChange = get(paginationComponent, "props.onPageSizeChange", identity);

  const isSortable = sortableComponent ? true : false;
  const sortDirection = get(sortableComponent, "props.sortDirection");
  const sortColumn = get(sortableComponent, "props.sortColumn");
  const onSortChange = get(sortableComponent, "props.onSortChange", identity);

  const isSelectable = selectableComponent ? true : false;
  const selectedRows = get(selectableComponent, "props.selectedRows", []);
  const onSelectRows = get(selectableComponent, "props.onSelectRows", identity);

  const isDetailed = renderPanelComponent ? true : false;
  const openPanels = get(renderPanelComponent, "props.openPanels", []);
  const onOpenPanels = get(renderPanelComponent, "props.onOpenPanels", identity);
  const renderPanel = extractRenderMethod(get(renderPanelComponent, "props", {}));

  const isRenderCell = renderCellComponent ? true : false;
  const renderCell = extractRenderMethod(get(renderCellComponent, "props", {}));

  const isRowActions = rowActionsComponent ? true : false;
  const rowActions = extractRenderMethod(get(rowActionsComponent, "props", {}));

  const numRows = rows.length;
  const unselectedRows = difference(rows.map(row => extractId(rowId, row)), selectedRows);
  const numUnselected = unselectedRows.length;
  const emptyRows = pageSize - numRows;
  const colSpan = columns.length + (isSelectable ? 1 : 0) + (isDetailed ? 1 : 0) + (isRowActions ? 1 : 0);

  return (
    <Table className={classes.root}>
      <colgroup>
        {isDetailed && <col style={{ width: 48 }} />}
        {isSelectable && (<col style={{ width: 58 }} />)}
        {columns.map(column => (
          <col key={column.id} style={column.numeric && { width: 58 }} />
        ))}
      </colgroup>

      <TableHead className={classes["data-grid-head"]}>
        <TableRow>
          {isDetailed && <TableCell padding="checkbox" />}
          {isSelectable && (
            <TableCell padding="checkbox">
              <Checkbox
                indeterminate={numUnselected > 0 && numUnselected < numRows}
                checked={numUnselected === 0}
                onChange={(event, checked) => onSelectRows(event, rows.map(row => extractId(rowId, row)), checked)}
              />
            </TableCell>
          )}
          {columns.map(column => (
            <TableCell
              key={column.id}
              className={classes["data-grid-cell"]}
              numeric={column.numeric}
              padding={column.disablePadding ? "none" : "default"}
              sortDirection={isSortable ? (sortColumn === column.id ? sortDirection : false) : undefined}>
              {isSortable
                ? (
                  <Tooltip
                    title="Sort"
                    placement={column.numeric ? "bottom-end" : "bottom-start"}
                    enterDelay={300}
                  >
                    <TableSortLabel
                      active={sortColumn === column.id}
                      direction={sortDirection}
                      onClick={event => onSortChange(event, column.id)}
                    >
                      {column.label}
                    </TableSortLabel>
                  </Tooltip>
                )
                : column.label}
            </TableCell>
          ))}
          {isRowActions && <TableCell padding="checkbox" />}
        </TableRow>
      </TableHead>

      <TableBody>
        {rows.map(row => {
          const currentRowId = extractId(rowId, row);
          const isSelected = selectedRows.find(el => el === currentRowId) ? true : false;
          const isPanelOpen = openPanels.find(el => el === currentRowId) ? true : false;

          return (
            <React.Fragment key={currentRowId}>
              <TableRow hover className={classes["data-grid-row"]}>
                {isDetailed && (
                  <TableCell padding="checkbox">
                    <IconButton onClick={event => onOpenPanels(event, [currentRowId], !isPanelOpen)}>
                      {isPanelOpen ? <KeyboardArrowDown /> : <KeyboardArrowRight />}
                    </IconButton>
                  </TableCell>
                )}
                {isSelectable && (
                  <TableCell padding="checkbox">
                    <Checkbox
                      checked={isSelected}
                      onChange={(event, checked) => onSelectRows(event, [currentRowId], checked)}
                    />
                  </TableCell>
                )}
                {columns.map(column => (
                  <TableCell
                    key={column.id}
                    title={row[column.id]}
                    className={classes["data-grid-cell"]}
                    numeric={column.numeric}
                    padding={column.disablePadding ? "none" : "default"}
                    data-header={column.label}>
                    {isRenderCell ? renderCell(column) : row[column.id]}
                  </TableCell>
                ))}
                {isRowActions && <TableCell className={classes.tableCell} padding="checkbox">{rowActions(row)}</TableCell>}
              </TableRow>
              <Fade in={isPanelOpen} unmountOnExit>
                <TableRow>
                  <TableCell className={classes["data-grid-panel"]} colSpan={colSpan}>
                    {renderPanel(row)}
                  </TableCell>
                </TableRow>
              </Fade>
            </React.Fragment>
          );
        })}

        {emptyRows > 0 && (
          <TableRow style={{ height: 49 * emptyRows }}>
            <TableCell colSpan={colSpan} />
          </TableRow>
        )}
      </TableBody>

      {isPaginated && (
        <TableFooter>
          <TableRow>
            <TablePagination
              colSpan={colSpan}
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
              onChangeRowsPerPage={(event, size) => onPageSizeChange(event, event.target.value)}
              rowsPerPageOptions={rowsPerPageOptions}
            />
          </TableRow>
        </TableFooter>
      )}
    </Table>
  );
};

const ComposedDataGrid = enhance(DataGrid);
ComposedDataGrid.Columns = Columns;
ComposedDataGrid.Pagination = Pagination;
ComposedDataGrid.Sortable = Sortable;
ComposedDataGrid.Selectable = Selectable;
ComposedDataGrid.RenderPanel = RenderPanel;
ComposedDataGrid.RenderCell = RenderCell;
ComposedDataGrid.RowActions = RowActions;

export default ComposedDataGrid;