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
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    static JTable table;
    JPanel bottombtnPnl;
    JPanel bottombtnPn2;
    JPanel bottombtnPn3;
    JPanel bottombtnPnx;
    ButtonListener buttonListener;
    JSlider volume;
    
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
    //static BasicController control;
    static BasicController control ;
    static String[][] data;
    static String[] columns = new String[8];
    static DefaultTableModel tableModel;
    static DBQuery Query;
    final JTextArea textArea;
    static boolean isPaused = false;
    JLabel nowPlaying = new JLabel("");
    static String playlist;

	
    
    /**
     * 
     */
    public Playlist(String playlistName){
    	
    	playlist = playlistName;
    	playlistWindow = new JFrame(playlist);
    	/*
    	 * ADDING MENU BAR NAME FILE WHICH WILL CONTAIN NEW,OPEN,EXIT
    	 */
    	JMenuBar mb = new JMenuBar();
    	JMenu menu = new JMenu("FILE");
    	JMenuItem New = new JMenuItem("Add To Library");
    	JMenuItem Open = new JMenuItem("Open A Song");
    	JMenuItem Exit = new JMenuItem("Exit");
    	JMenuItem cplay= new JMenuItem("Create Playlist");
    	
    	menu.add(New);
    	menu.add(Open);
    	menu.add(cplay);
    	menu.add(Exit);


    	mb.add(menu);
    	
    	Exit.addActionListener(new exitJmenuButton());
    	New.addActionListener(new newJmenuButton());
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
        
        playlistWindow.add(textArea);
        playlistWindow.setJMenuBar(mb);
        textArea.setText("Drop Songs Here To Add to Playlist : " + playlist);
        playlistWindow.add(scrollPane, BorderLayout.NORTH);
        playlistWindow.add(textArea, BorderLayout.CENTER);
        playlistWindow.add(btnPnl, BorderLayout.SOUTH);
        playlistWindow.add(nowPlaying, BorderLayout.EAST);
        playlistWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        playlistWindow.setExtendedState(JFrame.MAXIMIZED_BOTH); 
       // playlistWindow.setUndecorated(true);
        playlistWindow.setVisible(true);
       
        
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
    		 Query.insertSong(fileName,Title,Artist,Album,Genere,Year,Length,playlist);
    		 String rowEntry = "";
			
					System.out.println("The Actual Playlist "+playlist);
					//System.out.println("The Actual stk "+stk);
					String[] rown = { fileName, Title,Artist,Album,Genere,Year,Length,playlist};
					//if(Playlist.equals(stk))
					//{
						tableModel.addRow(rown);
    	}
    		
					//}

    				   

    			   
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
            if("Search".equals(e.getActionCommand()))
            {
            	isPaused = false;
            	String[] stk;
                String name = JOptionPane.showInputDialog(playlistWindow, "What is the name of the song?");//Note: input can be null.
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
               if (chooser.showOpenDialog(playlistWindow) == JFileChooser.APPROVE_OPTION) {
            	   File file = chooser.getSelectedFile();
            	    DefaultTableModel contactTableModel = (DefaultTableModel) table.getModel();
            	   System.out.println(file.getPath());
            	   try {
					addSong(file.getPath(), playlist);
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
                        	addSong(file1.getPath(), playlist);
                        	Stk+=file1.getPath()+"\n";
                        	textArea.setText(Stk);
                            System.out.println("File path is '" + file1.getPath() + "'.");

                        }

                    }
                    else {
                    	  String allFlavorData = (String)transferable.getTransferData(DataFlavor.stringFlavor);
                          String filepath = allFlavorData.substring(0, allFlavorData.indexOf(".mp3") + 4);
                          System.out.println(filepath);
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
    		add.addActionListener(new newJmenuButton());
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
    		add(add);
    		add(delete);
    	}
    }
    
}