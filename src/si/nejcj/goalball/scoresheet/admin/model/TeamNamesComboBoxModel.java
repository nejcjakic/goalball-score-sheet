package si.nejcj.goalball.scoresheet.admin.model;

import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import si.nejcj.goalball.scoresheet.db.entity.Team;

@SuppressWarnings("rawtypes")
public class TeamNamesComboBoxModel extends AbstractListModel implements
    ComboBoxModel {

  private static final long serialVersionUID = -8081281030707551605L;

  private List<Team> teams;
  private Team selectedTeam;

  public TeamNamesComboBoxModel(List<Team> teams) {
    if (teams == null) {
      throw new IllegalArgumentException("List of teams should be initialized");
    }
    this.teams = teams;
    Collections.sort(this.teams);
  }

  public void addTeam(Team team) {
    teams.add(team);
    Collections.sort(teams);
    int index = teams.indexOf(team);
    setSelectedItem(team);
    fireContentsChanged(this, index, index);
  }

  @Override
  public Object getElementAt(int index) {
    return teams.get(index);
  }

  @Override
  public int getSize() {
    return teams.size();
  }

  @Override
  public Object getSelectedItem() {
    return selectedTeam;
  }

  @Override
  public void setSelectedItem(Object anItem) {
    selectedTeam = (Team) anItem;
  }
}
