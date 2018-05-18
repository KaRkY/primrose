import { NOT_FOUND } from "redux-first-router";
import { createAction } from "redux-actions";

const createLoadActions = entity => {
  const load = createAction(`${entity}_LOAD`);
  const loadFinished = createAction(`${entity}_FINISHED`);
  const loadError = createAction(`${entity}_ERROR`);
  return [
    load,
    loadFinished,
    loadError,
  ];
};

const handlePaginated = ({
  force,
  page,
  size,
  sort,
  search,
  selected,
  ...rest,
}) => {
  const query = {
    page,
    size,
    selected,
  };

  if(sort) {
    query.sortProperty = sort.property;
    query.sortDirection = sort.direction;
  }

  if(search && search !== "") {
    query.search = search;
  }

  return {
    force,
    query,
  };
};

export const [pageLoad, pageFinished, pageError] = createLoadActions("PAGE");

export const dashboardPage = createAction("DASHBOARD_PAGE");

export const customerListPage = createAction("CUSTOMER_LIST_PAGE", handlePaginated);
export const customerViewPage = createAction("CUSTOMER_VIEW_PAGE", customer => ({ customer }));
export const customerNewPage = createAction("CUSTOMER_NEW_PAGE");
export const customerUpdatePage = createAction("CUSTOMER_EDIT_PAGE", customer => ({ customer }));
export const [customerListLoad, customerListFinished, customerListError] = createLoadActions("CUSTOMER_LIST");
export const [customerViewLoad, customerViewFinished, customerViewError] = createLoadActions("CUSTOMER_VIEW");
export const [customerUpdateLoad, customerUpdateFinished, customerUpdateError] = createLoadActions("CUSTOMER_EDIT");

export const contactListPage = createAction("CONTACT_LIST_PAGE", handlePaginated);
export const contactViewPage = createAction("CONTACT_VIEW_PAGE", contact => ({ contact }));
export const contactNewPage = createAction("CONTACT_NEW_PAGE");
export const contactUpdatePage = createAction("CONTACT_EDIT_PAGE", contact => ({ contact }));
export const [contactListLoad, contactListFinished, contactListError] = createLoadActions("CONTACT_LIST");
export const [contactViewLoad, contactViewFinished, contactViewError] = createLoadActions("CONTACT_VIEW");
export const [contactUpdateLoad, contactUpdateFinished, contactUpdateError] = createLoadActions("CONTACT_EDIT");

export const markdownExamplePage = createAction("MARKDOWN_EXAMPLE_PAGE");
export const [markdownExampleLoad, markdownExampleFinished, markdownExampleError] = createLoadActions("MARKDOWN_EXAMPLE");


export const [customerTypesLoad, customerTypesFinished, customerTypesError] = createLoadActions("CUSTOMER_TYPES");
export const [customerRelationTypesLoad, customerRelationTypesFinished, customerRelationTypesError] = createLoadActions("CUSTOMER_RELATION_TYPES");
export const [emailTypesLoad, emailTypesFinished, emailTypesError] = createLoadActions("EMAIL_TYPES");
export const [phoneNumberTypesLoad, phoneNumberTypesFinished, phoneNumberTypesError] = createLoadActions("PHONE_NUMBER_TYPES");

export const notFound = createAction(NOT_FOUND);