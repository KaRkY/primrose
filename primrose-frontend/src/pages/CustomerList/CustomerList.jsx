import React from "react";

import SelectSearchList from "../../components/SelectSearchList";

const CustomersList = ({
  classes,
  customers,
  customerTypes,
  customerRelationTypes,
  pagination,
  totalSize,
  handlePaged,
  handleSingle,
  handleNew,
  handleUpdate,
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
      onUpdate={handleUpdate}
    />
  );

export default CustomersList;