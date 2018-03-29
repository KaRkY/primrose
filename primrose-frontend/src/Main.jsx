import React from "react";
import PropTypes from "prop-types";

import App from "./components/App";
import List from "material-ui/List";
import IconButton from "material-ui/IconButton";
import MoreVert from "material-ui-icons/MoreVert";
import Tooltip from "material-ui/Tooltip";
import NavItem from "./components/nav/NavItem"
import { BrowserRouter } from "react-router-dom";
import { Switch, Route } from "react-router";
import dynamic from "./components/dynamic";

const createArray = size => new Array(size).fill(null);

const columnWidth = col => {
  if(col.children) {
    return col.children.reduce((acc, col) => acc + columnWidth(col), 0);
  } else {
    return 1;
  }
};

const walk = (node, level, index, width, acc) => {
  if(!acc[level]) {
    acc[level] = createArray(width);
  }

  acc[level][index] = node.title;

  if(node.children) {
    node.children.forEach(child => {
      walk(child, level + 1, index, width, acc);
      index = index + columnWidth(child);
    });
  }
};


const Test = props => {
  const { columns } = props;

  const fullWidth = columns.children.reduce((acc, col) => acc + columnWidth(col), 0);
  const acc = [];

  var index = 0;
  columns.children.forEach(child => {
    walk(child, 0, index, fullWidth, acc);
    index = index + columnWidth(child);
    console.log(index);
  });
  console.log(acc);

  return (
    <table border="1">
        {acc.map((r, index) => (
          <tr key={index}>
            {r.map((c, index) => <td style={{borderColor: "black", borderWidth: 1}} key={index}>{c}</td>)}
          </tr>
        ))}
    </table>
  );
}

const DTest = dynamic({
  Header: {
    key: "header",
    propTypes: {
      title: PropTypes.string.isRequired,
    }
  },

  Columns: {
    key: "columns",
    ordered: true,
  },

  ColumnGroup: {
    ordered: true,
    key: "columnGroup",
    propTypes: {
      title: PropTypes.string.isRequired,
    }
  },

  Column: {
    key: "column",
    propTypes: {
      title: PropTypes.string.isRequired,
    }
  },
})(Test);

const Main = () => (
  <DTest>
    <DTest.Header title="test1" />

    <DTest.Columns>
      <DTest.ColumnGroup title="Person">
        <DTest.Column title="Name"/>
        <DTest.Column title="Age" />
      </DTest.ColumnGroup>

      <DTest.Column title="Date Created" />

      <DTest.ColumnGroup title="Person1">
        <DTest.Column title="Name" />
        <DTest.Column title="Age" />
      </DTest.ColumnGroup>

    </DTest.Columns>
  </DTest>
);

export default Main;