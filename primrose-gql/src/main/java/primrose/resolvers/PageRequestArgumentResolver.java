package primrose.resolvers;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import primrose.rest.PageRequest;
import primrose.rest.PageRequest.PageRequestBuilder;
import primrose.rest.Sort;

@Component
public class PageRequestArgumentResolver implements HandlerMethodArgumentResolver {

  private ConversionService conversionService;

  public PageRequestArgumentResolver(ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return PageRequest.class.equals(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(
    MethodParameter parameter,
    ModelAndViewContainer mavContainer,
    NativeWebRequest webRequest,
    WebDataBinderFactory binderFactory)
    throws Exception {

    PageRequestBuilder paginationBuilder = PageRequest.builder();
    paginationBuilder.page(conversionService.convert(webRequest.getParameter("page"), Long.class));
    paginationBuilder.size(conversionService.convert(webRequest.getParameter("size"), Long.class));
    paginationBuilder.search(webRequest.getParameter("search"));


    if (webRequest.getParameter("sort") != null) {
      paginationBuilder.sortProperties(
      StreamSupport
        .stream(Splitter
          .on(",")
          .trimResults()
          .split(webRequest.getParameter("sort"))
          .spliterator(), false)
        .flatMap(sort -> {
          Iterator<String> splited = Splitter.on(" ").trimResults().omitEmptyStrings().split(sort).iterator();
          if(splited.hasNext()) {
            return Stream
              .of(Sort.builder()
              .property(splited.next())
              .direction(splited.hasNext() ? splited.next() : null)
              .build());
          } else {
            return Stream.empty();
          }
        })
        .collect(ImmutableList.toImmutableList()));
    }

    return paginationBuilder.build();
  }

}
