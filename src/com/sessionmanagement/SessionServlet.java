package com.sessionmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import RPC.RPCClient;
import RPC.RPCServer;
import RPC.RpcParameter;

/**
 * Servlet implementation class SessionServlet
 */
@WebServlet("/SessionServlet")
public class SessionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private int sessionNumber = 0;
    private int rebootNumber = 0;
    private RPCClient rpcClient = null;
    private RPCServer rpcServer = null;
    /** start the auto check timer thread
     * @see HttpServlet#HttpServlet()
     */
    public SessionServlet() {
        super();
        // TODO Auto-generated constructor stub
        SessionManager.cleanExpiredSession();
        rpcClient = new RPCClient();
        rpcServer = new RPCServer();
        rpcServer.start();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html;charset=UTF-8");
	      // Allocate a output writer to write the response message into the network socket
	      PrintWriter out = response.getWriter();
	      
	  
	      /*================================================*/
	      Session userSession = null;
	      String cookieInfo = SessionManager.getSessionInfoFromCookie(request);
	      
	      if(cookieInfo == "") {
	    	  System.out.println("generate new session");
	    	  this.sessionNumber += 1;
	    	  Session newSession = SessionManager.generateNewSession(DataBrickManager.getLocalServerID(), 
	    			  this.rebootNumber, this.sessionNumber);
	    	 
				this.rpcClient.writeTo(newSession);
				userSession = newSession;
	      } else {
	    	  System.out.println("cookie info" + cookieInfo);
	    	  String[] tokens = cookieInfo.split("_");
	    	  boolean [] readSuccess = new boolean[1];
	    	  Session retrivedSession = this.rpcClient.read(readSuccess, tokens);
<<<<<<< HEAD
	    	  System.out.println("finish read");
	    	  if(!readSuccess[0]) {
	    	      retrivedSession = SessionManager.generateNewSession(DataBrickManager.getLocalServerID(), this.rebootNumber, this.sessionNumber);
	    	  } else {
	    		  System.out.println("retrived session info " + retrivedSession.generateInfo());
	    		  retrivedSession.incVersion();
	    		  retrivedSession.refreshTimeStamp();
	    	  }
=======
	    	  if(!readSuccess[0]) {
	    		  
	    	  }
	    	  retrivedSession.incVersion();
	    	  retrivedSession.refreshTimeStamp();
>>>>>>> c0135f08a7ec8e306e7dd511e044ba2b9628686e
	    	 
	    	  
	    	  this.rpcClient.writeTo(retrivedSession);
	    	  userSession = retrivedSession; 
	      }
	     
	      
	     
	      String userBehavior = request.getParameter("behavior");
	      System.out.println("user " + userBehavior);
	      /**
	       * if userbehavior is null, it is the get request, use new session
	       * if not update the session
	       */
	      if(userBehavior != null) {
	    	  if(userBehavior.equals("REPLACE")){
	    		  String updatedMessage = request.getParameter("replacedText");
	    		  userSession.resetVersion();
	    		  userSession.ResetMessage(updatedMessage);
	    		  this.rpcClient.writeTo(userSession);
	      		}else if(userBehavior.equals("LOGOUT")){
	      		  try{
	      		  userSession.setMaxInterval(0);
	      		  cleanCookie(request, response);  
	      		  request.getRequestDispatcher("/Logout.html").forward(request, response);
	      		  } catch(NullPointerException e) {
	      			request.getRequestDispatcher("/Error.html").forward(request, response);
	      		  }
	            return;
	      		}
	      }
	      SessionManager.addNewCookie(response, userSession);
	      //SessionManager.storeSession(userSession);
	      request.setAttribute("SessionVersion", new Integer(userSession.getVersion()).toString());
	      System.out.println(userSession.getVersion());
	      /**
	       * set the attribute for JSP file
	       */
	      request.setAttribute("expirationDate", userSession.getExpirationTime().toString());
	      request.setAttribute("message", userSession.getMessage());
	      request.setAttribute("sessionID", userSession.getID());
	      request.setAttribute("lastactiveTime", userSession.getLastActiveTime());
	      request.getRequestDispatcher("/main.jsp").forward(request, response);
	      
	      
	      
	      
	      
	      
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("POST");
		doGet(request, response);
	}
	
	private void cleanCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		final String cookieName = "CS5300PROJ1SESSION";
		Cookie matchingCookie = null;
		for(Cookie cookie : cookies){
		    if(cookieName.equals(cookie.getName())){
		        matchingCookie = cookie;
		    }
		}
		if(matchingCookie != null) {
			matchingCookie.setMaxAge(0);
		}
		response.addCookie(matchingCookie);
	}
	
	
	

}