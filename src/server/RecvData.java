package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @ClassName:     RecvData
 * @Description:   Receive the data packet from client
 *
 * @author         Rongzhen.shi
 * @version        V1.0
 * @Date           2016.2.28
 */

public class RecvData {
	/** The RECV_DATA_SIZE_MAX is used for the receiving client data's max size storage. */
	final static short RECV_DATA_SIZE_MAX = 4 * 2;
	
	/** The m_clientwindow is used for get the client window. */
	private ClientWindow m_clientWindow;
	
	/** The m_inputStream is used for input stream from client socket. */
	private InputStream m_inputStream;
	
	/** The m_socket is used for get client socket. */
	private Socket m_socket;
	
	/** The m_unpackRecvData is used for create a new UnpackRecvData. */
	private UnpackRecvData m_unpackRecvData;
	
	/** The m_recvBuf is used for the receiving client data storage. */
	private byte[] m_recvBuf;
	
	/** The m_readLength is used for the receiving client data's length storage. */
	private int m_readLength;
	
	/** The m_packetSizeMax is used for the sending client data's max size storage. */
	private int m_packetSizeMax;
	
	/** The m_mainWindow is used for getting the main window. */
	private MainWindow m_mainWindow;
	
	/**
	 * Create a new RecvData, to get the client window, main window, packet max size and socket.
	 * 
	 * @param clientWindow   get the client window.
	 * @param mainWindow     get the main window.
	 * @param socket         get the client socket.
	 * @param packetSizeMax  get max size of packet.
	 */
	public
	RecvData(ClientWindow clientWindow, MainWindow mainWindow, Socket socket, int packetSizeMax){
		this.m_clientWindow  = clientWindow;
		this.m_mainWindow    = mainWindow;
		this.m_socket        = socket;
		this.m_packetSizeMax = packetSizeMax;
	}
	
	/**
	 * To start receiving client's data.
	 * 
	 * @exception IOException             if cannot get input stream.
	 * @exception SocketTimeoutException  if socket receiving timeout(set 100ms)
	 */
	public void startRecv() {
		try {
			m_unpackRecvData = new UnpackRecvData(m_clientWindow, m_mainWindow, m_packetSizeMax);
			m_recvBuf = new byte[RECV_DATA_SIZE_MAX];
			m_socket.setSoTimeout(100);
			m_inputStream = m_socket.getInputStream();
			while (true) {
				try {
					m_readLength = m_inputStream.read(m_recvBuf, 0, RECV_DATA_SIZE_MAX);
				} catch (SocketTimeoutException e) {
					m_readLength = 0;
				} finally {
					m_unpackRecvData.unpackData(m_recvBuf, m_readLength);
				}
			}
		} catch (IOException e) {
			m_clientWindow.disposeWindow();
			if (m_mainWindow != null) {
				if (!m_clientWindow.getClientName().equals("Unknown")) {
					m_mainWindow.removeNamefromJList(m_clientWindow.getClientName());
				} else {
					m_mainWindow.removeNamefromJList(m_clientWindow.getIPAndPort());
				}
			}
		}
	}
}
