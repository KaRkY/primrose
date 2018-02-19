import React from "react";
import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import withState from "recompose/withState";
import { withStyles } from "material-ui/styles";

import {
  TableCell,
  TableRow,
} from "material-ui/Table";
import Checkbox from "material-ui/Checkbox";
import { IconButton } from "material-ui";
import Fade from "material-ui/transitions/Fade";
import KeyboardArrowRight from "material-ui-icons/KeyboardArrowRight";
import KeyboardArrowDown from "material-ui-icons/KeyboardArrowDown";

const styles = theme => ({
  panelRow: {
    backgroundColor: theme.palette.background.default,
  },

  tableCell: {
    whiteSpace: "nowrap",
    overflow: "hidden",
    textOverflow: "ellipsis",
  },
});

const enhance = compose(
  withState("open", "setOpen", false),
  withHandlers({
    onSelect: ({ onSelect }) => (event, checked) => onSelect && onSelect(event, checked),
    onOpenPanel: ({ open, setOpen }) => () => setOpen(!open),
  }),
  withStyles(styles)
);

const AdvancedTableRow = ({
  classes,
  columns,
  row,
  detailed,
  selectable,
  selected,
  open,
  renderPanel,
  onSelect,
  onOpenPanel,
}) => {
  return (
    <React.Fragment>
      <TableRow hover>
        {detailed && (
          <TableCell padding="checkbox">
            <IconButton onClick={onOpenPanel}>
              {open ? <KeyboardArrowDown /> : <KeyboardArrowRight />}
            </IconButton>
          </TableCell>
        )}
        {selectable && (
          <TableCell padding="checkbox">
            <Checkbox
              checked={selected}
              onChange={onSelect}
            />
          </TableCell>
        )}
        {columns.map(column => (
          <TableCell title={row[column.id]} className={classes.tableCell} key={column.id}
            numeric={column.numeric}
            padding={column.disablePadding ? "none" : "default"}>
            {row[column.id]}
          </TableCell>
        ))}
      </TableRow>
      <Fade in={open} unmountOnExit>
        <TableRow>
          <TableCell className={classes.panelRow} colSpan={columns.length + (selectable ? 1 : 0) + (detailed ? 1 : 0)}>
            {renderPanel && renderPanel(row)}
          </TableCell>
        </TableRow>
      </Fade>
    </React.Fragment>
  );
};

const ComposedAdvancedTableRow = enhance(AdvancedTableRow);

export default ComposedAdvancedTableRow;