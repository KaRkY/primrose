import React from "react";
import compose from "recompose/compose";
import withProps from "recompose/withProps";
import withState from "recompose/withState";
import withHandlers from "recompose/withHandlers";
import { withStyles } from "material-ui/styles";

import get from "lodash/get";
import difference from "lodash/difference";
import union from "lodash/union";

import List from "../list/List";
import Paper from "material-ui/Paper";
import Grid from "material-ui/Grid";
import Typography from "material-ui/Typography";
import LoadCustomers from "../customers/LoadCustomers";
import LoadCustomer from "../customers/LoadCustomer";
import DeleteCustomers from "../customers/DeleteCustomers";
import Fade from "material-ui/transitions/Fade";
import Loading from "../Loading";

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
    onPageSizeChange: ({ router, onSizeChange }) => (event, size) => onSizeChange && onSizeChange(router, size),
    onSortChange: ({ router, onSortChange }) => (event, property) => onSortChange && onSortChange(router, property),
    onSelectRows: ({ selectedRows, setSelectedRows, ...rest }) => (event, value, checked) => {
      setSelectedRows(checked ? union(selectedRows, value) : difference(selectedRows, value));
    },
  }),

  withStyles(contentStyle)
);

const getRowId = row => row.id;

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
    <LoadCustomers
      pageNumber={pageNumber}
      pageSize={pageSize}
      sortProperty={sortProperty}
      sortDirection={sortDirection}
      render={({ customers, networkStatus, totalSize, error }) => (
        <DeleteCustomers
          selectedRows={selectedRows}
          onSelectRows={onSelectRows}
          render={({ deleteCustomers, deleting }) => (
            <List title="Customers"
              columns={[
                { id: "type", label: "Type" },
                { id: "relationType", label: "Relation type" },
                { id: "fullName", label: "Full name" },
                { id: "displayName", label: "Display name" },
              ]}
              selectable
              detailed
              loading={[1, 2, 4, 6].indexOf(networkStatus) > -1}
              deleting={deleting}
              rowId={getRowId}
              rows={customers || []}
              totalSize={totalSize || 0}
              pageNumber={pageNumber}
              pageSize={pageSize}
              selectedRows={selectedRows}
              sortColumn={sortProperty}
              sortDirection={sortDirection && sortDirection.toLowerCase()}
              onSelectRows={onSelectRows}
              onPageChange={onPageChange}
              onPageSizeChange={onPageSizeChange}
              onSortChange={onSortChange}
              onDelete={deleteCustomers}
              renderPanel={(row) => (
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
          )}
        />
      )}
    />
  );

export default enhance(Content);