import React from "react";

import DataGrid from "../../components/DataGrid";
import Paged from "../../components/Paged";

const ContactList = ({
  classes,
  contacts,
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
        
        rows={contacts}

        columns={[
          { name: "code", title: "Code" },
          { name: "fullName", title: "Name" },
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
    )}
  </Paged>
);


export default ContactList;