import React from "react";
import compose from "recompose/compose";
import withState from "recompose/withState";
import withHandlers from "recompose/withHandlers";
import { withStyles } from "material-ui/styles";

import difference from "lodash/difference";
import union from "lodash/union";

import DataLoading from "../DataLoading";
import gql from "graphql-tag";

import DataGrid from "../Grid/Grid";
import Paper from "material-ui/Paper";
import Grid from "material-ui/Grid";
import Typography from "material-ui/Typography";
import LoadCustomer from "../customers/LoadCustomer";
import DeleteCustomers from "../customers/DeleteCustomers";
import Fade from "material-ui/transitions/Fade";
import Loading from "../Loading";
import IconButton from "material-ui/IconButton";
import DeleteForever from "material-ui-icons/DeleteForever";
import FileDownload from "material-ui-icons/FileDownload";
import Tooltip from "material-ui/Tooltip";
import { CircularProgress } from "material-ui/Progress";

const contentStyle = theme => ({
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
  withState("selectedRows", "setSelectedRows", []),

  withHandlers({
    onPageChange: ({ router, onPageChange }) => (event, page) => onPageChange && onPageChange(router, page),
    onPageSizeChange: ({ router, onPageSizeChange }) => (event, size) => onPageSizeChange && onPageSizeChange(router, size),
    onSortChange: ({ router, onSortChange }) => (event, property) => onSortChange && onSortChange(router, property),
    onSelectRows: ({ selectedRows, setSelectedRows, ...rest }) => (event, value, checked) => {
      setSelectedRows(checked ? union(selectedRows, value) : difference(selectedRows, value));
    },
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
  onSelectRows,
  onPageChange,
  onPageSizeChange,
  onSortChange }) => (
    <DataLoading
      url="http://localhost:9080/graphql/"
      method="post"
      data={{
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
      }}
      render={props => (
        <DeleteCustomers
          selectedRows={selectedRows}
          onSelectRows={onSelectRows}
          render={({ deleteCustomers, deleting }) => (
            <DataGrid
              loading={props.networkState === "loading"}
              rowId={getRowId}
              rows={(props.response && props.response.data && props.response.data.data.customers) || []}
            >
              <DataGrid.Title>Customers</DataGrid.Title>

              <DataGrid.Columns>
                {[
                  { id: "type", label: "Type" },
                  { id: "relationType", label: "Relation type" },
                  { id: "fullName", label: "Full name" },
                  { id: "displayName", label: "Display name" },
                ]}
              </DataGrid.Columns>

              <DataGrid.Pagination
                totalSize={(props.response && props.response.data && props.response.data.data.customersCount) || 0}
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
                onSelectRows={onSelectRows}
              />

              <DataGrid.Actions>
                {selectedRows && selectedRows.length > 0 && (
                  <React.Fragment>
                    <Tooltip title="Delete selected customers" enterDelay={300}>
                      <IconButton
                        disabled={deleting}
                        variant="raised"
                        color="secondary"
                        onClick={() => deleteCustomers(selectedRows)}>
                        {deleting ? <CircularProgress color="secondary" size={24} /> : <DeleteForever />}
                      </IconButton>
                    </Tooltip>
                    <Tooltip title="Download" enterDelay={300}>
                      <IconButton
                        variant="raised"
                        color="secondary">
                        <FileDownload />
                      </IconButton>
                    </Tooltip>
                  </React.Fragment>
                )}
              </DataGrid.Actions>

              <DataGrid.RenderPanel
                render={(row) => (
                  <Paper className={classes.detailPanel}>
                    <LoadCustomer
                      id={row.id}
                      render={({ customer, networkStatus }) => (
                        <React.Fragment>
                          <Grid container>
                            <Grid item xs={2}><Typography variant="body2">Display name:</Typography></Grid>
                            <Grid item><Typography variant="body2">{customer && customer.displayName}</Typography></Grid>
                          </Grid>
                          <Grid container>
                            <Grid item xs={2}><Typography variant="body2">Full name:</Typography></Grid>
                            <Grid item><Typography variant="body2">{customer && customer.fullName}</Typography></Grid>
                          </Grid>
                          <Fade in={[1, 2, 4, 6].indexOf(networkStatus) > -1} unmountOnExit>
                            <Loading classes={{ root: classes.loadingContainer, icon: classes.loadingIcon }} />
                          </Fade>
                        </React.Fragment>
                      )}
                    />
                  </Paper>
                )}
              />
            </DataGrid>
          )}
        />
      )} />
  );

export default enhance(Content);