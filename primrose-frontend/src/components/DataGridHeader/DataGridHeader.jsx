import React from "react";
//import PropTypes from "prop-types";

import IconButton from "@material-ui/core/IconButton";
import Toolbar from "@material-ui/core/Toolbar";
import Tooltip from "@material-ui/core/Tooltip";
import TextField from "@material-ui/core/TextField";

import AddIcon from "@material-ui/icons/Add";
import DeleteIcon from "@material-ui/icons/Delete";
import SendIcon from "@material-ui/icons/Send";
import SearchIcon from "@material-ui/icons/Search";
import ClearIcon from "@material-ui/icons/Clear";

const defaultIcon = (obj, defaultIcon) => (obj && obj.icon) || defaultIcon;

const DataGridHeader = ({
  classes,
  searching,
  selecting,
  adding,
  sending,
  removing,
  hasSelected,

  searchTerm,
  searchOpen,
  onChangeSearchTerm,
  onClearSearchTerm,
  onSearchOpen,
  onSearchClose,
}) => (
    <Toolbar>
      {searching && searchOpen &&
        <TextField
          fullWidth
          onChange={onChangeSearchTerm}
          value={searchTerm}
          InputProps={{
            disableUnderline: true,
            placeholder: searching.placeholder || "Search",
          }}
          onKeyPress={event => {
            if (event.key === "Enter") {
              event.preventDefault();
              searching.onSearch(event, searchTerm);
            }
          }}
        />
      }
      <div className={classes.grow} />
      {searching && searchOpen &&
        <Tooltip
          title={searching.tooltip}
          placement="bottom"
          enterDelay={300}
        >
          <IconButton onClick={event => searching.onSearch(event, searchTerm)}>
            {defaultIcon(searching.icon, <SearchIcon />)}
          </IconButton>
        </Tooltip>
      }
      {searching && searchOpen &&
        <Tooltip
          title={"Close search"}
          placement="bottom"
          enterDelay={300}
        >
          <IconButton onClick={onSearchClose}>
            <ClearIcon />
          </IconButton>
        </Tooltip>
      }
      {searching && !searchOpen &&
        <Tooltip
          title={searching.tooltip}
          placement="bottom"
          enterDelay={300}
        >
          <IconButton onClick={event => searchOpen ? onSearchClose(event) : onSearchOpen(event)}>
            {defaultIcon(searching.icon, <SearchIcon />)}
          </IconButton>
        </Tooltip>
      }
      {adding && !searchOpen &&
        <Tooltip
          title={adding.text}
          placement="bottom"
          enterDelay={300}
        >
          <IconButton onClick={adding.onEvent}>
            {defaultIcon(adding.icon, <AddIcon />)}
          </IconButton>
        </Tooltip>
      }
      {removing && hasSelected && !searchOpen &&
        <Tooltip
          title={removing.text}
          placement="bottom"
          enterDelay={300}
        >
          <IconButton onClick={event => removing.onEvent(event, selecting.rowIds)}>
            {defaultIcon(removing.icon, <DeleteIcon />)}
          </IconButton>
        </Tooltip>
      }
      {sending && hasSelected && !searchOpen &&
        <Tooltip
          title={sending.text}
          placement="bottom"
          enterDelay={300}
        >
          <IconButton onClick={event => sending.onEvent(event, selecting.rowIds)}>
            {defaultIcon(sending.icon, <SendIcon />)}
          </IconButton>
        </Tooltip>
      }
    </Toolbar>
  );

export default DataGridHeader;