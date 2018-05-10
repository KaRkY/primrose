import React from "react";
import compose from "recompose/compose";
import { withStyles } from "material-ui/styles";
import withStateHandlers from "recompose/withStateHandlers";
import withHandlers from "recompose/withHandlers";

import Paper from "material-ui/Paper";
import Toolbar from "material-ui/Toolbar";
import SearchIcon from "@material-ui/icons/Search";
import ClearIcon from "@material-ui/icons/Clear";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import ExpandLessIcon from "@material-ui/icons/ExpandLess";
import IconButton from "material-ui/IconButton";
import Tooltip from "material-ui/Tooltip";
import TextField from "material-ui/TextField";
import InputAdornment from "material-ui/Input/InputAdornment";
import Collapse from "material-ui/transitions/Collapse";


export const style = theme => ({
  searchMore: theme.mixins.gutters({

  }),
});

const enhance = compose(
  withStyles(style),
  withStateHandlers(
    ({ value, expanded }) => ({ advancedOpen: expanded, general: value ? value : "" }),
    {
      toggleAdvancedOpen: ({ advancedOpen, ...rest }) => () => ({
        ...rest,
        advancedOpen: !advancedOpen,
      }),

      onChangeField: props => event => ({
        ...props,
        general: event.target.value,
      }),

      onClearField: props => event => ({
        ...props,
        general: "",
      }),

    }
  ),
  withHandlers({
    onClear: ({ general, onSearch, onClearField, onClear }) => event => {
      onClearField(event);
      onClear && onClear(event, "");
    },
    onSearch: ({ general, onSearch }) => event => onSearch && onSearch(event, general),
  }),
);

const Search = ({
  classes,
  children,
  advancedOpen,
  toggleAdvancedOpen,
  onChangeField,
  general,
  onSearch,
  onClear,
}) => (
    <Paper>
      <Toolbar>
        <TextField
          fullWidth
          onChange={onChangeField}
          value={general}
          InputProps={{
            disableUnderline: true,
            placeholder: "Search",
            endAdornment: (
              <InputAdornment position="end">
                {general &&
                  <Tooltip
                    title="Clear search"
                    enterDelay={300}
                  >
                    <IconButton onClick={onClear}>
                      <ClearIcon />
                    </IconButton>
                  </Tooltip>
                }
                {children &&
                  <Tooltip
                    title="Expand advanced search"
                    enterDelay={300}
                  >
                    <IconButton onClick={toggleAdvancedOpen}>
                      {advancedOpen ? <ExpandLessIcon /> : <ExpandMoreIcon />}
                    </IconButton>
                  </Tooltip>
                }
              </InputAdornment>
            ),
            startAdornment: (
              <InputAdornment position="start">
                <Tooltip
                  title="Search"
                  enterDelay={300}
                >
                  <IconButton onClick={onSearch}>
                    <SearchIcon />
                  </IconButton>
                </Tooltip>
              </InputAdornment>
            ),
          }}
          onKeyPress={event => {
            if (event.key === 'Enter') {
              event.preventDefault();
              onSearch(event);
            }
          }}
        />
      </Toolbar>
      {children &&
        <div className={classes.searchMore}>
          <Collapse in={advancedOpen}>{children}</Collapse>
        </div>
      }
    </Paper>
  );

export default enhance(Search);