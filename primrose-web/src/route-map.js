import AppActions from "./app/actions";
import DashboardActions from "./app/dashboard/actions";
import AccountActions from "./app/accounts/actions";
import ContactsActions from "./app/contacts/actions";

export default {
  [AppActions.types.ROOT_SCENE_REQUESTED]: "/",
  [DashboardActions.types.DASHBOARD_SCENE_REQUESTED]: "/dashboard",
  [AccountActions.types.ACCOUNTS_SCENE_REQUESTED]: "/accounts",
  [ContactsActions.types.CONTACTS_SCENE_REQUESTED]: "/contacts",
};