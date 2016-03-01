package server;

import java.util.zip.CRC32;

import javax.swing.JOptionPane;

/**
 * @ClassName:     UnpackRecvData
 * @Description:   Unpack the received data packet
 *
 * @author         Rongzhen.shi
 * @version        V1.1
 * @Date           2016.2.28
 */

public class UnpackRecvData {
	/** Used for static string storage. */
	final static String ERROR = "Error";
	
	/** The INVALID_COMMAND is used for error invalid command information storage. */
	final static String INVALID_COMMAND = "Received Error!\n" + "Has received packet, but is an invalid command.";

	/** The WRONG_CRC32 is used for error wrong command information storage. */
	final static String WRONG_CRC32 = "Received Error!\n" + "Has received packet, but CRC32 is wrong.";
	
	/** The m_readLength is used for getting once received data's length. */
	private int m_readLength;
	
	/** The m_recvExtraCount is used for counting extra received data(such as (full packet + extra data)). */
	private int m_recvExtraCount;
	
	/** The m_recvPointer is used for pointing next receiving data to save. */
	private int m_recvPointer = 0;
	
	/** The m_scanPointer is used for pointing next scanning data to deal with. */
	private int m_scanPointer = 0;
	
	/** The m_dataLength is used for data of packet length storage. */
	private int m_dataLength  = 0;
	
	/** The m_dataByte is used for data packet buffer storage. */
	private byte[] m_dataByte;
	
	/** The m_readData is used for getting once received data. */
	private byte[] m_readData;
	
	/** The m_packetHeader is used for creating a new PacketHeader. */
	private PacketHeader m_packetHeader;
	
	/** The m_packetData is used for creating a new PacketData. */
	private PacketData m_packetData;
	
	/** The m_clientWindow is used for getting the client communication window. */
	private ClientWindow m_clientWindow;
	
	/** The m_explainCommand is used for creating a new ExplainCommand. */
	private ExplainCommand m_explainCommand;
	
	/** The m_mainWindow is used for getting the main window. */
	private MainWindow m_mainWindow;
	
	/**
	 * Create a new UnpackRecvData, to get client window, main window and max size of packet.
	 * 
	 * @param clientWindow   get the client window.
	 * @param mainWindow     get the main window.
	 * @param packetSizeMax  get max size of packet.
	 */
	public
	UnpackRecvData(ClientWindow clientWindow, MainWindow mainWindow, int packetSizeMax) {
		this.m_clientWindow  = clientWindow;
		this.m_mainWindow    = mainWindow;
		
		m_dataByte     = new byte[packetSizeMax];
		m_packetData   = new PacketData(packetSizeMax);
		m_packetHeader = new PacketHeader();
	}
	
	/**
	 * To unpack the received data from client.
	 * 
	 * @param readData    once received data.
	 * @param readLength  once received data's length.
	 */
	public void unpackData(byte[] readData, int readLength) {
		this.m_readLength = readLength;
		this.m_readData   = readData;
		
		getPacket();
		getHeader();
		if (m_scanPointer >= 24) {
			if (m_packetHeader.checkMagic()) {
				if (m_dataLength > 0) {
					if (m_recvPointer > 24) {
						getData();
						if (m_dataLength != 0) {
							return;
						}
					}
				} else {
					if (checkCrc32()) {
						m_explainCommand = new ExplainCommand(m_packetHeader,
														  	  m_packetData,
														  	  m_clientWindow,
														      m_mainWindow);
				
						if (!m_explainCommand.checkCommand(true)) {
							JOptionPane.showMessageDialog(null, INVALID_COMMAND, ERROR, JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, WRONG_CRC32, ERROR, JOptionPane.ERROR_MESSAGE);
					}
					setPointerZero();
				}
			} else {
				m_recvExtraCount = m_recvPointer - m_scanPointer;
				for (int index = 0; index < m_recvExtraCount; index++, m_scanPointer++) {
					m_dataByte[index] = m_dataByte[m_scanPointer];
				}
				m_scanPointer = 0;
				m_recvPointer = m_recvExtraCount;
			}
		}
	}
	
	/**
	 * To set m_dataLength, m_recvPointer and m_scanPointer zero.
	 */
	private void setPointerZero() {
		m_dataLength  = 0;
		m_recvPointer = 0;
		m_scanPointer = 0;
	}
	
	/**
	 * To get data from data packet buffer and print to client window.
	 */
	private void getData() {
		if ((m_recvPointer - m_scanPointer) <= m_dataLength) {
			for (; m_scanPointer < m_recvPointer; m_scanPointer++, m_dataLength--) {
				m_packetData.setData(m_dataByte[m_scanPointer], m_scanPointer - 24);
				
//				m_clientWindow.appendJTextAreaRecvText(new String("" + (char)m_packetData.getDataByte()[m_scanPointer - 24]));
			}
		} else {
			m_recvExtraCount = m_recvPointer - m_scanPointer - m_dataLength;
			for (; m_scanPointer < (m_recvPointer - m_recvExtraCount); m_scanPointer++) {
				m_packetData.setData(m_dataByte[m_scanPointer], m_scanPointer - 24);
				
//				m_clientWindow.appendJTextAreaRecvText(new String("" + (char)m_packetData.getDataByte()[m_scanPointer - 24]));
			}
			m_scanPointer = 0;
			
			for (int index = 0; index < m_recvExtraCount; index++, m_recvPointer++) {
				m_dataByte[index] = m_dataByte[m_recvPointer];
			}
			m_recvPointer = m_recvExtraCount;
		}
	}
	
	/**
	 * Read once received data to data packet buffer.
	 */
	private void getPacket() {
		for(int index = 0; index < m_readLength; index++, m_recvPointer++) {
			m_dataByte[m_recvPointer] = m_readData[index];
		}
	}
	
	/**
	 * To get header from data packet buffer and print to client window.
	 */
	private void getHeader() {
		if ((m_recvPointer >= 4) && (m_scanPointer == 0)) {
			m_packetHeader.m_command = getByteOneOfHeader(0);
			cleanByteOneOfHeader(0);
			m_scanPointer = 4;
			
//			String str = new String("\n========header start========\n" +
//					"command: " +
//					(char)((m_packetHeader.m_command & 0xff000000) >> 24) +
//					(char)((m_packetHeader.m_command & 0xff0000) >> 16) +
//					(char)((m_packetHeader.m_command & 0xff00) >> 8) +
//					(char)((m_packetHeader.m_command & 0xff) >> 0));
//			m_clientWindow.appendJTextAreaRecvText(str + "\n");
		}
		if ((m_recvPointer >= 8) && (m_scanPointer == 4)) {
			m_packetHeader.m_arg0 = getByteOneOfHeader(4);
			cleanByteOneOfHeader(4);
			m_scanPointer = 8;
			
//			String str = new String("arg0: " +
//					(char)((m_packetHeader.m_arg0 & 0xff000000) >> 24) +
//					(char)((m_packetHeader.m_arg0 & 0xff0000) >> 16) +
//					(char)((m_packetHeader.m_arg0 & 0xff00) >> 8) +
//					(char)((m_packetHeader.m_arg0 & 0xff) >> 0));
//			m_clientWindow.appendJTextAreaRecvText(str + "\n");
		}
		if ((m_recvPointer >= 12) && (m_scanPointer == 8)) {
			m_packetHeader.m_arg1 = getByteOneOfHeader(8);
			cleanByteOneOfHeader(8);
			m_scanPointer = 12;

//			String str = new String("arg1: " +
//					(char)((m_packetHeader.m_arg1 & 0xff000000) >> 24) +
//					(char)((m_packetHeader.m_arg1 & 0xff0000) >> 16) +
//					(char)((m_packetHeader.m_arg1 & 0xff00) >> 8) +
//					(char)((m_packetHeader.m_arg1 & 0xff) >> 0));
//			m_clientWindow.appendJTextAreaRecvText(str + "\n");
		}
		if ((m_recvPointer >= 16) && (m_scanPointer == 12)) {
			m_packetHeader.m_dataLength = getByteOneOfHeader(12);
			cleanByteOneOfHeader(12);
			m_dataLength = m_packetHeader.m_dataLength;
			m_scanPointer = 16;

//			String str = new String("length: " +
//					m_packetHeader.m_dataLength);
//			m_clientWindow.appendJTextAreaRecvText(str + "\n");
		}
		if ((m_recvPointer >= 20) && (m_scanPointer == 16)) {
			m_packetHeader.m_dataCheck = getByteOneOfHeader(16);
			cleanByteOneOfHeader(16);
			m_scanPointer = 20;

//			String str = new String("Check: " +
//					m_packetHeader.m_dataCheck);
//			m_clientWindow.appendJTextAreaRecvText(str + "\n");
		}
		if ((m_recvPointer >= 24) && (m_scanPointer == 20)) {
			m_packetHeader.m_magic = getByteOneOfHeader(20);
			cleanByteOneOfHeader(20);
			m_scanPointer = 24;
			
//			String str = new String("magic: " +
//					m_packetHeader.m_magic +
//					"\n========Data start========");
//			m_clientWindow.appendJTextAreaRecvText(str + "\n");
		}
	}
	
	/**
	 * To set one parameter of header zero.
	 * 
	 * @param offset  header parameter's offset.
	 */
	private void cleanByteOneOfHeader(int offset) {
		for (int index = offset; index < (offset + 4); index++) {
			m_dataByte[index] = 0;
		}
	}
	
	/**
	 * To get one parameter of header.
	 * 
	 * @param offset  header parameter's offset.
	 */
	private int getByteOneOfHeader(int offset) {
		int member = 0;
		byte[] temp = new byte[4];
		
		for (int index = offset; index < (offset + 4); index++) {
			temp[index - offset] = m_dataByte[index];
		}
		for (int index = 0; index < 4; index++) {
			member |= (temp[index] & 0xFF) << (index * 8);
		}
		
		return member;
	}
	
	/**
	 * To check crc32 of data.
	 * 
	 * @return if crc32 is right return true, else return false.
	 */
	private boolean checkCrc32() {
		if (m_packetHeader.m_dataLength == 0) {
			if (m_packetHeader.m_dataCheck == 0) {
				return true;
			} else {
				return false;
			}
		}
		
		CRC32 crc32 = new CRC32();
		byte[] crc32Data = new byte[m_packetHeader.m_dataLength];
		
		for (int index = 0; index < m_packetHeader.m_dataLength; index++) {
			crc32Data[index] = m_packetData.getDataByte()[index];
		}
		
		crc32.update(crc32Data);
		if (m_packetHeader.m_dataCheck == (int) crc32.getValue()) {
			return true;
		} else {
			return false;
		}
	}
}
