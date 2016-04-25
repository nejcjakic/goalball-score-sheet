package si.nejcj.goalball.scoresheet.admin.model;

import java.util.List;

import javax.swing.DefaultComboBoxModel;

import si.nejcj.goalball.scoresheet.db.entity.OfficialLevel;

@SuppressWarnings("rawtypes")
public class OfficialLevelsComboBoxModel extends DefaultComboBoxModel {

  private static final long serialVersionUID = -3521272087584023359L;

  private List<OfficialLevel> officialLevels;

  public OfficialLevelsComboBoxModel(List<OfficialLevel> officialLevels) {
    this.officialLevels = officialLevels;
  }

  @Override
  public Object getElementAt(int index) {
    return officialLevels.get(index);
  }

  @Override
  public int getSize() {
    return officialLevels.size();
  }
}
