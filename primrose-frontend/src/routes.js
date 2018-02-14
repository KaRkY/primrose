import PageHome from "./components/pages/PageHome";
import PageCustomers from "./components/pages/PageCustomers";
import PageCustomer from "./components/pages/PageCustomer";
import PageNotFound from "./components/pages/PageNotFound";

export default [{
  name: "Home",
  path: "",
  match: {
    response: ({ set }) => {
      set.body(PageHome);
      set.title("Home");
    }
  },
}, {
  name: "Customers",
  path: "customers",
  match: {
    response: ({ set }) => {
      set.body(PageCustomers);
      set.title("Customers");
    }
  },
  children: [{
    name: "Customer",
    path: ":id",
    match: {
      response: ({ set, resolved }) => {
        set.body(PageCustomer);
        set.title("Customer");
      }
    },
  }]
},{
  name: "Not Found",
  path: "(.*)",
  match: {
    response: ({ set }) => {
      set.body(PageNotFound);
      set.title("Not Found");
    }
  },
}];