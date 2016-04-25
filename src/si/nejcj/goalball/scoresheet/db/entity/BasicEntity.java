package si.nejcj.goalball.scoresheet.db.entity;

public abstract class BasicEntity<T> {
  protected Integer id;
  protected boolean adminData;
  
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public boolean isAdminData() {
    return adminData;
  }

  public void setAdminData(boolean adminData) {
    this.adminData = adminData;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    BasicEntity<?> other = (BasicEntity<?>) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  public abstract boolean hasEqualData(T other);
}
