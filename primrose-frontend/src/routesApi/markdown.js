import * as actions from "../actions";
import * as markdown from "../api/markdown";

import reduxifyApi from "../util/reduxifyApi";

export const view = reduxifyApi({
  load: actions.markdownExampleLoad, 
  finished: actions.markdownExampleFinished, 
  error: actions.markdownExampleError,
  api: markdown.view,
});