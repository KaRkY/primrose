import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";
import normalizeArray from "../../../util/normalizeArray";
import difference from "lodash/difference";
import union from "lodash/union";
import * as actions from "../../../actions";
import * as location from "../../../store/location";
import contacts from "../../../store/contacts";

import DataGrid from "../../Data/ChildConfigDataGrid";
import Paper from "material-ui/Paper";
import Toolbar from "material-ui/Toolbar";
import PersonAddIcon from "@material-ui/icons/PersonAdd";
import DeleteIcon from "@material-ui/icons/Delete";
import EditIcon from "@material-ui/icons/Edit";
import ZoomInIcon from "@material-ui/icons/ZoomIn";
import IconButton from "material-ui/IconButton";
import Tooltip from "material-ui/Tooltip";
import Search from "../../Search";

const contentStyle = theme => ({
  root: {
    position: "relative",
    marginTop: theme.spacing.unit * 3,
  },

  grow: {
    flex: "1 1 auto",
  },

  detailPanel: theme.mixins.gutters({
    position: "relative",
    margin: theme.spacing.unit
  }),

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

const mapState = (state, props) => ({
  contacts: contacts.paged.getData(state),
  pagination: location.getCurrentPagination(state),
  totalSize: contacts.paged.getCount(state),
  query: location.getCurrentQuery(state),
});

const mapDispatchTo = dispatch => ({
  handlePaged: payload => dispatch(actions.contactsPage(payload)),
  handleSingle: payload => dispatch(actions.contactPage(payload)),
  handleNew: payload => dispatch(actions.contactPageNew(payload)),
  handleEdit: payload => dispatch(actions.contactPageEdit(payload)),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withHandlers({
    onPageChange: ({ query, handlePaged }) => (event, page) => handlePaged({
      ...query,
      page,
    }),
    onPageSizeChange: ({ query, handlePaged }) => (event, size) => handlePaged({
      ...query,
      size,
    }),
    onSortChange: ({ query, handlePaged }) => (event, sortProperty, direction) => handlePaged({
      ...query,
      sortProperty,
      sortDirection: direction,
    }),
    onQueryChange: ({ query, handlePaged }) => (event, value) => handlePaged({
      ...query,
      query: value ? value : undefined,
    }),
    onSelectedRowsChange: ({ query, pagination, handlePaged }) => (event, value, checked) => handlePaged({
      ...query,
      selected: (checked ? union(normalizeArray(pagination.selected), value) : difference(normalizeArray(pagination.selected), value)),
    }),
    onNew: ({ handleNew }) => (event) => handleNew(),
    onOpen: ({ handleSingle }) => (event, value) => handleSingle(value),
    onEdit: ({ handleEdit }) => (event, value) => handleEdit(value),
    onDelete: ({ pagination, handlePaged, query }) => (event, values) => actions.contactDeletePromise(values)
      .then(result => handlePaged({ ...query, selected: undefined, force: true }))
      .catch(console.log)
  }),
  withStyles(contentStyle)
);

const getRowId = row => row.code;

const Content = ({
  classes,
  contacts,
  pagination,
  totalSize,
  isDeleting,
  query,
  onSelectedRowsChange,
  onQueryChange,
  onPageChange,
  onPageSizeChange,
  onSortChange,
  onNew,
  onOpen,
  onEdit,
  onDelete,
}) => (
    <React.Fragment>
      <Search
        onSearch={onQueryChange}
        value={query && query.query}
      />
      <Paper className={classes.root}>
        <Toolbar>
          <div className={classes.grow} />
          <Tooltip
            title="New Contact"
            enterDelay={300}
          >
            <IconButton onClick={onNew}>
              <PersonAddIcon />
            </IconButton>
          </Tooltip>
          {pagination.selected && pagination.selected.length > 0 && (
            <Tooltip
              title="Delete Contacts"
              enterDelay={300}
            >
              <IconButton disabled={isDeleting} onClick={event => onDelete(event, pagination.selected)}>
                <DeleteIcon />
              </IconButton>
            </Tooltip>
          )}
        </Toolbar>
        <DataGrid
          getRowId={getRowId}
          rows={contacts || []}
        >

          <DataGrid.Columns>
            <DataGrid.Column name="code" title="Code" />
            <DataGrid.Column name="fullName" title="Name" />
            <DataGrid.Column name="primaryEmail" title="Email" />
            <DataGrid.Column name="primaryPhone" title="Phone" />
          </DataGrid.Columns>

          <DataGrid.Pagination
            totalSize={totalSize}
            page={pagination.page}
            size={pagination.size}
            onPageChange={onPageChange}
            onPageSizeChange={onPageSizeChange}
          />

          <DataGrid.Sorting
            sort={pagination.sort}
            onSortChange={onSortChange}
          />

          <DataGrid.Selecting
            rowIds={pagination.selected || []}
            onSelectRowsChange={onSelectedRowsChange}
          />

          <DataGrid.RowActions>{row => (
            <React.Fragment>
              <Tooltip
                title="Open Contact"
                enterDelay={300}
              >
                <IconButton onClick={event => onOpen(event, row.code)}>
                  <ZoomInIcon />
                </IconButton>
              </Tooltip>
              <Tooltip
                title="Edit Contact"
                enterDelay={300}
              >
                <IconButton onClick={event => onEdit(event, row.code)}>
                  <EditIcon />
                </IconButton>
              </Tooltip>
              <Tooltip
                title="Delete Contact"
                enterDelay={300}
              >
                <IconButton onClick={event => onDelete(event, row.code)}>
                  <DeleteIcon />
                </IconButton>
              </Tooltip>
            </React.Fragment>
          )}</DataGrid.RowActions>
        </DataGrid>
      </Paper >
    </React.Fragment>
  );

export default enhance(Content);