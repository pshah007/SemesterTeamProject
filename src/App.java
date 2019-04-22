/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
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
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
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
    JButton Play;
    JButton Pause;
    JButton Stop;
    JButton Previous;
    JButton Next;
    JButton Shuffle;
    JButton Delete;
    JButton Search;
    RowPopup pop;
    JPanel btnPnl;
    JSlider volume;
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
    JLabel nowPlaying = new JLabel("");

    
	   
	    

    public App(){
    	
    	/*
    	 * ADDING MENU BAR NAME FILE WHICH WILL CONTAIN NEW,OPEN,EXIT
    	 */
    	JMenuBar mb = new JMenuBar();
    	JMenu menu = new JMenu("FILE");
    	JMenuItem New = new JMenuItem("Add To Library");
    	JMenuItem Open = new JMenuItem("Open A Song");
    	JMenuItem Exit = new JMenuItem("Exit");
    	JMenuItem createPlaylist= new JMenuItem("Create Playlist");
    	
    	menu.add(New);
    	menu.add(Open);
    	menu.add(createPlaylist);
    	menu.add(Exit);


    	mb.add(menu);
    	
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
        
        bottombtnPnl.add(Play, BorderLayout.CENTER);
        bottombtnPnl.add(Stop, BorderLayout.LINE_START);
        bottombtnPnl.add(Pause, BorderLayout.LINE_END);
        bottombtnPn2.add(Previous, BorderLayout.LINE_START);
        //bottombtnPn2.add(Repeat, BorderLayout.LINE_END);
        bottombtnPn2.add(Delete, BorderLayout.CENTER);
        
        bottombtnPn3.add(Shuffle, BorderLayout.LINE_START);
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
       
        
        
        right.add(textArea);
        main.setJMenuBar(mb);
        textArea.setText("Drop Songs Here To Add To : Library");
        right.add(scrollPane, BorderLayout.NORTH);
        right.add(textArea, BorderLayout.CENTER);
        right.add(nowPlaying, BorderLayout.EAST);
        right.add(btnPnl, BorderLayout.SOUTH);
        mainPanel.add(right, BorderLayout.CENTER);
        //left.setPreferredSize(new Dimension(200,200));
        //left.add(pContainer1, BorderLayout.CENTER);
        mainPanel.add(treePane,BorderLayout.WEST);
        main.add(mainPanel);
       
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
    
    
    public static void addSong(String fileName,String Playlist) throws UnsupportedTagException, InvalidDataException, IOException
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

	public void go(){
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setExtendedState(JFrame.MAXIMIZED_BOTH); 
       // main.setUndecorated(true);
        main.setVisible(true);
    }

	



    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            File file=null;
            int column = 0;
        	int row = table.getSelectedRow();
            if("Play".equals(e.getActionCommand())){
            	try {
            		String Title = table.getModel().getValueAt(row, 1).toString();
            		String Artist = table.getModel().getValueAt(row, 2).toString();
            		nowPlaying.setText("<html>Now Playing:  &nbsp;&nbsp; <br/>" + Title + "&nbsp;&nbsp;<br/> by &nbsp;&nbsp; <br/>" + Artist + "&nbsp;&nbsp;</html>");
            	    player.open(new URL("file:///" + table.getModel().getValueAt(row, column).toString()));
            	    player.play();
            	} catch (BasicPlayerException | MalformedURLException e1) {
            	    e1.printStackTrace();
            	}
            	//testPlay(table.getModel().getValueAt(row, column).toString());
                  
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
            	nowPlaying.setText("");
            	try {
					player.stop();
				} catch (BasicPlayerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
            if(e.getSource()==Pause)
            {
            	if(!isPaused) {              	
                	try {
    					player.pause();
    					Pause.setText("Resume");
    				} catch (BasicPlayerException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
            	}
            	else if(isPaused){
            		try {
						player.resume();
						Pause.setText("Pause");
					} catch (BasicPlayerException e1) {
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
            	try {
            		player.open(new URL("file:///" + table.getModel().getValueAt(row, column).toString()));
            	    player.play();
				} catch (BasicPlayerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
            if("Previous".contentEquals(e.getActionCommand()))
            {
            	if(row == 0) {
            		row = table.getRowCount()-1;
            	}
            	else
            		row = row-1;
            	String Title = table.getModel().getValueAt(row, 1).toString();
        		String Artist = table.getModel().getValueAt(row, 2).toString();
        		nowPlaying.setText("<html>Now Playing: &nbsp;&nbsp;<br/>" + Title + "&nbsp;&nbsp;<br/> by &nbsp;&nbsp;<br/>" + Artist + "&nbsp;&nbsp;</html>");
            	table.changeSelection(row, column, false, false);
            	isPaused = false;
            	try {
            		player.open(new URL("file:///" + table.getModel().getValueAt(row, column).toString()));
            	    player.play();
				} catch (BasicPlayerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
            /*if("Repeat".contentEquals(e.getActionCommand()))
            {
            	isPaused = false;
            	
            	try {
            		player.open(new URL("file:///" + table.getModel().getValueAt(row, column).toString()));
            	    player.play();
				} catch (BasicPlayerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }*/
            if("Shuffle".contentEquals(e.getActionCommand()))
            {
            	isPaused = false;
            	//calculating a random row number to play a random song
            	Random rand = new Random();
            	int min = 0 ;
            	int max = table.getRowCount();
            	
            	//selecting the song and setting label
            	table.clearSelection();
            	row = rand.nextInt((max-min) + 1) + min;
            	table.addRowSelectionInterval(row, row);

            	String Title = table.getModel().getValueAt(row, 1).toString();
        		String Artist = table.getModel().getValueAt(row, 2).toString();
        		nowPlaying.setText("<html>Now Playing: &nbsp;&nbsp;<br/>" + Title + "&nbsp;&nbsp;<br/> by &nbsp;&nbsp;<br/>" + Artist + "&nbsp;&nbsp;</html>");
        		
            	try {
            		player.open(new URL("file:///" + table.getModel().getValueAt(row, column).toString()));
            	    player.play();
				} catch (BasicPlayerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }

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
   				path = treeForLeft.getSelectionPaths();
   				String stk="";
                   for (TreePath path : path) {
                       stk= ""+path.getLastPathComponent();
                       break;
                   }
            	   
            	   
            	   
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
    		JMenuItem addPlaylist = new JMenuItem("Add to playlist");
    		
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
    				
    			}
    			
    		});
    	
    		
    		
    		addPlaylist.addMouseListener(new MouseAdapter() {
    			
   			 DefaultTableModel tabm;
   			 JTable tab;
    			public void mouseEntered(MouseEvent me) {
    				 
        				
        	
        					String[] data2 = Query.playlistDisplay();
        					pm = new JPopupMenu();
        				 	tab = new JTable(); 
        				 	String[] stk = new String[1];
        				 	stk[0]="Playlist";
        		            
        				 	
        				 	  tabm= new DefaultTableModel();
        				 	  tabm.addColumn("Playlist",data2);
        			          tab = new JTable();
        			          tab.setModel(tabm);
        			          tab.setDragEnabled(true);
        			          pm.add(tab);
          		              //pm.setVisible(true);
          		              pm.show(me.getComponent(),me.getX(),me.getY());
          		              //pop.show(me.getComponent(),me.getX(),me.getY());
          		             // pm.setVisible(true);
          		              tab.addMouseListener(new java.awt.event.MouseAdapter() {
              		              @Override
              		              public void mousePressed(java.awt.event.MouseEvent evt) {
              		                  int row = tab.rowAtPoint(evt.getPoint());
              		                  int col = tab.columnAtPoint(evt.getPoint());
              		                  System.out.println("ROW IS "+row+" COL IS "+col);
              		                  System.out.println("SELECTED PALYLIST IS  "+tab.getValueAt(row, 0));
              		                System.out.println(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
              	    				  try {
              	    					pm.setVisible(true);
              							addSong(table.getModel().getValueAt(table.getSelectedRow(), 0).toString(),""+tab.getValueAt(row, 0));
              							//pm.setVisible(false);
              						} catch (UnsupportedTagException | InvalidDataException | IOException e) {
              							// TODO Auto-generated catch block
              							e.printStackTrace();
              						}
              		                  
              		                  
              		              }   
              		          });
          		        	//pm.setVisible(false);
    			}

 
    		});
    	
    		
    		/*
    		addPlaylist.addActionListener(new ActionListener() {
    			
    			 JPopupMenu pm;
    			 DefaultTableModel tabm;
    			 JTable tab;
    			public void actionPerformed(ActionEvent arg0) {
    				
    	
    					String[] data2 = Query.playlistDisplay();
    					pm = new JPopupMenu();
    				 	tab = new JTable(); 
    				 	String[] stk = new String[1];
    				 	stk[0]="Playlist";
    		            
    				 	
    				 	  tabm= new DefaultTableModel();
    				 	  tabm.addColumn("Playlist",data2);
    			          tab = new JTable();
    			          tab.setModel(tabm);
    			          tab.setDragEnabled(true);
    			          pm.add(tab);
      		              pm.setVisible(true);
      		              pm.setLocation((int)main.getMousePosition().getX(),(int)main.getMousePosition().getY());
      		              tab.addMouseListener(new java.awt.event.MouseAdapter() {
      		              @Override
      		              public void mousePressed(java.awt.event.MouseEvent evt) {
      		                  int row = tab.rowAtPoint(evt.getPoint());
      		                  int col = tab.columnAtPoint(evt.getPoint());
      		                  System.out.println("ROW IS "+row+" COL IS "+col);
      		                  System.out.println("SELECTED PALYLIST IS  "+tab.getValueAt(row, 0));
      		                System.out.println(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
      	    				  try {
      							addSong(table.getModel().getValueAt(table.getSelectedRow(), 0).toString(),""+tab.getValueAt(row, 0));
      						  pm.setVisible(false);
      						} catch (UnsupportedTagException | InvalidDataException | IOException e) {
      							// TODO Auto-generated catch block
      							e.printStackTrace();
      						}
      		                  
      		                  
      		              }

      		              
      		          });
    				 	

    			}
    			

    	        
    			
    		
    		});
    		*/
    		
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
                    System.out.println("this is the selected one : " +stk);
					Playlist playlistwindow = new Playlist(stk);
					
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