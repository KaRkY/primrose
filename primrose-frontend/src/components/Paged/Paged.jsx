import renderProp from "../../util/renderProp";

const Paged = ({ pagination = { page: 5, size: 5 }, onChange, ...props }) => renderProp(props, {
  pagination,
  onPaged: property => (event, value) => {
    const newValue = { ...pagination, [property]: value, };
    if(value === undefined) {
      delete newValue[property];
    }
    if(property === "search") {
      newValue.page = 0;
    }
    onChange(newValue);
  },
});

export default Paged;