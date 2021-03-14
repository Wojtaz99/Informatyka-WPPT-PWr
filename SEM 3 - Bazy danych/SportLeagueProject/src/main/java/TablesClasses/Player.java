package TablesClasses;

import javax.persistence.*;

@Entity
@Table(name = "zawodnik", schema = "sportleague", uniqueConstraints = {
        @UniqueConstraint(columnNames = "idZawodnika")})
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idZawodnika", unique = true, nullable = false)
    private int playerId;
    @Column(name = "idDruzyny", nullable = false)
    private int teamId;
    @Column(name = "imie", nullable = false)
    private String name;
    @Column(name = "nazwisko", nullable = false)
    private String surname;
    @Column(name = "plec", nullable = false)
    private String sex;
    @Column(name = "rokUrodzenia", nullable = false)
    private int birthYear;
    @Column(name = "numerZawodnika", nullable = false)
    private int playerNumber;
    @Column(name = "zdobytePunkty", nullable = false)
    private int scoredPoints;

    public int getScoredPoints() {
        return scoredPoints;
    }

    public void setScoredPoints(int scoredPoints) {
        this.scoredPoints = scoredPoints;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
