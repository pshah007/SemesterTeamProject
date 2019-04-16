import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.sql.ResultSetMetaData;


public class DBQuery
{
    private static String tableName = "Music";
    private static ResultSet results;
    private static ResultSetMetaData rsmd;
    private static Connection conn = null;
    private static Statement stmt = null;
    private static DatabaseMetaData dbm;
    Mp3File mp3file; 
    private static String dbURL ="jdbc:derby:codejava/webdb1;create=true";
   

   /*
    public static void main(String[] args) throws UnsupportedTagException, InvalidDataException, IOException {
    	
    	DBQuery query = new DBQuery();
    	query.createConnection();
    	//query.dropTable();
    	//query.createTable();
    	//query.insertSong("TEST","TEST","TEST","TEST","TEST","1900","1","Library");
    	//int temp = query.getPlaylistCount();
    	//String[][] stk2=playlistDisplaySongs("Library");
    	//System.out.println(temp);
    	int t=query.checkSong("Give Me Novacaine","TEST1");
    	System.out.println(t);
    	//for(int x = 0 ; x < stk2.length ; x++)
    	//{
    		//System.out.println(stk2[0][3]);
    	//}
    	//query.createTable();
    	//query.selectSong();
    }
   */
    
    
    public String[] searchSongByTitle(String Title) throws UnsupportedTagException, InvalidDataException, IOException
    {
    	String path="";
    	String title = "";
    	String artist = "";
        try
        {
            stmt = conn.createStatement();
            String Query="SELECT * FROM Music WHERE UPPER(Title) LIKE UPPER('%"+Title+"%')";
            ResultSet result = stmt.executeQuery(Query);
            
            if(result.next())
            {
            	path=result.getString(2);
            	title = result.getString(3);
            	artist = result.getString(4);
            }
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        return new  String [] {path,title,artist};
    }
    
    public String[] searchSongByTitlePlaylist(String Title, String Playlist) throws UnsupportedTagException, InvalidDataException, IOException
    {
    	String path="";
    	String title = "";
    	String artist = "";
    	String playlist = "";
        try
        {
        
            stmt = conn.createStatement();
            String Query="SELECT * FROM Music WHERE UPPER(Title) LIKE UPPER('%"+Title+"%') AND 	UPPER(Playlist) LIKE UPPER('%"+Playlist+"%')";
            ResultSet result = stmt.executeQuery(Query);
            
            if(result.next())
            {
            	path=result.getString(2);
            	title = result.getString(3);
            	artist = result.getString(4);
            	playlist = result.getString(9);
            }
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        return new  String [] {path,title,artist,playlist};
    }
    
    public int checkSong(String Title, String Playlist) throws UnsupportedTagException, InvalidDataException, IOException
    {
    	int reuslt=0;
    	
        try
        {
        	stmt = conn.createStatement();
            
            String Query="SELECT * FROM Music WHERE Title ='"+Title+"' AND Playlist = '"+Playlist+"'";
            ResultSet result = stmt.executeQuery(Query);
            
            if(result.next())
            {
            	reuslt=1;
            }
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        
        return reuslt;
    }
    
    
    
    
    
    
    
    
    
    public void createTable()
    {
        try
        {
            stmt = conn.createStatement();
            dbm = conn.getMetaData();
            results = dbm.getTables(null, "APP", "MUSIC", null);
            if (results.next()) {
                System.out.println("TABLE ALREADY PRESENT");	
            }
            else
            {
            	stmt.execute("CREATE TABLE Music("
                		+ "ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                		+ "File varchar(255)   ,"
                		+ "Title varchar(255) ,"
                		+ "Artist varchar(255) DEFAULT 'N/A',"
                		+ "Album varchar(500) DEFAULT 'N/A',"
                		+ "Genere varchar(255) DEFAULT 'N/A',"
                		+ "Yr varchar(255) DEFAULT 'N/A',"
                		+ "Length varchar(255) DEFAULT 'N/A' ,"
                		+ "InsertDate DATE not null with default current DATE ,"
                		+ "Playlist varchar(255) DEFAULT 'N/A'"
                		+ ")");
            	
            }
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
   
    
    public void createConnection()
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
    public void truncateTable()
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
    public void deleteSong(String FileName)
    {
    	String Title;
        try
        {
            stmt = conn.createStatement();
            Title =FileName.substring(FileName.lastIndexOf('\\')+1, FileName.length());
            System.out.println("The Title is "+Title);
            String[] stk=FileName.split(",");
            for(int k=0;k<stk.length;k++)
            {
            stmt.execute("DELETE FROM Music WHERE File = "+"'"+stk[k]+"'");
            }
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    
    public void deleteSongFromPlaylist(String FileName, String Playlist)
    {
    	String Title;
        try
        {
            stmt = conn.createStatement();
            Title =FileName.substring(FileName.lastIndexOf('\\')+1, FileName.length());
            System.out.println("Song title is  "+Title);
            System.out.println("Songs to delte from the playlist is  "+Playlist);
            
            if(Playlist.equals("Library")){
            	stmt.execute("DELETE FROM MUSIC WHERE File = "+"'"+FileName+"'");
            }
            else
            	stmt.execute("DELETE FROM MUSIC WHERE File = "+"'"+FileName+"' AND Playlist = '" +Playlist+ "'");
            
            selectSong();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    public int getPlaylistCount()
    {
    	 int numberRows=0;
	        try {
				stmt = conn.createStatement();
		        results = stmt.executeQuery("select count(DISTINCT Playlist) AS rowcount from MUSIC where Playlist <> 'Library'");
	            while (results.next()){
	            	numberRows = results.getInt(1);
	            }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           return numberRows;
    }
    
    
    public void deletePlaylist(String Playlist)
    {
    	String Title;
        try
        {
            stmt = conn.createStatement();
            String[] stk=Playlist.split(",");
            for(int k=0;k<stk.length;k++)
            {
            stmt.execute("DELETE FROM Music WHERE Playlist = "+"'"+stk[k]+"'");
            }
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    public void insertSong(String FileName,String Title,String Artist, String Album,String Genere,String Year,String Length,String Playlist) throws UnsupportedTagException, InvalidDataException, IOException
    {
  
    	
    	try
        {
            stmt = conn.createStatement();
           
          
   
            stmt.execute(
              		"insert into Music values(default,"+
              				"'"+FileName+"',"+
              				"'"+Title+"',"+
              				"'"+Artist+"',"+
              				"'"+Album+"',"+
              				"'"+Genere+"',"+
              				"'"+Year+"',"+
              				"'"+Length+"',"+
              				"default,"+
              				"'"+Playlist+"')");     


            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    public void selectSong()
    {
        try
        {
            stmt = conn.createStatement();
            results = stmt.executeQuery("select * from MUSIC");
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
                String ID = results.getString(1);
                String FILE = results.getString(2);
                String TITLE = results.getString(3);
                String ARTIST = results.getString(4);
                String ALBUM = results.getString(5);
                String GENERE = results.getString(6);
                String YR = results.getString(7);
                String Length = results.getString(8);
                String DATE = results.getString(9);
                String Playlist = results.getString(10);
                
                System.out.println(ID + "\t\t" 
                		+ FILE + "\t\t" 
                		+TITLE + "\t\t"
                		+ARTIST + "\t\t" 
                		+ALBUM + "\t\t"
                		+GENERE+"\t\t"
                		+YR+"\t\t"
                		+Length+"\t\t"
                		+DATE+"\t\t"
                		+Playlist+"\t\t");
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    
    
    public void selectSongFromPlaylist(String list)
    {
        try
        {
            stmt = conn.createStatement();
            results = stmt.executeQuery("select * from MUSIC Where Playlist"+"'"+list+"'");
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
                String ID = results.getString(1);
                String FILE = results.getString(2);
                String TITLE = results.getString(3);
                String ARTIST = results.getString(4);
                String ALBUM = results.getString(5);
                String GENERE = results.getString(6);
                String YR = results.getString(7);
                String Length = results.getString(8);
                String DATE = results.getString(9);
                String Playlist = results.getString(10);
                
                System.out.println(ID + "\t\t" 
                		+ FILE + "\t\t" 
                		+TITLE + "\t\t"
                		+ARTIST + "\t\t" 
                		+ALBUM + "\t\t"
                		+GENERE+"\t\t"
                		+YR+"\t\t"
                		+Length+"\t\t"
                		+DATE+"\t\t"
                		+Playlist+"\t\t");
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    public void dropTable()
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
    
    public void shutdown()
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
    public String[][] dataDisplay() 
    {
    	String[][] stk = null;
    	try {
	        stmt = conn.createStatement();
	        results = stmt.executeQuery("select * from MUSIC");
	        ResultSetMetaData rsmd = results.getMetaData();
	        int numberCols = rsmd.getColumnCount();
	        results = stmt.executeQuery("select count(*) AS rowcount from MUSIC");
	        results.next();
	        int numberRows = results.getInt("rowcount");
	        stk = new String[numberRows][numberCols-2];
	        
	        results = stmt.executeQuery("select * from MUSIC");
	        int t=0;
	        while(results.next())
	        {
	        	
	        	stk[t][0] = results.getString(2);
	        	stk[t][1] = results.getString(3);		
	        	stk[t][2] = results.getString(4);
	    	    stk[t][3] = results.getString(5);
	    	    stk[t][4] = results.getString(6);
	    	    stk[t][5] = results.getString(7);
	    	    stk[t][6] = results.getString(8);
	    	    stk[t][7] = results.getString(10);
	    	    t++;
	        }
    	}
		
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    	return stk;
    }
    
    public static String[][] playlistDisplaySongs(String play) 
    {
    	String[][] stk = null;
    	try {
	        stmt = conn.createStatement();
	        results = stmt.executeQuery("select * from MUSIC");
	        ResultSetMetaData rsmd = results.getMetaData();
	        int numberCols = rsmd.getColumnCount();
	        
	        results = stmt.executeQuery("select count(*) AS rowcount from MUSIC where Playlist = '"+play+"'"+" AND File <>'TEST' AND Title <>'TEST'");

	        int numberRows=0;
            while (results.next()){
            	numberRows = results.getInt(1);
            }
            System.out.println("IAM HERE ");
	        stk = new String[numberRows][numberCols-2];
	        
	        results = stmt.executeQuery("select * from MUSIC where Playlist = '"+play+"'"+" AND File <>'TEST' AND Title <>'TEST'");
	        int t=0;
	        while(results.next())
	        {
	        	
	        	stk[t][0] = results.getString(2);
	        	stk[t][1] = results.getString(3);		
	        	stk[t][2] = results.getString(4);
	    	    stk[t][3] = results.getString(5);
	    	    stk[t][4] = results.getString(6);
	    	    stk[t][5] = results.getString(7);
	    	    stk[t][6] = results.getString(8);
	    	    stk[t][7] = results.getString(10);
	    	    t++;
	        }
    	}
		
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    	return stk;
    }
    
    public String[] playlistDisplay() 
    {
    	String[] stk = null;
    	try {
	        stmt = conn.createStatement();
	        results = stmt.executeQuery("select count(DISTINCT Playlist) AS rowcount from MUSIC Where Playlist <> 'Library'");
	        int numberRows=0;
            while (results.next()){
            	numberRows = results.getInt(1);
            }
            if(numberRows>0)
	        
            {
		        results = stmt.executeQuery("select DISTINCT Playlist from MUSIC Where Playlist <> 'Library'");
		        ResultSetMetaData rsmd = results.getMetaData();
	
		        stk = new String[numberRows];
		        
		        int t=0;
		        while(results.next())
		        {
		        	stk[t] = results.getString(1);
		    	    t++;
		        }
            }
    	}
		
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    	return stk;
    }
    
    
    
}