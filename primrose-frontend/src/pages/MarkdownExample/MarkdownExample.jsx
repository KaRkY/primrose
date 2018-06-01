import React from "react";
import * as markdownExample from "../../store/markdownExample";

import MarkdownElement from "../../components/MarkdownElement";
import Connect from "../../components/Connect";

const MarkdownExample = () => (
  <Connect mapStateToProps={state => ({
    markdown: markdownExample.getData(state),
  })}>
    {state => (
      <MarkdownElement
        text={state.markdown}
      />
    )}
  </Connect>
);

export default MarkdownExample;