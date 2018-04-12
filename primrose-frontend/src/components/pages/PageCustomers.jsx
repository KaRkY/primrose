import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";
import normalizeArray from "../../util/normalizeArray";
import difference from "lodash/difference";
import union from "lodash/union";
import * as actions from "../../actions";
import * as location from "../../store/location";
import * as customers from "../../store/customers";

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

const mapState = (state, props) => ({
  customers: customers.getData(state),
  pagination: location.getCurrentPagination(state),
  totalSize: customers.getCount(state),
  query: location.getCurrentQuery(state),
});

const mapDispatchTo = dispatch => ({
  goToCustomers: payload => dispatch(actions.customers(payload)),
  goToCustomer: payload => dispatch(actions.customer(payload)),
  goToNewCustomer: payload => dispatch(actions.customerNew(payload)),
  goToEditCustomer: payload => dispatch(actions.customerEdit(payload)),
  executeDeleteCustomer: payload => dispatch(actions.customerDelete(payload)),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withHandlers({
    onPageChange: ({ query, goToCustomers }) => (event, page) => goToCustomers({
      query: {
        ...query,
        page,
      }
    }),
    onPageSizeChange: ({ query, goToCustomers }) => (event, size) => goToCustomers({
      query: {
        ...query,
        size,
      }
    }),
    onSortChange: ({ query, goToCustomers }) => (event, sortProperty, direction) => goToCustomers({
      query: {
        ...query,
        sortProperty,
        sortDirection: direction,
      }
    }),
    onSelectedRowsChange: ({ query, pagination, goToCustomers }) => (event, value, checked) => goToCustomers({
      query: {
        ...query,
        selected: (checked ? union(normalizeArray(pagination.selected), value) : difference(normalizeArray(pagination.selected), value)),
      }
    }),
    onNewCustomer: ({ goToNewCustomer }) => (event) => goToNewCustomer && goToNewCustomer(),
    onOpenCustomer: ({ goToCustomer }) => (event, id) => goToCustomer && goToCustomer({ id }),
    onEditCustomer: ({ goToEditCustomer }) => (event, id) => goToEditCustomer && goToEditCustomer({ id }),
    onDeleteCustomer: ({ query, executeDeleteCustomer }) => (event, customer) => executeDeleteCustomer && executeDeleteCustomer({
      customer,
      query,
    }),
  }),
  withStyles(contentStyle)
);

const getRowId = row => row.id;



const Content = ({
  classes,
  customers,
  pagination,
  totalSize,
  isDeleting,
  onSelectedRowsChange,
  onPageChange,
  onPageSizeChange,
  onSortChange,
  onNewCustomer,
  onOpenCustomer,
  onEditCustomer,
  onDeleteCustomer,
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
        {pagination.selected && pagination.selected.length > 0 && (
          <Tooltip
            title="Delete Customers"
            enterDelay={300}
          >
            <IconButton disabled={isDeleting} onClick={() => onPageChange(pagination.page)}>
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
              title="Open Customer"
              enterDelay={300}
            >
              <IconButton onClick={event => onOpenCustomer(event, row.id)}>
                <ZoomInIcon />
              </IconButton>
            </Tooltip>
            <Tooltip
              title="Edit Customer"
              enterDelay={300}
            >
              <IconButton onClick={event => onEditCustomer(event, row.id)}>
                <EditIcon />
              </IconButton>
            </Tooltip>
            <Tooltip
              title="Delete Customer"
              enterDelay={300}
            >
              <IconButton onClick={event => onDeleteCustomer(event, row.id)}>
                <DeleteIcon />
              </IconButton>
            </Tooltip>
          </React.Fragment>
        )}</DataGrid.RowActions>
      </DataGrid>
    </Paper >
  );

export default enhance(Content);