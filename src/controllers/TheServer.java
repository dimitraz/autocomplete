package controllers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.TheServerThreadDispatcher;

import java.net.Socket;
import java.net.ServerSocket;

/**
 * @author Luigi Zuccarelli
 */
class TheServer {
	
	private ExecutorService executor = null;
	private boolean bStop = false;
    private static int port = 9000;
			
	public static void main(String[] args) {
		TheServer srv = new TheServer(port, 5);
	}

    public TheServer(int port, int threads) {
	    try {
            executor = Executors.newFixedThreadPool(threads);
			ServerSocket listener = new ServerSocket(port);

            Runtime.getRuntime().addShutdownHook(new CleanUp());
		
			System.out.println("Initialising application server ...");
			System.out.println("Listening on port : " + port);
			System.out.println("Threads : " + threads);
			System.out.println(" ");
			System.out.println("Press CTRL-C to gracefully shutdown the server ");
			System.out.println(" ");
			
			while(!bStop) {
                Socket socket = listener.accept();
                TheServerThreadDispatcher td = new TheServerThreadDispatcher(socket);
                executor.execute(td);
            }
		}
		catch (Exception ioe) {
			ioe.printStackTrace();
            System.exit(-1);
		}
		finally {
			System.exit(0);
		}
	}

    public class CleanUp extends Thread {
        public void run() {
        	System.out.println("Server shutting down ...");
        }
    }
	
}
