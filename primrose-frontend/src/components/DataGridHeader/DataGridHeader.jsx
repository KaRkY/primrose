import React from "react";
//import PropTypes from "prop-types";

import IconButton from "@material-ui/core/IconButton";
import Toolbar from "@material-ui/core/Toolbar";
import Tooltip from "@material-ui/core/Tooltip";
import SearchBar from 'material-ui-search-bar';

import AddIcon from "@material-ui/icons/Add";
import DeleteIcon from "@material-ui/icons/Delete";
import SendIcon from "@material-ui/icons/Send";
import SearchIcon from "@material-ui/icons/Search";
import ClearIcon from "@material-ui/icons/Clear";

import { Value, Toggle, Compose } from "react-powerplug";

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
}) => (
    <Compose components={[<Value initial={searchTerm} />, <Toggle initial={false}/>]}>
      {(search, searchOpen) => (
        <Toolbar>
          {searching && searchOpen.on &&
            <SearchBar
              fullWidth
              onChange={search.setValue}
              value={search.value}
              onRequestSearch={value => searching.onSearch(null, value)}
              style={{
                width: "100%"
              }}
            />
          }
          <div className={classes.grow} />
          {searching && searchOpen.on &&
            <Tooltip
              title={"Close search"}
              placement="bottom"
              enterDelay={300}
            >
              <IconButton onClick={() => searchOpen.set(false)}>
                <ClearIcon />
              </IconButton>
            </Tooltip>
          }
          {searching && !searchOpen.on &&
            <Tooltip
              title={searching.tooltip}
              placement="bottom"
              enterDelay={300}
            >
              <IconButton onClick={() => searchOpen.set(true)}>
                {defaultIcon(searching.icon, <SearchIcon />)}
              </IconButton>
            </Tooltip>
          }
          {adding && !searchOpen.on &&
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
          {removing && hasSelected && !searchOpen.on &&
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
          {sending && hasSelected && !searchOpen.on &&
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
      )}
    </Compose>
  );

export default DataGridHeader;