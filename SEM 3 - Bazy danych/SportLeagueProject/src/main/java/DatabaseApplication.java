import TablesClasses.Match;
import TablesClasses.Player;
import TablesClasses.Team;
import TablesClasses.Tournament;
import org.apache.log4j.varia.NullAppender;
import org.hibernate.*;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class DatabaseApplication {

    DatabaseApplication() {}

    public static List queries(String[] args) {
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());
        Session session = HibernateUtil.getSessionFactory().openSession();
        List result = null;
        session.beginTransaction();
        if(args[0].equals("getTeams")) {
            Query query = session.createQuery("SELECT teamName FROM TablesClasses.Team WHERE teamId != 1");
            return query.list();
        }
        else if(args[0].equals("teamCity")) {
            Query query = session.createQuery("SELECT city FROM TablesClasses.Team WHERE teamId != 1");
            return query.list();
        }
        else if(args[0].equals("teamFoundationYear")) {
            Query query = session.createQuery("SELECT foundationYear FROM TablesClasses.Team WHERE teamId != 1");
            return query.list();
        }
        else if(args[0].equals("teamDivision")) {
            Query query = session.createQuery("SELECT division FROM TablesClasses.Team WHERE teamId != 1");
            return query.list();
        }
        else if(args[0].equals("teamWins")) {
            Query query = session.createQuery("SELECT wins FROM TablesClasses.Team WHERE teamId != 1");
            return query.list();
        }
        else if(args[0].equals("teamDraws")) {
            Query query = session.createQuery("SELECT draws FROM TablesClasses.Team WHERE teamId != 1");
            return query.list();
        }
        else if(args[0].equals("teamLosts")) {
            Query query = session.createQuery("SELECT losts FROM TablesClasses.Team WHERE teamId != 1");
            return query.list();
        }
        else if(args[0].equals("teamPoints")) {
            Query query = session.createQuery("SELECT scoredPoints FROM TablesClasses.Team WHERE teamId != 1");
            return query.list();
        }
        else if(args[0].equals("getTournaments")) {
            Query query = session.createQuery("SELECT tournamentName FROM TablesClasses.Tournament");
            return query.list();
        }
        else if(args[0].equals("tournamentDate")) {
            Query query = session.createQuery("SELECT tournamentDate FROM TablesClasses.Tournament");
            return query.list();
        }
        else if(args[0].equals("tournamentLocation")) {
            Query query = session.createQuery("SELECT location FROM TablesClasses.Tournament");
            return query.list();
        }
        else if(args[0].equals("tournamentDivision")) {
            Query query = session.createQuery("SELECT division FROM TablesClasses.Tournament");
            return query.list();
        }
        else if(args[0].equals("getTournamentId")) {
            Query query = session.createQuery("SELECT tournamentId FROM TablesClasses.Tournament WHERE tournamentName = :tournament");
            query.setParameter("tournament", args[1]);
            return query.list();
        }
        else if(args[0].equals("playerName")) {
            if (args[2].equals("1")) {
                Query query = session.createQuery("SELECT name FROM TablesClasses.Player WHERE playerId != 1 ORDER BY scoredPoints DESC");
                query.setMaxResults(Integer.parseInt(args[1]));
                return query.list();
            }
            else {
                Query query = session.createQuery("SELECT name FROM TablesClasses.Player WHERE teamId = :druzyna AND playerId != 1");
                query.setParameter("druzyna", Integer.parseInt(args[1]));
                return query.list();
            }
        }
        else if(args[0].equals("playerSurname")) {
            if (args[2].equals("1")) {
                Query query = session.createQuery("SELECT surname FROM TablesClasses.Player WHERE playerId != 1 ORDER BY scoredPoints DESC");
                query.setMaxResults(Integer.parseInt(args[1]));
                return query.list();
            }
            else {
                Query query = session.createQuery("SELECT surname FROM TablesClasses.Player WHERE teamId = :druzyna AND playerId != 1");
                query.setParameter("druzyna", Integer.parseInt(args[1]));
                return query.list();
            }
        }
        else if(args[0].equals("playerTeam")) {
            //TODO Tu
            SQLQuery query = session.createSQLQuery("SELECT nazwaDruzyny FROM Druzyna JOIN Zawodnik ON Druzyna.idDruzyny = Zawodnik.idDruzyny  WHERE Druzyna.idDruzyny <> 1 AND Zawodnik.idZawodnika <> 1 ORDER BY Zawodnik.zdobytePunkty");
            query.setMaxResults(Integer.parseInt(args[1]));
            return query.list();
        }
        else if(args[0].equals("playerNumber")) {
            if (args[2].equals("1")) {
                Query query = session.createQuery("SELECT playerNumber FROM TablesClasses.Player WHERE playerId != 1 ORDER BY scoredPoints DESC");
                query.setMaxResults(Integer.parseInt(args[1]));
                return query.list();
            }
            else {
                Query query = session.createQuery("SELECT playerNumber FROM TablesClasses.Player WHERE teamId = :druzyna AND playerId != 1");
                query.setParameter("druzyna", Integer.parseInt(args[1]));
                return query.list();
            }
        }
        else if(args[0].equals("playerSex")) {
            if (args[2].equals("1")) {
            Query query = session.createQuery("SELECT sex FROM TablesClasses.Player WHERE playerId != 1 ORDER BY scoredPoints DESC");
                query.setMaxResults(Integer.parseInt(args[1]));
            return query.list();
            }
            else {
                Query query = session.createQuery("SELECT sex FROM TablesClasses.Player WHERE teamId = :druzyna AND playerId != 1");
                query.setParameter("druzyna", Integer.parseInt(args[1]));
                return query.list();
            }
        }
        else if(args[0].equals("playerBirth")) {
            if (args[2].equals("1")) {
            Query query = session.createQuery("SELECT birthYear FROM TablesClasses.Player WHERE playerId != 1 ORDER BY scoredPoints DESC");
            query.setMaxResults(Integer.parseInt(args[1]));
            return query.list();
            }
            else {
                Query query = session.createQuery("SELECT birthYear FROM TablesClasses.Player WHERE teamId = :druzyna AND playerId != 1");
                query.setParameter("druzyna", Integer.parseInt(args[1]));
                return query.list();
            }
        }
        else if(args[0].equals("playerScoredPoints")) {
            if (args[2].equals("1")) {
            Query query = session.createQuery("SELECT scoredPoints FROM TablesClasses.Player WHERE playerId != 1 ORDER BY scoredPoints DESC");
            query.setMaxResults(Integer.parseInt(args[1]));
            return query.list();
            }
            else {
                Query query = session.createQuery("SELECT scoredPoints FROM TablesClasses.Player WHERE teamId = :druzyna AND playerId != 1");
                query.setParameter("druzyna", Integer.parseInt(args[1]));
                return query.list();
            }
        }
        else if(args[0].equals("getTeamId")) {
            Query query = session.createQuery("SELECT teamId FROM TablesClasses.Team WHERE teamName = :name");
            query.setParameter("name", args[1]);
            return query.list();
        }
        else if(args[0].equals("getMatchById")) {
            Query query = session.createQuery("FROM TablesClasses.Match WHERE idMeczu = :idMeczu");
            query.setParameter("idMeczu", Integer.parseInt(args[1]));
            result = query.list();
        }
        else if(args[0].equals("requestTournaments")) {
            Query query = session.createQuery("FROM TablesClasses.Tournament WHERE organizerId = :organizerId");
            query.setParameter("organizerId", Integer.parseInt(args[1]));
            return query.list();
        }
        else if(args[0].equals("getTeamNameById")) {
            Query query = session.createQuery("SELECT teamName FROM TablesClasses.Team WHERE teamId = :teamId");
            query.setParameter("teamId", Integer.parseInt(args[1]));
            result = query.list();
            return result;
        }
        else if(args[0].equals("getTeamsIdInMatch")) {
            Query query = session.createQuery("SELECT idDruzynyPierwszej, idDruzynyDrugiej FROM TablesClasses.Match WHERE idMeczu = :idMeczu");
            query.setParameter("idMeczu", Integer.parseInt(args[1]));
            return query.list();
        }
        else if(args[0].equals("tournamentNameById")) {
            Query query = session.createQuery("SELECT tournamentName FROM TablesClasses.Tournament WHERE tournamentId = :tournamentId");
            query.setParameter("tournamentId", Integer.parseInt(args[1]));
            return query.list();
        }
        else if(args[0].equals("deleteRecentEvent")) {
            try {
                Query query = session.createSQLQuery("SELECT id FROM punktacjameczu WHERE idMeczu = :idMeczu ORDER BY id DESC LIMIT 1");
                query.setParameter("idMeczu", Integer.parseInt(args[1]));
                result = query.list();
                query = session.createSQLQuery("DELETE FROM punktacjameczu WHERE id = :id");
                query.setParameter("id", Integer.parseInt(result.get(0).toString()));
                query.executeUpdate();
                result = null;
            }
            catch (Exception e){
                result = new ArrayList();
                result.add("NoMoreMoves");
            }
        }
        else if(args[0].equals("matchDetailsByMatchId")) {
            Query query = session.createQuery("FROM TablesClasses.Scores WHERE idMeczu = :idMeczu");
            query.setParameter("idMeczu", Integer.parseInt(args[1]));
            result = query.list();
            return result;
        }
        else if(args[0].equals("checkPassword")) {
            SQLQuery query = session.createSQLQuery("call sprawdzHaslo(:pass, :id)");
            query.setParameter("pass", args[1]);
            query.setParameter("id", Integer.parseInt(args[2]));
            result = query.list();
            return result;
        }
        else if(args[0].equals("addScore")) {
            Query query = session.createSQLQuery("call dodajPunkt(:idMeczu, :idDruzyny, :numerZawodnika, :czyPierwsza)");
            query.setParameter("idMeczu", Integer.parseInt(args[1]));
            query.setParameter("idDruzyny", Integer.parseInt(args[2]));
            query.setParameter("numerZawodnika", Integer.parseInt(args[3]));
            query.setParameter("czyPierwsza", args[4]);
            try {
                result = query.list();
                return result;
            }
            catch (NullPointerException e) {
                System.out.println("Poprawnie dodano punkt");
                result = new ArrayList();
                return result;
            }
        }
        else if(args[0].equals("endMatch")) {
            try {
                SQLQuery query = session.createSQLQuery("call zakonczMecz(:idMeczu)");
                query.setParameter("idMeczu", Integer.parseInt(args[1]));
                query.executeUpdate();
            }
            catch (Exception e) {
                result = new ArrayList();
                result.add("smthWentWrong");
            }
        }
        else if(args[0].equals("requestMatchesByTournamentId")) {
            Query query = session.createQuery("FROM TablesClasses.Match WHERE idTurnieju = :tournamentId");
            query.setParameter("tournamentId", Integer.parseInt(args[1]));
            return query.list();
        }
        else if(args[0].equals("addMatch")) {
            Query query = session.createQuery("SELECT COUNT(*) FROM TablesClasses.Match");
            Match match = new Match();
            match.setIdMeczu(Integer.parseInt(String.valueOf(query.list().get(0)))+1);
            query = session.createQuery("SELECT teamId FROM TablesClasses.Team WHERE teamName = :teamName AND teamId != 1");
            query.setParameter("teamName", args[1]);
            match.setIdDruzynyPierwszej(Integer.parseInt(String.valueOf(query.list().get(0))));
            query = session.createQuery("SELECT teamId FROM TablesClasses.Team WHERE teamName = :teamName AND teamId != 1");
            query.setParameter("teamName", args[2]);
            match.setIdDruzynyDrugiej(Integer.parseInt(String.valueOf(query.list().get(0))));
            query = session.createQuery("SELECT tournamentId FROM TablesClasses.Tournament WHERE tournamentName = :tournamentName");
            query.setParameter("tournamentName", args[3]);
            match.setIdTurnieju(Integer.parseInt(String.valueOf(query.list().get(0))));
            match.setPunktyDruzynyPierwszej(0);
            match.setPunktyDruzynyDrugiej(0);
            session.save(match);
        }
        else if(args[0].equals("allTeamsByDivision")) {
            Query query = session.createQuery("SELECT division FROM TablesClasses.Tournament WHERE tournamentId = :tournamentId");
            query.setParameter("tournamentId", Integer.parseInt(args[1]));
            String division = String.valueOf(query.list().get(0));
            query = session.createQuery("SELECT teamName FROM TablesClasses.Team WHERE division = :division AND teamId != 1");
            query.setParameter("division", division);
            return query.list();
        }
        else if(args[0].equals("allTeams")) {
            Query query = session.createQuery("FROM TablesClasses.Team WHERE teamId != 1");
            return query.list();
        }
        else if(args[0].equals("myAllTeams")) {
            Query query = session.createQuery("FROM TablesClasses.Team WHERE capitanUserId = :capitanUserId AND teamId != 1");
            query.setParameter("capitanUserId", Integer.parseInt(args[1]));
            return query.list();
        }
        else if(args[0].equals("allTournaments")) {
            Query query = session.createQuery("SELECT tournamentName FROM TablesClasses.Tournament ORDER BY tournamentDate DESC");
            return query.list();
        }
        else if(args[0].equals("userCounter")) {
            Query query = session.createQuery("SELECT COUNT(*) FROM TablesClasses.AppUser WHERE czyZatwierdzony = 0");
            return query.list();
        }
        else if(args[0].equals("capitanTeams")) {
            SQLQuery query = session.createSQLQuery("SELECT nazwaDruzyny FROM druzyna WHERE idUzytkownikaKapitana = :idUzytkownikaKapitana AND idDruzyny <> 1");
            query.setParameter("idUzytkownikaKapitana", args[1]);
            return query.list();
        }
        else if(args[0].equals("countTeams")) {
            Query query = session.createQuery("SELECT COUNT(*) FROM TablesClasses.Team WHERE idUzytkownikaKapitana = :idUzytkownikaKapitana AND teamId != 1");
            query.setParameter("idUzytkownikaKapitana", args[1]);
            return query.list();
        }
        else if(args[0].equals("requestsLogins")) {
            Query query = session.createQuery("SELECT login FROM TablesClasses.AppUser WHERE czyZatwierdzony = 0 ORDER BY idUzytkownika");
            return query.list();
        }
        else if(args[0].equals("requestsFirstNames")) {
            Query query = session.createQuery("SELECT imie FROM TablesClasses.AppUser WHERE czyZatwierdzony = 0 ORDER BY idUzytkownika");
            return query.list();
        }
        else if(args[0].equals("requestsNames")) {
            Query query = session.createQuery("SELECT nazwisko FROM TablesClasses.AppUser WHERE czyZatwierdzony = 0 ORDER BY idUzytkownika");
            return query.list();
        }
        else if(args[0].equals("teamIdByName")) {
            Query query = session.createQuery("SELECT teamId FROM TablesClasses.Team WHERE teamName = :teamName");
            query.setParameter("teamName", args[1]);
            return query.list();
        }
        else if(args[0].equals("deleteTeam")) {
            Query query = session.createQuery("DELETE FROM TablesClasses.Team WHERE teamId = :teamId");
            query.setParameter("teamId", Integer.parseInt(args[1]));
            query.executeUpdate();
        }
        else if(args[0].equals("requestsPesels")) {
            Query query = session.createQuery("SELECT pesel FROM TablesClasses.AppUser WHERE czyZatwierdzony = 0 ORDER BY idUzytkownika");
            return query.list();
        }
        else if(args[0].equals("requestsPermissions")) {
            Query query = session.createQuery("SELECT poziomUprawnien FROM TablesClasses.AppUser WHERE czyZatwierdzony = 0 ORDER BY idUzytkownika");
            return query.list();
        }
        else if(args[0].equals("userPermissionLevel")) {
            Query query = session.createQuery("SELECT poziomUprawnien FROM TablesClasses.AppUser WHERE idUzytkownika = :idUzytkownika");
            query.setParameter("idUzytkownika", Integer.parseInt(args[1]));
            return query.list();
        }
        else if(args[0].equals("playerById")) {
            Query query = session.createQuery("FROM TablesClasses.Player WHERE playerId = :playerId"); // tu sie chyba nie popsuje
            query.setParameter("playerId", Integer.parseInt(args[1]));
            return query.list();
        }
        else if(args[0].equals("confirmUser")) {
            Query query = session.createQuery("SELECT idUzytkownika FROM TablesClasses.AppUser WHERE login = :login");
            query.setParameter("login", args[1]);
            int id = Integer.parseInt(String.valueOf(query.list().get(0)));
            query = session.createQuery("UPDATE TablesClasses.AppUser set czyZatwierdzony = true WHERE idUzytkownika = :idUzytkownika");
            query.setParameter("idUzytkownika", id);
            query.executeUpdate();
        }
        else if(args[0].equals("declineUser")) {
            Query query = session.createQuery("SELECT idUzytkownika FROM TablesClasses.AppUser WHERE login = :login");
            query.setParameter("login", args[1]);
            int id = Integer.parseInt(String.valueOf(query.list().get(0)));
            query = session.createQuery("DELETE FROM TablesClasses.AppUser WHERE idUzytkownika = :idUzytkownika");
            query.setParameter("idUzytkownika", id);
            query.executeUpdate();

        }
        else if(args[0].equals("addUser")) {
            try {
                SQLQuery query = session.createSQLQuery("call dodajUzytkownika(:login, :haslo, :imie, :nazwisko, :pesel, :poziomUprawnien)");
                query.setParameter("login", args[1]);
                query.setParameter("haslo", args[2]);
                query.setParameter("imie", args[3]);
                query.setParameter("nazwisko", args[4]);
                query.setParameter("pesel", args[5]);
                query.setParameter("poziomUprawnien", Integer.parseInt(args[6]));
                query.executeUpdate();
            }
            catch (Exception e) {
                result = new ArrayList();
                result.add("duplicateLogin");
            }
        }
        else if(args[0].equals("loggIn")) {
            Query query = session.createSQLQuery("call zaloguj(:login, :haslo)");
            query.setParameter("login", args[1]);
            query.setParameter("haslo", args[2]);
            result = query.list();
            query = session.createQuery("SELECT poziomUprawnien FROM TablesClasses.AppUser WHERE login = :login");
            query.setParameter("login", args[1]);
            List temp = query.list();
            if(temp.size() == 0) {
                System.out.println("Nieprawidlowe dane");
            }
            else {
                result.add(temp.get(0));
                query = session.createQuery("SELECT idUzytkownika FROM TablesClasses.AppUser WHERE login = :login");
                query.setParameter("login", args[1]);
                temp = query.list();
                result.add(temp.get(0));
            }
            return result;
        }
        else if(args[0].equals("addPlayer")) {
            try {
                SQLQuery query = session.createSQLQuery("SELECT idDruzyny FROM druzyna WHERE nazwaDruzyny = :nazwaDruzyny");
                query.setParameter("nazwaDruzyny", args[6]);
                result = query.list();
                int teamId = Integer.parseInt(String.valueOf(result.get(0)));
                query = session.createSQLQuery("SELECT COUNT(*) FROM zawodnik");
                result = query.list();
                Player player = new Player();
                player.setPlayerId(Integer.parseInt(String.valueOf(result.get(0))));
                player.setName(args[1]);
                player.setSurname(args[2]);
                player.setSex(args[3]);
                player.setBirthYear(Integer.parseInt(args[4]));
                player.setPlayerNumber(Integer.parseInt(args[5]));
                player.setTeamId(teamId);
                session.save(player);
                result = null;
            }
            catch (ConstraintViolationException e) {
                result = new ArrayList();
                result.add("wrongNumber");
                return result;
            }
            catch (Exception ex) {
                result = new ArrayList();
                if (ex instanceof GenericJDBCException) {
                    if(String.valueOf(((GenericJDBCException) ex).getSQLException()).equals("java.sql.SQLException: Wrong team for this player!")) {
                        result.add("wrongDivision");
                    }
                    else {
                        if(String.valueOf(((GenericJDBCException) ex).getSQLException()).equals("java.sql.SQLException: Invalid date!")) {
                            result.add("wrongAge");
                        }
                        else if(String.valueOf(((GenericJDBCException) ex).getSQLException()).equals("java.sql.SQLException: Player with this number already exists!")) {
                            result.add("wrongNumber");
                        }
                    }
                }
                else {
                    result.add("smthWentWrong");
                }
                return result;
            }
        }
        else if(args[0].equals("addTeam")) {
            Query query = session.createQuery("SELECT COUNT(*) FROM TablesClasses.Team");
            int id = Integer.parseInt(String.valueOf(query.list().get(0)));
            Team team = new Team();
            team.setTeamId(id);
            team.setTeamName(args[1]);
            team.setCity(args[2]);
            team.setFoundationYear(Integer.parseInt(args[3]));
            team.setDivision(args[4]);
            team.setDraws(0);
            team.setScoredPoints(0);
            team.setWins(0);
            team.setLosts(0);
            team.setCapitanUserId(Integer.parseInt(args[5]));
            session.save(team);
        }
        else if(args[0].equals("addTournament")) {
            Query query = session.createQuery("SELECT COUNT(*) FROM TablesClasses.Tournament");
            int id = Integer.parseInt(String.valueOf(query.list().get(0)));
            Tournament tournament = new Tournament();
            tournament.setTournamentId(id);
            tournament.setTournamentName(args[1]);
            SimpleDateFormat formatter =new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = formatter.parse(args[3]);
            } catch (ParseException e) {
                return new ArrayList(Arrays.asList("wrongDateFormat"));
            }
            tournament.setTournamentDate(new java.sql.Date(date.getTime()));
            tournament.setDivision(args[4]);
            tournament.setLocation(args[2]);
            tournament.setOrganizerId(Integer.parseInt(args[5]));
            session.save(tournament);
        }
        session.getTransaction().commit();
        if(result == null) {
            result = new ArrayList();
        }
        return result;
    }
}
