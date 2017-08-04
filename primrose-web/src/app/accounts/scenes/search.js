import React from "react";
import PropTypes from "prop-types";
import { bindActionCreators } from "redux";
import { connect } from "react-redux";
import compose from "recompose/compose";
import { createStructuredSelector } from "reselect";
import classnames from "classnames";
import Link from "redux-first-router-link";
import { Pager } from "@/components/pagination";

import AccountActions from "../actions";
import AccountSelectors from "../selectors";



const Search = ({
  data,
  query,
  isFirst,
  isLast,
  currentPageNumber,
  numberOfPages
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
          disabled={isFirst}
        >Previous</Link>
        <Link
          className="pagination-next"
          to={AccountActions.creators.requestAccountsScene(Object.assign({}, query, { page: parseInt(query.page) + 1 }))}
          disabled={isLast}
        >Next page</Link>
        {query && numberOfPages !== 0 && <Pager
          size={numberOfPages}
          page={parseInt(query.page)}
          step={1}
        />}
      </nav>
    </div>
  )

Search.propTypes = {
};

export default compose(
  connect(
    createStructuredSelector({
      data: AccountSelectors.search.currentPageData,
      query: AccountSelectors.search.query,
      isFirst: AccountSelectors.search.isFirst,
      isLast: AccountSelectors.search.isLast,
      currentPageNumber: AccountSelectors.search.currentPageNumber,
      numberOfPages: AccountSelectors.search.numberOfPages,
    }),
    dispatch => bindActionCreators({
    }, dispatch)
  )
)(Search);