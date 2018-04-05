import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import findByType from "../../util/findByType";
import extractRenderMethod from "../../util/extractRenderMethod";
import get from "lodash/get";
import identity from "lodash/identity";

import ExpansionPanel, {
  ExpansionPanelSummary,
  ExpansionPanelDetails,
  ExpansionPanelActions,
} from "material-ui/ExpansionPanel";
import TablePagination from "material-ui/Table/TablePagination";
import Typography from "material-ui/Typography";
import Grid from "material-ui/Grid";
import ExpandMoreIcon from "material-ui-icons/ExpandMore";

const styles = theme => ({
  root: {
    flexGrow: 1,
  },

  heading: {
    fontSize: theme.typography.pxToRem(15),
    fontWeight: theme.typography.fontWeightRegular,
  },

  pagination: {
    fontSize: theme.typography.pxToRem(12),
  },
});

const enhance = compose(
  withStyles(styles)
);

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

const Pagination = () => null;
Pagination.displayName = "Pagination";

const Sortable = () => null;
Sortable.displayName = "Sortable";

const PanelActions = () => null;
PanelActions.displayName = "PanelActions";

const RenderCell = () => null;
RenderCell.displayName = "RenderCell";

const DataList = ({
  classes,
  rows = [],
  columns,
  rowId,
  heading,
  children }) => {

  const paginationComponent = findByType(children, Pagination)[0];
  //const sortableComponent = findByType(children, Sortable)[0];
  const panelActionsComponent = findByType(children, PanelActions)[0];
  const renderCellComponent = findByType(children, RenderCell)[0];

  const isPaginated = paginationComponent ? true : false;
  const pageSize = get(paginationComponent, "props.pageSize", 10);
  const pageNumber = get(paginationComponent, "props.pageNumber", 0);
  const specifiedSize = get(paginationComponent, "props.totalSize", 0);
  // To prevent page change until data is loaded
  const totalSize = specifiedSize ? specifiedSize : (pageSize * (pageNumber + 1));
  const rowsPerPageOptions = get(paginationComponent, "props.rowsPerPageOptions", [5, 10, 25]);
  const onPageChange = get(paginationComponent, "props.onPageChange", identity);
  const onPageSizeChange = get(paginationComponent, "props.onPageSizeChange", identity);

  //const isSortable = sortableComponent ? true : false;
  //const sortDirection = get(sortableComponent, "props.sortDirection");
  //const sortColumn = get(sortableComponent, "props.sortColumn");
  //const onSortChange = get(sortableComponent, "props.onSortChange", identity);

  const isRenderCell = renderCellComponent ? true : false;
  const renderCell = extractRenderMethod(get(renderCellComponent, "props", {}));

  const isPanelActions = panelActionsComponent ? true : false;
  //const numOfActions = get(panelActionsComponent, "props.num", 1);
  const panelActions = extractRenderMethod(get(panelActionsComponent, "props", {}));

  return (
    <div className={classes.root}>
      {rows.map(row => (
        <ExpansionPanel key={extractId(rowId, row)}>
          <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
            <Typography variant="subheading">{heading(row)}</Typography>
          </ExpansionPanelSummary>
          <ExpansionPanelDetails>
            <Grid container>
              {columns.map(column => {
                const value = isRenderCell ? renderCell(column) : row[column.id];
                return (
                  <React.Fragment key={column.id}>
                    <Grid item xs={6}><Typography variant="subheading">{column.label}</Typography></Grid>
                    <Grid item xs={6}><Typography variant="body2">{value}</Typography></Grid>
                  </React.Fragment>
                );
              })}
            </Grid>
          </ExpansionPanelDetails>
          {isPanelActions && <ExpansionPanelActions>{panelActions(row)}</ExpansionPanelActions>}
        </ExpansionPanel>
      ))}
      {isPaginated && (
        <TablePagination
          classes={{
            input: classes.pagination
          }}
          component="div"
          count={totalSize}
          rowsPerPage={pageSize}
          page={pageNumber}
          backIconButtonProps={{
            "aria-label": "Previous Page",
          }}
          nextIconButtonProps={{
            "aria-label": "Next Page",
          }}
          onChangePage={onPageChange}
          onChangeRowsPerPage={(event, size) => onPageSizeChange(event, event.target.value)}
          rowsPerPageOptions={rowsPerPageOptions}
        />
      )}
    </div>
  );
};

const ComposedDataList = enhance(DataList);
ComposedDataList.Pagination = Pagination;
ComposedDataList.Sortable = Sortable;
ComposedDataList.PanelActions = PanelActions;
ComposedDataList.RenderCell = RenderCell;

export default ComposedDataList;