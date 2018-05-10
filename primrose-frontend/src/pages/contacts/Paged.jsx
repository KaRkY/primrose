import React from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import { withStyles } from "material-ui/styles";

import * as actions from "../../actions";
import * as location from "../../store/location";
import contacts from "../../store/contacts";

import SelectSearchList from "../../components/SelectSearchList";

const contentStyle = theme => ({

});

const mapState = (state, props) => ({
  contacts: contacts.paged.getData(state),
  pagination: location.getCurrentPagination(state),
  totalSize: contacts.paged.getCount(state),
  query: location.getCurrentQuery(state),
});

const mapDispatchTo = dispatch => ({
  handlePaged: payload => dispatch(actions.contactsPage(payload)),
  handleSingle: payload => dispatch(actions.contactPage(payload)),
  handleNew: payload => dispatch(actions.contactPageNew(payload)),
  handleEdit: payload => dispatch(actions.contactPageEdit(payload)),
});

const enhance = compose(
  connect(mapState, mapDispatchTo),
  withStyles(contentStyle)
);

const Content = ({
  classes,
  contacts,
  pagination,
  totalSize,
  handlePaged,
  handleSingle,
  handleNew,
  handleEdit,
}) => (
    <SelectSearchList
      rows={contacts}
      getRowId={row => row.code}
      totalSize={totalSize}
      pagination={pagination}
      columns={[
        { name: "code", title: "Code" },
        { name: "fullName", title: "Name" },
        { name: "primaryEmail", title: "Primary email" },
        { name: "primaryPhone", title: "Primary phone" },
      ]}
      onPaged={handlePaged}
      onOpen={handleSingle}
      onNew={handleNew}
      onEdit={handleEdit}
      onDeactivate={actions.contactDeactivatePromise}
      onSend={console.log}
    />
  );

export default enhance(Content);