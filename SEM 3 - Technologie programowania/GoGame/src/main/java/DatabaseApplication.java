import org.apache.log4j.varia.NullAppender;
import org.hibernate.*;
import org.hibernate.boot.model.relational.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** Klasa kwerend */
class DatabaseApplication {

    DatabaseApplication() {}

    /** Metoda egzekwujaca kwerendy */
     static List queries(String[] args) {
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List result = null;
        Query query;
        switch (args[0]) {
             case "date list":
                 query = session.createSQLQuery("SELECT DATE_SUB(gameDate, INTERVAL 1 HOUR ) FROM SavedGames");
                 result = query.list();
                 break;
             case "newGame":
                 SavedGames savedGames = new SavedGames();
                 Date date = new Date();
                 Calendar cal = Calendar.getInstance(); // creates calendar

                 cal.setTime(date); // sets calendar time/date

                 cal.add(Calendar.HOUR, 1); // adds one hour

                 Date data = cal.getTime();
                 java.sql.Date sDate = new java.sql.Date(data.getTime());
                 query = session.createQuery("SELECT COUNT(*) FROM SavedGames");
                 savedGames.setId(Integer.parseInt(String.valueOf(query.list().get(0))));
                 savedGames.setDate(sDate);
                 session.save(savedGames);
                 break;
             case "data": {
                 query = session.createQuery("SELECT id FROM SavedGames WHERE date = '" + args[1] + "'");
                 result = query.list();
                 query = session.createQuery("SELECT moveString FROM OneGame WHERE gameId = :gameId ORDER BY id");
                 query.setParameter("gameId", result.get(0));
                 result = query.list();
                 break;
             }
             case "doMove": {
                 query = session.createQuery("SELECT COUNT(*) FROM SavedGames");
                 result = query.list();
                 OneGame oneGame = new OneGame();
                 oneGame.setGameId(Integer.parseInt(String.valueOf(result.get(0))));
                 oneGame.setMoveId(Integer.parseInt(args[1]));
                 oneGame.setMoveString(args[2]);
                 session.save(oneGame);
                 break;
             }
         }
        session.getTransaction().commit();
        return result;
    }
}
