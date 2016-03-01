package server;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

/**
 * @ClassName     MainWindow
 * @Description   To create a main window
 *
 * @author        Rongzhen.shi
 * @version       V1.1
 * @Date          2016.2.27
 */

public class MainWindow{
	/** Used for static string storage. */
	final static String WAIT_CONNECT = "Waiting for connection...";
	
	/** Used for static string storage. */
	final static String CONNECTING = "Connecting List";
	
	/** Used for static string storage. */
	final static String FONT_TYPE = "Times NewRoman";

	/** The m_jFrame is used for create a JFrame. */
	private JFrame m_jFrame;
	
	/** The m_jLabel is used for create a JLabel for connect status. */
	private JLabel m_jLabel;
	
	/** The m_jMenuBar is used for create a JMenuBar. */
	private JMenuBar m_jMenuBar;
	
	/** The m_jList is used for create a JList for show client information. */
	private JList<String> m_jList;
	
	/** The m_defaultListModel is used for client name list storage. */
	private DefaultListModel<String> m_defaultListModel;
	
	/**
	 * Create a new MainWindow, to initialize the window and change JLabel.
	 */
	public
	MainWindow() {
		initWindow();
		m_jLabel.setText(WAIT_CONNECT);
	}
	
	/**
	 * To show the main window.
	 */
	public void showWindow() {
		m_jFrame.setVisible(true);
	}
	
	/**
	 * Change to connecting window.
	 */
	public void changeToConnecting() {
		m_jLabel.setText(CONNECTING);
		m_jFrame.setSize(250, 360);
		m_jFrame.setLocationRelativeTo(null);
		
		m_jMenuBar.setBounds(0, 0, 250, 20);
	}
	
	/**
	 * Add client name to jList.
	 * 
	 * @param str  the name string to add.
	 */
	public void addNameToJList(String str) {
		m_defaultListModel.addElement(str);
		m_jList.setModel(m_defaultListModel);
	}
	
	/**
	 * Change client name in jList.
	 * 
	 * @param oldStr  the old name string to be changed.
	 * @param newStr  the new name string to change.
	 */
	public void changeNameInJList(String oldStr, String newStr) {
		for (int index = 0; index < m_defaultListModel.getSize(); index++) {
			if (m_defaultListModel.getElementAt(index).equals(oldStr)) {
				m_defaultListModel.setElementAt(newStr, index);
			}
		}
		m_jList.setModel(m_defaultListModel);
	}
	
	/**
	 * Remove client name from jList.
	 * 
	 * @param str  the name string to remove.
	 */
	public void removeNamefromJList(String str) {
		for (int index = 0; index < m_defaultListModel.getSize(); index++) {
			if (m_defaultListModel.getElementAt(index).equals(str)) {
				m_defaultListModel.removeElementAt(index);
			}
		}
		m_jList.setModel(m_defaultListModel);
	}
	
	/**
	 * To initialize the main window.
	 */
	private void initWindow() {
		initJLabel();
		initJList();
		initJMenuBar();
		initJFrame();
		addJFrame();
	}
	
	/**
	 * To initialize the JLabel.
	 */
	private void initJLabel() {
		m_jLabel = new JLabel();
		m_jLabel.setBounds(25, 5, 270, 80);
		m_jLabel.setFont(new Font(FONT_TYPE, Font.BOLD, 18));
	}
	
	/**
	 * To initialize the JFrame.
	 */
	private void initJFrame() {
		m_jFrame = new JFrame();
		m_jFrame.setTitle("Java Server");
		m_jFrame.setSize(270, 100);
		m_jFrame.setResizable(false);
		m_jFrame.setLayout(null);
		m_jFrame.setLocationRelativeTo(null);
		m_jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * To initialize the JList.
	 */
	private void initJList() {
		m_defaultListModel = new DefaultListModel<String>();

		m_jList = new JList<String>(m_defaultListModel);
		m_jList.setBounds(25, 75, 195, 230);
		m_jList.setFont(new Font(FONT_TYPE, Font.PLAIN, 15));
		m_jList.setBorder(new LineBorder(Color.GRAY));
		m_jList.setFocusable(false);
		m_jList.setSelectionBackground(null);
	}
	
	/**
	 * To initialize the JMenuBar.
	 * <p><b>This method must not to change or remove.</b></p>
	 */
	private void initJMenuBar() {
		m_jMenuBar = new JMenuBar();
		m_jMenuBar.setBounds(0, 0, 270, 20);
		m_jMenuBar.setLayout(null);
		m_jMenuBar.setBorder(new LineBorder(Color.GRAY));
		
		JMenuItem jMenuItem = new JMenuItem("About");
		jMenuItem.setFont(new Font(FONT_TYPE, Font.BOLD, 10));
		jMenuItem.setBounds(0, 0, 40, 20);
		jMenuItem.setBorder(new LineBorder(Color.GRAY));

		m_jMenuBar.add(jMenuItem);
		
		jMenuItem.addActionListener(new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent e) {
				  JOptionPane.showMessageDialog(null, "Developed by Rongzhen Shi.", "About Java Server", JOptionPane.INFORMATION_MESSAGE);
			  }
		});
	}
	
	/**
	 * To add widget for JFrame.
	 */
	private void addJFrame() {
		m_jFrame.add(m_jLabel);
		m_jFrame.add(m_jList);
		m_jFrame.add(m_jMenuBar);
	}
}
