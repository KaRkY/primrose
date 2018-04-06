import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import withWidth from "material-ui/utils/withWidth";

import Paper from "material-ui/Paper";
import DataGrid from "../Data/ChildConfigDataGrid";
import IconButton from "material-ui/IconButton";
import DeleteIcon from "material-ui-icons/Delete";



const contentStyle = theme => ({

});

const enhance = compose(
  withWidth(),
  withStyles(contentStyle)
);

const Content = ({ classes, width }) => (
  <Paper>
    <DataGrid
      rows={[
        { id: 1, displayName: "Rene1", type: "Bla" },
        { id: 2, displayName: "Rene2", type: "Vla" },
        { id: 3, displayName: "Rene3", type: "Sad" }
      ]}

      getRowId={row => row.id}
    >
      <DataGrid.Columns>
        <DataGrid.Column name="id" title="Id" numeric />
        <DataGrid.Column name="displayName" title="Display name" sortable />
        <DataGrid.Column name="type" title="Type" sortable />
        <DataGrid.Column grow name="test" title="test" getCellValue={row => `${row.displayName} ${row.type} ${row.id}`} />
      </DataGrid.Columns>

      <DataGrid.Pagination
        page={1}
        size={5}
        totalSize={8}
        onPageChange={console.log}
        onPageSizeChange={console.log}
      />

      <DataGrid.Sorting
        sort={{
          column: "displayName",
          direction: "asc",
        }}
        onSortChange={console.log}
      />

      <DataGrid.Selecting
        rowIds={[3]}
        onSelectRowsChange={console.log}
      />

      <DataGrid.Detailed
        rowIds={[2]}
        onOpenRowsChange={console.log}
      >
        {row => (
          <pre>{JSON.stringify(row, null, 2)}</pre>
        )}
      </DataGrid.Detailed>

      <DataGrid.RowActions>
        {row => (
          <React.Fragment>
            <IconButton>
              <DeleteIcon />
            </IconButton>
          </React.Fragment>
        )}
      </DataGrid.RowActions>
    </DataGrid>
  </Paper>
);

export default enhance(Content);