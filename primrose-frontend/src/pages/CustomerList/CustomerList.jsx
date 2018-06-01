import React from "react";

import * as actions from "../../actions";
import * as location from "../../store/location";
import * as customerList from "../../store/customerList";
import meta from "../../store/meta";


import DataGrid from "../../components/DataGrid";
import Paged from "../../components/Paged";
import Connect from "../../components/Connect";

const CustomersList = ({
  classes,
}) => (
    <Connect
      mapStateToProps={
        state => ({
          customers: customerList.getData(state),
          customerTypes: meta.customerTypes.getData(state),
          customerRelationTypes: meta.customerRelationTypes.getData(state),
          pagination: location.getCurrentPagination(state),
          totalSize: customerList.getCount(state),
        })
      }

      mapDispatchToProps={dispatch => ({
        handleList: payload => dispatch(actions.customerListPage(payload)),
        handleView: (event, payload) => dispatch(actions.customerViewPage(payload)),
        handleNew: () => dispatch(actions.customerNewPage()),
        handleUpdate: (event, payload) => dispatch(actions.customerUpdatePage(payload)),
      })}
    >
      {state => (
        <Paged pagination={state.pagination} onChange={state.handleList}>
          {paged => (
            <DataGrid
              getRowId="code"

              rows={state.customers}

              columns={[
                { name: "code", title: "Code" },
                { name: "relationType", sortable: true, title: "Relation type", getCellValue: row => state.customerRelationTypes[row.relationType] },
                { name: "type", sortable: true, title: "Type", getCellValue: row => state.customerTypes[row.type] },
                { name: "name", sortable: true, title: "Name", getCellValue: row => row.displayName || row.fullName },
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

              searching={{
                text: paged.pagination.search,
                tooltip: "Search customers",
                onSearch: paged.onPaged("search"),
              }}

              opening={{
                text: "Open customer",
                onEvent: state.handleView,
              }}

              editing={{
                text: "Update customer",
                onEvent: state.handleUpdate,
              }}

              removing={{
                text: "Remove customer",
                onEvent: console.log,
              }}

              adding={{
                text: "Add customer",
                onEvent: state.handleNew,
              }}
            />
          )}
        </Paged>
      )}
    </Connect>
  );

export default CustomersList;