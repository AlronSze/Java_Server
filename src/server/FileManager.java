package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * @ClassName     FileManager
 * @Description   To manage the selected file.
 *
 * @author        Rongzhen.shi
 * @version       V1.1
 * @Date          2016.2.29
 */

public class FileManager {
	/** The TITLE is Used for setting file chooser's title. */
	final static String TITLE = "Select a file";
	
	/** The m_file is used for file data storage. */
	private File m_file;
	
	/** The m_fileLength is used for file data' length storage. */
	private int m_fileLength = 0;
	
	/**
	 * To get file's data.
	 * 
	 * @return if get file's data successfully return file's data byte[], else return null.
	 */
	public byte[] getFileData(int fileSizeMax) {
		if (isTextFile()) {
			FileReader fileReader;
			BufferedReader bufferedReader;
			StringBuffer fileData = new StringBuffer();
			
			try {
				fileReader = new FileReader(m_file.getAbsolutePath());
			} catch (IOException e) {
				return null;
			}
			
			bufferedReader = new BufferedReader(fileReader);
			String readtemp = null;
			
			try {
				while((readtemp = bufferedReader.readLine()) != null) {
					fileData.append(readtemp);
				}
				bufferedReader.close();
				return fileData.toString().getBytes();
			} catch (IOException e) {
				try {
					bufferedReader.close();
				} catch (IOException ee) {
					
				}
				return null;
			}
		} else {
			InputStream inputStream;
			int readTemp;
			byte[] fileDataByte = new byte[fileSizeMax];
			
			try {
				inputStream = new FileInputStream(m_file);
				while ((readTemp = inputStream.read()) != -1) {
					fileDataByte[m_fileLength++] = (byte)readTemp;
				}
				inputStream.close();
				return fileDataByte;
			} catch (IOException e) {
				return null;
			}
		}
	}
	
	/**
	 * To get file data' length.
	 * 
	 * @return file data' length.
	 */
	public int getFileDataLength() {
		return m_fileLength;
	}
	
	/**
	 * To select a file for read.
	 * 
	 * @param jTextAreaSendFile  get send file's text area to set absolute path.
	 * @return if file is selected return true, else return false.
	 */
	public boolean selectFile(JTextArea jTextAreaSendFile) {
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);  
		jFileChooser.showDialog(new JLabel(), TITLE);
		try {
			m_file = jFileChooser.getSelectedFile();
			jTextAreaSendFile.setText(m_file.getAbsolutePath());
			return true;
		} catch(NullPointerException e) {
			return false;
		}
	}
	
	private boolean isTextFile() {
		String name = m_file.getName();
		
		// there can add more text file type.
		if (name.trim().toLowerCase().endsWith(".txt")) {
			return true;
		} else {
			return false;
		}
	}
}
