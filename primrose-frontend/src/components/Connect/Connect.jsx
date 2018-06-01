import renderProp from "../../util/renderProp";
import { bindActionCreators } from "redux";
import { connect } from "react-redux";

const ConnectWrapper = ({ children, render, ...props }) => renderProp({ children, render }, props);

export default connect(
  (state, { mapStateToProps, ...props }) => mapStateToProps(state, props),
  (dispatch, { mapDispatchToProps, ...props }) => {
    if (typeof mapDispatchToProps === "object") {
      return bindActionCreators(mapDispatchToProps, dispatch);
    }
    return mapDispatchToProps ? mapDispatchToProps(dispatch, props) : {};
  }
)(ConnectWrapper);