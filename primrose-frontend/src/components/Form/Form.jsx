import renderProp from "../../util/renderProp";
import { reduxForm } from "redux-form";

const FormWrapper = ({ children, render, ...props}) => renderProp({ children, render }, props);

export default reduxForm()(FormWrapper);