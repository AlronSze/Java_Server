package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @ClassName:     SendData
 * @Description:   Send the data packet to client
 *
 * @author         Rongzhen.shi
 * @version        V1.0
 * @Date           2016.2.28
 */

public class SendData {
	/** The m_packetSizeMax is used for the sending client data's max size storage. */
	private int m_packetSizeMax;
	
	/** The m_clientwindow is used for get the client window. */
	private ClientWindow m_clientWindow;
	
	/** The m_mainWindow is used for getting the main window. */
	private MainWindow m_mainWindow;
	
	/** The m_socket is used for get client socket. */
	private Socket m_socket;
	
	/** The m_outputStream is used for input stream to client socket. */
	private OutputStream m_outputStream;
	
	/** The m_packSendData is used for create a new PackSendData. */
	private PackSendData m_packSendData;
	
	/**
	 * Create a new SendData, to get the client window and socket.
	 * 
	 * @param clientWindow   get the client window.
	 * @param mainWindow     get the main window.
	 * @param socket         get the client socket.
	 * @param packetSizeMax  get max size of packet.
	 */
	public
	SendData(ClientWindow clientWindow, MainWindow mainWindow, Socket socket, int packetSizeMax) {
		this.m_clientWindow  = clientWindow;
		this.m_mainWindow    = mainWindow;
		this.m_socket        = socket;
		this.m_packetSizeMax = packetSizeMax;
	}
	
	/**
	 * To add listener for send button and wait for user sending.
	 */
	public void startSend() {
		m_clientWindow.getJButtonForSend().addActionListener(new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent e) {
				  sendToClient();
			  }
		});
	}
	
	/**
	 * Send data to client.
	 * 
	 * @exception IOException  if cannot send data to client.
	 */
	private void sendToClient() {
		String data = new String(m_clientWindow.getJTextAreaSendCommand());
		byte[] buf  = new byte[m_packetSizeMax];
		m_packSendData = new PackSendData(data, m_packetSizeMax, m_clientWindow, m_mainWindow);
		
		if ((buf = m_packSendData.packData()) != null) {
			try {
				m_outputStream = m_socket.getOutputStream();
				m_outputStream.write(buf, 0, m_packSendData.getPacketLength());
				m_clientWindow.clearJTextAreaSendWindow();
			} catch (IOException e) {
				// do nothing
			}
		}
		
		m_packSendData = null;
	}
}
