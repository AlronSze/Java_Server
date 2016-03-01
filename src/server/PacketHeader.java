package server;

/**
 * @ClassName     PacketHeader
 * @Description   The header of the data packet
 *
 * @author        Rongzhen.shi
 * @version       V1.0
 * @Date          2016.2.28
 */

public class PacketHeader {
	/** The MAGIC_MASK is used for magic mask storage. */
	final static int MAGIC_MASK = 0xFFFFFFFF;
	
	/** Packet header param1: command identifier constant. */
	public int m_command;
	
	/** Packet header param2: first argument. */
	public int m_arg0;
	
	/** Packet header param3: second argument. */
	public int m_arg1;
	
	/** Packet header param4: length of payload (0 is allowed). */
	public int m_dataLength;
	
	/** Packet header param5: crc32 of data payload. */
	public int m_dataCheck;
	
	/** Packet header param6: command ^ 0xffffffff. */
	public int m_magic;
	
	
	/**
	 * Convert byte[4] to integer.
	 * 
	 * @param bytebuf  the byte[4] to convert.
	 * @return converted integer.
	 */
	public int byteToInt(byte[] byteBuf) {
		int intBuf = 0;
		for (int index = 0; index < 4; index++) {
			intBuf |= (byteBuf[index] & 0xFF) << (index * 8);
		}
		return intBuf;
	}
	
	/**
	 * To set magic(command ^ 0xffffffff).
	 */
	public void setMagic() {
		m_magic = m_command ^ MAGIC_MASK;
	}
	
	/**
	 * To check if magic is right or not.
	 * 
	 * @return if magic right return true, else return false.
	 */
	public boolean checkMagic() {
		return (m_command ^ MAGIC_MASK) == m_magic;
	}
	
	/**
	 * To get packet header and store into byte[24].
	 * 
	 * @return packet header in byte[24].
	 */
	public byte[] getHeader() {
		byte[] header = new byte[24];
		
		header = insertToByte(header, intToByte(m_command), 0);
		header = insertToByte(header, intToByte(m_arg0), 4);
		header = insertToByte(header, intToByte(m_arg1), 8);
		header = insertToByte(header, intToByte(m_dataLength), 12);
		header = insertToByte(header, intToByte(m_dataCheck), 16);
		header = insertToByte(header, intToByte(m_magic), 20);
		
		return header;
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
	 * Insert byte[4] to byte[24].
	 * 
	 * @param byte24  the final data to return.
	 * @param byte4   the byte[4] data to insert.
	 * @param offset  the offset to insert byte[4] data.
	 * @return updated byte[24].
	 */
	private byte[] insertToByte(byte[] byte24, byte[] byte4, int offset) {
		for (int index = 0; index < 4; index++, offset++) {
			byte24[offset] = byte4[index];
		}
		return byte24;
	}
}
