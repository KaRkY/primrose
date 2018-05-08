import React from "react";
import PropTypes from "prop-types";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import withProps from "recompose/withProps";
import withHandlers from "recompose/withHandlers";
import defaultProps from "recompose/defaultProps";

import normalizeArray from "../../util/normalizeArray";
import difference from "lodash/difference";
import union from "lodash/union";
import identity from "lodash/identity";

import DataGrid from "./DataGrid";
import Paper from "material-ui/Paper";
import Toolbar from "material-ui/Toolbar";
import PersonAddIcon from "@material-ui/icons/PersonAdd";
import DeleteIcon from "@material-ui/icons/Delete";
import EditIcon from "@material-ui/icons/Edit";
import SendIcon from "@material-ui/icons/Send";
import ZoomInIcon from "@material-ui/icons/ZoomIn";
import IconButton from "material-ui/IconButton";
import Tooltip from "material-ui/Tooltip";
import Search from "../Search";

const styles = theme => ({
  root: {
    position: "relative",
    marginTop: theme.spacing.unit * 3,
  },

  grow: {
    flex: "1 1 auto",
  },
});

const enhance = compose(
  defaultProps({
    rows: [],
    columns: [],
    getRowId: row => row.id,
    pagination: {},
    rowsPerPage: [5, 10, 25],
    totalSize: 0,
    onPaged: identity,
    newTooltip: "New",
    sendSingleTooltip: "Send",
    sendMultiTooltip: "Send",
    deactivateMultiTooltip: "Deactivate",
    deactivateSingleTooltip: "Deactivate",
    editTooltip: "Edit",
    openTooltip: "Open",
    selectMultiTooltip: "Select",
    selectSingleTooltip: "Select",
  }),
  withProps(({onNew, onOpen, onEdit, onDeactivate, onSend }) => ({
    hasNew: !!onNew,
    hasOpen: !!onOpen,
    hasEdit: !!onEdit,
    hasDeactivate: !!onDeactivate,
    hasSend: !!onSend,
  })),
  withHandlers({
    onPageChange: ({ pagination, onPaged }) => (event, page) => onPaged({
      ...pagination,
      page,
    }),
    onPageSizeChange: ({ pagination, onPaged }) => (event, size) => onPaged({
      ...pagination,
      size,
    }),
    onSortChange: ({ pagination, onPaged }) => (event, sortProperty, direction) => onPaged({
      ...pagination,
      sortProperty,
      sortDirection: direction,
    }),
    onQueryChange: ({ pagination, onPaged }) => (event, value) => onPaged({
      ...pagination,
      pagination: value ? value : undefined,
    }),
    onSelectRowsChange: ({ pagination, onPaged }) => (event, value, checked) => onPaged({
      ...pagination,
      selected: (checked ? union(normalizeArray(pagination.selected), value) : difference(normalizeArray(pagination.selected), value)),
    }),
    onNew: ({ onNew }) => (event) => onNew && onNew(),
    onOpen: ({ onOpen }) => (event, value) => onOpen && onOpen(value),
    onEdit: ({ onEdit }) => (event, value) => onEdit && onEdit(value),
    onDeactivate: ({ pagination, onPaged, onDeactivate }) => (event, values) => onDeactivate && Promise.all([onDeactivate(values)])
      .then(result => onPaged({ ...pagination, selected: undefined, force: true }))
      .catch(console.log)
  }),
  withStyles(styles),
);

const SelectSearchList = ({
  classes,
  rows,
  columns,
  getRowId,
  select,
  search,
  pagination,
  rowsPerPage,
  totalSize,
  newTooltip,
  sendSingleTooltip,
  sendMultiTooltip,
  deactivateMultiTooltip,
  deactivateSingleTooltip,
  editTooltip,
  openTooltip,
  selectMultiTooltip,
  selectSingleTooltip,
  isDeleting, // Implement this
  onSelectRowsChange,
  onQueryChange,
  onPageChange,
  onPageSizeChange,
  onSortChange,
  onOpen,
  onNew,
  onEdit,
  onDeactivate,
  onSend,
  hasOpen,
  hasNew,
  hasEdit,
  hasDeactivate,
  hasSend,
}) => {
  const { page = 5, size = 5, query = "", selected = [], sort } = pagination;
  return (
    <React.Fragment>
      {search && <Search
        onSearch={onQueryChange}
        value={query}
      />}
      <Paper className={classes.root}>
        {(hasNew || hasSend || hasDeactivate) && <Toolbar>
          <div className={classes.grow} />

          {hasNew &&
            <Tooltip
              title={newTooltip}
              enterDelay={300}
            >
              <IconButton onClick={onNew}>
                <PersonAddIcon />
              </IconButton>
            </Tooltip>
          }

          {hasSend && selected.length > 0 &&
            <Tooltip
              title={sendMultiTooltip}
              enterDelay={300}
            >
              <IconButton onClick={event => onSend(event, rows.filter(row => selected.find(sel => getRowId(row) === sel) !== undefined))}>
                <SendIcon />
              </IconButton>
            </Tooltip>
          }

          {hasDeactivate && selected.length > 0 &&
            <Tooltip
              title={deactivateMultiTooltip}
              enterDelay={300}
            >
              <IconButton disabled={isDeleting} onClick={event => onDeactivate(event, selected)}>
                <DeleteIcon />
              </IconButton>
            </Tooltip>
          }
        </Toolbar>}
        <DataGrid
          getRowId={getRowId}
          rows={rows}
          columns={columns}
          pagination={{
            totalSize,
            page,
            size,
            onPageChange,
            onPageSizeChange,
            rowsPerPageOptions: rowsPerPage,
          }}
          sorting={sort}
          selecting={(hasSend || hasDeactivate) ? {
            rowIds: selected,
            onSelectRowsChange,
          } : undefined}
          rowActions={(hasSend || hasOpen || hasEdit || hasDeactivate) ? row => (
            <React.Fragment>
              {hasSend &&
                <Tooltip
                  title={sendSingleTooltip}
                  enterDelay={300}
                >
                  <IconButton onClick={event => onSend(event, row)}>
                    <SendIcon />
                  </IconButton>
                </Tooltip>
              }
              {hasOpen &&
                <Tooltip
                  title={openTooltip}
                  enterDelay={300}
                >
                  <IconButton onClick={event => onOpen(event, getRowId(row))}>
                    <ZoomInIcon />
                  </IconButton>
                </Tooltip>
              }
              {hasEdit &&
                <Tooltip
                  title={editTooltip}
                  enterDelay={300}
                >
                  <IconButton onClick={event => onEdit(event, getRowId(row))}>
                    <EditIcon />
                  </IconButton>
                </Tooltip>
              }
              {hasDeactivate &&
                <Tooltip
                  title={deactivateSingleTooltip}
                  enterDelay={300}
                >
                  <IconButton onClick={event => onDeactivate(event, getRowId(row))}>
                    <DeleteIcon />
                  </IconButton>
                </Tooltip>
              }
            </React.Fragment>
          ) : undefined} />
      </Paper>
    </React.Fragment>
  );
};

const ComposedSelectSearchList = enhance(SelectSearchList);
ComposedSelectSearchList.propTypes = {
  rows: PropTypes.array.isRequired,
  search: PropTypes.bool,

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
  select: PropTypes.bool,

  pagination: PropTypes.shape({
    page: PropTypes.number.isRequired,
    size: PropTypes.number.isRequired,
  }).isRequired,

  rowsPerPage: PropTypes.arrayOf(PropTypes.number.isRequired),
  totalSize: PropTypes.number.isRequired,

  onPaged: PropTypes.func.isRequired,
  onOpen: PropTypes.func,
  onNew: PropTypes.func,
  onEdit: PropTypes.func,
  onDeactivate: PropTypes.func,
  onSend: PropTypes.func,

  newTooltip: PropTypes.string,
  deactivateMultiTooltip: PropTypes.string,
  deactivateSingleTooltip: PropTypes.string,
  editTooltip: PropTypes.string,
  openTooltip: PropTypes.string,
  selectMultiTooltip: PropTypes.string,
  selectSingleTooltip: PropTypes.string,
};

export default ComposedSelectSearchList;