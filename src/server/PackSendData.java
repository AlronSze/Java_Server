package server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import javax.swing.JOptionPane;

/**
 * @ClassName     PackSendData
 * @Description   Pack the sending data packet
 *
 * @author        Rongzhen.shi
 * @version       V1.2
 * @Date          2016.2.28
 */

public class PackSendData {
	/** Used for static string storage. */
	final static String ERROR = "Error";
	
	/** The USAGE is used for error usage information storage. */
	final static String USAGE = "Input Error!\n" + "Usage: command [arg0] [arg1]\n" + "Each part must less than or equal to four bytes.";
	
	/** The INVALID_COMMAND is used for error invalid command information storage. */
	final static String INVALID_COMMAND = "Input Error!\n" + "Invalid command, please try again.";
	
	/** The EMPTY_DATA is used for error empty data information storage. */
	final static String EMPTY_DATA = "Input Error!\n" + "Data is empty, please input some text.";
	
	/** The FILE_ERROR is used for error file information storage. */
	final static String FILE_ERROR = "Input Error!\n" + "File is empty or too big, please try again.";

	/** The REGULAR is used for parameters match rule storage. */
	final static String REGULAR = "^[ ]*([a-zA-Z0-9]{1,4})([ ]+[a-zA-Z0-9-]{1,4}){0,1}([ ]+[a-zA-Z0-9-]{1,4}){0,1}[ ]*$";
	
	/** The m_fileSizeMax is used for file's max size storage. */
	private int m_fileSizeMax;
	
	/** The m_dataPacked is used for packed data storage. */
	private byte[] m_dataPacked;
	
	/** The m_sendText is used for user sending text storage. */
	private String m_sendText;
	
	/** The m_matcher is used for regular expression match information storage. */
	private Matcher m_matcher;
	
	/** The m_packetHeader is used for header of packet storage. */
	private PacketHeader m_packetHeader;
	
	/** The m_packetData is used for data of packet storage. */
	private PacketData m_packetData;
	
	/** The m_explainCommand is used for creating a new ExplainCommand. */
	private ExplainCommand m_explainCommand;
	
	/** The m_clientWindow is used for getting the client communication window. */
	private ClientWindow m_clientWindow;
	
	/** The m_mainWindow is used for getting the main window. */
	private MainWindow m_mainWindow;
	
	/**
	 * Create a new PackSendData, to get the user sending text, get max 
	 * size of file, create a new packet, get client window and main window.
	 * 
	 * @param sendText       the user sending text.
	 * @param packetSizeMax  get max size of packet.
	 * @param clientWindow   get the client window.
	 * @param mainWindow     get the main window.
	 */
	public
	PackSendData(String sendText, int packetSizeMax, ClientWindow clientWindow, MainWindow mainWindow) {
		this.m_sendText     = sendText;
		this.m_fileSizeMax  = packetSizeMax - 24;
		m_packetHeader      = new PacketHeader();
		m_packetData        = new PacketData(packetSizeMax);
		this.m_clientWindow = clientWindow;
		this.m_mainWindow   = mainWindow;
	}	
	
	/**
	 * To pack data packet.
	 * 
	 * @return if the user sending text is valid return m_dataPacked, else return null.
	 */
	public byte[] packData() {
		if (checkSendText()) {
			insertHeader();
			m_explainCommand = new ExplainCommand(m_packetHeader,
					m_packetData,
					m_clientWindow,
					m_mainWindow);
			if (m_explainCommand.checkCommand(false)) {
				if (m_explainCommand.checkHasText()) {
					if (!insertText()) {
						JOptionPane.showMessageDialog(null, EMPTY_DATA, ERROR, JOptionPane.ERROR_MESSAGE);
						return null;
					}
				} else if (m_explainCommand.checkHasFile()) {
					if (!insertFile()) {
						JOptionPane.showMessageDialog(null, FILE_ERROR, ERROR, JOptionPane.ERROR_MESSAGE);
						return null;
					}
				}
				getCrc32();
				jointByte();
				return m_dataPacked;
			}
			else {
				JOptionPane.showMessageDialog(null, INVALID_COMMAND, ERROR, JOptionPane.ERROR_MESSAGE);
				return null;
			}
		} else {
			JOptionPane.showMessageDialog(null, USAGE, ERROR, JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	/**
	 * To get data packet's length.
	 * 
	 * @return data packet's length.
	 */
	public int getPacketLength() {
		return (24 + m_packetHeader.m_dataLength);
	}
	
	/**
	 * To joint header of packet and data of packet into m_dataPacked.
	 */
	private void jointByte() {
		if (getPacketLength() > 24) {
			m_dataPacked = new byte[getPacketLength()];
			for(int index = 0; index < 24; index++) {
				m_dataPacked[index] = m_packetHeader.getHeader()[index];
			}
			for(int index = 24; index < getPacketLength(); index++) {
				m_dataPacked[index] = m_packetData.getDataByte()[index - 24];
			}
		} else {
			m_dataPacked = new byte[24];
			for(int index = 0; index < 24; index++) {
				m_dataPacked[index] = m_packetHeader.getHeader()[index];
			}
		}
	}
	
	/**
	 * To check if user sending text is valid or not.
	 * 
	 * @return if the user sending text is valid return true, else return false.
	 */
	private boolean checkSendText() {
		Pattern pattern = Pattern.compile(REGULAR);
		m_matcher = pattern.matcher(m_sendText);

		if (m_matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * To get matched parameters and insert them into header of packet.
	 */
	private void insertHeader() {
		String group1 = null, group2 = null, group3 = null;
		
		m_matcher.reset();
		while (m_matcher.find()) {
			if (m_matcher.group(1) != null) {
				group1 = new String(m_matcher.group(1));
			}
			if (m_matcher.group(2) != null) {
				group2 = new String(m_matcher.group(2));
			}if (m_matcher.group(3) != null) {
				group3 = new String(m_matcher.group(3));
			}
		}

		m_packetHeader.m_command = m_packetHeader.byteToInt(stringToByte(group1));
		m_packetHeader.setMagic();

		if (group2 != null) {
			m_packetHeader.m_arg0 = m_packetHeader.byteToInt(stringToByte(group2));
		}
		if (group3 != null) {
			m_packetHeader.m_arg1 = m_packetHeader.byteToInt(stringToByte(group3));
		}
	}
	
	/**
	 * Convert string to byte[4].
	 * 
	 * @param str  the string to convert.
	 * @return converted byte[4].
	 */
	private byte[] stringToByte(String str) {
		byte[] byteBuf = new byte[4];
		int len = str.length();
		
		for (int index = 0; (index < len) && (index < 4); index++) {
			if ((byteBuf[index] = str.getBytes()[len - index - 1]) == ' ') {
				byteBuf[index] = 0;
			}
		}
		for (int index = len; index < 4; index++) {
			byteBuf[index] = 0;
		}
		return byteBuf;
	}
	
	/**
	 * Insert text to data of packet and insert data's length to header of packet.
	 * 
	 * @return if insert text successfully return true, else return false
	 */
	private boolean insertText() {
		String data = m_clientWindow.getJTextAreaSendText();
		if (!data.equals("")) {
			m_packetHeader.m_dataLength = data.length();
			for (int index = 0; index < m_packetHeader.m_dataLength; index++) {
				m_packetData.setData(data.getBytes()[index], index);
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Insert text to file of packet and insert data's length to header of packet.
	 * 
	 * @return if insert text successfully return true, else return false
	 */
	private boolean insertFile() {
		byte[] data = m_clientWindow.getFileData();
		m_packetHeader.m_dataLength = m_clientWindow.getFileDataLength();
		
		if ((data != null) && (m_packetHeader.m_dataLength <= m_fileSizeMax)) {
			for (int index = 0; index < m_packetHeader.m_dataLength; index++) {
				m_packetData.setData(data[index], index);
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * If packet has data, to get CRC32 of data, else set zero.
	 */
	private void getCrc32() {
		if (m_packetHeader.m_dataLength > 0) {
			CRC32 crc32 = new CRC32();
			byte[] crc32Data = new byte[m_packetHeader.m_dataLength];
			
			for (int index = 0; index < m_packetHeader.m_dataLength; index++) {
				crc32Data[index] = m_packetData.getDataByte()[index];
			}
			
			crc32.update(crc32Data);
			m_packetHeader.m_dataCheck = (int) crc32.getValue();
		} else {
			m_packetHeader.m_dataCheck = 0;
		}
	}
}
