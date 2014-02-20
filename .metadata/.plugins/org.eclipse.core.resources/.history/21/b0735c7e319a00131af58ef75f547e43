package esial.gmd.tp2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecuteOneSimpleQuery {

	static String DB_SERVER  = "jdbc:mysql://mysql.loria.fr:3306/";
	static String DB         = "coulet";
	static String DRIVER     = "com.mysql.jdbc.Driver";
	static String USER_NAME  = "couletadm";
	static String USER_PSWD  = "blabla";
	static String DB_TABLE1  = "Etudiant";
	    
	public static void main(String[] args) {
       try{
    	  // Set up the connection 
          Class.forName(DRIVER);
          Connection con = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);
          // Build and execute the SQL query
          String myQuery = "SELECT * " +
          		           "FROM Etudiant " +
          		           "WHERE prenom='Clement';";
          Statement st = con.createStatement();
          ResultSet res = st.executeQuery(myQuery);

          while (res.next()) {
        	// if we know attributes names
            String id     = res.getString("id");
            String prenom = res.getString("prenom");
            String nom    = res.getString("nom");

            System.out.println(" ");
            System.out.println("Etudiant:"+id+"\n\tNom: "+nom+" "+prenom);
          }
          res.close();
          st.close();
          con.close();
        }
        catch (ClassNotFoundException e){
          System.err.println("Could not load JDBC driver");
          System.out.println("Exception: " + e);
          e.printStackTrace();
        }
        catch(SQLException ex){
          System.err.println("SQLException information");
          while(ex!=null) {
            System.err.println ("Error msg: " + ex.getMessage());
            System.err.println ("SQLSTATE: " + ex.getSQLState());
            System.err.println ("Error code: " + ex.getErrorCode());
            ex.printStackTrace();
            ex = ex.getNextException(); // For drivers that support chained exceptions
          }
        }
	}
}
