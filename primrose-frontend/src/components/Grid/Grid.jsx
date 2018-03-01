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

const styles = theme => ({
  root: {
    tableLayout: "fixed",
  },

  detailPanel: {
    backgroundColor: theme.palette.background.default,
  },

  tableCell: {
    whiteSpace: "nowrap",
    overflow: "hidden",
    textOverflow: "ellipsis",
  },

  loadingContainer: {
    position: "absolute",
    top: 0,
    left: 0,
    width: "100%",
    height: "100%",
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

const Grid = ({
  classes,
  rows = [],
  rowId,
  children }) => {

  const columnsComponent = findByType(children, Columns)[0];
  const paginationComponent = findByType(children, Pagination)[0];
  const sortableComponent = findByType(children, Sortable)[0];
  const selectableComponent = findByType(children, Selectable)[0];
  const renderPanelComponent = findByType(children, RenderPanel)[0];

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

  const numRows = rows.length;
  const unselectedRows = difference(rows.map(row => extractId(rowId, row)), selectedRows);
  const numUnselected = unselectedRows.length;
  const emptyRows = pageSize - numRows;
  const colSpan = columns.length + (isSelectable ? 1 : 0) + (isDetailed ? 1 : 0);

  return (
      <Table className={classes.root}>
        <colgroup>
          {isDetailed && <col style={{ width: 48 }} />}
          {isSelectable && (<col style={{ width: 58 }} />)}
          {columns.map(column => (
            <col key={column.id} style={column.numeric && { width: 58 }} />
          ))}
        </colgroup>

        <TableHead>
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
              <TableCell key={column.id}
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
          </TableRow>
        </TableHead>

        <TableBody>
          {rows.map(row => {
            const currentRowId = extractId(rowId, row);
            const isSelected = selectedRows.find(el => el === currentRowId) ? true : false;
            const isPanelOpen = openPanels.find(el => el === currentRowId) ? true : false;

            return (
              <React.Fragment key={currentRowId}>
                <TableRow hover>
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
                    <TableCell title={row[column.id]} className={classes.tableCell} key={column.id}
                      numeric={column.numeric}
                      padding={column.disablePadding ? "none" : "default"}>
                      {row[column.id]}
                    </TableCell>
                  ))}
                </TableRow>
                <Fade in={isPanelOpen} unmountOnExit>
                  <TableRow>
                    <TableCell className={classes.detailPanel} colSpan={colSpan}>
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

const ComposedGrid = enhance(Grid);
ComposedGrid.Columns = Columns;
ComposedGrid.Pagination = Pagination;
ComposedGrid.Sortable = Sortable;
ComposedGrid.Selectable = Selectable;
ComposedGrid.RenderPanel = RenderPanel;

export default ComposedGrid;