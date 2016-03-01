package server;

import java.net.Socket;

/**
 * @ClassName:     RunThread
 * @Description:   Run a thread for receiving & sending
 *
 * @author         Rongzhen.shi
 * @version        V1.0
 * @Date           2016.2.27
 */

public class RunThread extends Thread{
	/** The m_packetSizeMax is used for the packet's max size storage. */
	private int m_packetSizeMax;
	
	/** The m_port is used for get the TCP client port. */
	private int m_port;
	
	/** The m_socket is used for get the client socket. */
	private Socket m_socket;
	
	/** The m_ip is used for get the TCP client IP. */
	private String m_ip;
	
	/** The m_clientWindow is used for creating a client window. */
	private ClientWindow m_clientWindow;
	
	/** The m_mainWindow is used for getting the main window. */
	private MainWindow m_mainWindow;
	
	/** The m_sendData is used for creating a new SendData. */
	private SendData m_sendData;
	
	/** The m_recvData is used for creating a new RecvData. */
	private RecvData m_recvData;
	
	
	/**
	 * Create a new RunThread, to get client TCP information and get max size of packet.
	 * 
	 * @param socket         get the client window.
	 * @param ip             get the client IP.
	 * @param port           get the client port.
	 * @param packetSizeMax  get max size of packet.
	 * @param mainWindow     get the main window.
	 */
	public
	RunThread(Socket socket, String ip, int port, int packetSizeMax, MainWindow mainWindow) {
		this.m_socket        = socket;
		this.m_ip            = ip;
		this.m_port          = port;
		this.m_packetSizeMax = packetSizeMax;
		this.m_mainWindow    = mainWindow;
	}
	
	/**
	 * Start a thread, to create a client window and start send and receive.
	 */
	public void run() {
		m_clientWindow = new ClientWindow(m_ip, m_port, m_socket, m_packetSizeMax, m_mainWindow);
		m_sendData     = new SendData(m_clientWindow, m_mainWindow, m_socket, m_packetSizeMax);
		m_recvData     = new RecvData(m_clientWindow, m_mainWindow, m_socket, m_packetSizeMax);
		
		m_clientWindow.showWindow();
		
		m_sendData.startSend();
		m_recvData.startRecv();
	}
}
