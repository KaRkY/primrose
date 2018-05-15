import meta from "../../store/meta";

export default (state, props) => ({
  emailTypes: meta.emailTypes.getData(state),
  phoneNumberTypes: meta.phoneNumberTypes.getData(state),
});