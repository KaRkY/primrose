import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { withStyles } from "material-ui/styles";

import { Post } from "react-axios";
import gql from "graphql-tag";

import DataGrid from "../Grid/Grid";
import Paper from "material-ui/Paper";
import Grid from "material-ui/Grid";
import Typography from "material-ui/Typography";
import Fade from "material-ui/transitions/Fade";
import Loading from "../Loading";
import Toolbar from "material-ui/Toolbar";
import PersonAddIcon from "material-ui-icons/PersonAdd";
import DeleteIcon from "material-ui-icons/Delete";
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
    onPanelsOpenChange: ({ router, onPanelsOpenChange }) => (event, value, open) => onPanelsOpenChange && onPanelsOpenChange(router, value, open),
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

export const loadCustomer = gql`
  query loadCustomer($id: ID!) {
    customer(id: $id) {
      id
      type
      relationType
      fullName
      displayName
      email
      phone
      description
    }
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
  openPanels,
  onSelectedRowsChange,
  onPanelsOpenChange,
  onPageChange,
  onPageSizeChange,
  onSortChange }) => (
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
                <IconButton>
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

              <DataGrid.RenderPanel
                openPanels={openPanels}
                onOpenPanels={onPanelsOpenChange}
                render={(row) => (
                  <Paper className={classes.detailPanel}>
                    <Post data={{
                      query: loadCustomer.loc.source.body,
                      variables: {
                        id: row.id
                      }
                    }}>{(customerError, customerResponse, isCustomerLoading, reloadCustomer) => (
                      <React.Fragment>
                        <Grid container>
                          <Grid item xs={2}><Typography variant="body2">Display name:</Typography></Grid>
                          <Grid item><Typography variant="body2">{customerResponse && customerResponse.data && customerResponse.data.data.customer.displayName}</Typography></Grid>
                        </Grid>
                        <Grid container>
                          <Grid item xs={2}><Typography variant="body2">Full name:</Typography></Grid>
                          <Grid item><Typography variant="body2">{customerResponse && customerResponse.data && customerResponse.data.data.customer.fullName}</Typography></Grid>
                        </Grid>
                        <Fade in={isCustomerLoading} unmountOnExit>
                          <Loading classes={{ root: classes.loadingContainer, icon: classes.loadingIcon }} />
                        </Fade>
                      </React.Fragment>
                    )}</Post>
                  </Paper>
                )}
              />
            </DataGrid>
            <Fade in={isCustomersLoading} unmountOnExit>
              <Loading classes={{ root: classes.loadingContainer, icon: classes.loadingIcon }} />
            </Fade>
          </Paper >
        )}</Post>
    )}</Post>
  );

export default enhance(Content);