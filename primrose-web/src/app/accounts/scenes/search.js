import React from "react";
import PropTypes from "prop-types";
import { bindActionCreators } from "redux";
import { connect } from "react-redux";
import compose from "recompose/compose";
import { createStructuredSelector } from "reselect";
import classnames from "classnames";

import AccountSelectors from "../selectors";



const Search = ({
  data
}) => (
    <table className="table is-fullwidth">
      <thead>
        <tr>
          <th>Code</th>
          <th>Display name</th>
          <th>Email</th>
          <th>Website</th>
          <th>Phone</th>
        </tr>
      </thead>
      <tbody>{
        (console.log(data) || data) && data.map(item => (
          <tr key={item.code}>
            <td>{item.code}</td>
            <td>{item.displayName}</td>
            <td>{item.email}</td>
            <td>{item.website}</td>
            <td>{item.phone}</td>
          </tr>
        ))
      }</tbody>
    </table>
  )

Search.propTypes = {
};

export default compose(
  connect(
    createStructuredSelector({
      data: AccountSelectors.search.currentPageData
    }),
    dispatch => bindActionCreators({

    }, dispatch)
  )
)(Search);