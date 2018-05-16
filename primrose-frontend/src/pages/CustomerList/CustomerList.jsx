import React from "react";

import DataGrid from "../../components/DataGrid";

const CustomersList = ({
  classes,
  customers,
  customerTypes,
  customerRelationTypes,
  pagination,
  totalSize,
  searchTerm,
  onChangeSearchTerm,
  onClearSearchTerm,
  onPaged,
  handleView,
  handleNew,
  handleUpdate,
}) => {
  const { page = 5, size = 5, selected = [], sort } = pagination;
  console.log(pagination);

  return (
    <DataGrid
      getRowId="code"
      
      rows={customers}

      columns={[
        { name: "code", title: "Code", sortable: true },
        { name: "relationType", title: "Relation type", getCellValue: row => customerRelationTypes[row.relationType] },
        { name: "type", title: "Type", getCellValue: row => customerTypes[row.type] },
        { name: "name", title: "Name", getCellValue: row => row.displayName || row.fullName },
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
        open: "searchOpen" in pagination,
        term: searchTerm,
        tooltip: "Search customers",
        onChange: onChangeSearchTerm,
        onClear: onClearSearchTerm,
        onSearch: console.log,
        onOpen: event => console.log("open") || onPaged("searchOpen")(event, null),
        onClose: event => console.log("close") || onPaged("searchOpen")(event, undefined),
      }}

      sending={{
        text: "Send customer",
        onEvent: console.log,
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