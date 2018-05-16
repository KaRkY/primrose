import React from "react";

import DataGrid from "../../components/DataGrid";

const CustomersList = ({
  classes,
  customers,
  customerTypes,
  customerRelationTypes,
  pagination,
  totalSize,
  onPaged,
  handleView,
  handleNew,
  handleUpdate,
}) => {
  const { page = 5, size = 5, selected = [], sort, search } = pagination;

  return (
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
        page,
        size,
        onPageChange: onPaged("page"),
        onPageSizeChange: onPaged("size"),
      }}

      sorting={{
        sort,
        onChange: onPaged("sort")
      }}

      selecting={{
        rowIds: selected,
        onSelectRowsChange: onPaged("selected"),
      }}

      searching={{
        text: search,
        tooltip: "Search customers",
        onSearch: onPaged("search"),
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
  );
};

export default CustomersList;