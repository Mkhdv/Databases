import java.io.*;
import java.sql.*;
import java.util.Scanner;

class Student {
	// the host name of the server and the server instance name/id
	public static final String oracleServer = "dbs3.cs.umb.edu";
	public static final String oracleServerSid = "dbs3";
	public static void main(String args[]) {
		Connection conn = null;
		conn = getConnection();
		if (conn==null)
			System.exit(1);

		//now execute query
		Scanner input = new Scanner(System.in);
		try {
		  // Create statement object
		  Statement stmt = conn.createStatement();
		  int sid;
		  while(true){

		  	// Firs entry point
		    System.out.print("Student ID = ");
		    sid=input.nextInt();

		    // option for new students
			if(sid == -1) {
		    System.out.print("Please enter new student's ID = ");
		    sid=input.nextInt();
		    ResultSet cond = stmt.executeQuery("select sid from Students where sid ="+ sid);
		    if (cond.next() == true) {
		    System.out.println("Student ID is taken, please enter '-1' to register another student ID"); 
			continue;}
		    System.out.print("Please enter new student's name: ");
	    	String sname=input.next();

			ResultSet ins = stmt.executeQuery("insert into Students values ("+ sid +", '"+ sname +"')");
		    System.out.println("Created new sudent with ID: " + sid + " and name: " + sname);
			} 
			ResultSet cond = stmt.executeQuery("select sid from Students where sid ="+ sid);
	    	if (cond.next() == false) {
		    System.out.println("Student ID doesnt't exist, please enter '-1' to register new student"); 
			continue;}
			
			break;}


			// MAIN MENU
		    System.out.println("Welcome to the main menu");

		    while(true) {

   			System.out.println();
			System.out.println("Please select menu option:");
		    String req=input.next();

		    if (req.charAt(0)=='L') {
			ResultSet rs = stmt.executeQuery("select * from Courses");
		    if (rs.next()){
		    	System.out.print("Available courses: \n");
		    	do{
		        	System.out.println(rs.getString(1) + " " + rs.getString(2)+ " " + rs.getString(3));
		      	}while(rs.next());
	 	    	}
	 	    	continue; }


	 	    // MENU CHOICE E
	 	    if (req.charAt(0)=='E') {
		    System.out.print("Please enter course id: ");
	    	int cid=input.nextInt();

	    	ResultSet res = stmt.executeQuery("select cid from Courses where cid ="+ cid);
	    	if (res.next() == false) {
		    System.out.println("Course doesnt't exist, please use 'S' to search for available courses"); 
			continue;
			} else {
	    		ResultSet cond = stmt.executeQuery("select cid from Enrolled where sid ="+ sid +" and cid ="+ cid);
	    		if (cond.next() == false) {
	    		//	cond.getString("cid"));
				ResultSet rs = stmt.executeQuery("insert into Enrolled values ("+ sid +", "+ cid +")");
		    	System.out.println("Student with ID: " + sid + " enrolled into the course with ID: " + cid); }
		    	else {
		    	System.out.println("Student cannot be enrolled twice in the same course," +
		    		"please select other course to enroll");
		    	continue;
		    		}
				}
	 	    }

	 	    // MENU CHOINCE W
	 	    else if (req.charAt(0)=='W') {
		    System.out.print("Please enter course id: ");
	    	int cid=input.nextInt();
			ResultSet rs = stmt.executeQuery("delete from Enrolled where sid ="+ sid +" and cid ="+ cid);
		    System.out.println("Student with ID: " + sid + " was wihdrawn from the course with ID: " + cid);
	 	    	}

	 	    //MENU CHOICE S
			else if (req.charAt(0)=='S') {
	 	    System.out.print("Please enter course name: ");
	    	String cname=input.next();
	    	ResultSet cond = stmt.executeQuery("select * from Courses");
	    			    if (cond.next()){
		    	do{
		        	if (cond.getString("cname").contains(cname)){
		        	System.out.println(cond.getString("cname"));
		        }
		      	}while(cond.next());
	    	continue;
	 	    	} }

	 	    // MENU CHOICE M
	 	    else if (req.charAt(0)=='M') {
			ResultSet rs = stmt.executeQuery("select * from Courses C, Enrolled E where E.sid ="+ sid +" and C.cid = E.cid");
			System.out.println("Student with ID: " + sid + " is enrolled in the following courses: \n");

				    if (rs.next()) {
		    	do{
		    		System.out.println("cid: " + rs.getString("cid") + ", course name: " + rs.getString("cname") + 
		    			", credits: " + rs.getString("credits"));
		    	  } while(rs.next());
	 	    	} 
	 	    continue; 
	 		}

	 	    // MENU CHOICE X
	 	    else if (req.charAt(0)=='X') {
			System.out.println("Application is Closed \n");
			break;
	 	    	}



		    else
			System.out.println("No Records Retrieved");
		  } // here
		} catch (SQLException e) {
			System.out.println ("ERROR OCCURRED");	
			e.printStackTrace();
		}
	}

	public static Connection getConnection(){

		// first we need to load the driver
		String jdbcDriver = "oracle.jdbc.OracleDriver";
		try {
			Class.forName(jdbcDriver); 
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get username and password
		Scanner input = new Scanner(System.in);
		System.out.print("Username:");
		String username = input.nextLine();
		System.out.print("Password:");
		//the following is used to mask the password
		Console console = System.console();
		String password = new String(console.readPassword()); 
		String connString = "jdbc:oracle:thin:@" + oracleServer + ":1521:"
				+ oracleServerSid;

		System.out.println("Connecting to the database...");
	
		Connection conn;
		// Connect to the database
		try{
			conn = DriverManager.getConnection(connString, username, password);
			System.out.println("Connection Successful");
		}
		catch(SQLException e){
			System.out.println("Connection ERROR");
			e.printStackTrace();	
			return null;
		}

		return conn;
	}
}