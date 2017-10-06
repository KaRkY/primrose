package primrose.hal;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface HalResource {

  @JsonProperty("_links")
  Map<String, Link> links();

  @JsonProperty("_embedded")
  Map<String, Object> embedded();
}
