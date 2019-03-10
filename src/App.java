/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.BorderLayout;
import java.util.List;

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
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

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
    ButtonListener bl;
    JScrollPane scrollPane; 
    int CurrentSelectedRow;
    JButton Play;
    JButton Pause;
    JButton Stop;
    JButton Previous;
    JButton Next;
    JButton Repeat;
    JButton Shuffle;
    JButton Delete;
    JPanel btnPnl;
    BasicPlayer player;
    static DefaultTableModel tableModel;
    static DBQuery Query;
    final JTextArea textArea;
    public App(){
    	
    	/*
    	 * ADDING MENU BAR NAME FILE WHICH WILL CONTAIN NEW,OPEN,EXIT
    	 */
    	JMenuBar mb = new JMenuBar();
    	JMenu menu = new JMenu("FILE");
    	JMenuItem New = new JMenuItem("NEW");
    	JMenuItem Open = new JMenuItem("OPEN");
    	JMenuItem Exit = new JMenuItem("EXIT");
    	menu.add(New);
    	menu.add(Open);
    	menu.add(Exit);
    	mb.add(menu);
    	
    	Exit.addActionListener(new exitJmenuButton());
    	New.addActionListener(new newJmenuButton());
    	
    	/*
    	 * CREATING NEW OBJECT FOR DB QUERY TO CONNECT DB WHICH IS CALLED WEBDB1
    	 * ALSO CREATES TABLE AS WELL CALLED MUSIC
    	 */
    	 Query = new DBQuery();
    	 Query.createConnection();
    	 Query.createTable();
    	 MyDragDropListener myDragDropListener = new MyDragDropListener();

    	 
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
    	String[] columns = {"FILE","TITLE","ARTIST","ALBUM","GENERE","YEAR","LENGTH"};
    	  player = new BasicPlayer();
          //data holds the table data and maps as a 2d array into the table
          String[][] data = Query.dataDisplay();
          
          
          
          
          table = new JTable(data, columns);
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
        
        
        bl = new ButtonListener();
        Play = new JButton("Play");
        Pause = new JButton("Pause");
        Stop = new JButton("Stop");
        Previous = new JButton("Previous");
        Next = new JButton("Next");
        Repeat = new JButton("Repeat");
        Delete = new JButton("Delete");
        
        Play.addActionListener(bl);
        scrollPane = new JScrollPane(table);
        
        bottombtnPnl.add(Play, BorderLayout.CENTER);
        bottombtnPnl.add(Stop, BorderLayout.LINE_START);
        bottombtnPnl.add(Pause, BorderLayout.LINE_END);
        bottombtnPn2.add(Previous, BorderLayout.LINE_START);
        bottombtnPn2.add(Repeat, BorderLayout.LINE_END);
        bottombtnPn2.add(Delete, BorderLayout.CENTER);
        
        bottombtnPn3.add(Previous, BorderLayout.LINE_START);
        bottombtnPn3.add(Repeat, BorderLayout.LINE_END);
        bottombtnPn3.add(Delete, BorderLayout.CENTER);
        
        
        /*
        bottombtnPnl.add(Play);
        bottombtnPnl.add(Pause);
        bottombtnPnl.add(Stop);
        bottombtnPnl.add(Previous);
        bottombtnPnl.add(Next);
        */
        
        btnPnl.add(bottombtnPnl, BorderLayout.CENTER);
        btnPnl.add(bottombtnPn2, BorderLayout.LINE_START);
        btnPnl.add(bottombtnPn3, BorderLayout.LINE_END);
        main.add(textArea);
        main.setJMenuBar(mb);
       
        main.add(scrollPane, BorderLayout.NORTH);
        main.add(textArea, BorderLayout.CENTER);
        main.add(btnPnl, BorderLayout.SOUTH);
        main.setSize(1500,300);
    }
    


	public void go(){
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
    }
     
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String url=null;
            if("Play".equals(e.getActionCommand())){
            	if(CurrentSelectedRow==0)
            	{
            		System.out.println("Rock");
            		 url = "https://mp3.ffh.de/ffhchannels/hqrock.mp3";
            	}
            	if(CurrentSelectedRow==1)
            	{
            		System.out.println("The 80's");
            		 url = "https://mp3.ffh.de/ffhchannels/hq80er.mp3";
            	}
            	if(CurrentSelectedRow==2)
            	{
            		System.out.println("Pur Deutsch");
            		 url = "https://mp3.ffh.de/ffhchannels/hqdeutsch.mp3";
            		
            	}
            	if(CurrentSelectedRow==3)
            	{
            		System.out.println("The 90's");
            		 url = "https://mp3.ffh.de/ffhchannels/hq90er.mp3";
            	}

                  
            }
      

//create if, output and url assignment statements for the other two channels
            
            try {
                player.open(new URL(url));
                player.play();
            } catch (MalformedURLException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Malformed url");
            } catch (BasicPlayerException ex) {
                System.out.println("BasicPlayer exception");
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
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
					Query.checkSong(file.getPath());
				} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
