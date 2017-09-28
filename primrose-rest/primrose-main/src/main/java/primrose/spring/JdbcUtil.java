package primrose.spring;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JdbcUtil {

  public static LocalDate toLocalDate(final Date date) {
    if (date == null) {
      return null;
    } else {
      return date.toLocalDate();
    }
  }

  public static LocalDateTime toLocalDateTime(final Timestamp date) {
    if (date == null) {
      return null;
    } else {
      return date.toLocalDateTime();
    }
  }

  public static Date toDate(final LocalDate date) {
    if (date == null) {
      return null;
    } else {
      return Date.valueOf(date);
    }
  }

  public static Timestamp toTimestamp(final LocalDateTime date) {
    if (date == null) {
      return null;
    } else {
      return Timestamp.valueOf(date);
    }
  }
}
