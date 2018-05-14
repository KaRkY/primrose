import React from "react";
import { connect } from "react-redux";
import normalizeArray from "../../util/normalizeArray";
import difference from "lodash/difference";
import union from "lodash/union";
import * as actions from "../../actions";
import * as location from "../../store/location";
import customers from "../../store/customers";
import meta from "../../store/meta";

import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";

import Search from "../../components/Search";

import IconButton from "@material-ui/core/IconButton";
import Paper from "@material-ui/core/Paper";
import Toolbar from "@material-ui/core/Toolbar";
import Tooltip from "@material-ui/core/Tooltip";
import withStyles from "@material-ui/core/styles/withStyles";

import DeleteIcon from "@material-ui/icons/Delete";
import PersonAddIcon from "@material-ui/icons/PersonAdd";

const contentStyle = theme => ({
  root: {
    position: "relative",
    marginTop: theme.spacing.unit * 3,
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
  },
});

const mapState = (state, props) => ({
  customers: customers.paged.getData(state),
  customerTypes: meta.customerTypes.getData(state),
  customerRelationTypes: meta.customerRelationTypes.getData(state),
  pagination: location.getCurrentPagination(state),
  totalSize: customers.paged.getCount(state),
  query: location.getCurrentQuery(state),
});

const mapDispatchTo = dispatch => ({
  handlePaged: payload => dispatch(actions.customersPage(payload)),
  handleSingle: payload => dispatch(actions.customerPage(payload)),
  handleNew: payload => dispatch(actions.customerPageNew(payload)),
  handleEdit: payload => dispatch(actions.customerPageEdit(payload)),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withHandlers({
    onPageChange: ({ query, handlePaged }) => (event, page) => handlePaged({
      ...query,
      page,
    }),
    onPageSizeChange: ({ query, handlePaged }) => (event, size) => handlePaged({
      ...query,
      size,
    }),
    onSortChange: ({ query, handlePaged }) => (event, sortProperty, direction) => handlePaged({
      ...query,
      sortProperty,
      sortDirection: direction,
    }),
    onQueryChange: ({ query, handlePaged }) => (event, value) => handlePaged({
      ...query,
      query: value ? value : undefined,
    }),
    onSelectedRowsChange: ({ query, pagination, handlePaged }) => (event, value, checked) => handlePaged({
      ...query,
      selected: (checked ? union(normalizeArray(pagination.selected), value) : difference(normalizeArray(pagination.selected), value)),
    }),
    onNew: ({ handleNew }) => (event) => handleNew(),
    onOpen: ({ handleSingle }) => (event, value) => handleSingle(value),
    onEdit: ({ handleEdit }) => (event, value) => handleEdit(value),
    onDeactivate: ({ pagination, handlePaged, query }) => (event, values) => actions.customerDeactivatePromise(values)
      .then(result => handlePaged({ ...query, selected: undefined, force: true }))
      .catch(console.log)
  }),
  withStyles(contentStyle)
);

// Reimplement search this does not work. Search schould have search button
const Content = ({
  classes,
  customers,
  customerTypes,
  customerRelationTypes,
  pagination,
  totalSize,
  isDeleting,
  query,
  searchOpen,
  handlePaged,
  onSelectedRowsChange,
  onQueryChange,
  onPageChange,
  onPageSizeChange,
  onSortChange,
  onNew,
  onOpen,
  onEdit,
  onDeactivate,
}) => (
    <React.Fragment>
      <Search
        onSearch={onQueryChange}
        value={query && query.query}
      />
      <Paper className={classes.root}>
        <Toolbar>
          <div className={classes.grow} />
          <Tooltip
            title="New Customer"
            enterDelay={300}
          >
            <IconButton onClick={onNew}>
              <PersonAddIcon />
            </IconButton>
          </Tooltip>
          {pagination.selected && pagination.selected.length > 0 && (
            <Tooltip
              title="Deactivate Customers"
              enterDelay={300}
            >
              <IconButton disabled={isDeleting} onClick={event => onDeactivate(event, pagination.selected)}>
                <DeleteIcon />
              </IconButton>
            </Tooltip>
          )}
        </Toolbar>

      </Paper>
    </React.Fragment>
  );

export default enhance(Content);