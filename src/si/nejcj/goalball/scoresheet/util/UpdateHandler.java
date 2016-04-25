package si.nejcj.goalball.scoresheet.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import si.nejcj.goalball.scoresheet.db.entity.Official;
import si.nejcj.goalball.scoresheet.db.entity.OfficialLevel;
import si.nejcj.goalball.scoresheet.db.entity.Player;
import si.nejcj.goalball.scoresheet.db.entity.Staff;
import si.nejcj.goalball.scoresheet.db.entity.Team;

public class UpdateHandler extends DefaultHandler {
  private StringBuilder tempCharacterData;

  private List<Official> officials;
  private Map<Team, List<Staff>> staff;
  private Map<Team, List<Player>> players;

  private List<Staff> teamStaff;
  private List<Player> teamPlayers;

  private boolean processingOfficial = false;
  private Official official;
  private Team team;
  private boolean processingStaff = false;
  private Staff staffMember;
  private boolean processingPlayer = false;
  private Player player;

  private static final String ELEMENT_LIST_OF_OFFICIALS = "listOfOfficials";
  private static final String ELEMENT_LIST_OF_TEAMS = "listOfTeams";
  private static final String ELEMENT_OFFICIAL = "official";
  private static final String ELEMENT_TEAM = "team";
  private static final String ELEMENT_STAFF = "staff";
  private static final String ELEMENT_PLAYER = "player";

  private static final String ELEMENT_LAST_NAME = "lastName";
  private static final String ELEMENT_FIRST_NAME = "firstName";
  private static final String ELEMENT_GENDER = "gender";
  private static final String ELEMENT_COUNTRY = "country";
  private static final String ELEMENT_OFFICIAL_LEVEL = "officialLevel";
  private static final String ELEMENT_PERSON_POSITION = "personPosition";

  private static final String ATTRIBUTE_TEAM_NAME = "teamName";
  private static final String ATTRIBUTE_TEAM_COUNTRY = "country";
  private static final String ATTRIBUTE_NATIONAL_TEAM = "nationalTeam";

  @Override
  public void startElement(String uri, String localName, String qName,
      Attributes attributes) {
    tempCharacterData = new StringBuilder();

    if (qName.equals(ELEMENT_LIST_OF_OFFICIALS)) {
      officials = new ArrayList<Official>();
    } else if (qName.equals(ELEMENT_LIST_OF_TEAMS)) {
      staff = new HashMap<Team, List<Staff>>();
      players = new HashMap<Team, List<Player>>();
    } else if (qName.equals(ELEMENT_OFFICIAL)) {
      processingOfficial = true;
      official = new Official();
    } else if (qName.equals(ELEMENT_TEAM)) {
      String teamName = attributes.getValue(ATTRIBUTE_TEAM_NAME);
      String teamCountry = attributes.getValue(ATTRIBUTE_TEAM_COUNTRY);
      boolean nationalTeam = Boolean.parseBoolean(attributes
          .getValue(ATTRIBUTE_NATIONAL_TEAM));
      team = new Team(null, teamName, teamCountry, nationalTeam);
      teamStaff = new ArrayList<Staff>();
      teamPlayers = new ArrayList<Player>();
    } else if (qName.equals(ELEMENT_STAFF)) {
      processingStaff = true;
      staffMember = new Staff();
    } else if (qName.equals(ELEMENT_PLAYER)) {
      processingPlayer = true;
      player = new Player();
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    String value = tempCharacterData.toString().trim();

    if (processingOfficial) {
      if (qName.equals(ELEMENT_OFFICIAL)) {
        officials.add(official);
        processingOfficial = false;
        official = null;
      } else if (qName.equals(ELEMENT_LAST_NAME)) {
        official.setLastName(value);
      } else if (qName.equals(ELEMENT_FIRST_NAME)) {
        official.setFirstName(value);
      } else if (qName.equals(ELEMENT_GENDER)) {
        official.setGender(value);
      } else if (qName.equals(ELEMENT_COUNTRY)) {
        official.setCountry(value);
      } else if (qName.equals(ELEMENT_OFFICIAL_LEVEL)) {
        official.setOfficialLevel(new OfficialLevel(-1, value, null));
      }
    } else if (processingStaff) {
      if (qName.equals(ELEMENT_STAFF)) {
        teamStaff.add(staffMember);
        processingStaff = false;
        staffMember = null;
      } else if (qName.equals(ELEMENT_LAST_NAME)) {
        staffMember.setLastName(value);
      } else if (qName.equals(ELEMENT_FIRST_NAME)) {
        staffMember.setFirstName(value);
      } else if (qName.equals(ELEMENT_PERSON_POSITION)) {
        staffMember.setPersonPosition(value);
      }
    } else if (processingPlayer) {
      if (qName.equals(ELEMENT_PLAYER)) {
        teamPlayers.add(player);
        processingPlayer = false;
        player = null;
      } else if (qName.equals(ELEMENT_LAST_NAME)) {
        player.setLastName(value);
      } else if (qName.equals(ELEMENT_FIRST_NAME)) {
        player.setFirstName(value);
      }
    } else if (qName.equals(ELEMENT_TEAM)) {
      staff.put(team, teamStaff);
      players.put(team, teamPlayers);
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    tempCharacterData.append(ch, start, length);
  }

  public List<Official> getOfficials() {
    return officials;
  }

  public Map<Team, List<Staff>> getTeamStaff() {
    return staff;
  }

  public Map<Team, List<Player>> getTeamPlayers() {
    return players;
  }
}
