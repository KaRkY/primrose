import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";
import * as actions from "../../actions";
import normalizeArray from "../../util/normalizeArray";
import difference from "lodash/difference";
import union from "lodash/union";
import getData from "../../selectors/contacts/getData";
import getCount from "../../selectors/contacts/getCount";
import getCurrentPage from "../../selectors/getCurrentPage";
import getCurrentSize from "../../selectors/getCurrentSize";
import getCurrentSortProperty from "../../selectors/getCurrentSortProperty";
import getCurrentSortDirection from "../../selectors/getCurrentSortDirection";
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
  contacts: getData(state, props),
  page: getCurrentPage(state, props),
  size: getCurrentSize(state, props),
  sortProperty: getCurrentSortProperty(state, props),
  sortDirection: getCurrentSortDirection(state, props),
  selected: getSelected(state, props),
  totalSize: getCount(state, props),
});

const mapDispatchTo = dispatch => ({
  goToContacts: payload => dispatch(actions.contacts(payload)),
  goToContact: payload => dispatch(actions.contact(payload)),
  goToNewContact: payload => dispatch(actions.contactNew(payload)),
  goToEditContact: payload => dispatch(actions.contactEdit(payload)),
  executeDeleteContact: payload => dispatch(actions.contactDelete(payload)),
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
        sortDirection: uppercase(direction),
      }
    }),
    onSelectedRowsChange: ({ query, selected, goToContacts }) => (event, value, checked) => goToContacts({
      query: {
        ...query,
        selected: (checked ? union(normalizeArray(selected), value) : difference(normalizeArray(selected), value)),
      }
    }),
    onNewContact: ({ goToContactNew }) => (event) => goToContactNew && goToContactNew(),
    onOpenContact: ({ goToContact }) => (event, id) => goToContact && goToContact({ id }),
    onEditContact: ({ goToEditContact }) => (event, id) => goToEditContact && goToEditContact({ id }),
    onDeleteContacts: ({ query, executeDeleteContacts }) => (event, contacts) => executeDeleteContacts && executeDeleteContacts({
      contacts,
      query,
    }),
  }),
  withStyles(contentStyle)
);

const getRowId = row => row.id;



const Content = ({
  classes,
  contacts,
  page,
  size,
  sortProperty,
  sortDirection,
  totalSize,
  selected,
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
        {selected && selected.length > 0 && (
          <Tooltip
            title="Delete Contacts"
            enterDelay={300}
          >
            <IconButton disabled={isDeleting} onClick={() => onDeleteContacts(selected)}>
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
          page={page}
          size={size}
          onPageChange={onPageChange}
          onPageSizeChange={onPageSizeChange}
        />

        <DataGrid.Sorting
          sort={sortProperty && {
            column: sortProperty,
            direction: lowercase(sortDirection),
          }}
          onSortChange={onSortChange}
        />

        <DataGrid.Selecting
          rowIds={selected || []}
          onSelectRowsChange={onSelectedRowsChange}
        />

        <DataGrid.RowActions>{row => (
          <React.Fragment>
            <Tooltip
              title="Open Contact"
              enterDelay={300}
            >
              <IconButton onClick={event => onOpenContact(event, row.id)}>
                <ZoomInIcon />
              </IconButton>
            </Tooltip>
            <Tooltip
              title="Edit Contact"
              enterDelay={300}
            >
              <IconButton onClick={event => onEditContact(event, row.id)}>
                <EditIcon />
              </IconButton>
            </Tooltip>
            <Tooltip
              title="Delete Contact"
              enterDelay={300}
            >
              <IconButton onClick={event => onDeleteContacts(event, [row.id])}>
                <DeleteIcon />
              </IconButton>
            </Tooltip>
          </React.Fragment>
        )}</DataGrid.RowActions>
      </DataGrid>
    </Paper >
  );

export default enhance(Content);