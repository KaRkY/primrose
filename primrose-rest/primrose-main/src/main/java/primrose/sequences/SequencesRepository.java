package primrose.sequences;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SequencesRepository {

  private final NamedParameterJdbcTemplate template;

  public SequencesRepository(final NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  public long nextvalue(final Sequences sequence) {
    return template.queryForObject(
      "select nextval(:sequence_name)",
      new MapSqlParameterSource()
        .addValue("sequence_name", sequence.getName()),
      (rs, rownum) -> rs.getLong(1));
  }
}
