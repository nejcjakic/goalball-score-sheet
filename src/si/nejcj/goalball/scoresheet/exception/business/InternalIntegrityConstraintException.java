package si.nejcj.goalball.scoresheet.exception.business;

import java.sql.SQLException;

public class InternalIntegrityConstraintException extends
    InternalBusinessException {

  private static final long serialVersionUID = 1L;

  private static final String DEFAULT_MESSAGE = "Entity is referenced by another entity";

  public InternalIntegrityConstraintException(SQLException cause) {
    this(DEFAULT_MESSAGE, cause);
  }

  public InternalIntegrityConstraintException(String message) {
    super(message);
  }

  public InternalIntegrityConstraintException(String message, SQLException cause) {
    super(message, cause);
  }
}
