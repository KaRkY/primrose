package primrose.rpcservices;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import primrose.services.ImmutableCustomersSearchResult;

@Value.Immutable
@Value.Style(deepImmutablesDetection = true, depluralize = true, throwForInvalidImmutableState = IllegalArgumentException.class)
@JsonDeserialize(as = ImmutableCustomersSearchResult.class)
public interface CustomersSearchResult {

  List<CustomerSearch> data();

  long count();
}
