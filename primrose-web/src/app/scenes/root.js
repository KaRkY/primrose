import React from "react";
import { connect } from "react-redux";
import compose from "recompose/compose";
import { createStructuredSelector } from "reselect";

import AppSelectors from "../selectors";
import LocationSelectors from "@/location-selectors";

import DocumentTitle from "react-document-title";

import Frame from "./frame";

const Root = ({ type }) => (
  <DocumentTitle title={"Testing | Primrose CRM"}>
    <Frame />
  </DocumentTitle>
);



export default compose(
  connect(createStructuredSelector({
    type: LocationSelectors.type,
  }))
)(Root);