package primrose.accounts;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import primrose.addresses.ImmutableSearchAddress;
import primrose.pagging.sort.SortUtil;

public class AccountSearchWebArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(final MethodParameter parameter) {
    return AccountsSearch.class.equals(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(
    final MethodParameter parameter,
    final ModelAndViewContainer mavContainer,
    final NativeWebRequest webRequest,
    final WebDataBinderFactory binderFactory) throws Exception {
    final ImmutableAccountsSearch.Builder accountsSearchBuilder = ImmutableAccountsSearch.builder();

    if (webRequest.getParameter("page") != null) {
      accountsSearchBuilder.page(Integer.parseInt(webRequest.getParameter("page")));
    }

    if (webRequest.getParameter("size") != null) {
      accountsSearchBuilder.size(Integer.parseInt(webRequest.getParameter("size")));
    }

    return accountsSearchBuilder
      .account(ImmutableSearchAccount
        .builder()
        .type(webRequest.getParameter("account.type"))
        .displayName(webRequest.getParameter("account.displayName"))
        .name(webRequest.getParameter("account.name"))
        .email(webRequest.getParameter("account.email"))
        .phone(webRequest.getParameter("account.phone"))
        .website(webRequest.getParameter("account.website"))
        .description(webRequest.getParameter("account.description"))
        .build())
      .address(ImmutableSearchAddress
        .builder()
        .street(webRequest.getParameter("address.street"))
        .streetNumber(webRequest.getParameter("address.streetNumber"))
        .city(webRequest.getParameter("address.city"))
        .postalCode(webRequest.getParameter("address.postalCode"))
        .state(webRequest.getParameter("address.state"))
        .country(webRequest.getParameter("address.country"))
        .build())
      .sort(SortUtil.parseSort(webRequest.getParameter("sort")))
      .build();
  }

}
