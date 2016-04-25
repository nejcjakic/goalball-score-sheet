package si.nejcj.goalball.scoresheet.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.AbstractListHandler;

public class IntegerListHandler extends AbstractListHandler<Integer> {

  @Override
  protected Integer handleRow(ResultSet rs) throws SQLException {
    return rs.getInt(1);
  }
}
