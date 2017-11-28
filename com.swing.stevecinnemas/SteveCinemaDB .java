import java.sql.*;


public class SteveCinemasDB {


    public static void main(String[] args)throws Exception{

    
       class.forName("com.mysql.jdbc.Driver");
       
       Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb","root","mysql");
       
        Statement st=conn.createStatement();
	
	String 	query="Select title from movies where type='action'";
	ResultSet actionList=st.executeQuery(query);
        
      //  String query="Select title from movies where type='comedy'";
	//ResultSet cinemaList=St.executeQuery(query);
         
        while(actionList.next())
        {
          System.out.println(actionList.getString(0));
        }

       st.close();  con.close();  actionList.close();

}



}
