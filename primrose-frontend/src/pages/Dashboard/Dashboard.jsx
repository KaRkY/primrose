import React from "react";

import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import LoadingContainer from "../../components/LoadingContainer";
import DataGrid from "../../components/DataGrid";

const Dashboard = ({ classes, width, style, toggleLoading, loading }) => (
  <React.Fragment>
    <Button onClick={toggleLoading}>Toggle loading</Button>
    <Typography>{loading.toString()}</Typography>
    <LoadingContainer loading={loading}>
      <DataGrid
        rows={[]}

        columns={[
          { name: "code", title: "Code" },
          { name: "relationType", sortable: true, title: "Relation type" },
          { name: "type", sortable: true, title: "Type" },
          { name: "name", sortable: true, title: "Name" },
          { name: "primaryEmail", title: "Primary email" },
          { name: "primaryPhone", title: "Primary phone" },
        ]}

        pagination={{
          totalSize: 0,
          page: 0,
          size: 5,
          onPageChange: console.log,
          onPageSizeChange: console.log,
        }}
      />
    </LoadingContainer>
  </React.Fragment>
);

export default Dashboard;