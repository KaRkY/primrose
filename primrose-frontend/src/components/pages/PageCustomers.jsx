import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";
import getCustomersPage from "../../selectors/getCustomersPage";
import getQuery from "../../selectors/getQuery";
import getSelected from "../../selectors/getSelected";
import getCustomersTotalSize from "../../selectors/getCustomersTotalSize";
import * as actions from "../../actions";
import normalizeArray from "../../util/normalizeArray";
import difference from "lodash/difference";
import union from "lodash/union";

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
  customers: getCustomersPage(state, props),
  query: getQuery(state, props),
  totalSize: getCustomersTotalSize(state, props),
  selectedRows: getSelected(state, props)
});

const mapDispatchTo = ({
  goToCustomers: actions.goToCustomers,
  goToCustomerNew: actions.goToCustomerNew,
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withHandlers({
    onPageChange: ({ query, goToCustomers }) => (event, page) => goToCustomers({ ...query, page }),
    onPageSizeChange: ({ query, goToCustomers }) => (event, size) => goToCustomers({ ...query, size }),
    onSortChange: ({ query, goToCustomers }) => (event, property, direction) => goToCustomers({ ...query, sortProperty: property, sortDirection: uppercase(direction)}),
    onSelectedRowsChange: ({ query, goToCustomers }) => (event, value, checked) => goToCustomers({
      ...query,
      selected: (checked ? union(normalizeArray(query.selected), value) : difference(normalizeArray(query.selected), value))
    }),
    onNewCustomer: ({ goToCustomerNew }) => (event) => goToCustomerNew && goToCustomerNew(),
    onEditCustomer: ({ router, onEditCustomer }) => (id) => onEditCustomer && onEditCustomer(router, id),
  }),
  withStyles(contentStyle)
);

const getRowId = row => row.id;



const Content = ({
  classes,
  customers,
  query,
  totalSize,
  selectedRows,
  isDeleting,
  onSelectedRowsChange,
  onPageChange,
  onPageSizeChange,
  onSortChange,
  onNewCustomer,
  onEditCustomer,
  deleteCustomers,
}) => (
    <Paper className={classes.root}>
      <Toolbar>
        <div className={classes.grow} />
        <Tooltip
          title="New Customer"
          enterDelay={300}
        >
          <IconButton onClick={onNewCustomer}>
            <PersonAddIcon />
          </IconButton>
        </Tooltip>
        {selectedRows && selectedRows.length > 0 && (
          <Tooltip
            title="Delete Customers"
            enterDelay={300}
          >
            <IconButton disabled={isDeleting} onClick={() => deleteCustomers()}>
              <DeleteIcon />
            </IconButton>
          </Tooltip>
        )}
      </Toolbar>
      <DataGrid
        getRowId={getRowId}
        rows={customers || []}
      >

        <DataGrid.Columns>
          <DataGrid.Column name="relationType" title="Relation type" />
          <DataGrid.Column name="type" title="Type" />
          <DataGrid.Column name="fullName" title="Full name" />
          <DataGrid.Column name="displayName" title="Display name" />
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
              <IconButton onClick={() => onEditCustomer(row.id)}>
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