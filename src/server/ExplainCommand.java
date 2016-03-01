package server;

import java.util.Arrays;

/**
 * @ClassName     ExplainCommand
 * @Description   To explain command of packet head.
 *
 * @author        Rongzhen.shi
 * @version       V1.0
 * @Date          2016.2.29
 */

public class ExplainCommand {
	/** The m_command is used for command byte[4] storage. */
	private byte[] m_command;
	
	/** The m_arg0 is used for arg0 byte[4] storage. */
	private byte[] m_arg0;
	
	/** The m_arg1 is used for arg1 byte[4] storage. */
	private byte[] m_arg1;
	
	/** The m_hasText is used for judging if has text for data of packet or not. */
	private boolean m_hasText;
	
	/** The m_hasFile is used for judging if has file for data of packet or not. */
	private boolean m_hasFile;
	
	/** The m_dataLength is used for data length storage. */
	private int m_dataLength;
	
	/** The m_clientWindow is used for getting the client communication window. */
	private ClientWindow m_clientWindow;
	
	/** The m_mainWindow is used for getting the main window. */
	private MainWindow m_mainWindow;
	
	/** The m_packetData is used for getting a new PacketData. */
	private PacketData m_packetData;
	
	/**
	 * Create a new ExplainCommand, to get command, arg0, arg1, client window and main window.
	 * 
	 * @param command       get command.
	 * @param arg0          get arg0.
	 * @param arg1          get arg1.
	 * @param clientWindow  get the client window.
	 * @param mainWindow    get the main window.
	 */
	public
	ExplainCommand(PacketHeader packetHeader, PacketData packetData,
			ClientWindow clientWindow, MainWindow mainWindow) {
		this.m_command      = intToByte(packetHeader.m_command);
		this.m_arg0         = intToByte(packetHeader.m_arg0);
		this.m_arg1         = intToByte(packetHeader.m_arg1);
		this.m_dataLength   = packetHeader.m_dataLength;
		this.m_packetData   = packetData;
		this.m_clientWindow = clientWindow;
		this.m_mainWindow   = mainWindow;
	}
	
	/**
	 * Check command is right or not.
	 * <p><b>Now the method is a demo, wait for customizing.</b><p>
	 * 
	 * @param toExplain  if toExplain is true, explain command do things.
	 * @return if command is right return true, else return false.
	 */
	public boolean checkCommand(boolean toExplain) {
		if (Arrays.equals(m_command, stringToByte("who"))) {
			if (Arrays.equals(m_arg0, stringToByte("am")) && Arrays.equals(m_arg1, stringToByte("i"))) {
				if (toExplain) {
					// do things
					setClientName();
					m_clientWindow.appendJTextAreaRecvText("Do things over!\n");
				} else {
					// if has text
					setHasFile(false);
					setHasText(true);
				}
				return true;
			}
		}
		else if (Arrays.equals(m_command, stringToByte("file"))) {
			if (Arrays.equals(m_arg0, stringToByte("-l")) && Arrays.equals(m_arg1, stringToByte(""))) {
				if (toExplain) {
					// do things
					m_clientWindow.appendJTextAreaRecvText("Do things over!\n");
				} else {
					// if has file
					setHasFile(true);
					setHasText(false);
				}
				return true;
			} else if (Arrays.equals(m_arg0, stringToByte("-a")) && Arrays.equals(m_arg1, stringToByte(""))) {
				if (toExplain) {
					// do things
					m_clientWindow.appendJTextAreaRecvText("Do things over!\n");
				} else {
					// if has nothing
					setHasFile(false);
					setHasText(false);
				}
				return true;
			}
		} else if (Arrays.equals(m_command, stringToByte("text"))) {
			if (Arrays.equals(m_arg0, stringToByte("")) && Arrays.equals(m_arg1, stringToByte(""))) {
				if (toExplain) {
					// do things
					m_clientWindow.appendJTextAreaRecvText("Do things over!\n");
				} else {
					// if has Text
					setHasFile(false);
					setHasText(true);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * To check packet has text or not.
	 * 
	 * @return if has text return true, else return false.
	 */
	public boolean checkHasText() {
		return m_hasText;
	}
	
	/**
	 * To check packet has file or not.
	 * 
	 * @return if has file return true, else return false.
	 */
	public boolean checkHasFile() {
		return m_hasFile;
	}
	
	/**
	 * To set client real name.
	 */
	private void setClientName() {
		byte[] dataByte = m_packetData.getDataByte();
		StringBuffer stringBuffer = new StringBuffer();
		for (int index = 0; index < m_dataLength; index++) {
			stringBuffer.append((char)dataByte[index]);
		}
		
		String clientName = stringBuffer.toString();
		m_clientWindow.setJFrameTitle(clientName);
		m_mainWindow.changeNameInJList(m_clientWindow.getIPAndPort(), clientName);
		
		if (clientName.equals("Rongzhen")) {
			m_clientWindow.appendJTextAreaRecvText("Rongzhen is connected!\n");
		}
	}
	
	/**
	 * To set packet has text.
	 * 
	 * @param flag  set for m_hasText
	 */
	private void setHasText(boolean flag) {
		m_hasText = flag;
	}
	
	/**
	 * Set packet has file.
	 * 
	 * @param flag  set for m_hasFile
	 */
	private void setHasFile(boolean flag) {
		m_hasFile = true;
	}
	
	/**
	 * Convert integer to byte[4].
	 * 
	 * @param integer  the integer to convert.
	 * @return converted byte[4].
	 */
	private byte[] intToByte(int integer) {
		byte[] byteBuf = new byte[4];
		for (int index = 0; index < 4; index++) {
			byteBuf[index] = (byte) ((integer & (0xFF << (index * 8))) >> (index * 8));
		}
		return byteBuf;
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
		
		for (int index = 0; index < len; index++) {
			byteBuf[index] = str.getBytes()[len - index - 1];
		}
		for (int index = len; index < 4; index++) {
			byteBuf[index] = 0;
		}
		return byteBuf;
	}
}
