import React from "react";

import SelectSearchList from "../../components/SelectSearchList";

const ContactList = ({
  classes,
  contacts,
  pagination,
  totalSize,
  handlePaged,
  handleSingle,
  handleNew,
  handleUpdate,
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
      onUpdate={handleUpdate}
    />
  );

export default ContactList;