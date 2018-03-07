import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";
import getCustomersPage from "../../selectors/getCustomersPage";
import getQuery from "../../selectors/getQuery";
import getSelected from "../../selectors/getSelected";
import getCustomersTotalSize from "../../selectors/getCustomersTotalSize";
import { bindActionCreators } from "redux";
import * as actions from "../../actions";
import normalizeArray from "../../util/normalizeArray";
import difference from "lodash/difference";
import union from "lodash/union";

import DataGrid from "../Data/DataGrid";
import DataList from "../Data/DataList";
import Paper from "material-ui/Paper";
import Fade from "material-ui/transitions/Fade";
import Loading from "../Loading";
import Toolbar from "material-ui/Toolbar";
import PersonAddIcon from "material-ui-icons/PersonAdd";
import DeleteIcon from "material-ui-icons/Delete";
import EditIcon from "material-ui-icons/Edit";
import ZoomInIcon from "material-ui-icons/ZoomIn";
import IconButton from "material-ui/IconButton";
import Tooltip from "material-ui/Tooltip";
import Hidden from "material-ui/Hidden";


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

const parseSort = (query, property) => {
  const prop = query && query.sortProperty;
  const dir = query && query.sortDirection && query.sortDirection.toUpperCase();

  let sortProperty;
  let sortDirection;
  if (prop === property) {
    sortProperty = prop;
    switch (dir) {
      case "ASC":
        sortDirection = "DESC";
        break;

      case "DESC":
        sortDirection = undefined;
        sortProperty = undefined;
        break;

      default:
        sortDirection = "ASC";
        break;
    }
  } else {
    sortProperty = property;
    sortDirection = "ASC";
  }

  return {
    sortProperty,
    sortDirection
  };
};

const mapState = (state, props) => ({
  customers: getCustomersPage(state, props),
  query: getQuery(state, props),
  totalSize: getCustomersTotalSize(state, props),
  selectedRows: getSelected(state, props)
});

const mapDispatchTo = ({
  goToCustomers: actions.goToCustomers,
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withHandlers({
    onPageChange: ({ query, goToCustomers }) => (event, page) => goToCustomers({ ...query, page }),
    onPageSizeChange: ({ query, goToCustomers }) => (event, size) => goToCustomers({ ...query, size }),
    onSortChange: ({ query, goToCustomers }) => (event, property) => goToCustomers({ ...query, ...parseSort(query, property) }),
    onSelectedRowsChange: ({ query, goToCustomers }) => (event, value, checked) => goToCustomers({
      ...query,
      selected: (checked ? union(normalizeArray(query.selected), value) : difference(normalizeArray(query.selected), value))
    }),
    onNewCustomer: ({ router, onNewCustomer }) => (event) => onNewCustomer && onNewCustomer(router),
    onEditCustomer: ({ router, onEditCustomer }) => (id) => onEditCustomer && onEditCustomer(router, id),
  }),
  withStyles(contentStyle)
);

const getRowId = row => row.id;

const columns = [
  { id: "type", label: "Type" },
  { id: "relationType", label: "Relation type" },
  { id: "fullName", label: "Full name" },
  { id: "displayName", label: "Display name" },
];

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
    <Hidden smDown>
      <DataGrid
        rowId={getRowId}
        rows={customers || []}
        columns={columns}
      >

        {/* 
                Set on page change listener after data has been loaded 
                Pagination has a rule that it fires page change to valid page
                so it must be ignored. Maybe set page 0 until data is loaded that might help.
              */}
        <DataGrid.Pagination
          totalSize={totalSize}
          pageNumber={parseInt(query.page, 10)}
          pageSize={parseInt(query.size, 10)}
          onPageChange={onPageChange}
          onPageSizeChange={onPageSizeChange}
        />

        <DataGrid.Sortable
          sortColumn={query.sortProperty}
          sortDirection={query.sortDirection && query.sortDirection.toLowerCase()}
          onSortChange={onSortChange}
        />

        <DataGrid.Selectable
          selectedRows={selectedRows}
          onSelectRows={onSelectedRowsChange}
        />

        <DataGrid.RowActions num={3}>{row => (
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
    </Hidden>

    <Hidden mdUp>
      <DataList
        rowId={getRowId}
        rows={customers || []}
        columns={columns}
        heading={row => row.displayName ? row.displayName : row.fullName}
      >

        {/* 
                Set on page change listener after data has been loaded 
                Pagination has a rule that it fires page change to valid page
                so it must be ignored. Maybe set page 0 until data is loaded that might help.
              */}
        <DataList.Pagination
          totalSize={totalSize}
          pageNumber={parseInt(query.page, 10)}
          pageSize={parseInt(query.size, 10)}
          onPageChange={onPageChange}
          onPageSizeChange={onPageSizeChange}
        />

        <DataList.PanelActions num={3}>{row => (
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
        )}</DataList.PanelActions>
      </DataList>
    </Hidden>
  </Paper >
);

export default enhance(Content);