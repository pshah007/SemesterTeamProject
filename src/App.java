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
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

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
    ButtonListener buttonListener;
    
    
    JScrollPane scrollPane; 
    int CurrentSelectedRow;
    JButton Play;
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
    BasicController control ;
    static String[][] data;
    static String[] columns = new String[7];
    static DefaultTableModel tableModel;
    static DBQuery Query;
    final JTextArea textArea;
    static boolean isPaused = false;
    JLabel nowPlaying = new JLabel("");


	
    
    /**
     * 
     */
    public App(){
    	
    	/*
    	 * ADDING MENU BAR NAME FILE WHICH WILL CONTAIN NEW,OPEN,EXIT
    	 */
    	JMenuBar mb = new JMenuBar();
    	JMenu menu = new JMenu("FILE");
    	JMenuItem New = new JMenuItem("Add To Library");
    	JMenuItem Open = new JMenuItem("Open A Song");
    	JMenuItem Exit = new JMenuItem("Exit");
    	menu.add(New);
    	menu.add(Open);
    	menu.add(Exit);
    	mb.add(menu);
    	
    	Exit.addActionListener(new exitJmenuButton());
    	New.addActionListener(new newJmenuButton());
    	Open.addActionListener(new openJmenuButton());
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
    	player = new BasicPlayer();
        control = (BasicController) player;
      
          //data holds the table data and maps as a 2d array into the table
          data = Query.dataDisplay();
          
          
          
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
        
        bottombtnPnl.add(Play, BorderLayout.CENTER);
        bottombtnPnl.add(Stop, BorderLayout.LINE_START);
        bottombtnPnl.add(Pause, BorderLayout.LINE_END);
        bottombtnPn2.add(Previous, BorderLayout.LINE_START);
        //bottombtnPn2.add(Repeat, BorderLayout.LINE_END);
        bottombtnPn2.add(Delete, BorderLayout.CENTER);
        
        bottombtnPn3.add(Shuffle, BorderLayout.LINE_START);
        bottombtnPn3.add(Search, BorderLayout.CENTER);
        bottombtnPn3.add(Next, BorderLayout.LINE_END);
 
        
        btnPnl.add(bottombtnPnl, BorderLayout.CENTER);
        btnPnl.add(bottombtnPn2, BorderLayout.LINE_START);
        btnPnl.add(bottombtnPn3, BorderLayout.LINE_END);
        
        main.add(textArea);
        main.setJMenuBar(mb);
        textArea.setText("Drop Songs Here To Add to Library");
        main.add(scrollPane, BorderLayout.NORTH);
        main.add(textArea, BorderLayout.CENTER);
        main.add(btnPnl, BorderLayout.SOUTH);
        main.add(nowPlaying, BorderLayout.EAST);
       
       
       
    }
    
    
    public static void addSong(String fileName) throws UnsupportedTagException, InvalidDataException, IOException
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
    		 Query.checkSong(fileName,Title,Artist,Album,Genere,Year,Length);
    		 String rowEntry = "";
    		 int check=0;
    		   for (int i = 0; i < tableModel.getRowCount(); i++) {
    		 
    		            rowEntry = table.getValueAt(i, 0).toString();
    		        if (rowEntry.equalsIgnoreCase(fileName)) {
    		        	check=1;
    		            break;
    		        }
    		    }
    		   if(check==0)
    		   {
    			   String[] rown = { fileName, Title,Artist,Album,Genere,Year,Length };
    			   tableModel.addRow(rown);
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
            	Query.deleteSong(table.getModel().getValueAt(row, column).toString());
            	
            	
             	// i = the index of the selected row
            	int i = table.getSelectedRow();
            	if (i >= 0) {
            	// remove a row from jtable
            		tableModel.removeRow(i);
            	} else {
            	System.out
            	.println("There were issue while Deleting the Row(s).");
            	}
            	
            	table.addNotify();
            }
            if("Search".equals(e.getActionCommand()))
            {
            	isPaused = false;
            	String stk="";
                String name = JOptionPane.showInputDialog(main, "What is the name of the song?");//Note: input can be null.
               // String Title = table.getModel().getValueAt(row, 1).toString();
        		//String Artist = table.getModel().getValueAt(row, 2).toString();
        		nowPlaying.setText("");
                try {
					stk=Query.searchSongByTitle(name);
					System.out.println("The Search Path is  "+stk);
	            	try {
	            	    player.open(new URL("file:///" + stk));
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
            	int max = table.getColumnCount();
            	
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
        	
        	   JFileChooser chooser = new JFileChooser();
               if (chooser.showOpenDialog(main) == JFileChooser.APPROVE_OPTION) {
            	   File file = chooser.getSelectedFile();
            	    DefaultTableModel contactTableModel = (DefaultTableModel) table.getModel();
            	   System.out.println(file.getPath());
            	   try {
					addSong(file.getPath());
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
                        	addSong(file1.getPath());
                        	Stk+=file1.getPath()+"\n";
                        	textArea.setText(Stk);
                            System.out.println("File path is '" + file1.getPath() + "'.");

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
    
    
}
