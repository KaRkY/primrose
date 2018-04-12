import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";
import getData from "../../selectors/customers/getData";
import getCount from "../../selectors/customers/getCount";
import getCurrentQuery from "../../selectors/getCurrentQuery";
import getCurrentPage from "../../selectors/getCurrentPage";
import getCurrentSize from "../../selectors/getCurrentSize";
import getCurrentSortProperty from "../../selectors/getCurrentSortProperty";
import getCurrentSortDirection from "../../selectors/getCurrentSortDirection";
import getSelected from "../../selectors/getSelected";
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
  customers: getData(state, props),
  page: getCurrentPage(state, props),
  size: getCurrentSize(state, props),
  sortProperty: getCurrentSortProperty(state, props),
  sortDirection: getCurrentSortDirection(state, props),
  selected: getSelected(state, props),
  totalSize: getCount(state, props),
  query: getCurrentQuery(state, props),
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
        sortDirection: uppercase(direction),
      }
    }),
    onSelectedRowsChange: ({ query, selected, goToCustomers }) => (event, value, checked) => goToCustomers({
      query: {
        ...query,
        selected: (checked ? union(normalizeArray(selected), value) : difference(normalizeArray(selected), value)),
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
  onNewCustomer,
  onOpenCustomer,
  onEditCustomer,
  onDeleteCustomer,
  style,
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
        {selected && selected.length > 0 && (
          <Tooltip
            title="Delete Customers"
            enterDelay={300}
          >
            <IconButton disabled={isDeleting} onClick={() => onPageChange(page)}>
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