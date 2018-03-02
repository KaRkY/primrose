import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { withStyles } from "material-ui/styles";

import { Post } from "react-axios";
import gql from "graphql-tag";

import DataGrid from "../Data/DataGrid";
import Paper from "material-ui/Paper";
import Fade from "material-ui/transitions/Fade";
import Loading from "../Loading";
import Toolbar from "material-ui/Toolbar";
import PersonAddIcon from "material-ui-icons/PersonAdd";
import DeleteIcon from "material-ui-icons/Delete";
import EditIcon from "material-ui-icons/Edit";
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

const enhance = compose(
  withHandlers({
    onPageChange: ({ router, onPageChange }) => (event, page) => onPageChange && onPageChange(router, page),
    onPageSizeChange: ({ router, onPageSizeChange }) => (event, size) => onPageSizeChange && onPageSizeChange(router, size),
    onSortChange: ({ router, onSortChange }) => (event, property) => onSortChange && onSortChange(router, property),
    onSelectedRowsChange: ({ router, onSelectedRowsChange }) => (event, value, checked) => onSelectedRowsChange && onSelectedRowsChange(router, value, checked),
    onNewCustomer: ({ router, onNewCustomer }) => (event) => onNewCustomer && onNewCustomer(router),
  }),

  withStyles(contentStyle)
);

const getRowId = row => row.id;

export const loadCustomers = gql`
  query loadCustomers($pageable: Pageable, $sort: [PropertySort]) {
    customers(pageable: $pageable, sort: $sort) {
      id
      type
      relationType
      fullName
      displayName
    }
    customersCount
  }
`;

export const deleteCustomers = gql`
  mutation deleteCustomers($ids: [ID]!) {
    deleteCustomers(ids: $ids)
  }
`;

const parseDirection = (dir) => {
  if (dir && (dir.toUpperCase() === "ASC" || dir.toUpperCase() === "DESC" || dir.toUpperCase() === "DEFAULT")) {
    return dir.toUpperCase();
  }
};

const Content = ({
  classes,
  pageNumber,
  pageSize,
  totalSize,
  sortProperty,
  sortDirection,
  selectedRows,
  onSelectedRowsChange,
  onPageChange,
  onPageSizeChange,
  onSortChange,
  onNewCustomer, }) => (
    <Post data={{
      query: loadCustomers.loc.source.body,
      variables: {
        pageable: {
          pageNumber,
          pageSize
        },
        sort: (sortProperty && [{
          propertyName: sortProperty,
          direction: parseDirection(sortDirection)
        }]) || []
      }
    }}>{(customersErros, customersResponse, isCustomersLoading, reloadCustomers) => (
      <Post data={{
        query: deleteCustomers.loc.source.body,
        variables: {
          ids: selectedRows
        }
      }}
        debounce={500}
        debounceImmediate={true}
        onSuccess={() => reloadCustomers()}
        isReady={false}>{(deleteError, deleteResponse, isDeleting, deleteCustomers) => (
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
              rowId={getRowId}
              rows={(customersResponse && customersResponse.data && customersResponse.data.data.customers) || []}
            >
              <DataGrid.Columns>
                {[
                  { id: "type", label: "Type" },
                  { id: "relationType", label: "Relation type" },
                  { id: "fullName", label: "Full name" },
                  { id: "displayName", label: "Display name" },
                ]}
              </DataGrid.Columns>

              {/* 
                Set on page change listener after data has been loaded 
                Pagination has a rule that it fires page change to valid page
                so it must be ignored. Maybe set page 0 until data is loaded that might help.
              */}
              <DataGrid.Pagination
                totalSize={(customersResponse && customersResponse.data && customersResponse.data.data.customersCount)}
                pageNumber={pageNumber}
                pageSize={pageSize}
                onPageChange={onPageChange}
                onPageSizeChange={onPageSizeChange}
              />

              <DataGrid.Sortable
                sortColumn={sortProperty}
                sortDirection={sortDirection && sortDirection.toLowerCase()}
                onSortChange={onSortChange}
              />

              <DataGrid.Selectable
                selectedRows={selectedRows}
                onSelectRows={onSelectedRowsChange}
              />

              <DataGrid.RowActions>{row => (
                <React.Fragment>
                  <IconButton>
                    <EditIcon />
                  </IconButton>
                  <IconButton>
                    <DeleteIcon />
                  </IconButton>
                </React.Fragment>
              )}</DataGrid.RowActions>
            </DataGrid>

            <Fade in={isCustomersLoading} unmountOnExit>
              <Loading classes={{ root: classes.loadingContainer, icon: classes.loadingIcon }} />
            </Fade>
          </Paper >
        )}</Post>
    )}</Post>
  );

export default enhance(Content);