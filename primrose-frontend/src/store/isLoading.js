import * as actions from "../actions";

import * as customerList from "./customerList";
import * as customerUpdate from "./customerUpdate";
import * as customerView from "./customerView";
import * as contactList from "./contactList";
import * as contactUpdate from "./contactUpdate";
import * as contactView from "./contactView";
import meta from "./meta";

import * as location from "./location";

const components = {
      [actions.customerListPage]: state =>
            customerList.isLoading(state) ||
            meta.customerTypes.isLoading(state) ||
            meta.customerRelationTypes.isLoading(state),

      [actions.customerNewPage]: state =>
            meta.customerTypes.isLoading(state) ||
            meta.customerRelationTypes.isLoading(state) ||
            meta.emailTypes.isLoading(state) ||
            meta.phoneNumberTypes.isLoading(state),

      [actions.customerUpdatePage]: state =>
            customerUpdate.isLoading(state) ||
            meta.customerTypes.isLoading(state) ||
            meta.customerRelationTypes.isLoading(state) ||
            meta.emailTypes.isLoading(state) ||
            meta.phoneNumberTypes.isLoading(state),

      [actions.customerViewPage]: state =>
            customerView.isLoading(state) ||
            meta.customerTypes.isLoading(state) ||
            meta.customerRelationTypes.isLoading(state) ||
            meta.emailTypes.isLoading(state) ||
            meta.phoneNumberTypes.isLoading(state),

      [actions.contactListPage]: state =>
            contactList.isLoading(state),

      [actions.contactViewPage]: state =>
            contactView.isLoading(state) ||
            meta.emailTypes.isLoading(state) ||
            meta.phoneNumberTypes.isLoading(state),

      [actions.contactNewPage]: state =>
            meta.emailTypes.isLoading(state) ||
            meta.phoneNumberTypes.isLoading(state),

      [actions.contactUpdatePage]: state =>
            contactUpdate.isLoading(state) ||
            meta.emailTypes.isLoading(state) ||
            meta.phoneNumberTypes.isLoading(state),

}

export default state => {
      const fun = components[location.getPageType(state)];
      return fun && fun(state);
};