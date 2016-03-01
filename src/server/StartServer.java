package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName     StartServer
 * @Description   To start a server
 *
 * @author        Rongzhen.shi
 * @version       V1.0
 * @Date          2016.2.27
 */

public class StartServer {
	/** The TCP_PORT is used for TCP port storage. */
	final static int TCP_PORT = 8888;
	
	/** The PACKET_SIZE_MAX is used for the packet's max size storage. */
	final static int PACKET_SIZE_MAX = 10240;
	
	/** The CLIENT_MAX is used for the max number of client accepted storage. */
	final static int CLIENT_ACCEPT_MAX = 10;

	/** The m_socket is used for accepting client. */
	private Socket m_socket;
	
	/** The m_serverSocket is used for creating a server */
	private ServerSocket m_serverSocket;
	
	/** The m_server is used for client communication threads run storage. */
	private RunThread[] m_server;
	
	/** The m_list is used for client communication threads wait storage. */
	private List<RunThread> m_list;
	
	/** The m_mainWindow is used for creating a main window. */
	private MainWindow m_mainWindow;
	
	/**
	 * Create a server and a main window.
	 * 
	 * @exception IOException  if cannot create a server socket.
	 */
	public void createServer() {
		try {
			if (m_serverSocket == null) {
				m_serverSocket = new ServerSocket(TCP_PORT);
				m_mainWindow   = new MainWindow();
				m_mainWindow.showWindow();
			}
		} catch (IOException e) {
			System.exit(1);
		}
	}
	
	/**
	 * Accept clients and run a thread for clients' communication.
	 * 
	 * @exception IOException  if cannot accept a client or create a thread.
	 */
	public void acceptClient() {
		m_server = new RunThread[CLIENT_ACCEPT_MAX];
		m_list   = new ArrayList<RunThread>();
		m_socket = new Socket();

		for (int i = 0; i < CLIENT_ACCEPT_MAX; i++) {
			try {
				m_socket = m_serverSocket.accept();
				if (i == 0) {
					m_mainWindow.changeToConnecting();
				}
				m_server[i] = new RunThread(m_socket, m_socket.getInetAddress().getHostName(), m_socket.getPort(), PACKET_SIZE_MAX, m_mainWindow);
				m_server[i].start();
				m_mainWindow.addNameToJList(new String(m_socket.getInetAddress().getHostName() + ":" + m_socket.getPort()));
				m_list.add(m_server[i]);
			} catch (IOException e) {
				i--;
			}
		}
	}
	
	/**
	 * Waiting for thread over.
	 * 
	 * @exception IOException if cannot join a thread.
	 */
	public void waitThread() {
		try  {
            for (RunThread waitList : m_list) {
            	waitList.join();
            }
        }
        catch (InterruptedException e) {  
        	System.exit(1);
        }
	}
	
	/**
	 * Main function, to start server function.
	 */
	public static void main(String[] args) {
		StartServer startServer = new StartServer();
		
		startServer.createServer();
		startServer.acceptClient();
		startServer.waitThread();

		System.exit(0);
	}
}
