package si.nejcj.goalball.scoresheet.tournament.model;

import java.util.List;

import javax.swing.DefaultComboBoxModel;

import si.nejcj.goalball.scoresheet.db.entity.TournamentOfficial;

@SuppressWarnings("rawtypes")
public class TournamentOfficialsComboBoxModel extends DefaultComboBoxModel {

  private static final long serialVersionUID = 1L;

  private List<TournamentOfficial> tournamentOfficials;

  public TournamentOfficialsComboBoxModel(
      List<TournamentOfficial> tournamentOfficials) {
    this.tournamentOfficials = tournamentOfficials;
  }

  @Override
  public Object getElementAt(int index) {
    return tournamentOfficials.get(index);
  }

  @Override
  public int getSize() {
    return tournamentOfficials.size();
  }
}