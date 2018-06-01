import React from "react";

import * as actions from "../../actions";
import * as location from "../../store/location";
import * as contactList from "../../store/contactList";

import DataGrid from "../../components/DataGrid";
import Paged from "../../components/Paged";
import Connect from "../../components/Connect";

const ContactList = ({
  classes,
}) => (
    <Connect
      mapStateToProps={
        state => ({
          contacts: contactList.getData(state),
          pagination: location.getCurrentPagination(state),
          totalSize: contactList.getCount(state),
        })
      }

      mapDispatchToProps={dispatch => ({
        handleList: payload => dispatch(actions.contactListPage(payload)),
        handleView: (event, payload) => dispatch(actions.contactViewPage(payload)),
        handleNew: () => dispatch(actions.contactNewPage()),
        handleUpdate: (event, payload) => dispatch(actions.contactUpdatePage(payload)),
      })}
    >
      {state => (
        <Paged pagination={state.pagination} onChange={state.handleList}>
          {paged => (
            <DataGrid
              getRowId="code"

              rows={state.contacts}

              columns={[
                { name: "code", title: "Code" },
                { name: "fullName", title: "Name" },
                { name: "primaryEmail", title: "Primary email" },
                { name: "primaryPhone", title: "Primary phone" },
              ]}

              pagination={{
                totalSize: state.totalSize,
                page: paged.pagination.page,
                size: paged.pagination.size,
                onPageChange: paged.onPaged("page"),
                onPageSizeChange: paged.onPaged("size"),
              }}

              sorting={{
                sort: paged.pagination.sort,
                onChange: paged.onPaged("sort")
              }}

              selecting={{
                rowIds: paged.pagination.selected || [],
                onSelectRowsChange: paged.onPaged("selected"),
              }}

              opening={{
                text: "Open contact",
                onEvent: state.handleView,
              }}

              editing={{
                text: "Update contact",
                onEvent: state.handleUpdate,
              }}

              removing={{
                text: "Remove contact",
                onEvent: console.log,
              }}

              adding={{
                text: "Add contact",
                onEvent: state.handleNew,
              }}
            />
          )}
        </Paged>
      )}
    </Connect>
  );


export default ContactList;