import React from "react";
import PropTypes from "prop-types";
import { bindActionCreators } from "redux";
import { connect } from "react-redux";
import compose from "recompose/compose";
import { createStructuredSelector } from "reselect";
import classnames from "classnames";
import Link from "redux-first-router-link";

import AccountActions from "../actions";
import AccountSelectors from "../selectors";



const Search = ({
  data,
  query,
}) => (
    <div>
      <table className="table is-fullwidth is-striped">
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
          data && data.map(item => (
            <tr key={item.code}>
              <td><Link to={AccountActions.creators.requestAccountsViewScene({ account: item.urlCode })}>{item.code}</Link></td>
              <td>{item.displayName}</td>
              <td><a href={`mailto:${item.email}`}>{item.email}</a></td>
              <td><a href={`//${item.website}`}>{item.website}</a></td>
              <td>{item.phone}</td>
            </tr>
          ))
        }</tbody>
      </table>
      <nav className="pagination is-centered">
        <Link
          className="pagination-previous"
          to={AccountActions.creators.requestAccountsScene(Object.assign({}, query, { page: query.page - 1 }))}
          disabled={query.page - 1 < 1}
        >Previous</Link>
        <Link
          className="pagination-next"
          to={AccountActions.creators.requestAccountsScene(Object.assign({}, query, { page: parseInt(query.page) + 1 }))}
        >Next page</Link>
        <ul className="pagination-list">
          <li><a className="pagination-link">1</a></li>
          <li><span className="pagination-ellipsis">&hellip;</span></li>
          <li><a className="pagination-link">45</a></li>
          <li><a className="pagination-link is-current">46</a></li>
          <li><a className="pagination-link">47</a></li>
          <li><span className="pagination-ellipsis">&hellip;</span></li>
          <li><a className="pagination-link">86</a></li>
        </ul>
      </nav>
    </div>
  )

Search.propTypes = {
};

export default compose(
  connect(
    createStructuredSelector({
      data: AccountSelectors.search.currentPageData,
      query: AccountSelectors.search.query
    }),
    dispatch => bindActionCreators({
    }, dispatch)
  )
)(Search);