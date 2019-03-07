/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

//import StreamPlayerNew.ButtonListener;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

/**
 *
 * @author Daddy
 */
public class App {
    
    
    JFrame  main = new JFrame("StreamPlayer by Dr. Hoffman");
    JTable table;
    JPanel bottombtnPnl;
    ButtonListener bl;
    JScrollPane scrollPane; 
    int CurrentSelectedRow;
    JButton Play;
    JPanel btnPnl;
    BasicPlayer player;
    public App(){
        //colums labels the columns in the table
        //if the table is not in a scrollpane the column header will not show
        //it would have to be added separately
    	bottombtnPnl = new JPanel();
    	bottombtnPnl.setLayout(new BorderLayout(0, 0));
    	btnPnl = new JPanel(new BorderLayout());
    	  String[] columns = {"Station URL", "Description"};
    	  player = new BasicPlayer();
          //data holds the table data and maps as a 2d array into the table
          Object[][] data = {{"https://mp3.ffh.de/ffhchannels/hqrock.mp3","Rock"},
                             {"https://mp3.ffh.de/ffhchannels/hq80er.mp3","The 80's"},
                             {"https://mp3.ffh.de/ffhchannels/hqdetusch.mp3","Pur Deutsch"},
                             {"https://mp3.ffh.de/ffhchannels/hq90er.mp3","The 90's"}};
          table = new JTable(data, columns);

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
        column.setPreferredWidth(250);
        //using the same TableColumn variable set the "Year" coulumn to 20
        column = table.getColumnModel().getColumn(1); //Year is column 3
        column.setPreferredWidth(50);
        bl = new ButtonListener();
        Play = new JButton("Play");
        Play.addActionListener(bl);
        scrollPane = new JScrollPane(table);
        
        bottombtnPnl.add(Play);
        btnPnl.add(bottombtnPnl, BorderLayout.CENTER);
        
        
        main.setSize(400,150);
        main.add(scrollPane, BorderLayout.CENTER);
        main.add(btnPnl, BorderLayout.SOUTH);

       
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
    
}
