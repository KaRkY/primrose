import React from "react";
import { connect } from "react-redux";

import * as actions from "../../actions";
import * as location from "../../store/location";
import customers from "../../store/customers";
import meta from "../../store/meta";

import compose from "recompose/compose";

import SelectSearchList from "../../components/SelectSearchList";

import withStyles from "@material-ui/core/styles/withStyles";


const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  customers: customers.paged.getData(state),
  customerTypes: meta.customerTypes.getData(state),
  customerRelationTypes: meta.customerRelationTypes.getData(state),
  pagination: location.getCurrentPagination(state),
  totalSize: customers.paged.getCount(state),
});

const mapDispatchTo = dispatch => ({
  handlePaged: payload => console.log(payload) || dispatch(actions.customersPage(payload)),
  handleSingle: payload => dispatch(actions.customerPage(payload)),
  handleNew: payload => dispatch(actions.customerPageNew(payload)),
  handleEdit: payload => dispatch(actions.customerPageEdit(payload)),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle)
);

const Content = ({
  classes,
  customers,
  customerTypes,
  customerRelationTypes,
  pagination,
  totalSize,
  handlePaged,
  handleSingle,
  handleNew,
  handleEdit,
}) => (

    <SelectSearchList 
      rows={customers}
      getRowId={row => row.code}
      totalSize={totalSize}
      pagination={pagination}
      columns={[
        { name: "code", title: "Code" },
        { name: "relationType", title: "Relation type", getCellValue: row => customerRelationTypes[row.relationType] },
        { name: "type", title: "Type", getCellValue: row => customerTypes[row.type] },
        { name: "name", title: "Name", getCellValue: row => row.displayName || row.fullName },
        { name: "primaryEmail", title: "Primary email" },
        { name: "primaryPhone", title: "Primary phone" },
      ]}
      onPaged={handlePaged}
      onOpen={handleSingle}
      onNew={handleNew}
      onEdit={handleEdit}
      onDeactivate={actions.customerDeactivatePromise}
    />
  );

export default enhance(Content);