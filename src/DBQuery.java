import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.ResultSetMetaData;


public class DBQuery
{
    private static String tableName = "Music";
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;
    private static String dbURL = "jdbc:derby:codejava/webdb1;create=true";
    public static void main(String[] args) {
       createConnection();
       //insertSong("C:\\Hello.mp3");
      // insertSong("C:\\Doc\\Top.mp3");
      // insertSong("C:\\Doc\\Down.mp3");
       deleteSong("C:\\Doc\\Down.mp3");
       selectSong();
       //truncateTable();
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
    private static void truncateTable()
    {
        try
        {
            stmt = conn.createStatement();
            stmt.execute("TRUNCATE TABLE Music");
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    private static void deleteSong(String FileName)
    {
    	String Title;
        try
        {
            stmt = conn.createStatement();
            Title =FileName.substring(FileName.lastIndexOf('\\')+1, FileName.length());
            System.out.println("The Title is "+Title);
            stmt.execute("DELETE FROM Music WHERE File="+"'"+FileName+"'");
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void insertSong(String FileName)
    {
    	String Title;
        try
        {
            stmt = conn.createStatement();
            Title =FileName.substring(FileName.lastIndexOf('\\')+1, FileName.length());
            System.out.println("The Title is "+Title);
            stmt.execute("insert into Music values(default,"+"'"+FileName+"'"+",'"+Title+"',default)");
            //stmt.execute("insert into " + tableName + " values ("+
              //      id + ",'" +FileName+ ",'" + Title + "')");
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void selectSong()
    {
        try
        {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + tableName);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
              //  int id = results.getInt(1);
                String ID = results.getString(1);
                String FILE = results.getString(2);
                String TITLE = results.getString(3);
                String DATE = results.getString(4);
                System.out.println(ID + "\t\t" + FILE + "\t\t" +TITLE+"\t\t"+DATE+"\t\t");
            }
            results.close();
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