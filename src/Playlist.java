/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import java.util.Random;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;


import javazoom.jlgui.basicplayer.BasicController;
//import StreamPlayerNew.ButtonListener;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;


public class Playlist {
    
    
    static JFrame playlistWindow;
    App app;
    static JTable table;
    JPanel bottombtnPnl;
    JPanel bottombtnPn2;
    JPanel bottombtnPn3;
    JPanel bottombtnPnx;
    ButtonListener buttonListener;
    static JSlider volume;
    
    JScrollPane scrollPane; 
    int CurrentSelectedRow;
    static JButton Play;
    JButton Pause;
    JButton Stop;
    JButton Previous;
    JButton Next;
    //JButton Repeat;
    JButton Shuffle;
    JButton Delete;
    JButton Search;
    JPanel btnPnl;
    static BasicPlayer player;
    //static BasicController control;
    static BasicController control ;
    static String[][] data;
    static String[] columns = new String[8];
    static DefaultTableModel tableModel;
    static DBQuery Query;
    final JTextArea textArea;
    static boolean isPaused = false;
    static JLabel nowPlaying = new JLabel("");
    static String playlist;
    static JProgressBar progressBar;
    static Timer progressTimer ;
    static long lengthOfSongInSeconds=0;
    static long progressEverySecond=0;
    static int pointerPr;
    static int threadStop=0;
    static int row =0;
    static int next = 0;
    static int stopCheck=0;
    static int previous = 0; 
    static int playControl=0;
    static int  currentSelectedSong = 0;
    static Playlist playlistwindow;
    static int pointerDg = 0;
    static int pointerPs = 0;
    static int randLast=0;
    static JLabel tottimeLabel;
    static JLabel remTimeLabel;
    static JPanel progressPanel;
    static JMenu menu2 ;
    static JMenuItem Play2 ;
    static JMenuItem Next2 ;
    static JMenuItem Previous2 ;
    static JMenu Playrecent2 ;
    static JMenuItem Currentsongt2 ;
    static JMenuItem Increasevol2 ;
    static JMenuItem Decreasevol2 ;
    static JCheckBoxMenuItem Shuffle2 ;
    static JCheckBoxMenuItem Repeat2 ;
	static JPanel mainPanel;
	static JPanel finalPanel;
	static FileInputStream fin;
	static Mp3File mp3file;
    
    /**
     * 
     */
    public Playlist(String playlistName, App appObj){
    	
    	playlist = playlistName;
    	playlistWindow = new JFrame(playlist);
    	app = appObj;
    	finalPanel = new JPanel(new BorderLayout(0, 0));
    	mainPanel = new JPanel(new BorderLayout(0, 0));
    	/*
    	 * ADDING MENU BAR NAME FILE WHICH WILL CONTAIN NEW,OPEN,EXIT
    	 */
    	JMenuBar mb = new JMenuBar();
    	JMenu menu = new JMenu("FILE");
    	JMenuItem New = new JMenuItem("Add To " + playlist);
    	JMenuItem Open = new JMenuItem("Open A Song");
    	JMenuItem Exit = new JMenuItem("Exit");
    	JMenuItem cplay= new JMenuItem("Create Playlist");
    	
    	
    	 menu2 = new JMenu("CONTROLS");
    	 Play2 = new JMenuItem("Play");
    	 Next2 = new JMenuItem("Next");
    	 Previous2 = new JMenuItem("Previous");
    	 
    	 Playrecent2= new JMenu("Play Recent");
    	 Currentsongt2= new JMenuItem("Go to  Current Song");
    	 Increasevol2= new JMenuItem("Increase Volume");
    	 Decreasevol2= new JMenuItem("Decrease Volume");
    	 Shuffle2= new JCheckBoxMenuItem("Shuffle");
    	 Repeat2= new JCheckBoxMenuItem("Repeat");

    	
    	
    	
    	
    	menu.add(New);
    	menu.add(Open);
    	menu.add(cplay);
    	menu.add(Exit);
    	
    	menu2.add(Play2);
    	menu2.add(Next2);
    	menu2.add(Previous2);
    	menu2.add(Playrecent2);
    	menu2.add(Currentsongt2);
    	menu2.add(Increasevol2);
    	menu2.add(Decreasevol2);
    	menu2.add(Shuffle2);
    	menu2.add(Repeat2);


    	mb.add(menu);
    	mb.add(menu2);
    	
    	Exit.addActionListener(new exitJmenuButton());
    	New.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Yes this is the one");
	        	   JFileChooser chooser = new JFileChooser();
	               if (chooser.showOpenDialog(playlistWindow) == JFileChooser.APPROVE_OPTION) {
	            	   File file = chooser.getSelectedFile();
	            	    DefaultTableModel contactTableModel = (DefaultTableModel) table.getModel();
	            	   System.out.println(file.getPath());
	            	   try {
	            		   addSong(file.getPath(), "Library");
						addSong(file.getPath(), playlist);
						Playlist.this.app.tableRefresh();
					} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	               }
	               app.tableRefresh();
			}
    		
    	});
    	Open.addActionListener(new openJmenuButton());
    	cplay.addActionListener(new cplayJmenuButton());
    	playlistWindow.pack();
    	playlistWindow.setSize(800, 600);
    	
    	/*
    	 * CREATING NEW OBJECT FOR DB QUERY TO CONNECT DB WHICH IS CALLED WEBDB1
    	 * ALSO CREATES TABLE AS WELL CALLED MUSIC
    	 */
    	 Query = new DBQuery();
    	 Query.createConnection();
    	 Query.createTable();
    	 MyDragDropListener myDragDropListener = new MyDragDropListener();

    	 /*
    	  * CREATING THREE BUTTON PANELS
    	  * bottombtnPnl CONTAINS PLAY,STOP,PAUSE
    	  * bottombtnPn2 CONTAINS PREVIOUS,REPEAT,DELETE
    	  * bottombtnPn3 CONTAINS Shuffle,SEARCH,NEXT
    	  */
   
    	bottombtnPnl = new JPanel();
    	bottombtnPnl.setLayout(new BorderLayout(0, 0));
    	bottombtnPn2 = new JPanel();
    	bottombtnPn2.setLayout(new BorderLayout(0, 0));
    	bottombtnPn3 = new JPanel();
    	bottombtnPn3.setLayout(new BorderLayout(0, 0));
    	btnPnl = new JPanel(new BorderLayout());
    	
    	/*
    	 * CREATING TEXT AREA FOR DRAG AND DROP FILE
    	 */
    	textArea= new JTextArea();
    	new DropTarget(textArea, myDragDropListener);	
    	columns[0] ="FILE";
    	columns[1] ="TITLE";
    	columns[2] ="ARTIST";
    	columns[3] ="ALBUM";
    	columns[4] ="GENERE";
    	columns[5] ="YEAR";
    	columns[6] ="LENGTH";
    	columns[7] ="PLAYLIST";
    	player = new BasicPlayer();
        control = (BasicController) player;
      
          //data holds the table data and maps as a 2d array into the table
          data = Query.playlistDisplaySongs(playlistName);
          
          
          
          tableModel= new DefaultTableModel(data, columns);
          table = new JTable();
          table.setModel(tableModel);
          table.setDragEnabled(true);
          
          
          table.setAutoCreateRowSorter(true); //adding the sorting functionality on all columns
          
          /*hiding File column*/
          table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0));
          
          MouseListener mouseListener = new MouseAdapter() {
              //this will print the selected row index when a user clicks the table
              public void mousePressed(MouseEvent e) {
                 CurrentSelectedRow = table.getSelectedRow();
                 System.out.println("Selected index = " + CurrentSelectedRow);
              }
          };         
        //assign the listener
        table.addMouseListener(mouseListener);
        //change some column's width
        //first get the column from the column model from the table
        //column 0 is the leftmost - make it 250 pixels
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(500);
       
        column = table.getColumnModel().getColumn(1); 
        column.setPreferredWidth(100);
        column = table.getColumnModel().getColumn(2); 
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(3); 
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(4); 
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(5); 
        column.setPreferredWidth(50);
        
        
        buttonListener = new ButtonListener();
        Play = new JButton("Play");
        Pause = new JButton("Pause");
        Stop = new JButton("Stop");
        Previous = new JButton("Previous");
        Next = new JButton("Next");
        //Repeat = new JButton("Repeat");
        Delete = new JButton("Delete");
        Shuffle = new JButton("Shuffle");
        Search = new JButton("Search");
        
        Play.addActionListener(buttonListener);
        Delete.addActionListener(buttonListener);
        Pause.addActionListener(buttonListener);
        Stop.addActionListener(buttonListener);
        Search.addActionListener(buttonListener);
        Next.addActionListener(buttonListener);
        Previous.addActionListener(buttonListener);
        //Repeat.addActionListener(buttonListener);
        Shuffle.addActionListener(buttonListener);
        scrollPane = new JScrollPane(table);
        volume = new JSlider(0,100,25);
        
        volume.addChangeListener( new ChangeListener() {
        	public void stateChanged(ChangeEvent evt)
        	{
        		volume((double)volume.getValue()/100);
        	}
        	
        });
        
        
        tottimeLabel = new JLabel();
        remTimeLabel = new JLabel();
        remTimeLabel.setText("00:00:00");
        tottimeLabel.setText("00:00:00");
        progressBar=new JProgressBar();
     	progressPanel =new JPanel(new BorderLayout());
     	progressPanel.add(remTimeLabel,BorderLayout.LINE_START);
     	progressPanel.add(progressBar,BorderLayout.CENTER);
     	progressPanel.add(tottimeLabel,BorderLayout.LINE_END);
     	
        
        
        
        bottombtnPnl.add(Play, BorderLayout.CENTER);
        bottombtnPnl.add(Stop, BorderLayout.LINE_START);
        bottombtnPnl.add(Pause, BorderLayout.LINE_END);
        bottombtnPn2.add(Previous, BorderLayout.LINE_START);
        //bottombtnPn2.add(Repeat, BorderLayout.LINE_END);
        bottombtnPn2.add(Delete, BorderLayout.CENTER);
        
        bottombtnPn3.add(Shuffle, BorderLayout.LINE_START);
        bottombtnPn3.add(volume, BorderLayout.CENTER);
        //bottombtnPn3.add(Search, BorderLayout.CENTER);
        bottombtnPn3.add(Next, BorderLayout.LINE_END);
 
        
        btnPnl.add(bottombtnPnl, BorderLayout.CENTER);
        btnPnl.add(bottombtnPn2, BorderLayout.LINE_START);
        btnPnl.add(bottombtnPn3, BorderLayout.LINE_END);
        
        final RowPopup pop = new RowPopup(table);
        
        table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if(SwingUtilities.isRightMouseButton(me)) {
					pop.show(me.getComponent(), me.getX(), me.getY() );
				}
			}
		});
        playlistWindow.setJMenuBar(mb);
     
      
        mainPanel.add(textArea);
        textArea.setText("Drop Songs Here To Add to Playlist : " + playlist);
        mainPanel.add(scrollPane, BorderLayout.NORTH);
        mainPanel.add(textArea, BorderLayout.CENTER);
        mainPanel.add(btnPnl, BorderLayout.SOUTH);
        mainPanel.add(nowPlaying, BorderLayout.EAST);
        finalPanel.add(progressPanel, BorderLayout.NORTH);
        finalPanel.add(mainPanel, BorderLayout.CENTER);
        playlistWindow.add(finalPanel);
        

    	
    	Next2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.InputEvent.CTRL_MASK));
    	Next2.addActionListener(new Next2JmenuButton());
    	
    	
    	Previous2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, java.awt.event.InputEvent.CTRL_MASK));
    	Previous2.addActionListener(new Previous2JmenuButton());
    	
    	
    	Increasevol2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
    	Increasevol2.addActionListener(new Increasevol2JmenuButton());

    	
    	Decreasevol2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
    	Decreasevol2.addActionListener(new Decreasevol2JmenuButton());
    	
    	
    	Currentsongt2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
       
    	Currentsongt2.addActionListener(new Currentsongt2JmenuButton());
    	Play2.setAccelerator(
    	         KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
    	Play2.addActionListener(new Play2JmenuButton());
        
        
        
        playlistWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        playlistWindow.setExtendedState(JFrame.MAXIMIZED_BOTH); 
       // playlistWindow.setUndecorated(true);
        playlistWindow.setVisible(true);
        mb.requestFocus();
        
        playlistWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
            	try {
					player.stop();
				} catch (BasicPlayerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
  
      
    }
    
    public static void tableRemoveAllRows()
    {
    	
    	int rowCount = tableModel.getRowCount();
    	//Remove rows one by one from the end of the table
    	for (int i = rowCount - 1; i >= 0; i--) {
    		tableModel.removeRow(i);
    	}
    }
    
    public void tableRefresh() {
    	
    	String fileName;
        String Title ;
        String Artist=" ";
        String Album =" ";
        String Genere = " ";
        String Year="";
        String Length = "";
        
		
		tableRemoveAllRows();

	   data = Query.playlistDisplaySongs(playlist);
	    for(int i=0; i<data.length; i++) {
	        fileName=data[i][0];
	        Title=data[i][1];
	        Artist=data[i][2];
	        Album =data[i][3];
	        Genere = data[i][4];
	        Year=data[i][5];
	        Length =data[i][6];
	        String[] rown = {fileName, Title,Artist,Album,Genere,Year,Length,playlist};
	        tableModel.addRow(rown);
	    }
    }
    
    public String getFrameName()
    {
    	return playlist;
    }
    public void exit()
    {
    	 playlistWindow.setVisible(false);
    }
    
    public static void addSong(String fileName, String Playlist) throws UnsupportedTagException, InvalidDataException, IOException
    {
        Mp3File mp3file = new Mp3File(fileName);
        String Title =fileName.substring(fileName.lastIndexOf('\\')+1, fileName.length());
        Title = Title.substring(0,Title.indexOf('.'));
        String Artist=" ";
        String Album =" ";
        String Genere = " ";
        String Year="";
        String Length = "";
       
    	if (mp3file.hasId3v1Tag()) {
    		  ID3v1 id3v1Tag = mp3file.getId3v1Tag();
			  System.out.println("Title: " + id3v1Tag.getTitle());
    		  System.out.println("Artist: " + id3v1Tag.getArtist());
    		  System.out.println("Album: " + id3v1Tag.getAlbum());
    		  System.out.println("Genre: " + id3v1Tag.getGenreDescription() );
              
              Title= id3v1Tag.getTitle();
              Artist= id3v1Tag.getArtist();
              Album= id3v1Tag.getAlbum();
              Genere= id3v1Tag.getGenreDescription();
              Year= id3v1Tag.getYear();
              Length= ""+mp3file.getLengthInSeconds();    		    
    	}
    	int result =Query.checkSong(Title, Playlist);
    	if(result==0) {
    		 Query.insertSong(fileName,Title,Artist,Album,Genere,Year,Length, Playlist);
    		 String rowEntry = "";
			
					System.out.println("The Actual Playlist "+Playlist);
					//System.out.println("The Actual stk "+stk);
					String[] rown = { fileName, Title,Artist,Album,Genere,Year,Length,Playlist};
					if(!Playlist.equals("Library"))
					{
						tableModel.addRow(rown);
    	}
    		
					}

    				   

    			   
    			   /*
   		    library.removeAllChildren();
   		    System.out.println("ABOUT TO PRINT TABLE ROW COUNT BEFORE DEFUALTMUTABLETTREENODE "+table.getRowCount());
    		   for (int i = 0; i < table.getRowCount(); i++) {
    			  library.add(new DefaultMutableTreeNode(table.getModel().getValueAt(i, 1).toString()));
		    }
    		   */
   	        //model.reload(library);
    }
    
    public static void volume(double voume)
    {
   	 try {
			control.setGain(voume);
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


	/*public void go(){
		playlistWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		playlistWindow.setExtendedState(JFrame.MAXIMIZED_BOTH); 
       // playlistWindow.setUndecorated(true);
		playlistWindow.setVisible(true);
    }*/

	


    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int column = 0;
        	int row = table.getSelectedRow();
            if("Play".equals(e.getActionCommand())){
         	   		row = table.getSelectedRow();
         	   	currentSelectedSong=row;
         	   	
         	   	if(Shuffle2.isSelected())
         	   	{
         	   		System.out.println("THE SHUFFLE SELECTED "+Shuffle2);
         	            shuffle();
         	   	}
         	   	
         	   	else
         	   	{
   	        	if(row<0)
   	        	{
   	        		row=0;
   	        		currentSelectedSong=row;
   	        	}
   	        	
   	        	try {
   					mp3file = new Mp3File(table.getModel().getValueAt(row, column).toString());
   					fin = new FileInputStream(new File(table.getModel().getValueAt(row, column).toString()));
   					ProgressBarSetup();
   					lengthOfSongInSeconds = mp3file.getLengthInSeconds();
   					pointerDg=(int) lengthOfSongInSeconds;
   					System.out.println("FIN AVALIABLE "+fin.available());
   					System.out.println("lengthOfSongInSeconds "+lengthOfSongInSeconds);
   					addSongRecentlyPlayed(table.getModel().getValueAt(row, column).toString());
   					table.setRowSelectionInterval(currentSelectedSong, currentSelectedSong);
   	                table.scrollRectToVisible(table.getCellRect(currentSelectedSong, 0, true));
   					//Play(table.getModel().getValueAt(row, column).toString());
   					playSong();
   					//threadStop=1;

   				} catch (UnsupportedTagException | InvalidDataException | IOException e2) {
   					// TODO Auto-generated catch block
   					e2.printStackTrace();
   				}
           	
         	   	}
                  
            }
            if("Delete".equals(e.getActionCommand())){
            	String sk="";
            	String pl="";
            	int i = table.getSelectedRowCount();
            	
            
            	int rows = table.getSelectedRow();

            	if (i >= 0) {

                			sk= table.getModel().getValueAt(rows, column).toString(); //-+",";
                			pl= table.getModel().getValueAt(rows, 7).toString(); //",";
                			System.out.println("THE PLAYLIST NAME TO DELTE "+pl);
                			Query.deleteSongFromPlaylist(sk,pl);
              		  		tableModel.removeRow(table.getSelectedRow());
              		  		;
            	}
            	
            
            	else {
            	System.out
            	.println("There were issue while Deleting the Row(s).");
            	}
            	
            	table.addNotify();
            }
            if("Search".equals(e.getActionCommand()))
            {
            	
                
            }
            
            if("Stop".contentEquals(e.getActionCommand()))
            {
            	isPaused = false;
            	stop();
            	
            	
            }
            if(e.getSource()==Pause)
            {
               	if(!isPaused) {              	
                	try {
    					pause();
    					Pause.setText("Resume");
    				} catch (BasicPlayerException | IOException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
            	}
            	else if(isPaused){
            		try {
						resume();
						Pause.setText("Pause");
					} catch (BasicPlayerException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	}
            	isPaused = !isPaused;
            	
            }
            if("Next".contentEquals(e.getActionCommand()))
            {

 
            	if(row==table.getRowCount()-1) {
            		row=0;
            	}
            	else
            		row = row+1;	
            	String Title = table.getModel().getValueAt(row, 1).toString();
        		String Artist = table.getModel().getValueAt(row, 2).toString();
        		nowPlaying.setText("<html>Now Playing: &nbsp;&nbsp;<br/>" + Title + "&nbsp;&nbsp;<br/> by &nbsp;&nbsp;<br/>" + Artist + "&nbsp;&nbsp;</html>");
            	table.changeSelection(row, column, false, false);
            	isPaused = false;
            	table.addRowSelectionInterval(row, row);
            	currentSelectedSong=row;
    			table.setRowSelectionInterval(currentSelectedSong, currentSelectedSong);
                table.scrollRectToVisible(table.getCellRect(currentSelectedSong, 0, true));
                try {
					if(!Shuffle2.isSelected())
					{
						addSongRecentlyPlayed(table.getModel().getValueAt(row, column).toString());
					}
				} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                //Play(table.getModel().getValueAt(row, column).toString());
            	playSong();
            }
            if("Previous".contentEquals(e.getActionCommand()))
            {
            	if(row==0 || row<0) {
            		row = table.getRowCount()-1;
            	}
            	else
            		
            		row = row-1;	
            	String Title = table.getModel().getValueAt(row, 1).toString();
        		String Artist = table.getModel().getValueAt(row, 2).toString();
        		nowPlaying.setText("<html>Now Playing: &nbsp;&nbsp;<br/>" + Title + "&nbsp;&nbsp;<br/> by &nbsp;&nbsp;<br/>" + Artist + "&nbsp;&nbsp;</html>");
            	table.changeSelection(row, column, false, false);
            	isPaused = false;
            	table.addRowSelectionInterval(row, row);
            	currentSelectedSong=row;
    			table.setRowSelectionInterval(currentSelectedSong, currentSelectedSong);
                table.scrollRectToVisible(table.getCellRect(currentSelectedSong, 0, true));
                try {
					if(!Shuffle2.isSelected())
					{
						addSongRecentlyPlayed(table.getModel().getValueAt(row, column).toString());
					}
				} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	playSong();

            	
            }

            if("Shuffle".contentEquals(e.getActionCommand()))
            {
            	shuffle();
            }

        }
        
    }
   
    public void pause() throws IOException, BasicPlayerException  {
        pointerPs = 1;
        disablePlay();
        player.pause();
    }
    private void resume() throws BasicPlayerException, IOException {
        playControl = 1;
        pointerPs = 0;
        player.resume();
        disablePlay();
    }

    
    /*
     * RELATED TO THE JMENU BUTTON EXIT
     * CLOSES THE WINDOW
     */
    static class exitJmenuButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }
    
    /*
     * RELATED TO THE JMENU BUTTON NEW
     * OPENS NEW BROWSER WINDOW TO SELECT SONG
     */

    static class openJmenuButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	
        	   JFileChooser chooser = new JFileChooser();
               if (chooser.showOpenDialog(playlistWindow) == JFileChooser.APPROVE_OPTION) {
            	   File file = chooser.getSelectedFile();
            	    DefaultTableModel contactTableModel = (DefaultTableModel) table.getModel();
            	    try {
            	    player.open(new URL("file:///" + file.getPath()));
            	    player.play();
            	    }
             	   catch (BasicPlayerException | MalformedURLException e2) {
	            	    e2.printStackTrace();
	            	}
            	    

               }
         }
    }
    
    static class cplayJmenuButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	
        	   JFileChooser chooser = new JFileChooser();
               if (chooser.showOpenDialog(playlistWindow) == JFileChooser.APPROVE_OPTION) {
            	   File file = chooser.getSelectedFile();
            	    DefaultTableModel contactTableModel = (DefaultTableModel) table.getModel();
            	    try {
            	    player.open(new URL("file:///" + file.getPath()));
            	    player.play();
            	    }
             	   catch (BasicPlayerException | MalformedURLException e2) {
	            	    e2.printStackTrace();
	            	}
            	    

               }
         }
    }
    
    
    
    class MyDragDropListener implements DropTargetListener {

        @Override
        public void drop(DropTargetDropEvent event) {

            // Accept copy drops
            event.acceptDrop(DnDConstants.ACTION_COPY);

            // Get the transfer which can provide the dropped item data
            Transferable transferable = event.getTransferable();

            // Get the data formats of the dropped item
            DataFlavor[] flavors = transferable.getTransferDataFlavors();
            String Stk="";
            // Loop through the flavors
            for (DataFlavor flavor : flavors) {

                try {

                    // If the drop items are files
                    if (flavor.isFlavorJavaFileListType()) {

                        // Get all of the dropped files
                        List <File>file = (List) transferable.getTransferData(flavor);
                        	
                        // Loop them through
                        for (File file1 : file) {

                            // Print out the file path
                        	addSong(file1.getPath(), "Library");
                        	addSong(file1.getPath(), playlist);
                        	Stk+=file1.getPath()+"\n";
                        	textArea.setText(Stk);
                            System.out.println("File path is '" + file1.getPath() + "'.");
                            System.out.println("amaka");

                        }

                    }
                    else {
                    	  String allFlavorData = (String)transferable.getTransferData(DataFlavor.stringFlavor);
                          String filepath = allFlavorData.substring(0, allFlavorData.indexOf(".mp3") + 4);
                          System.out.println(filepath);
                          addSong(filepath, "Library");
                          addSong(filepath, playlist);
                          break;
                    }
                   
                } catch (Exception e) {

                    // Print out the error stack
                    e.printStackTrace();

                }
            }

            // Inform that the drop is complete
            event.dropComplete(true);
            
            	System.out.println("bmaka");
            	app.tableRefresh();
            	

        }

        @Override
        public void dragEnter(DropTargetDragEvent event) {
        }

        @Override
        public void dragExit(DropTargetEvent event) {
        }

        @Override
        public void dragOver(DropTargetDragEvent event) {
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent event) {
        }

    }
    class RowPopup extends JPopupMenu{
    	public RowPopup(JTable table) {
    		JMenuItem add = new JMenuItem("Add");
    		JMenuItem delete = new JMenuItem("Delete");
    		
    		add.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					System.out.println("Yes this is the one");
		        	   JFileChooser chooser = new JFileChooser();
		               if (chooser.showOpenDialog(playlistWindow) == JFileChooser.APPROVE_OPTION) {
		            	   File file = chooser.getSelectedFile();
		            	    DefaultTableModel contactTableModel = (DefaultTableModel) table.getModel();
		            	   System.out.println(file.getPath());
		            	   try {
		            		   addSong(file.getPath(), "Library");
							addSong(file.getPath(), playlist);
							Playlist.this.app.tableRefresh();
						} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		               }
				}
    			
    		});
    		
    		
    		delete.addActionListener(new ActionListener() {

    			@Override
    			public void actionPerformed(ActionEvent arg0) {
    				/*JOptionPane.showMessageDialog(add, "Deleted");*/
    				String sk="";
    				int column = 0;
                	int i = table.getSelectedRowCount();
                	   int[] rows = table.getSelectedRows();

                	if (i >= 0) {

                		System.out.println("THE NUMBER OF ROWS TO DELTE "+i);

                		  for (int row3 = 0; row3<rows.length;row3++) {
                    			sk+= table.getModel().getValueAt(rows[row3], column).toString()+",";
                    			//System.out.println("ROWS FOR "+sk);
                              }
                  		  sk=sk.substring(0, sk.length()-1);
                  		System.out.println("ROWS FOR "+sk);
                  		  Query.deleteSong(sk);
                  		int numRows = table.getSelectedRows().length;
                  		for(int t=0; t<numRows ; t++ ) {

                  		    tableModel.removeRow(table.getSelectedRow());
                  		}

                	} else {
                	System.out
                	.println("There were issue while Deleting the Row(s).");
                	}
                	
                	table.addNotify();
    				
    			}
    			
    		});
    		
    		
    		  
    		Playrecent2.addMenuListener(new MenuListener() {

                JMenuItem menuItem;
                public void menuSelected(MenuEvent me) {
                	Playrecent2.removeAll();//remove previous opened window jmenuitems
                	String[] data2=	Query.recentlyPlayedDisplay();
					for(int i=0;i<data2.length;i++)
					{
						//System.out.println("index Recently Played:"+ data2[i].toString());
                        menuItem = new JMenuItem(data2[i]);
                        Playrecent2.add(menuItem);
                        menuItem.addActionListener(new ActionListener() {
                        	public void actionPerformed(ActionEvent arg0) {
                        		 JMenuItem menuitem=(JMenuItem) arg0.getSource();
                                 JPopupMenu popupMenu =(JPopupMenu) menuitem.getParent();
                                 int index= popupMenu.getComponentIndex(menuitem);
                                 
                                

                                 for (int c = 0; c < table.getRowCount(); c++) {
                                	 System.out.println("index: "+table.getModel().getValueAt(c, 1).toString());
                                	 System.out.println("indexw: "+data2[index].toString());
                                	 if(data2[index].toString().equals(table.getModel().getValueAt(c, 1).toString()))
                                	 {
                                		 currentSelectedSong=c;
                                		 System.out.println("CURRENTLY "+currentSelectedSong);
                                		 break;
                                	 }
                                	 
                                   }
                                 try {
									addSongRecentlyPlayed(table.getModel().getValueAt(currentSelectedSong, 0).toString());
								} catch (UnsupportedTagException | InvalidDataException | IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                         		table.setRowSelectionInterval(currentSelectedSong, currentSelectedSong);
             	                table.scrollRectToVisible(table.getCellRect(currentSelectedSong, 0, true));
             					playSong();
                                 
                                 

                                 
                        	}
                        }
                        );
					}

	
                }

                @Override
                public void menuDeselected(MenuEvent me) {
                }

                @Override
                public void menuCanceled(MenuEvent me) {
                }
            });
    		
    		
    		
    		
    		add(add);
    		add(delete);
    	}
    }
    
    static class Next2JmenuButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {

            int column = 0;
        	int row = table.getSelectedRow();
        	if(row==table.getRowCount()-1) {
        		row=0;
        	}
        	else
        		row = row+1;	
        	String Title = table.getModel().getValueAt(row, 1).toString();
    		String Artist = table.getModel().getValueAt(row, 2).toString();
    		nowPlaying.setText("<html>Now Playing: &nbsp;&nbsp;<br/>" + Title + "&nbsp;&nbsp;<br/> by &nbsp;&nbsp;<br/>" + Artist + "&nbsp;&nbsp;</html>");
        	table.changeSelection(row, column, false, false);
        	isPaused = false;
        	table.addRowSelectionInterval(row, row);
        	currentSelectedSong=row;
			table.setRowSelectionInterval(currentSelectedSong, currentSelectedSong);
            table.scrollRectToVisible(table.getCellRect(currentSelectedSong, 0, true));
            try {
				if(!Shuffle2.isSelected())
				{
					addSongRecentlyPlayed(table.getModel().getValueAt(row, column).toString());
				}
			} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	playSong();

		
         }
    }
    public static void playSong() {
        next = 0;
        previous = 0;
            if (threadStop != 0) {
                stop();
            }
            stopCheck = 0;

            currentSelectedSong = table.getSelectedRow(); 
        	row = table.getSelectedRow();
        	Play(table.getModel().getValueAt(row, 0).toString());
            threadStop = 1;
        
    }
    public static void Play(String file2)
    {

   	try {
			mp3file = new Mp3File(file2);
			fin = new FileInputStream(new File(file2));
			ProgressBarSetup();
			lengthOfSongInSeconds = mp3file.getLengthInSeconds();
			pointerDg=(int) lengthOfSongInSeconds;
			System.out.println("FIN AVALIABLE "+fin.available());
			System.out.println("lengthOfSongInSeconds "+lengthOfSongInSeconds);
			progressEverySecond = (fin.available() / lengthOfSongInSeconds);
		} catch (UnsupportedTagException | InvalidDataException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
   	
       progressTimer = new Timer(1000, new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               if (pointerPr < lengthOfSongInSeconds) {
               	System.out.println("PROGRESS BAR SETUP VALUE "+Math.round(progressEverySecond * pointerPr));
               	progressBar.setValue(Math.round(progressEverySecond * pointerPr));
                   if (pointerPs == 0) {
                       pointerPr++;
                       pointerDg--;
                       System.out.println("pointerPr Value "+pointerPr);
                       System.out.println("pointerDg Value "+pointerDg);
                       System.out.println("progressEverySecond Value "+progressEverySecond);
   	                remTimeLabel.setText(getTimeFormat(pointerPr));
   	                tottimeLabel.setText(getTimeFormat(pointerDg));

                   }
                       System.out.println("Song progress "+pointerPr+" The Length second "+lengthOfSongInSeconds);
                       
                   }
               else {
               		stop();
                       nowPlaying.setText("");
                       if (Repeat2.isSelected()) {
                       	
                       	System.out.println("REPEATE SELECTED pointer progress "+pointerPr);
                       	playSong();	
                       } 
                       
                       else if (Shuffle2.isSelected()) {
                           shuffle();
                           
                       } 
                       /*else {
                           next_Song_ButtonMouseClicked(null);
                       }
                       */
                   
               }
               
               
           }
       });
   	
       progressTimer.start();
   	try {
   		String Title = table.getModel().getValueAt(row, 1).toString();
   		String Artist = table.getModel().getValueAt(row, 2).toString();
   		nowPlaying.setText("<html>Now Playing:  &nbsp;&nbsp; <br/>" + Title + "&nbsp;&nbsp;<br/> by &nbsp;&nbsp; <br/>" + Artist + "&nbsp;&nbsp;</html>");
   	    player.open(new URL("file:///" + file2));
   	    player.play();
   	    disablePlay();
   	} catch (BasicPlayerException  | IOException e1) {
   	    e1.printStackTrace();
   	}
   	
    }
    
    private static void disablePlay() throws IOException {

        Play2.setSelected(false);
        Play.setSelected(false);
    }
    
    static class Increasevol2JmenuButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
                int volume2 = volume.getValue();
                if (volume2 > 95) {
                	volume.setValue(100);
                } else {
                	volume.setValue(volume2 + 5);
                }


         }
    }
    
    static class Decreasevol2JmenuButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	
            int volume2 = volume.getValue();
            if (volume2 < 5) {
            	volume.setValue(0);
            } else {
            	volume.setValue(volume2 - 5);
            }
        	
        	


         }
    }
    
    static class Currentsongt2JmenuButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
                table.setRowSelectionInterval(currentSelectedSong, currentSelectedSong);
                table.scrollRectToVisible(table.getCellRect(currentSelectedSong, 0, true));
        

         }
    }
    
    
    private static String getTimeFormat(long seconds) {
    	int min, sec;
        String time;

        min = (int) seconds / 60;
        sec = (int) seconds % 60;

        if (min < 10) {
            time = "00:0" + Integer.toString(min);
        } else {
            time = "00:" + Integer.toString(min);
        }
        if (sec < 10) {
            time = time + ":0" + Integer.toString(sec);
        } else {
            time = time + ":" + Integer.toString(sec);
        }
        return time;
    }
    
    
	public static void stop()
	{
		isPaused = false;
    	nowPlaying.setText("");
    	try {
    		
			player.stop();
			disablePlay();
            stopCheck = 1;
            playControl = 0;
            progressTimer.stop();
            pointerPr = 0;
            progressBar.setValue(0);
            remTimeLabel.setText("00:00:00");
            tottimeLabel.setText(getTimeFormat(lengthOfSongInSeconds));
		} catch (BasicPlayerException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
    
    
    public static void addSongRecentlyPlayed(String fileName) throws UnsupportedTagException, InvalidDataException, IOException
    {
         mp3file = new Mp3File(fileName);
        String Title =fileName.substring(fileName.lastIndexOf('\\')+1, fileName.length());
        Title = Title.substring(0,Title.indexOf('.'));
    	if (mp3file.hasId3v1Tag()) {
    		  ID3v1 id3v1Tag = mp3file.getId3v1Tag();
			  System.out.println("Title: " + id3v1Tag.getTitle());
              Title= id3v1Tag.getTitle(); 		    
    	}

   		 Query.insertSongRecentlyPlayed(fileName,Title);
					
    	
    }

    static class Previous2JmenuButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            int column = 0;
        	int row = table.getSelectedRow();
        	if(row==0 || row<0) {
        		row = table.getRowCount()-1;
        	}
        	else
        		
        		row = row-1;	
        	String Title = table.getModel().getValueAt(row, 1).toString();
    		String Artist = table.getModel().getValueAt(row, 2).toString();
    		nowPlaying.setText("<html>Now Playing: &nbsp;&nbsp;<br/>" + Title + "&nbsp;&nbsp;<br/> by &nbsp;&nbsp;<br/>" + Artist + "&nbsp;&nbsp;</html>");
        	table.changeSelection(row, column, false, false);
        	isPaused = false;
        	table.addRowSelectionInterval(row, row);

        	currentSelectedSong=row;
			table.setRowSelectionInterval(currentSelectedSong, currentSelectedSong);
            table.scrollRectToVisible(table.getCellRect(currentSelectedSong, 0, true));
            try {
				if(!Shuffle2.isSelected())
				{
					addSongRecentlyPlayed(table.getModel().getValueAt(row, column).toString());
				}
			} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	//Play(table.getModel().getValueAt(row, column).toString());
        	playSong();
        	
         }
    }
    
    
    
    
    static class Play2JmenuButton implements ActionListener
    {
    	  
        public void actionPerformed(ActionEvent e)
        {
        	playControl=1;
        	 int column = 0;
      	   		row = table.getSelectedRow();
      	   	currentSelectedSong=row;
      	   	
      	   	if(Shuffle2.isSelected())
      	   	{
      	   		System.out.println("THE SHUFFLE SELECTED "+Shuffle2);
      	            shuffle();
      	   	}
      	   	
      	   	else
      	   	{
	        	if(row<0)
	        	{
	        		row=0;
	        		currentSelectedSong=row;
	        	}
	        	
	        	try {
					mp3file = new Mp3File(table.getModel().getValueAt(row, column).toString());
					fin = new FileInputStream(new File(table.getModel().getValueAt(row, column).toString()));
					ProgressBarSetup();
					lengthOfSongInSeconds = mp3file.getLengthInSeconds();
					pointerDg=(int) lengthOfSongInSeconds;
					System.out.println("FIN AVALIABLE "+fin.available());
					System.out.println("lengthOfSongInSeconds "+lengthOfSongInSeconds);
					if(!Shuffle2.isSelected())
					{
						addSongRecentlyPlayed(table.getModel().getValueAt(row, column).toString());
					}
					table.setRowSelectionInterval(currentSelectedSong, currentSelectedSong);
	                table.scrollRectToVisible(table.getCellRect(currentSelectedSong, 0, true));
					playSong();

				} catch (UnsupportedTagException | InvalidDataException | IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
        	
      	   	}
        	
         }
    }

    private static void ProgressBarSetup() throws IOException {
    	progressBar.setMaximum(fin.available());
        progressBar.setMinimum(0);
        
    }
    
    public static void shuffle() {
    	isPaused = false;
    	//calculating a random row number to play a random song
    	Random rand = new Random();
    	int min = 0 ;
    	int max = table.getRowCount();
    	
    	table.clearSelection();
    	row = rand.nextInt((max-min) + 1) + min;
    	if(randLast==row)
    	{
    		while(randLast==row)
    		{
    			row = rand.nextInt((max-min) + 1) + min;
    		}
    		randLast=row;
	    	table.addRowSelectionInterval(row, row);
	    	playSong();
    	}
    	else
    	{
	    	randLast=row;
	    	table.addRowSelectionInterval(row, row);
	    	playSong();
    	}
    	
    }

    
    
    
    
}