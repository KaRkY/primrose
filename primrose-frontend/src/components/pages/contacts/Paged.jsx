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

const mapState = (state, props) => ({
  contacts: contacts.paged.getData(state),
  pagination: location.getCurrentPagination(state),
  totalSize: contacts.paged.getCount(state),
  query: location.getCurrentQuery(state),
});

const mapDispatchTo = dispatch => ({
  goToContacts: payload => dispatch(actions.contactsPage(payload)),
  goToContact: payload => dispatch(actions.contactPage(payload)),
  goToNewContact: payload => dispatch(actions.contactPageNew(payload)),
  goToEditContact: payload => dispatch(actions.contactPageEdit(payload)),
  executeDeleteContact: payload => dispatch(actions.contactsDelete(payload)),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withHandlers({
    onPageChange: ({ query, goToContacts }) => (event, page) => goToContacts({
      query: {
        ...query,
        page,
      }
    }),
    onPageSizeChange: ({ query, goToContacts }) => (event, size) => goToContacts({
      query: {
        ...query,
        size,
      }
    }),
    onSortChange: ({ query, goToContacts }) => (event, sortProperty, direction) => goToContacts({
      query: {
        ...query,
        sortProperty,
        sortDirection: direction,
      }
    }),
    onSelectedRowsChange: ({ query, pagination, goToContacts }) => (event, value, checked) => goToContacts({
      query: {
        ...query,
        selected: (checked ? union(normalizeArray(pagination.selected), value) : difference(normalizeArray(pagination.selected), value)),
      }
    }),
    onNewContact: ({ goToNewContact }) => (event) => goToNewContact && goToNewContact(),
    onOpenContact: ({ goToContact }) => (event, contact) => goToContact && goToContact({ contact }),
    onEditContact: ({ goToEditContact }) => (event, contact) => goToEditContact && goToEditContact({ contact }),
    onDeleteContacts: ({ query, executeDeleteContacts }) => (event, contacts) => executeDeleteContacts && executeDeleteContacts({
      contacts,
      query,
    }),
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
  onSelectedRowsChange,
  onPageChange,
  onPageSizeChange,
  onSortChange,
  onNewContact,
  onOpenContact,
  onEditContact,
  onDeleteContacts,
}) => (
    <Paper className={classes.root}>
      <Toolbar>
        <div className={classes.grow} />
        <Tooltip
          title="New Contact"
          enterDelay={300}
        >
          <IconButton onClick={onNewContact}>
            <PersonAddIcon />
          </IconButton>
        </Tooltip>
        {pagination.selected && pagination.selected.length > 0 && (
          <Tooltip
            title="Delete Contacts"
            enterDelay={300}
          >
            <IconButton disabled={isDeleting} onClick={() => onDeleteContacts(pagination.selected)}>
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
              <IconButton onClick={event => onOpenContact(event, row.code)}>
                <ZoomInIcon />
              </IconButton>
            </Tooltip>
            <Tooltip
              title="Edit Contact"
              enterDelay={300}
            >
              <IconButton onClick={event => onEditContact(event, row.code)}>
                <EditIcon />
              </IconButton>
            </Tooltip>
            <Tooltip
              title="Delete Contact"
              enterDelay={300}
            >
              <IconButton onClick={event => onDeleteContacts(event, row.code)}>
                <DeleteIcon />
              </IconButton>
            </Tooltip>
          </React.Fragment>
        )}</DataGrid.RowActions>
      </DataGrid>
    </Paper >
  );

export default enhance(Content);