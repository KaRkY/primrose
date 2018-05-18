import React from "react";

import MarkdownElement from "../../components/MarkdownElement";

const Dashboard = ({ classes, width, style, customer }) => (
  <React.Fragment>
    <MarkdownElement 
    text={customer} 
    />
  </React.Fragment>
);

export default Dashboard;