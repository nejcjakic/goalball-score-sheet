package si.nejcj.goalball.scoresheet.tournament.model;

import java.util.List;

import javax.swing.DefaultComboBoxModel;

import si.nejcj.goalball.scoresheet.db.entity.TournamentTeam;

@SuppressWarnings("rawtypes")
public class TournamentTeamsComboBoxModel extends DefaultComboBoxModel {

  private static final long serialVersionUID = 1L;

  private List<TournamentTeam> tournamentTeams;

  public TournamentTeamsComboBoxModel(List<TournamentTeam> tournamentTeams) {
    this.tournamentTeams = tournamentTeams;
  }

  @Override
  public Object getElementAt(int index) {
    return tournamentTeams.get(index);
  }

  @Override
  public int getSize() {
    return tournamentTeams.size();
  }
}