import React from "react";

import DataGrid from "../../components/DataGrid";
import Paged from "../../components/Paged";

const CustomersList = ({
  classes,
  customers,
  customerTypes,
  customerRelationTypes,
  pagination,
  totalSize,
  handleList,
  handleView,
  handleNew,
  handleUpdate,
}) => (
  <Paged pagination={pagination} onChange={handleList}>
    {paged => (
      <DataGrid
        getRowId="code"

        rows={customers}

        columns={[
          { name: "code", title: "Code" },
          { name: "relationType", sortable: true, title: "Relation type", getCellValue: row => customerRelationTypes[row.relationType] },
          { name: "type", sortable: true, title: "Type", getCellValue: row => customerTypes[row.type] },
          { name: "name", sortable: true, title: "Name", getCellValue: row => row.displayName || row.fullName },
          { name: "primaryEmail", title: "Primary email" },
          { name: "primaryPhone", title: "Primary phone" },
        ]}

        pagination={{
          totalSize,
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
          onEvent: handleView,
        }}

        editing={{
          text: "Update customer",
          onEvent: handleUpdate,
        }}

        removing={{
          text: "Remove customer",
          onEvent: console.log,
        }}

        adding={{
          text: "Add customer",
          onEvent: handleNew,
        }}
      />
    )}
  </Paged>
);

export default CustomersList;