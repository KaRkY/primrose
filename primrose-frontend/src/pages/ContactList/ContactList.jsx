import React from "react";

import DataGrid from "../../components/DataGrid";

const ContactList = ({
  classes,
  contacts,
  pagination,
  totalSize,
  onPaged,
  handleView,
  handleNew,
  handleUpdate,
}) => {
  const { page = 5, size = 5, selected = [], sort } = pagination;
  return (
    <DataGrid
      getRowId="code"
      
      rows={contacts}

      columns={[
        { name: "code", title: "Code" },
        { name: "fullName", title: "Name" },
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

      sending={{
        text: "Send contact",
        onEvent: console.log,
      }}

      opening={{
        text: "Open contact",
        onEvent: handleView,
      }}

      editing={{
        text: "Update contact",
        onEvent: handleUpdate,
      }}

      removing={{
        text: "Remove contact",
        onEvent: console.log,
      }}

      adding={{
        text: "Add contact",
        onEvent: handleNew,
      }}
    />
  );
};

export default ContactList;