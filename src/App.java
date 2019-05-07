/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Enumeration;
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
import java.io.FileNotFoundException;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
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
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import javazoom.jlgui.basicplayer.BasicController;
//import StreamPlayerNew.ButtonListener;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

/**
 *
 * @author Daddy
 */
public class App {
    

    static JFrame  main = new JFrame("AudioPlayer");
    static JTable table;
    JPanel bottombtnPnl;
    JPanel bottombtnPn2;
    JPanel bottombtnPn3;
    JPanel bottombtnPnx;
    JPanel mainPanel;
    JPanel right;
    ButtonListener buttonListener;    
    JScrollPane scrollPane; 
    static JScrollPane scrollLibrary; 
    static JScrollPane scrollPlaylist; 
    int CurrentSelectedRow;
    static JButton Play;
    JButton Pause;
    JButton Stop;
    JButton Previous;
    JButton Next;
    JButton Shuffle;
    JButton Delete;
    JButton Search;
    RowPopup pop;
    JPanel btnPnl;
    static JPanel progressPanel;
    static JSlider volume;
    static JTree treeForLeft ;
    static TreePath[] path ;
    static TreePath pathAfterCreation; 
    static DefaultMutableTreeNode root ;
    static DefaultMutableTreeNode library ;
    static DefaultMutableTreeNode playlist ;
    static JPopupMenu pm;
    static BasicPlayer player;
    static BasicController control ;
    static String[][] data;
    static String[] columns = new String[8];
    static DefaultTableModel tableModel;
    static DefaultTreeModel model;
    static DBQuery Query;
    final static JTextArea textArea = new JTextArea();
    static boolean isPaused = false;
    static JLabel nowPlaying = new JLabel("");
    static FileInputStream fin;
    static JMenu menu ;
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
    static JMenuBar mb ;
    static Mp3File mp3file;
    static JLabel tottimeLabel;
    static JLabel remTimeLabel;
    static JMenuItem New ;
    static JMenuItem Open ;
    static JMenuItem Exit ;
    static JMenuItem createPlaylist ;
    static Random rand;
    static int lastRandom=-1;
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
    ArrayList<Playlist> play = new ArrayList<Playlist>(); // Create an ArrayList object
    
	   
	    

    public App(){
    	
    	/*
    	 * ADDING MENU BAR NAME FILE WHICH WILL CONTAIN NEW,OPEN,EXIT
    	 */
    	 mb = new JMenuBar();
    	 menu = new JMenu("FILE");
    	 New = new JMenuItem("Add To Library");
    	 Open = new JMenuItem("Open A Song");
    	 Exit = new JMenuItem("Exit");
    	 createPlaylist= new JMenuItem("Create Playlist");
    	 rand = new Random();
   
    	
    	
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
    	//Repeat2.addActionListener(new Repeat2JmenuButton());
    	//Shuffle2.addActionListener(new Shuffle2JmenuButton());
    	
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
     	menu.add(New);
    	menu.add(Open);
    	menu.add(createPlaylist);
    	menu.add(Exit);
    	
    	
    	Exit.addActionListener(new exitJmenuButton());
    	New.addActionListener(new newJmenuButton());
    	Open.addActionListener(new openJmenuButton());
    	createPlaylist.addActionListener(new createPlaylistJmenuButton());
    	main.pack();
    	
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
    	mainPanel = new JPanel(new BorderLayout(0, 0));
    	//left = new JPanel(new BorderLayout(0, 0));
    	right = new JPanel(new BorderLayout(0, 0));
    	
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
    	//textArea= new JTextArea();
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
          data = Query.playlistDisplaySongs("Library");
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
                 mb.requestFocus();
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
        scrollPane = new JScrollPane(table);
        volume = new JSlider(0,100,25);
        
        volume.addChangeListener( new ChangeListener() {
        	public void stateChanged(ChangeEvent evt)
        	{
        		volume((double)volume.getValue()/100);
        	}
        	
        });
        
        bottombtnPnl.add(Play, BorderLayout.CENTER);
        bottombtnPnl.add(Stop, BorderLayout.LINE_START);
        bottombtnPnl.add(Pause, BorderLayout.LINE_END);
        bottombtnPn2.add(Previous, BorderLayout.LINE_START);
        //bottombtnPn2.add(Repeat, BorderLayout.LINE_END);
        bottombtnPn2.add(Delete, BorderLayout.CENTER);
        
        bottombtnPn3.add(volume, BorderLayout.CENTER);
        bottombtnPn3.add(Next, BorderLayout.LINE_END);
 
        
        btnPnl.add(bottombtnPnl, BorderLayout.CENTER);
        btnPnl.add(bottombtnPn2, BorderLayout.LINE_START);
        btnPnl.add(bottombtnPn3, BorderLayout.LINE_END);
        
         pop = new RowPopup(table);
        
        table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if(SwingUtilities.isRightMouseButton(me)) {
					pop.show(me.getComponent(), me.getX(), me.getY() );
					
				}
			}
		});
        
        	
        //making tree on the left side
    	root = new DefaultMutableTreeNode("Root");
    	
    	library = new DefaultMutableTreeNode("Library");
    	playlist = new DefaultMutableTreeNode("Playlist");
    	
    	
        
        //Create a tree that allows one selection at a time.
        root.add(library);
        root.add(playlist);
        if(Query.getPlaylistCount() > 0)
        {
        	createPlaylistnode(playlist);
        }
        
        treeForLeft = new JTree(root);
        treeForLeft.setRootVisible(false);
        
    	
        treeForLeft.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if(SwingUtilities.isRightMouseButton(me)) {
					pop.show(me.getComponent(), me.getX(), me.getY() );
				}
				else
				{
					doMouseClicked(me);
				}
			}
		});
        
        
        model = (DefaultTreeModel)treeForLeft.getModel();
        
    	JScrollPane treePane = new JScrollPane(treeForLeft);
        treePane.setPreferredSize(new Dimension(200,1000));
       
        
        
        tottimeLabel = new JLabel();
        remTimeLabel = new JLabel();
        remTimeLabel.setText("00:00:00");
        tottimeLabel.setText("00:00:00");
        progressBar=new JProgressBar();
     	progressPanel =new JPanel(new BorderLayout());
     	progressPanel.add(remTimeLabel,BorderLayout.LINE_START);
     	progressPanel.add(progressBar,BorderLayout.CENTER);
     	progressPanel.add(tottimeLabel,BorderLayout.LINE_END);
     	/*
        try {
			ProgressBarSetup();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
        
        
        mainPanel.add(treePane,BorderLayout.WEST);
        textArea.setText("Drop Songs Here To Add To : Library");
        right.add(scrollPane, BorderLayout.NORTH);
        right.add(textArea, BorderLayout.CENTER);
        right.add(nowPlaying, BorderLayout.EAST);
        right.add(btnPnl, BorderLayout.SOUTH);
        mainPanel.add(right, BorderLayout.CENTER);
        mainPanel.add(progressPanel, BorderLayout.NORTH);
        main.requestFocus();
        main.setJMenuBar(mb);
        
       main.add(mainPanel);
       
       mb.requestFocus();
       
    }

    private static void ProgressBarSetup() throws IOException {
    	progressBar.setMaximum(fin.available());
        progressBar.setMinimum(0);
        
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

    public void tableRefresh() {
    	
    	String fileName;
        String Title ;
        String Artist=" ";
        String Album =" ";
        String Genere = " ";
        String Year="";
        String Length = "";
        
		path = treeForLeft.getSelectionPaths();
		String stk="";
        for (TreePath path : path) {
            stk= ""+path.getLastPathComponent();
            break;
        }
		tableRemoveAllRows();

	   data = Query.playlistDisplaySongs(stk);
	    for(int i=0; i<data.length; i++) {
	        fileName=data[i][0];
	        Title=data[i][1];
	        Artist=data[i][2];
	        Album =data[i][3];
	        Genere = data[i][4];
	        Year=data[i][5];
	        Length =data[i][6];
	        String[] rown = {fileName, Title,Artist,Album,Genere,Year,Length,stk};
	        tableModel.addRow(rown);
	    }
    }
    
    
    public static void tableRemoveAllRows()
    {
    	
    	int rowCount = tableModel.getRowCount();
    	//Remove rows one by one from the end of the table
    	for (int i = rowCount - 1; i >= 0; i--) {
    		tableModel.removeRow(i);
    	}
    }
    public void doMouseClicked(MouseEvent me) {
    	
        String fileName;
        String Title ;
        String Artist=" ";
        String Album =" ";
        String Genere = " ";
        String Year="";
        String Length = "";
        
		path = treeForLeft.getSelectionPaths();
		String stk="";
        for (TreePath path : path) {
            stk= ""+path.getLastPathComponent();
            break;
        }
		tableRemoveAllRows();

	   data = Query.playlistDisplaySongs(stk);
	    for(int i=0; i<data.length; i++) {
	        fileName=data[i][0];
	        Title=data[i][1];
	        Artist=data[i][2];
	        Album =data[i][3];
	        Genere = data[i][4];
	        Year=data[i][5];
	        Length =data[i][6];
	        String[] rown = {fileName, Title,Artist,Album,Genere,Year,Length,stk};
	        tableModel.addRow(rown);
	    }
	   
   


        
        System.out.println("STK PLAYLIST VALUE IS "+stk); 
        textArea.setText("Drop Songs Here To Add To : " + stk);
      }
    public static void createNodes(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode category2 = null;
        int nRow = table.getRowCount();
        for (int i = 0 ; i < nRow ; i++)
        {
        	category2 = new DefaultMutableTreeNode(table.getModel().getValueAt(i, 1).toString());
        	top.add(category2);
        }
    }
     public static void createPlaylistnode(DefaultMutableTreeNode top) {
			String[] stk1 =Query.playlistDisplay();
			//System.out.println(stk1.length);
			if(stk1.length>0 && stk1!=null)
			{
                    for (int k = 0 ; k < stk1.length ; k++)
                    {
                    	//System.out.println(stk1[k]);
                    	DefaultMutableTreeNode category2 = new DefaultMutableTreeNode(stk1[k]);
                    	top.add(category2);
                    }
			}
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
    
    
    public static void addSong(String fileName,String Playlist) throws UnsupportedTagException, InvalidDataException, IOException
    {
         mp3file = new Mp3File(fileName);
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
    	if(result==0)
    	{
   		 Query.insertSong(fileName,Title,Artist,Album,Genere,Year,Length,Playlist);
   		 String rowEntry = "";
				path = treeForLeft.getSelectionPaths();
				String stk="";
					for (TreePath path : path) {
							stk= ""+path.getLastPathComponent();
							break;
					}
					System.out.println("The Actual Playlist "+Playlist);
					System.out.println("The Actual stk "+stk);
					String[] rown = { fileName, Title,Artist,Album,Genere,Year,Length,stk};
					if(Playlist.equals(stk))
					{
						tableModel.addRow(rown);
					}
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
    
    
    

	public void go(){
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setExtendedState(JFrame.MAXIMIZED_BOTH); 
       // main.setUndecorated(true);
        main.setVisible(true);
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
              		  		model.reload(root);
            	}
            	
            
            	else {
            	System.out
            	.println("There were issue while Deleting the Row(s).");
            	}
            	
            	table.addNotify();
            }
            if("Search".equals(e.getActionCommand()))
            {
            	isPaused = false;
            	String[] stk;
                String name = JOptionPane.showInputDialog(main, "What is the name of the song?");//Note: input can be null.
               // String Title = table.getModel().getValueAt(row, 1).toString();
        		//String Artist = table.getModel().getValueAt(row, 2).toString();
        		nowPlaying.setText("");
        		
                try {
					stk=Query.searchSongByTitle(name);
					System.out.println("The Search Path is  "+stk[0]);
					
					nowPlaying.setText("<html>Now Playing: &nbsp;&nbsp;<br/>" + stk[1] + "&nbsp;&nbsp;<br/> by &nbsp;&nbsp;<br/>" + stk[2] + "&nbsp;&nbsp;</html>");
					
					
	            	try {
	            	    player.open(new URL("file:///" + stk[0]));
	            	    player.play();
	            	} catch (BasicPlayerException | MalformedURLException e1) {
	            	    e1.printStackTrace();
	            	}
				} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
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
    
    
    
    
    
    
    
    /*
     * RELATED TO THE JMENU BUTTON NEW
     * OPENS NEW BROWSER WINDOW TO SELECT SONG
     */
    static class newJmenuButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	System.out.println("Yes this is the one");
        	   JFileChooser chooser = new JFileChooser();
               if (chooser.showOpenDialog(main) == JFileChooser.APPROVE_OPTION) {
            	   File file = chooser.getSelectedFile();
            	    DefaultTableModel contactTableModel = (DefaultTableModel) table.getModel();
            	   
            	   System.out.println(file.getPath());
   				//System.out.println("YOU ARE IN DETELE PLAYLIST SECTION");
   				//path = treeForLeft.getSelectionPaths();
   				String stk="Library";
                  /* for (TreePath path : path) {
                       stk= ""+path.getLastPathComponent();
                       break;
                   }*/
            	   
            	   
            	   
            	   try {
            		   if(stk=="" || stk ==" " || stk.equals("Library"))
            		   {
            			   addSong(file.getPath(),"Library");
            		   }
            		   else
            		   {
            			   addSong(file.getPath(),"Library");
            			   addSong(file.getPath(),stk);
            		   }
					
					
				} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
               }
         }
    }
    
    static class openJmenuButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	
        	   JFileChooser chooser = new JFileChooser();
               if (chooser.showOpenDialog(main) == JFileChooser.APPROVE_OPTION) {
            	   File file = chooser.getSelectedFile();
            	    stop();
            	    Play(file.getPath());
            	

               }
         }
    }
    
    static class createPlaylistJmenuButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	
        	
        	String[] stk;
            String Playlist = JOptionPane.showInputDialog(main, "What is the name of the new Playlist?");//Note: input can be null.
            try {
            	if(Playlist!="" && Playlist !=" " && Playlist.length()>0)
            	{
					Query.insertSong("TEST","TEST","TEST","TEST","TEST","1900","1",Playlist);
					String[] stk1 =Query.playlistDisplay();
	                        DefaultMutableTreeNode newPlay = new DefaultMutableTreeNode(Playlist);
	                        playlist.add(newPlay);
	                        treeForLeft.updateUI();
	                        treeForLeft.expandPath(new TreePath(playlist.getPath()));
	                        //treeForLeft.setSelectionRow(1);
	                        pathAfterCreation = new TreePath(newPlay.getPath());
	                        treeForLeft.getExpandsSelectedPaths();
	                        System.out.println(pathAfterCreation);
	                        treeForLeft.setSelectionPath(pathAfterCreation);
	                        treeForLeft.scrollPathToVisible(pathAfterCreation);
	                        tableRemoveAllRows();
	                        textArea.setText("Drop Songs Here To Add To : " + Playlist);
	                        
	                        
	                        
            	}

			} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
                        	
                        	
                        	
                        	
               				path = treeForLeft.getSelectionPaths();
               				String stk="";
                               for (TreePath path : path) {
                                   stk= ""+path.getLastPathComponent();
                                   break;
                               }
                        	   
                        	   
           
                               if(stk=="" || stk ==" " || stk.equals("Library"))
                        		   {
                            	 
                        			   addSong(file1.getPath(),"Library");
                        		   }
                        		   else
                        		   {
                        			
                        			   addSong(file1.getPath(),"Library");
                        			   addSong(file1.getPath(),stk);
                        		   }


                        }

                    }

                } catch (Exception e) {

                    // Print out the error stack
                    e.printStackTrace();

                }
            }
            
            // Inform that the drop is complete
            event.dropComplete(true);
            mb.requestFocus();
            playlistwindow.tableRefresh();
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
    		JMenuItem newWindow = new JMenuItem("Open in New Window");
    		JMenuItem delPlaylist = new JMenuItem("Delete Playlist");
    		JMenu addPlaylist = new JMenu("Add to playlist");
    		//JMenuItem addPlaylist = new JMenuItem("Add to playlist");
    		
    		add.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
		        	System.out.println("Yes this is the one");
		        	   JFileChooser chooser = new JFileChooser();
		        	   path = treeForLeft.getSelectionPaths();
	    				String stk="";
	                    for (TreePath path : path) {
	                        stk= ""+path.getLastPathComponent();
	                        break;
	                    }
		               if (chooser.showOpenDialog(main) == JFileChooser.APPROVE_OPTION) {
		            	   File file = chooser.getSelectedFile();
		            	    DefaultTableModel contactTableModel = (DefaultTableModel) table.getModel();
		            	   
		            	   System.out.println(file.getPath());
		            	   try {
		            		   if(stk.equals("Library")) {
		            			   addSong(file.getPath(),"Library");
		            		   }
		            		   else {
		            			   addSong(file.getPath(),"Library");
		            			   addSong(file.getPath(), stk);
		            		   }
							
						} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		               }
		               if(playlistwindow != null) {
		            	   playlistwindow.tableRefresh();
		               }
              		    //library.removeAllChildren();      		    
               		  /* for (int t = 0; t < table.getRowCount(); t++) {
               			  library.add(new DefaultMutableTreeNode(table.getModel().getValueAt(t, 1).toString()));
           		    }*/
               		// System.out.println("ABOUT TO COUNT CHILD FOR ROOT AFTER ADDING "+ root.getChildCount());

               		// model.reload(library);

				}
    			
    		});
    		delete.addActionListener(new ActionListener() {

    			@Override
    			public void actionPerformed(ActionEvent arg0) {
    				/*JOptionPane.showMessageDialog(add, "Deleted");*/
    				String sk="";
    				String pl="";
    				int column = 0;
                	int i = table.getSelectedRowCount();
                	   int rows = table.getSelectedRow();

                	if (i >= 0) {

                				System.out.println("THE NUMBER OF ROWS TO DELTE "+i);
                    			sk= table.getModel().getValueAt(rows, column).toString();//+",";
                    			pl= table.getModel().getValueAt(rows, 7).toString();//+",";
                    			Query.deleteSongFromPlaylist(sk, pl);
                    			tableModel.removeRow(table.getSelectedRow());
 

                	} else {
                	System.out
                	.println("There were issue while Deleting the Row(s).");
                	}
                	
                	table.addNotify();
    				
    			}
    			
    		});
    		delPlaylist.addActionListener(new ActionListener() {
    			
    			@Override
    			public void actionPerformed(ActionEvent arg0) {
    				
    				//System.out.println("YOU ARE IN DETELE PLAYLIST SECTION");
    				path = treeForLeft.getSelectionPaths();
    				String stk="";
                    for (TreePath path : path) {
                        stk= ""+path.getLastPathComponent();
                        break;
                    }
                    int reply = JOptionPane.showConfirmDialog(null, "Would you like to delete the Playlist "+stk, "Delete Option", JOptionPane.YES_NO_OPTION);
                  if(reply == JOptionPane.YES_OPTION)
                 {
                   Query.deletePlaylist(stk);
                   model = (DefaultTreeModel) treeForLeft.getModel();

                   path = treeForLeft.getSelectionPaths();
                   if (path != null) {
                       for (TreePath path1 : path) {
                           DefaultMutableTreeNode node = (DefaultMutableTreeNode) 
                               path1.getLastPathComponent();
                           if (node.getParent() != null) {
                               model.removeNodeFromParent(node);
                           }
                       }
                   }
                   
    			}
                  tableRemoveAllRows();
                  int temp=0;
                  for(int i=0;i<play.size();i++)
                  {
                	  if(play.get(i).getFrameName().equals(stk))
                	  {
                		  System.out.println("ABOT TO CLOSE "+stk);
                		  play.get(i).exit();
                		  temp=i;
                	  }
                  }

    			}
    			
    		});
    	
    
    		
  
    		Playrecent2.addMenuListener(new MenuListener() {

                JMenuItem menuItem;
                public void menuSelected(MenuEvent me) {
                	Playrecent2.removeAll();//remove previous opened window jmenuitems
                	String[] data2=	Query.recentlyPlayedDisplay();
					for(int i=0;i<data2.length;i++)
					{
						System.out.println("index Recently Played:"+ data2[i].toString());
                        menuItem = new JMenuItem(data2[i]);
                        Playrecent2.add(menuItem);
                        menuItem.addActionListener(new ActionListener() {
                        	public void actionPerformed(ActionEvent arg0) {
                        		 JMenuItem menuitem=(JMenuItem) arg0.getSource();
                                 JPopupMenu popupMenu =(JPopupMenu) menuitem.getParent();
                                 int index= popupMenu.getComponentIndex(menuitem);
                                 
                                 System.out.println("index:"+ data2[index].toString());
                                 if(threadStop!=0)
                                 {
                                	 stop();
                                 }
                                 System.out.println("SONGS TO BE PLAYED FILE NAME "+menuItem.getText());
                                 Play(Query.recentlyPlayedDisplayFileName(menuItem.getText()));
                                 try {
									addSongRecentlyPlayed(Query.recentlyPlayedDisplayFileName(menuItem.getText()));
								} catch (UnsupportedTagException | InvalidDataException | IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                                 threadStop=1;
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
    		
    		addPlaylist.addMenuListener(new MenuListener() {

                JMenuItem menuItem;
                public void menuSelected(MenuEvent me) {
                	addPlaylist.removeAll();//remove previous opened window jmenuitems
                	
                	String[] 
					data2=	Query.playlistDisplay();
					for(int i=0;i<data2.length;i++)
					{
                        menuItem = new JMenuItem(data2[i]);
                        addPlaylist.add(menuItem);
                        menuItem.addActionListener(new ActionListener() {
                        	public void actionPerformed(ActionEvent arg0) {
                        		 JMenuItem menuitem=(JMenuItem) arg0.getSource();
                                 JPopupMenu popupMenu =(JPopupMenu) menuitem.getParent();
                                 int index= popupMenu.getComponentIndex(menuitem);
                                 System.out.println("index:"+ data2[index].toString());
                           	  try {
        							addSong(table.getModel().getValueAt(table.getSelectedRow(), 0).toString(),data2[index].toString());
        							if(playlistwindow!=null)
        							{
        								playlistwindow.tableRefresh();
        							}
        						} catch (UnsupportedTagException | InvalidDataException | IOException e) {
        							// TODO Auto-generated catch block
        							e.printStackTrace();
        						}
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
    		
    		
    		
    		
    		
    		

    		newWindow.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					path = treeForLeft.getSelectionPaths();
    				String stk="";
                    for (TreePath path : path) {
                        stk= ""+path.getLastPathComponent();
                        break;
                    }
                    System.out.println("this is the selected one : " +play.size());
					playlistwindow = new Playlist(stk, App.this);
					treeForLeft.setSelectionPath(new TreePath(library.getPath()));
					textArea.setText("Drop Songs Here To Add To : Library");
					App.this.tableRefresh();
					play.add(playlistwindow);
					
					
				}
    			
    			
    		});
    		
    		
    		
    		add(add);
    		add(newWindow);
    		add(delPlaylist);
    		add(addPlaylist);
    		add(delete);
    	}
    }
    
}