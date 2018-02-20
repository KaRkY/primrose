import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { withStyles } from "material-ui/styles";

import Paper from "material-ui/Paper";
import Button from "material-ui/Button";
import Toolbar from "material-ui/Toolbar";
import Typography from "material-ui/Typography";
import Tooltip from "material-ui/Tooltip";
import Fade from "material-ui/transitions/Fade";
import { CircularProgress } from "material-ui/Progress";
import Table from "material-ui/Table";
import AdvancedTableHead from "./AdvancedTableHead";
import AdvancedTableBody from "./AdvancedTableBody";
import AdvancedTableFooter from "./AdvancedTableFooter";
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
  withHandlers({
    onSelectRows: ({ onSelectRows }) => (rows) => (event, checked) => onSelectRows && onSelectRows(event, rows, checked),
    onSelectRow: ({ onSelectRows }) => (event, row, checked) => onSelectRows && onSelectRows(event, [row], checked),
    onDelete: ({ onDelete }) => (event) => onDelete && onDelete(event),
  }),
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

const List = ({
  classes,
  title,
  columns,
  rows,
  totalSize,
  pageSize,
  pageNumber,
  selectable,
  detailed,
  selectedRows,
  loading,
  deleting,
  sortDirection,
  sortColumn,
  rowsPerPageOptions,
  renderPanel,
  onSelectRows,
  onSelectRow,
  onSortChange,
  onPageChange,
  onPageSizeChange,
  onDelete,
  rowId }) => {
  const numRows = rows.length;
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
      <Table className={classes.table}>
        <colgroup>
          {detailed && <col style={{ width: 48 }} />}
          {selectable && (<col style={{ width: 58 }} />)}
          {columns.map(column => (
            <col key={column.id} style={column.numeric && { width: 58 }} />
          ))}
        </colgroup>
        <AdvancedTableHead
          columns={columns}
          detailed={detailed}
          selectable={selectable}
          indeterminate={numSelected > 0 && numSelected < numRows}
          checked={numSelected > 0 && numSelected === numRows}
          sortDirection={sortDirection}
          sortColumn={sortColumn}
          onSelect={onSelectRows(rows.map(row => extractId(rowId, row)))}
          onSortChange={onSortChange}
        />

        <AdvancedTableBody
          columns={columns}
          rows={rows}
          detailed={detailed}
          selectable={selectable}
          pageSize={pageSize}
          renderPanel={renderPanel}
          selectedRows={selectedRows}
          onSelectRow={onSelectRow}
        />

        <AdvancedTableFooter
          columns={columns}
          detailed={detailed}
          selectable={selectable}
          pageSize={pageSize}
          totalSize={totalSize}
          pageNumber={pageNumber}
          rowsPerPageOptions={rowsPerPageOptions}
          onPageChange={onPageChange}
          onPageSizeChange={onPageSizeChange}
        />
      </Table>
      <Fade in={loading} unmountOnExit>
        <Loading classes={{ root: classes.loadingContainer, icon: classes.loadingIcon}} />
      </Fade>
    </Paper>
  );
};

const ComposedList = enhance(List);

export default ComposedList;