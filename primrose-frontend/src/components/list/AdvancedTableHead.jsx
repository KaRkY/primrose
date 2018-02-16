import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import { withStyles } from "material-ui/styles";

import {
  TableCell,
  TableHead,
  TableRow,
  TableSortLabel,
} from "material-ui/Table";
import Tooltip from "material-ui/Tooltip";
import Checkbox from "material-ui/Checkbox";

const styles = theme => ({

});

const enhance = compose(
  withHandlers({
    onSelect: ({ onSelect }) => (event, checked) => onSelect && onSelect(event, checked),
    onSortChange: ({ onSortChange }) => (columnId) => (event) => onSortChange && onSortChange(event, columnId),
  }),
  withStyles(styles)
);

const AdvancedTableHead = ({
  classes,
  columns,
  selectable,
  checked,
  indeterminate,
  sortDirection,
  sortColumn,
  onSelect,
  onSortChange,
}) => (
    <TableHead>
      <TableRow>
        <TableCell padding="checkbox" />
        {selectable && (
          <TableCell padding="checkbox">
            <Checkbox
              indeterminate={indeterminate}
              checked={checked}
              onChange={onSelect}
            />
          </TableCell>
        )}
        {columns.map(column => (
          <TableCell key={column.id}
            numeric={column.numeric}
            padding={column.disablePadding ? "none" : "default"}
            sortDirection={sortColumn === column.id ? sortDirection : false}>
            <Tooltip
              title="Sort"
              placement={column.numeric ? "bottom-end" : "bottom-start"}
              enterDelay={300}
            >
              <TableSortLabel
                active={sortColumn === column.id}
                direction={sortDirection}
                onClick={onSortChange(column.id)}
              >
                {column.label}
              </TableSortLabel>
            </Tooltip>
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );

const ComposedAdvancedTableHead = enhance(AdvancedTableHead);

export default ComposedAdvancedTableHead;