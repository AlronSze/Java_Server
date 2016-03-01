package server;

/**
 * @ClassName     PacketData
 * @Description   The data packet of transmission
 *
 * @author        Rongzhen.shi
 * @version       V1.0
 * @Date          2016.2.28
 */

public class PacketData {
	/** The m_data is used for data of packet storage. */
	private byte[] m_data;
	
	/**
	 * Create a new PacketData, initialize the m_data.
	 * 
	 * @param packetSizeMax  get max size of packet.
	 */
	public
	PacketData(int packetSizeMax) {
		m_data = new byte[packetSizeMax - 24];
	}
	
	/**
	 * To get data of packet.
	 * 
	 * @return data of packet.
	 */
	public byte[] getDataByte() {
		return m_data;
	}
	
	/**
	 * To set data of packet.
	 * 
	 * @param buf     the data to set.
	 * @param offset  the offset to set data.
	 */
	public void setData(byte buf, int offset) {
		m_data[offset] = buf;
	}
}
