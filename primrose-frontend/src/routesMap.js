import * as actions from "./actions";
import * as customer from "./routesApi/customer";
import * as contact from "./routesApi/contact";
import * as meta from "./routesApi/meta";
import * as markdown from "./routesApi/markdown";

import thunk from "./util/thunk";


export default {
  [actions.dashboardPage]: {
    path: "/",
  },
  [actions.customerListPage]: {
    path: "/customers",
    thunk: thunk([
      customer.list,
      meta.customerTypes,
      meta.customerRelationTypes
    ]),
  },
  [actions.customerNewPage]: {
    path: "/customers/new",
    thunk: thunk([
      meta.customerTypes,
      meta.customerRelationTypes,
      meta.emailTypes,
      meta.phoneNumberTypes
    ]),
  },
  [actions.customerViewPage]: {
    path: "/customers/:customer",
    fromPath: param => param,
    thunk: thunk([
      customer.view,
      meta.customerTypes,
      meta.customerRelationTypes,
      meta.emailTypes,
      meta.phoneNumberTypes
    ]),
  },
  [actions.customerUpdatePage]: {
    path: "/customers/:customer/update",
    fromPath: param => param,
    thunk: thunk([
      customer.update,
      meta.customerTypes,
      meta.customerRelationTypes,
      meta.emailTypes,
      meta.phoneNumberTypes
    ]),
  },

  [actions.contactListPage]: {
    path: "/contacts",
    thunk: thunk([
      contact.list
    ]),
  },
  [actions.contactNewPage]: {
    path: "/contacts/new",
    thunk: thunk([
      meta.emailTypes,
      meta.phoneNumberTypes
    ]),
  },
  [actions.contactViewPage]: {
    path: "/contacts/:contact",
    fromPath: param => param,
    thunk: thunk([
      contact.view,
      meta.emailTypes,
      meta.phoneNumberTypes
    ]),
  },
  [actions.contactUpdatePage]: {
    path: "/contacts/:contact/update",
    fromPath: param => param,
    thunk: thunk([
      contact.update,
      meta.emailTypes,
      meta.phoneNumberTypes
    ]),
  },

  [actions.markdownExamplePage]: {
    path: "/markdown",
    fromPath: param => param,
    thunk: thunk([
      markdown.view
    ]),
  },
};