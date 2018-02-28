import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import findByType from "../../util/findByType";
import extractRenderMethod from "../../util/extractRenderMethod";
import get from "lodash/get";
import identity from "lodash/identity";

import Paper from "material-ui/Paper";
import Toolbar from "material-ui/Toolbar";
import Typography from "material-ui/Typography";
import Tooltip from "material-ui/Tooltip";
import Fade from "material-ui/transitions/Fade";
import Checkbox from "material-ui/Checkbox";
import Table, {
  TableBody,
  TableCell,
  TableRow,
  TableFooter,
  TablePagination,
  TableHead,
  TableSortLabel,
} from "material-ui/Table";
import AdvancedTableRow from "./AdvancedTableRow";
import Loading from "../Loading";

const styles = theme => ({
  root: {
    position: "relative",
  },

  grow: {
    flex: "1 1 auto",
  },

  table: {
    tableLayout: "fixed",
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

const Title = () => null;
Title.displayName = "Title";

const Columns = () => null;
Columns.displayName = "Columns";

const Pagination = () => null;
Pagination.displayName = "Pagination";

const Sortable = () => null;
Sortable.displayName = "Sortable";

const Selectable = () => null;
Selectable.displayName = "Selectable";

const Actions = () => null;
Actions.displayName = "Actions";

const RenderPanel = () => null;
RenderPanel.displayName = "RenderPanel";

const Grid = ({
  classes,
  rows = [],
  loading,
  rowId,
  children }) => {

  const titleComponent = findByType(children, Title)[0];
  const columnsComponent = findByType(children, Columns)[0];
  const paginationComponent = findByType(children, Pagination)[0];
  const sortableComponent = findByType(children, Sortable)[0];
  const selectableComponent = findByType(children, Selectable)[0];
  const actionsComponent = findByType(children, Actions)[0];
  const renderPanelComponent = findByType(children, RenderPanel)[0];

  const title = get(titleComponent, "props.children");

  const columns = get(columnsComponent, "props.children", []);

  const isPaginated = paginationComponent ? true : false;
  const pageSize = get(paginationComponent, "props.pageSize", 10);
  const pageNumber = get(paginationComponent, "props.pageNumber", 0);
  const totalSize = get(paginationComponent, "props.totalSize", 0);
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

  const actionsChildren = get(actionsComponent, "props.children", null);

  const isDetailed = renderPanelComponent ? true : false;
  const renderPanel = extractRenderMethod(get(renderPanelComponent, "props", {}));

  const numRows = rows.length;
  const numSelected = selectedRows.length;
  const emptyRows = pageSize - numRows;
  const colSpan = columns.length + (isSelectable ? 1 : 0) + (isDetailed ? 1 : 0);

  return (
    <Paper className={classes.root}>
      <Toolbar>
        <Typography variant="title">{title}</Typography>
        <div className={classes.grow} />
        {actionsChildren}
      </Toolbar>
      <Table className={classes.table}>
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
                  indeterminate={numSelected > 0 && numSelected < numRows}
                  checked={numSelected > 0 && numSelected === numRows}
                  onChange={(event, checked) => onSelectRows(event, rows.map(row => extractId(rowId, row)), checked)}
                />
              </TableCell>
            )}
            {columns.map(column => (
              <TableCell key={column.id}
                numeric={column.numeric}
                padding={column.disablePadding ? "none" : "default"}
                sortDirection={isSortable? (sortColumn === column.id ? sortDirection : false) : undefined}>
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
          {rows.map(row => <AdvancedTableRow
            key={extractId(rowId, row)}
            columns={columns}
            row={row}
            colSpan={colSpan}
            detailed={isDetailed}
            selectable={isSelectable}
            selected={(selectedRows && selectedRows.find(el => el === extractId(rowId, row))) ? true : false}
            onSelect={(event, checked) => onSelectRows(event, [extractId(rowId, row)], checked)}
            renderPanel={renderPanel}
          />)}

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
      <Fade in={loading} unmountOnExit>
        <Loading classes={{ root: classes.loadingContainer, icon: classes.loadingIcon }} />
      </Fade>
    </Paper>
  );
};

const ComposedGrid = enhance(Grid);
ComposedGrid.Title = Title;
ComposedGrid.Columns = Columns;
ComposedGrid.Pagination = Pagination;
ComposedGrid.Sortable = Sortable;
ComposedGrid.Selectable = Selectable;
ComposedGrid.Actions = Actions;
ComposedGrid.RenderPanel = RenderPanel;

export default ComposedGrid;