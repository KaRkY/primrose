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
            customerList.getError(state) ||
            meta.customerTypes.getError(state) ||
            meta.customerRelationTypes.getError(state),

      [actions.customerNewPage]: state =>
            meta.customerTypes.getError(state) ||
            meta.customerRelationTypes.getError(state) ||
            meta.emailTypes.getError(state) ||
            meta.phoneNumberTypes.getError(state),

      [actions.customerUpdatePage]: state =>
            customerUpdate.getError(state) ||
            meta.customerTypes.getError(state) ||
            meta.customerRelationTypes.getError(state) ||
            meta.emailTypes.getError(state) ||
            meta.phoneNumberTypes.getError(state),

      [actions.customerViewPage]: state =>
            customerView.getError(state) ||
            meta.customerTypes.getError(state) ||
            meta.customerRelationTypes.getError(state) ||
            meta.emailTypes.getError(state) ||
            meta.phoneNumberTypes.getError(state),

      [actions.contactListPage]: state =>
            contactList.getError(state),

      [actions.contactViewPage]: state =>
            contactView.getError(state) ||
            meta.emailTypes.getError(state) ||
            meta.phoneNumberTypes.getError(state),

      [actions.contactNewPage]: state =>
            meta.emailTypes.getError(state) ||
            meta.phoneNumberTypes.getError(state),

      [actions.contactUpdatePage]: state =>
            contactUpdate.getError(state) ||
            meta.emailTypes.getError(state) ||
            meta.phoneNumberTypes.getError(state),

}

export default state => {
      const fun = components[location.getPageType(state)];
      return fun && fun(state);
};