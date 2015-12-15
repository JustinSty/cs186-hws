import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class YelpQueries
{
  public static void main(String[] args) throws ClassNotFoundException
  {
    // load the sqlite-JDBC driver using the current class loader
    Class.forName("org.sqlite.JDBC");

    String dbLocation = "yelp_dataset.db"; 

    Connection connection = null;
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

      Statement statement = connection.createStatement();

      // Question 0
      statement.execute("DROP VIEW IF EXISTS q0"); // Clean out views
      String q0 = "CREATE VIEW q0 AS "
                   + "SELECT count(*) FROM reviews";
      statement.execute(q0);

      // Question 1
      statement.execute("DROP VIEW IF EXISTS q1");
      String q1 = "CREATE VIEW q1 AS " 
                  + "SELECT AVG(review_count) FROM users WHERE review_count<10"; // Replace this line
      statement.execute(q1);

      // Question 2
      statement.execute("DROP VIEW IF EXISTS q2");
      String q2 = "CREATE VIEW q2 AS "
                  + "SELECT name FROM users WHERE yelping_since > '2014-11' and review_count > 50"; // Replace this line
      statement.execute(q2);

      // Question 3
      statement.execute("DROP VIEW IF EXISTS q3");
      String q3 = "CREATE VIEW q3 AS "
                  + "SELECT name, stars FROM businesses WHERE stars > 3.0 and city = 'Pittsburgh'"; // Replace this line
      statement.execute(q3);

      // Question 4
      statement.execute("DROP VIEW IF EXISTS q4");
      String q4 = "CREATE VIEW q4 AS "
                  + "SELECT name FROM businesses WHERE city = 'Las Vegas' and review_count >=500 ORDER BY stars ASC LIMIT 1 "; // Replace this line
      statement.execute(q4);

      // Question 5
      statement.execute("DROP VIEW IF EXISTS q5");
      String q5 = "CREATE VIEW q5 AS "
                  + "SELECT B.name FROM businesses B, checkins C WHERE C.business_id = B.business_id and C.day = 0 ORDER BY C.num_checkins DESC LIMIT 5"; // Replace this line
      statement.execute(q5);

      // Question 6
      statement.execute("DROP VIEW IF EXISTS q6");
      String q6 = "CREATE VIEW q6 AS "
                  + "SELECT day FROM checkins GROUP BY day ORDER BY SUM(num_checkins) DESC LIMIT 1 "; // Replace this line
      statement.execute(q6);

      // Question 7
      statement.execute("DROP VIEW IF EXISTS q7");
      String q7 = "CREATE VIEW q7 AS "
                  + "SELECT B.name from businesses AS B, reviews AS R WHERE B.business_id = R.business_id and R.user_id = (select user_id FROM users ORDER BY review_count DESC LIMIT 1)";
      statement.execute(q7);

      // Question 8
      statement.execute("DROP VIEW IF EXISTS q8");
      String q8 = "CREATE VIEW q8 AS "
                  + "select avg(stars) from (select stars from businesses where city = 'Edinburgh' order by review_count desc limit (select count(business_id) from businesses where city = 'Edinburgh')/10) "; // Replace this line
      statement.execute(q8);

      // Question 9
      statement.execute("DROP VIEW IF EXISTS q9");
      String q9 = "CREATE VIEW q9 AS "
                  + "SELECT name FROM users WHERE name LIKE '%..%' "; // Replace this line
      statement.execute(q9);

      // Question 10
      statement.execute("DROP VIEW IF EXISTS q10");
      String q10 = "CREATE VIEW q10 AS "
                  + "select city from businesses B, reviews R where R.user_id in (SELECT user_id FROM users WHERE name LIKE '%..%' ) and R.business_id = B.business_id group by B.city order by count(B.business_id) DESC LIMIT 1"; // Replace this line
      statement.execute(q10);

      connection.close();

    }
    catch(SQLException e)
    {
      // if the error message is "out of memory", 
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
  }
}