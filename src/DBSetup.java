import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.ResultSetMetaData;


public class DBSetup
{
    private static String tableName = "restaurants";
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;
    private static String dbURL = "jdbc:derby:codejava/webdb1;create=true";
    public static void main(String[] args) {
        createConnection();
        //dropTable();
        createTable();
        shutdown();
    }
    
    private static void createConnection()
    {
    	try {
            // connect method #1 - embedded driver
            
             conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected to database #1");
            }
       
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    private static void createTable()
    {
        try
        {
            stmt = conn.createStatement();
            stmt.execute("CREATE TABLE Music("
            		+ "ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
            		+ "File varchar(255),"
            		+ "Title varchar(255),"
            		+ "InsertDate DATE not null with default current DATE"
            		+ ")");
            		
            
//            stmt.execute("CREATE TABLE Music("
//            		+ "ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
//            		+ "File varchar(255),"
//            			
//            		+ "InsertDate DATE not null with default current DATE)");
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    private static void dropTable()
    {
        try
        {
            stmt = conn.createStatement();
            stmt.execute("DROP TABLE Music");
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    

    
    

    private static void shutdown()
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
            
        }

    }
    
    
    
}