import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { withStyles } from "material-ui/styles";

import {
  TableBody,
  TableCell,
  TableRow,
} from "material-ui/Table";
import AdvancedTableRow from "./AdvancedTableRow";

const styles = theme => ({

});

const enhance = compose(
  withHandlers({
    onSelectRow: ({ onSelectRow }) => (row) => (event, checked) => onSelectRow && onSelectRow(event, row, checked),
  }),
  withStyles(styles)
);

const AdvancedTableBody = ({
  classes,
  columns,
  rows,
  pageSize,
  rowId,
  detailed,
  selectable,
  selectedRows,
  renderPanel,
  onSelectRow,
}) => {
  const numRows = rows.length;
  const emptyRows = pageSize - numRows;

  return (
    <TableBody>
      {rows.map(row => <AdvancedTableRow
        key={extractId(rowId, row)}
        columns={columns}
        row={row}
        detailed={detailed}
        selectable={selectable}
        selected={(selectedRows && selectedRows.find(el => el === extractId(rowId, row))) ? true : false}
        onSelect={onSelectRow(extractId(rowId, row))}
        renderPanel={renderPanel}
      />)}

      {emptyRows > 0 && (
        <TableRow style={{ height: 49 * emptyRows }}>
          <TableCell colSpan={columns.length + (selectable ? 1 : 0) + (detailed ? 1 : 0)} />
        </TableRow>
      )}
    </TableBody>
  );
};

const ComposedAdvancedTableBody = enhance(AdvancedTableBody);

export default ComposedAdvancedTableBody;

const extractId = (getId, obj) => {
  if (typeof getId === "function") {
    return getId(obj);
  } else {
    if (typeof getId === "string") {
      return obj[getId];
    } else {
      return obj.id;
    }
  }
}