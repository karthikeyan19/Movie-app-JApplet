import java.sql.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ThreadLocalRandom;
 class SteveCinemasDB {
    
    static Statement st;
     private static int result;
     public static final String actionQuery="select title,star,price,type from movies where type='action'"; 
     public static final String comedyQuery="Select title,star,price,type from movies where type='comedy'";
    public static void main(String[] args)throws Exception{

       Class.forName("com.mysql.jdbc.Driver"); 
       
       Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb","root","");
       st=con.createStatement();  	 
      
	
       List<Movie> actionList=getMovieList(actionQuery);
      
        
       List<Movie> comedyList=getMovieList(comedyQuery);
       
       NowShowingFrame nowShowing=NowShowingFrame.getInstance(actionList,comedyList);
       nowShowing.setVisible(true); 
      
    }


     public static List<Movie> getMovieList(String query) throws Exception{
        ResultSet movieSet=st.executeQuery(query);
        List<Movie> movieList=new LinkedList();
        while(movieSet.next())
        {
         Movie movie=new Movie();
         movie.title=movieSet.getString("title");
         movie.star=movieSet.getString("star");
         movie.price=movieSet.getFloat("price");
         movie.type=movieSet.getString("type");
         movieList.add(movie);
        }
        return movieList;
    }
    public static boolean updateMovie(String query) throws Exception{
       
       result=st.executeUpdate(query);
       return (result>-1)?true:false;
      
    }  
    public static boolean deleteMovie(String query)throws Exception{
    
       result=st.executeUpdate(query);
       return (result>-1)?true:false;
    } 
    public static boolean insertMovie(String query)throws Exception{
    
       result=st.executeUpdate(query);
       return (result>-1)?true:false;
    } 

}

class Movie{
     public String title;
     public String star;
     public double price; 
     public String type;   
}
class NowShowingFrame extends JFrame
{
      public  JPanel content;
      private List<Movie> actionList,comedyList;
      private static NowShowingFrame nowShowingFrame;
      private JButton changeJButton;
      private GridBagConstraints gc=new GridBagConstraints();
      NowShowingFrame(List<Movie> actionList,List<Movie> comedyList){
        this.actionList=actionList;
        this.comedyList=comedyList; 
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        setSize(dim.width, dim.height);
        
        changeJButton=new JButton("Change Movies");
        changeJButton.addActionListener(new ActionListener(){
        
          public void actionPerformed(ActionEvent ae)
          {
            try{
            new LoginFrame();
            }catch(Exception e){
               System.out.println(e.getMessage());
            }
          }
        }); 
        //changeJPanel.add(changeJButton); 
        addContent();
           
      }
     public static NowShowingFrame getInstance(List<Movie> actionList,List<Movie> comedyList){
     nowShowingFrame=(nowShowingFrame==null)?new NowShowingFrame(actionList,comedyList):nowShowingFrame;
     return nowShowingFrame;
     }
     public static NowShowingFrame getInstance(){
     return nowShowingFrame;
     }
     public  void removeAllContent(){
        content.removeAll();
        try{
        actionList=SteveCinemasDB.getMovieList(SteveCinemasDB.actionQuery);
        comedyList=SteveCinemasDB.getMovieList(SteveCinemasDB.comedyQuery);
       }catch(Exception e){} 
      }
     public  void addContent(){
        content=new JPanel();
        
        content.setBackground(new Color(237,116,169));
         
        content.setBorder(BorderFactory.createLineBorder(Color.yellow,10));
        content.setLayout(new GridBagLayout());
       
        gc.gridx=2;
        gc.gridy=0;
        content.add(changeJButton,gc);
        gc.gridx=1;
        gc.gridy=1;
        gc.insets=new Insets(10,10,10,10);
        content.add(setTitle(new JLabel("SteveCinemas"),70),gc);
        gc.gridx=1;
        gc.gridy=2;
        content.add(setTitle(new JLabel("Now Showing"),50),gc);
        gc.gridy+=1;
        gc.gridx=0;
        content.add(setTitle(new JLabel("Action movies"),35),gc);
        addMovieList(actionList,content);        
        gc.gridy=3;
        gc.gridx=2;
        content.add(setTitle(new JLabel("Comedy movies"),35),gc);
        addMovieList(comedyList,content); 
        add(content);
     }
     public JLabel setTitle(JLabel titleJLabel,int fontSize){
        titleJLabel.setFont(new Font("Serif", Font.BOLD, fontSize));
        titleJLabel.setForeground(Color.YELLOW);
        return titleJLabel;
      }
     public void addMovieList(List<Movie> movieList,JPanel content){
      for(final Movie movie:movieList)
        {
          JLabel movieJLabel=new JLabel(movie.title);
          movieJLabel.setForeground(Color.white);
          gc.gridy+=1;
          movieJLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
              JOptionPane.showMessageDialog(NowShowingFrame.this, "Title:"+movie.title+"\nStar:"+movie.star+"\nPrice:"+movie.price);  
              }
            });
          
          content.add(movieJLabel,gc);
        }   
     }

} 

class LoginFrame extends JFrame
{


   LoginFrame()throws Exception{
     
     JPasswordField passwordField = new JPasswordField();
     int okCxl = JOptionPane.showConfirmDialog(null, passwordField, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

     if (okCxl == JOptionPane.OK_OPTION) {
       String password = new String(passwordField.getPassword());
        System.out.println(password);
      // if(password.equals("admin")){
         ChangeMovies changeMovies=new ChangeMovies();
         changeMovies.setVisible(true); 
         //dispose();        
       //}
     }   
      
   }


} 
class ChangeMovies extends JFrame{

    private GridBagConstraints gc=new GridBagConstraints();
    private JTextField titleJTextField,starJTextField,priceJTextField,typeJTextField;
    private JButton addMovieJButton,removeMovieJButton,updateMovieJButton;
    ChangeMovies()throws Exception
    {
     setSize(500,500);
     setLayout(new GridBagLayout());
     gc.gridx=0;
     gc.gridy=0;
     gc.insets=new Insets(10,10,10,10);
     add(new JLabel("Select Movie"),gc); 
     final JComboBox moviesJComboBox=new JComboBox();
     String query="select title,star,price,type from movies"; 
     final List<Movie> movies=SteveCinemasDB.getMovieList(query);
     final Map<String,Movie> moviesMap=new HashMap();
     for(Movie movie:movies){
        moviesJComboBox.addItem(movie.title);
        moviesMap.put(movie.title,movie);
     } 
     Movie firstMovie=movies.get(0);
     gc.gridx=1;
     gc.gridy=0;
     add(moviesJComboBox,gc);
     gc.gridx=0;
     gc.gridy=1; 
     add(new JLabel("Title"),gc);
     titleJTextField=new JTextField(30);
     titleJTextField.setText(firstMovie.title);
     gc.gridx=1;
     gc.gridy=1;
     add(titleJTextField,gc);
     gc.gridx=0;
     gc.gridy=2; 
     add(new JLabel("Star"),gc);
     starJTextField=new JTextField(30);
     starJTextField.setText(firstMovie.star);
     gc.gridx=1;
     gc.gridy=2;
     add(starJTextField,gc);
     gc.gridx=0;
     gc.gridy=3; 
     add(new JLabel("Type"),gc);
     typeJTextField=new JTextField(30);
     typeJTextField.setText(firstMovie.type);
     gc.gridx=1;
     gc.gridy=3;
     add(typeJTextField,gc);
     gc.gridx=0;
     gc.gridy=4; 
     add(new JLabel("Price"),gc);
     priceJTextField=new JTextField(30);
     priceJTextField.setText(firstMovie.price+"");
     gc.gridx=1;
     gc.gridy=4;
     add(priceJTextField,gc);
     JPanel buttonsContainer=new JPanel();
      
     JButton addMovieJButton=new JButton("Add Movie");  
     JButton removeMovieJButton=new JButton("Remove Movie");
     JButton updateMovieJButton=new JButton("Update Movie"); 
     addMovieJButton.setActionCommand("add");
     removeMovieJButton.setActionCommand("delete");
     updateMovieJButton.setActionCommand("update");
     buttonsContainer.setLayout(new FlowLayout(FlowLayout.LEADING));
     buttonsContainer.add(addMovieJButton);
     buttonsContainer.add(removeMovieJButton);
     buttonsContainer.add(updateMovieJButton);
     gc.gridx=0;
     gc.gridy=5;
     gc.gridwidth=3;
     add(buttonsContainer,gc);
     ActionListener actionListener=new ActionListener(){
         public void actionPerformed(ActionEvent ae){
           Movie movie=new Movie(); 
           String query;
           int okCxl; 
           int index=moviesJComboBox.getSelectedIndex();
           switch(ae.getActionCommand()){

              case "add":
                     
                     JPanel insertPanel=new JPanel();
                     insertPanel.setLayout(new GridLayout(4,2));
                     final JLabel titleJLabel=new JLabel("Title");
                     final JLabel starJLabel=new JLabel("Star");
                     final JLabel priceJLabel=new JLabel("Price");
                     final JLabel typeJLabel=new JLabel("Type");
                     final JTextField insertTitle= new JTextField();
                     final JTextField insertStar=new JTextField();
                     final JTextField insertPrice=new JTextField();
                     final JTextField insertType=new JTextField();
                      insertTitle.setColumns(15); insertStar.setColumns(15); insertPrice.setColumns(15); insertType.setColumns(15);
                     insertPanel.add(titleJLabel);
                     insertPanel.add(insertTitle); 
                     insertPanel.add(starJLabel);
                     insertPanel.add(insertStar); 
                     insertPanel.add(priceJLabel);
                     insertPanel.add(insertPrice);
                     insertPanel.add(typeJLabel);  
                     insertPanel.add(insertType); 
                     okCxl = JOptionPane.showConfirmDialog(null, insertPanel, "Add Movie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                     if (okCxl == JOptionPane.OK_OPTION) {
                         if(!(insertTitle.getText().equals("")&&insertStar.getText().equals("")&&insertPrice.getText().equals("")&&insertType.getText().equals(""))){
                         movie.title= insertTitle.getText();
                         movie.star=insertStar.getText();
                         try{
                         movie.price=Double.parseDouble( insertPrice.getText());
                         }catch(NumberFormatException e){
                            JOptionPane.showMessageDialog(ChangeMovies.this, "Enter the correct price Amount");
                         }
                         movie.type=insertType.getText();
                         StringBuilder insertQueryBuilder=new StringBuilder("insert  into movies (movie_id,title,star,price,type) values(");
                         insertQueryBuilder.append(ThreadLocalRandom.current().nextInt(0, 1000 + 1)+",")
                                           .append("'"+movie.title+"',")
                                           .append("'"+movie.star+"',")
                                           .append(movie.price+",")  
                                           .append("'"+movie.type+"');");
                         try{         
                         if(SteveCinemasDB.insertMovie(insertQueryBuilder.toString())){
                            JOptionPane.showMessageDialog(ChangeMovies.this, "The movie"+movie.title+" Sucessfully Added");
                            moviesJComboBox.insertItemAt(movie.title,index);
                            movies.add(index,movie);
                            refersh(); 
                         }}catch(Exception e){
                           JOptionPane.showMessageDialog(ChangeMovies.this, "Error in adding movie"); 
                         }
                       }else{ 
                           JOptionPane.showMessageDialog(ChangeMovies.this, "Please fill all fields");  
                       }
                     }
                     break;
              case "delete":
                    
                    okCxl = JOptionPane.showConfirmDialog(null, new JLabel("Are you sure in removing "+moviesJComboBox.getSelectedItem()+" movie?"), "Remove Movie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                     
                     if (okCxl == JOptionPane.OK_OPTION) {
                         query="delete from movies where title='"+moviesJComboBox.getSelectedItem()+"'";
                         try{  
                             if(SteveCinemasDB.deleteMovie(query)){
                              JOptionPane.showMessageDialog(ChangeMovies.this, "The movie"+moviesJComboBox.getSelectedItem()+" removed");
                               moviesJComboBox.removeItemAt(index);
                               movies.remove(index);
                               refersh(); 
                             }
                         }catch(Exception e){}
                     }
                    break; 
              case "update":
                     boolean isUpdated=false;
                     JPanel selectJPanel=new JPanel();
                     selectJPanel.setLayout(new GridLayout(4,2));
                     final JCheckBox titleJCheckBox=new JCheckBox("Title");
                     final JCheckBox starJCheckBox=new JCheckBox("Star");
                     final JCheckBox priceJCheckBox=new JCheckBox("Price");
                     final JCheckBox typeJCheckBox=new JCheckBox("Type");
                     final JTextField changeTitle= new JTextField(titleJTextField.getText());
                     final JTextField changeStar=new JTextField(starJTextField.getText());
                     final JTextField changePrice=new JTextField(priceJTextField.getText());
                     final JTextField changeType=new JTextField(typeJTextField.getText());
                     changeTitle.setColumns(15); changeStar.setColumns(15); changePrice.setColumns(15); changeType.setColumns(15);
                     changeTitle.setEnabled(false); changeStar.setEnabled(false); changePrice.setEnabled(false); 
                     changeType.setEnabled(false);
                     selectJPanel.add(titleJCheckBox);
                     selectJPanel.add(changeTitle); 
                     selectJPanel.add(starJCheckBox);
                     selectJPanel.add(changeStar); 
                     selectJPanel.add(priceJCheckBox);
                     selectJPanel.add(changePrice);
                     selectJPanel.add(typeJCheckBox);  
                     selectJPanel.add(changeType); 
                     ItemListener checkBoxItemListener =new ItemListener(){
                       
                      public void itemStateChanged(ItemEvent ie){
                       String field=((JCheckBox)ie.getItem()).getText();
                       switch(field){
                                case "Title":
                                      if(ie.getStateChange()==ItemEvent.SELECTED)
                                      changeTitle.setEnabled(true);
                                      else
                                      changeTitle.setEnabled(false);
                                      break;
                                case "Star":
                                      if(ie.getStateChange()==ItemEvent.SELECTED)
                                      changeStar.setEnabled(true);
                                      else
                                      changeStar.setEnabled(false);
                                      break;
                                case "Price":
                                      if(ie.getStateChange()==ItemEvent.SELECTED)
                                      changePrice.setEnabled(true);
                                      else
                                      changePrice.setEnabled(false);
                                      break;     
                                case "Type":
                                      if(ie.getStateChange()==ItemEvent.SELECTED)
                                      changeType.setEnabled(true);
                                      else
                                      changeType.setEnabled(false);
                                      break; 
                          }
                        }
                      }; 
                      titleJCheckBox.addItemListener(checkBoxItemListener); 
                      starJCheckBox.addItemListener(checkBoxItemListener);  
                      priceJCheckBox.addItemListener(checkBoxItemListener);
                      typeJCheckBox.addItemListener(checkBoxItemListener); 
                     okCxl = JOptionPane.showConfirmDialog(null, selectJPanel, "Select and Update Movie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                Movie sMovie=movies.get(index);
             if (okCxl == JOptionPane.OK_OPTION) {
               
               StringBuilder updateQuery=new StringBuilder("update movies set ");
               
               int count=0;
               if(changeTitle.isEnabled()){
                     sMovie.title=changeTitle.getText();
                     updateQuery.append("title='"+sMovie.title+"', ");
                     
                  
               }
               if(changeStar.isEnabled()){
                  sMovie.star=changeStar.getText();
                  updateQuery.append("star='"+sMovie.star+"', ");
                  
                 
               }
               if(changePrice.isEnabled()){
                 sMovie.price=Double.parseDouble(changePrice.getText());
                 updateQuery.append("price="+changePrice.getText()+", ");
                 
               }
               if(changeType.isEnabled()){
                  sMovie.type=changeType.getText();
                  updateQuery.append("type='"+sMovie.type+"', ");
                  
               }
               if(updateQuery.toString().contains(",")){
               updateQuery.deleteCharAt(updateQuery.toString().length()-2); 
              }
               updateQuery.append("where title='"+moviesJComboBox.getSelectedItem()+"';");
               System.out.println(updateQuery.toString());
               try{
               isUpdated=SteveCinemasDB.updateMovie(updateQuery.toString());
               }catch(Exception e){}
                 
          }
          if(isUpdated)
           { 
              JOptionPane.showMessageDialog(ChangeMovies.this, "The movie"+moviesJComboBox.getSelectedItem()+" Sucessfully updated");
              moviesJComboBox.removeItemAt(index);
              moviesJComboBox.insertItemAt(sMovie.title,index);
              movies.set(index,sMovie); 
              refersh();
            
               
           }
          else{ JOptionPane.showMessageDialog(ChangeMovies.this, "The movie"+moviesJComboBox.getSelectedItem()+" not updated"); }
          break;
       }
     }};
     addMovieJButton.addActionListener(actionListener);
     removeMovieJButton.addActionListener(actionListener);
     updateMovieJButton.addActionListener(actionListener);
     moviesJComboBox.addItemListener(new ItemListener(){

          public void itemStateChanged(ItemEvent ie){
            String keyTitle=(String)ie.getItem();
            Movie movie=moviesMap.get(keyTitle);
            titleJTextField.setText(movie.title);
            starJTextField.setText(movie.star);
            priceJTextField.setText(movie.price+"");
            typeJTextField.setText(movie.type);
          }
      });
    }
    public void refersh(){
              NowShowingFrame.getInstance().removeAllContent();
              NowShowingFrame.getInstance().addContent(); 
              NowShowingFrame.getInstance().content.revalidate();
              NowShowingFrame.getInstance().content.repaint();
    }


}




