import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";
import * as actions from "../../actions";
import normalizeArray from "../../util/normalizeArray";
import difference from "lodash/difference";
import union from "lodash/union";
import getContactsPage from "../../selectors/contacts/getContactsPage";
import getContactsTotalSize from "../../selectors/contacts/getContactsTotalSize";
import getQuery from "../../selectors/getQuery";
import getSelected from "../../selectors/getSelected";

import DataGrid from "../Data/ChildConfigDataGrid";
import Paper from "material-ui/Paper";
import Toolbar from "material-ui/Toolbar";
import PersonAddIcon from "material-ui-icons/PersonAdd";
import DeleteIcon from "material-ui-icons/Delete";
import EditIcon from "material-ui-icons/Edit";
import ZoomInIcon from "material-ui-icons/ZoomIn";
import IconButton from "material-ui/IconButton";
import Tooltip from "material-ui/Tooltip";


const contentStyle = theme => ({
  root: {
    position: "relative",
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

const lowercase = (value) => {
  if (typeof value === "string") {
    return value.toLowerCase();
  } else {
    return value;
  }
};

const uppercase = (value) => {
  if (typeof value === "string") {
    return value.toUpperCase();
  } else {
    return value;
  }
};

const mapState = (state, props) => ({
  contacts: getContactsPage(state, props),
  query: getQuery(state, props),
  totalSize: getContactsTotalSize(state, props),
  selectedRows: getSelected(state, props)
});

const mapDispatchTo = ({
  goToContacts: actions.goToContacts,
  goToNewContact: actions.goToNewContact,
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withHandlers({
    onPageChange: ({ query, goToContacts }) => (event, page) => goToContacts({ ...query, page }),
    onPageSizeChange: ({ query, goToContacts }) => (event, size) => goToContacts({ ...query, size }),
    onSortChange: ({ query, goToContacts }) => (event, property, direction) => goToContacts({ ...query, sortProperty: property, sortDirection: uppercase(direction)}),
    onSelectedRowsChange: ({ query, goToContacts }) => (event, value, checked) => goToContacts({
      ...query,
      selected: (checked ? union(normalizeArray(query.selected), value) : difference(normalizeArray(query.selected), value))
    }),
    onNewContact: ({ goToNewContact }) => (event) => goToNewContact && goToNewContact(),
  }),
  withStyles(contentStyle)
);

const getRowId = row => row.id;



const Content = ({
  classes,
  contacts,
  query,
  totalSize,
  selectedRows,
  isDeleting,
  onSelectedRowsChange,
  onPageChange,
  onPageSizeChange,
  onSortChange,
  onNewContact,
  deleteContacts,
}) => (
    <Paper className={classes.root}>
      <Toolbar>
        <div className={classes.grow} />
        <Tooltip
          title="New Customer"
          enterDelay={300}
        >
          <IconButton onClick={onNewContact}>
            <PersonAddIcon />
          </IconButton>
        </Tooltip>
        {selectedRows && selectedRows.length > 0 && (
          <Tooltip
            title="Delete Contacts"
            enterDelay={300}
          >
            <IconButton disabled={isDeleting} onClick={() => deleteContacts()}>
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
          <DataGrid.Column name="name" title="Name" width="40%" />
          <DataGrid.Column name="primaryEmail" title="Email" width="30%" />
          <DataGrid.Column name="primaryPhone" title="Phone" width="28%" />
        </DataGrid.Columns>

        <DataGrid.Pagination
          totalSize={totalSize}
          page={parseInt(query.page, 10)}
          size={parseInt(query.size, 10)}
          onPageChange={onPageChange}
          onPageSizeChange={onPageSizeChange}
        />

        <DataGrid.Sorting
          sort={query.sortProperty && {
            column: query.sortProperty,
            direction: lowercase(query.sortDirection),
          }}
          onSortChange={onSortChange}
        />

        <DataGrid.Selecting
          rowIds={selectedRows || []}
          onSelectRowsChange={onSelectedRowsChange}
        />

        <DataGrid.RowActions>{row => (
          <React.Fragment>
            <Tooltip
              title="Open Customer"
              enterDelay={300}
            >
              <IconButton>
                <ZoomInIcon />
              </IconButton>
            </Tooltip>
            <Tooltip
              title="Edit Customer"
              enterDelay={300}
            >
              <IconButton onClick={() => console.log(row)}>
                <EditIcon />
              </IconButton>
            </Tooltip>
            <Tooltip
              title="Delete Customer"
              enterDelay={300}
            >
              <IconButton>
                <DeleteIcon />
              </IconButton>
            </Tooltip>
          </React.Fragment>
        )}</DataGrid.RowActions>
      </DataGrid>
    </Paper >
  );

export default enhance(Content);