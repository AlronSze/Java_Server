package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

/**
 * @ClassName     ClientWindow
 * @Description   To create a window to communicate with client
 *
 * @author        Rongzhen.shi
 * @version       V1.1
 * @Date          2016.2.28
 */

public class ClientWindow{
	/** Used for static string storage. */
	final static String RECV_LABEL = "Receive Window";
	
	/** Used for static string storage. */
	final static String SEND_LABEL = "Send Window";
	
	/** Used for static string storage. */
	final static String SEND_COMMAND_LABEL = "Command";
	
	/** Used for static string storage. */
	final static String SEND_TEXT_LABEL = "Text";
	
	/** Used for static string storage. */
	final static String SEND_FILE_LABEL = "File";
	
	/** Used for static string storage. */
	final static String SEND_BUTTON = "Send";
	
	/** Used for static string storage. */
	final static String SELECT_FILE_BUTTON = "Select file";
	
	/** Used for static string storage. */
	final static String CLEAR_BUTTON = "Clear";
	
	/** Used for static string storage. */
	final static String UP_FONT_SIZE = "Up font size";
	
	/** Used for static string storage. */
	final static String DOWN_FONT_SIZE = "Down font size";
	
	/** Used for static string storage. */
	final static String FONT_TYPE = "Times NewRoman";
	
	/** The CLIENT_NAME is used for client name storage. */
	private String m_clientName = "Unknow";
	
	/** The m_default_font_size is used for default font size storage. */
	private int m_default_font_size = 16;
	
	/** The m_fileSizeMax is used for the file's max size storage. */
	private int m_fileSizeMax;
	
	/** The m_port is used for TCP client port storage. */
	private int m_port;
	
	/** The m_fileData is used for file data storage. */
	private byte[] m_fileData;
	
	/** The m_fileDataLength is used for file data's length storage. */
	private int m_fileDataLength;
	
	/** The m_socket is used for get the client socket. */
	private Socket m_socket;
	
	/** JButton for TCP sending and receiving window functions. */
	private JButton m_jButtonForSend, m_jButtonForClear, m_jButtonForSelectFile, m_jButtonForUpFontSize, m_jButtonForDownFontSize;
	
	/** The m_jFrame is used for create a JFrame. */
	private JFrame m_jFrame;
	
	/** JLabel for windows of receiving and sending. */
	private JLabel m_jLabelForRecv, m_jLabelForSend, m_jLabelForSendCommand, m_jLabelForSendText, m_jLabelForSendFile;
	
	/** The m_jPanelForRecv is used for add a scroll for receive window. */
	private JPanel m_jPanelForRecv;
	
	/** JTextArea for creating windows of receiving and sending. */
	private JTextArea m_jTextAreaRecv, m_jTextAreaSendCommand, m_jTextAreaSendText, m_jTextAreaSendFile;
	
	/** The m_port is used for TCP client IP storage. */
	private String m_ip;
	
	/** The m_mainWindow is used for getting the main window. */
	private MainWindow m_mainWindow;
	
	/**
	 * Create a new ClientWindow, to get client information, initialize the window and get max size of file.
	 * 
	 * @param ip           to get the client IP.
	 * @param port         to get the client port.
	 * @param fileSizeMax  get max size of file.
	 * @param mainWindow   get the main window.
	 */
	public
	ClientWindow(String ip, int port, Socket socket, int fileSizeMax, MainWindow mainWindow) {
		this.m_ip          = ip;
		this.m_port        = port;
		this.m_socket      = socket;
		this.m_fileSizeMax = fileSizeMax;
		this.m_mainWindow  = mainWindow;
		initWindow();
	}
	
	/**
	 * To show the client communication window.
	 */
	public void showWindow() {
		m_jFrame.setVisible(true);
	}
	
	/**
	 * To get IP and port string.
	 * 
	 * @return string "[IP]:[port]".
	 */
	public String getIPAndPort() {
		return (new String(m_ip + ":" + m_port));
	}
	
	/**
	 * To destroy the client communication window.
	 */
	public void disposeWindow() {
		m_jFrame.dispose();
	}
	
	/**
	 * To get the send button.
	 * 
	 * @return m_jButtonForSend.
	 */
	public JButton getJButtonForSend() {
		return m_jButtonForSend;
	}
	
	/**
	 * To get the send window's command.
	 * 
	 * @return send windows's command.
	 */
	public String getJTextAreaSendCommand() {
		return m_jTextAreaSendCommand.getText();
	}
	
	/**
	 * To get the send window's text.
	 * 
	 * @return send windows's text.
	 */
	public String getJTextAreaSendText() {
		return m_jTextAreaSendText.getText();
	}
	
	/**
	 * To clear the send window.
	 */
	public void clearJTextAreaSendWindow() {
		m_jTextAreaSendCommand.setText(null);
		m_jTextAreaSendText.setText(null);
		m_jTextAreaSendFile.setText(null);
	}
	
	/**
	 * To append string for the receive window.
	 * 
	 * @param str  the string to append the receive window.
	 */
	public void appendJTextAreaRecvText(String str) {
		m_jTextAreaRecv.append(str);
	}
	
	/**
	 * To get file's data.
	 * 
	 * @param file's data string.
	 */
	public byte[] getFileData() {
		return m_fileData;
	}
	
	/**
	 * To get file data' length.
	 * 
	 * @return file data' length.
	 */
	public int getFileDataLength() {
		return m_fileDataLength;
	}
	
	/**
	 * To set Client communication window's title.
	 * 
	 * @param str  the title string to set.
	 */
	public void setJFrameTitle(String str) {
		m_jFrame.setTitle("[" + str + "] " + m_ip + ":" + Integer.toString(m_port));
	}
	
	/**
	 * To get the client name.
	 * 
	 * @return the client name string.
	 */
	public String getClientName() {
		return m_clientName;
	}
	
	/**
	 * To set the client real name.
	 * 
	 * @param clientName  the client real name string to set.s
	 */
	public void setClientName(String clientName) {
		m_clientName = clientName;
	}
	
	/**
	 * To clear the receive window.
	 */
	private void clearJTextAreaRecvText() {
		m_jTextAreaRecv.setText(null);
	}
	
	/**
	 * To initialize the client communication window.
	 */
	private void initWindow() {
		initRecvWindow();
		initSendWindow();
		initSendButton();
		initClearButton();
		initUpFontSizeButton();
		initDownFontSizeButton();
		initJFrame();
		addJFrame();
	}

	/**
	 * To initialize the receive window.
	 */
	private void initRecvWindow() {
		m_jLabelForRecv = new JLabel(RECV_LABEL);
		m_jLabelForRecv.setBounds(10, 15, 500, 20);
		m_jLabelForRecv.setFont(new Font(FONT_TYPE, Font.BOLD, 14));
		
		m_jTextAreaRecv = new JTextArea();
		m_jTextAreaRecv.setFont(new Font(FONT_TYPE, Font.BOLD, m_default_font_size));
		m_jTextAreaRecv.setEditable(false);
		
		m_jPanelForRecv = new JPanel();
		m_jPanelForRecv.setLayout(new BorderLayout());
		m_jPanelForRecv.setBounds(10, 40, 475, 200);
		m_jPanelForRecv.add(new JScrollPane(m_jTextAreaRecv));
	}
	
	/**
	 * To initialize the send window.
	 */
	private void initSendWindow() {
		m_jLabelForSend = new JLabel(SEND_LABEL);
		m_jLabelForSend.setBounds(10, 270, 500, 20);
		m_jLabelForSend.setFont(new Font(FONT_TYPE, Font.BOLD, 14));
		
		initSendCommandWindow();
		initSendTextWindow();
		initSendFileWindow();
		initSelectFileButton();
	}
	
	/**
	 * To initialize the send command window.
	 */
	private void initSendCommandWindow() {
		m_jLabelForSendCommand = new JLabel(SEND_COMMAND_LABEL);
		m_jLabelForSendCommand.setBounds(40, 305, 80, 20);
		m_jLabelForSendCommand.setFont(new Font(FONT_TYPE, Font.PLAIN, 14));
		
		m_jTextAreaSendCommand = new JTextArea();
		m_jTextAreaSendCommand.setBounds(120, 305, 365, 25);
		m_jTextAreaSendCommand.setFont(new Font(FONT_TYPE, Font.BOLD, 16));
		m_jTextAreaSendCommand.setBorder(new LineBorder(Color.GRAY));
	}
	
	/**
	 * To initialize the send text window.
	 */
	private void initSendTextWindow() {
		m_jLabelForSendText = new JLabel(SEND_TEXT_LABEL);
		m_jLabelForSendText.setBounds(40, 345, 80, 20);
		m_jLabelForSendText.setFont(new Font(FONT_TYPE, Font.PLAIN, 14));
		
		m_jTextAreaSendText = new JTextArea();
		m_jTextAreaSendText.setBounds(120, 345, 365, 25);
		m_jTextAreaSendText.setFont(new Font(FONT_TYPE, Font.BOLD, 16));
		m_jTextAreaSendText.setBorder(new LineBorder(Color.GRAY));
	}
	
	/**
	 * To initialize the send file window.
	 */
	private void initSendFileWindow() {
		m_jLabelForSendFile = new JLabel(SEND_FILE_LABEL);
		m_jLabelForSendFile.setBounds(40, 385, 80, 20);
		m_jLabelForSendFile.setFont(new Font(FONT_TYPE, Font.PLAIN, 14));
		
		m_jTextAreaSendFile = new JTextArea();
		m_jTextAreaSendFile.setBounds(120, 385, 365, 25);
		m_jTextAreaSendFile.setFont(new Font(FONT_TYPE, Font.BOLD, 16));
		m_jTextAreaSendFile.setBorder(new LineBorder(Color.GRAY));
		m_jTextAreaSendFile.setEditable(false);
	}
	
	/**
	 * To initialize the send button.
	 */
	private void initSendButton() {
		m_jButtonForSend = new JButton(SEND_BUTTON);
		m_jButtonForSend.setBounds(405, 425, 80, 20);
	}
	
	/**
	 * To initialize the up font size button.
	 */
	private void initUpFontSizeButton() {
		m_jButtonForUpFontSize = new JButton(UP_FONT_SIZE);
		m_jButtonForUpFontSize.setBounds(150, 255, 120, 20);
		
		m_jButtonForUpFontSize.addActionListener(new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent e) {
				  m_default_font_size++;
				  m_jTextAreaRecv.setFont(new Font(FONT_TYPE, Font.BOLD, m_default_font_size));
			  }
		});
	}
	
	/**
	 * To initialize the down font size button.
	 */
	private void initDownFontSizeButton() {
		m_jButtonForDownFontSize = new JButton(DOWN_FONT_SIZE);
		m_jButtonForDownFontSize.setBounds(278, 255, 120, 20);
		
		m_jButtonForDownFontSize.addActionListener(new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent e) {
				  m_default_font_size--;
				  m_jTextAreaRecv.setFont(new Font(FONT_TYPE, Font.BOLD, m_default_font_size));
			  }
		});
	}
	
	/**
	 * To initialize the select file button and add listener for selecting file.
	 */
	private void initSelectFileButton() {
		m_jButtonForSelectFile = new JButton(SELECT_FILE_BUTTON);
		m_jButtonForSelectFile.setBounds(285, 425, 100, 20);
		
		m_jButtonForSelectFile.addActionListener(new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent e) {
				  selectFile();
			  }
		});
	}
	
	/**
	 * To select file and get data.
	 */
	private void selectFile() {
		FileManager fileManager = new FileManager();
		if (fileManager.selectFile(m_jTextAreaSendFile)) {
			m_fileData = fileManager.getFileData(m_fileSizeMax);
			m_fileDataLength = fileManager.getFileDataLength();
		} else {
			m_fileData = null;
		}
	}
	
	/**
	 * To initialize the clear button and add a listener for clicking.
	 */
	private void initClearButton() {
		m_jButtonForClear = new JButton(CLEAR_BUTTON);
		m_jButtonForClear.setBounds(405, 255, 80, 20);
		
		m_jButtonForClear.addActionListener(new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent e) {
				  clearJTextAreaRecvText();
			  }
		});
	}
	
	/**
	 * To initialize JFrame of the client communication window.
	 */
	private void initJFrame() {
		m_jFrame = new JFrame();
		if (m_mainWindow != null) {
			m_jFrame.setTitle("[" + m_clientName + "] " + m_ip + ":" + Integer.toString(m_port));
		}
		else {
			m_jFrame.setTitle("[Connecting with server] " + m_ip + ":" + Integer.toString(m_port));
		}
		m_jFrame.setSize(500,500);
		m_jFrame.setLayout(null);
		m_jFrame.setResizable(false);
		m_jFrame.setLocationRelativeTo(null);
		
		m_jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing (WindowEvent we) {
				try {
					m_socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (m_mainWindow != null) {
						if (m_clientName.equals("Unknown")) {
							m_mainWindow.removeNamefromJList(m_clientName);
						}
						else {
							m_mainWindow.removeNamefromJList(getIPAndPort());
						}
					}
				}
			}
		});
	}

	/**
	 * To add widget for JFrame.
	 */
	private void addJFrame() {
		m_jFrame.add(m_jLabelForRecv);
		m_jFrame.add(m_jPanelForRecv);
		m_jFrame.add(m_jLabelForSend);
		m_jFrame.add(m_jLabelForSendCommand);
		m_jFrame.add(m_jTextAreaSendCommand);
		m_jFrame.add(m_jLabelForSendText);
		m_jFrame.add(m_jTextAreaSendText);
		m_jFrame.add(m_jLabelForSendFile);
		m_jFrame.add(m_jTextAreaSendFile);
		m_jFrame.add(m_jButtonForSend);
		m_jFrame.add(m_jButtonForSelectFile);
		m_jFrame.add(m_jButtonForClear);
		m_jFrame.add(m_jButtonForUpFontSize);
		m_jFrame.add(m_jButtonForDownFontSize);
	}
}
