import RenderComp from "../RenderComp";
import compose from "recompose/compose";
import withStateHandlers from "recompose/withStateHandlers";
import { graphql } from "react-apollo";
import gql from "graphql-tag";

export const deleteCustomers = gql`
mutation deleteCustomers($ids: [ID]!) {
	deleteCustomers(ids: $ids)
}
`;

const hoc = compose(
  withStateHandlers(
    () => ({ deleting: false, }),
    {
      onDeleting: ({ deleting, ...rest }) => (value) => ({ deleting: value, ...rest }),
    }
  ),
  graphql(deleteCustomers, {
    props: ({ mutate, ownProps }) => ({
      deleteCustomers: () => {
        ownProps.onDeleting(true);
        return mutate({
          variables: {
            ids: ownProps.selectedRows
          },
  
          refetchQueries: ["loadCustomers"],
        })
          .then(result => result.data.deleteCustomers)
          .then(result => {
            ownProps.selectRow(result, false);
            ownProps.onDeleting(false);
            return result;
          });
      },
    })
  }),
);

const DeleteCustomers = hoc(RenderComp);
export default DeleteCustomers;