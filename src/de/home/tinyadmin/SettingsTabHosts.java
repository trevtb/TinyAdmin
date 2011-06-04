package de.home.tinyadmin;

//--- Importe
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/*------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------
--				| Copyright (c) by Tobias Burkard, 2011 |							--
--	This program is free software; you can redistribute it and/or 					--		
--	modify it under the terms of the GNU General Public License						--
--	as published by the Free Software Foundation; version 2							--	
--	of the License only.															--
--																					--
--	This program is distributed in the hope that it will be useful,					--	
--	but WITHOUT ANY WARRANTY; without even the implied warranty of					--
--	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the					--
--	GNU General Public License for more details.									--
--																					--
-- 	The Icons are taken from the Humanity Icon Theme found in Ubuntu.				--
-- 	The Humanity Icon Theme is also licensed under the GPL v2.						--
-- 	Parts of the work are based on the Tango icons, which are released under the	--
-- 	public domain.																	--	
-- 	Whereas the main application icon is taken from the "Soft Scraps Icons"-Set		--
-- 	made by Deleket (JoJo Mendoza) and distributed under the						--
-- 	CC Attribution-Noncommercial-No Derivate 3.0 License which can be found at		--
-- 	http://creativecommons.org/licenses/by-nc-nd/3.0/								--
-- 	The homepage of the author can be found at http://www.deleket.com				--
--																					--
-- 	This Software uses Ganymed SSH-2 for Java - build 250, take a look at the		--
--	corresponding class file SSHModule.java for further details and copyright		--
--	information.																	--
--																					--
-- 	You should have received a copy of the GNU General Public License				--
-- 	along with this program; if not, write to the Free Software						--
-- 	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.	--
--																					--
--	If you have problems or questions regarding the software, contact the author by	--
--	e-mail, write to: tobias.burkard (at) gmail (dot) com							--
--------------------------------------------------------------------------------------
-- 																					--
--	CLASS: SettingsTabHosts 														--
-- 																					--
--------------------------------------------------------------------------------------
-- 																					--
--	PROJECT: TinyAdmin 																--
-- 																					--
--------------------------------------------------------------------------------------
-- 																					--
--	SYSTEM ENVIRONMENT 																--
-- 	OS			Ubuntu 11.04 (Linux 2.6.38)											--
-- 	SOFTWARE 	JDK 1.6 															--
-- 																					--
--------------------------------------------------------------------------------------
------------------------------------------------------------------------------------*/

/**
 *	<p>Stellt das Host-Einstellungs-Tab des Einstellungs-GUIs bereit.
 *	Hier kann der Benutzer die Hostinformationen editieren, reseten und speichern. 
 *	Das Objekt selbst ist ein SettingsTab, welches wiederum von einem JPanel erbt.</p>
 *	<p>Das Objekt wird mit einer Referenz auf das EinstellungsGUI <i>TinyAdminSettingsGUI</i>
 *	erzeugt und baut sich (das JPanel) dann selbststaendig zusammen. Diese Klasse enthaelt
 *	alle fuer dieses Tab noetigen Listener und GUI-Komponenten, sowie Methoden.</p>
 *
 *	@see SettingsTab
 *
 * 	@version 0.2 von 06.2011
 *
 * 	@author Tobias Burkard
 */
class SettingsTabHosts extends SettingsTab {
	// --- Attribute
	private final String[] HOST_TABLENAMES = {"Name", "IP-Adresse/Hostname", "Benutzername", 
			"Passwort", "Sudo-Passwort", "MAC-Adresse", "OS"};	// Tabellenueberschriften
	private DefaultTableModel hostTableMod_ref;	// Referenz auf das TableModel der Tabelle des Host-Einstellungs-Tabs, um dieses 
												// bei Bedarf zu manipulieren oder abzufragen
	private JTable hostTable_ref;	// Die Tabelle, welche die Hosteinstellungen fasst
	private TinyAdminSettingsGUI setGui_ref;	// Referenz auf das Einstellungs-GUI
	
	// --- Klassenvariablen
	static final long serialVersionUID = 750007982315148991L; // Eigene serialVersionUID
	
	// --- Konstruktoren 
	/**
	 *	Setzt die uebergebene Referenz <i>setGui_ref</i> auf die gleichnamige Objektvariable, 
	 *	initialisiert das <i>DefaultTableModel</i> der Host-Einstellungs-Tabelle und
	 *	ruft zum Schluss die <i>drawTab()</i>-Methode des Objekts auf, welche das eigentliche Zeichnen 
	 *	des JPanels ubernimmt.
	 *
	 *	@see #drawTab()
	 */
	SettingsTabHosts(TinyAdminSettingsGUI setGui_ref) {
		this.setGui_ref = setGui_ref;
		hostTableMod_ref = new DefaultTableModel(null, HOST_TABLENAMES);
		drawTab();
	} //endconstructor
	
	// --- Methoden
	/**
	 *	<p>Diese Methode baut das JPanel (also das Objekt selbst, da es ueber seine Superklasse von JPanel erbt)
	 *	zusammen und fuellt es mit Werten. Dieses fasst die Tabelle mit Host-Einstellungen und diverse GUI-Elemente.</p>
	 *	<p><ul><li>Es wird eine Tabelle mit eigenem TableCellRenderer <i>MyCellRenderer</i> konstruiert und mit den 
	 *	eingelesenen Werten gefuettert.</li>
	 *	<li>Die Passwortfelder hingegen erhalten einen <i>PasswordCellRenderer</i>.</li>
	 *	<li>Die ComboBoxes fuer die Auswahl des Betriebssystems erhalten einen eigenen Renderer <i>MyComboBoxRenderer</i> 
	 *	und Editor <i>MyComboBoxEditor</i>, welche sich um die Darstellung der Zellen als JComboBox und die Fuellung dieser mit Werten kuemmern.
	 *	<li>Der Passwort-Renderer schuetzt die Passwoerter in dem er die Zellen als <i>JPasswordField</i> 
	 *	rendert.</li>
	 *	<li>Die Tabelle erhaelt auch den von ihrer Superklasse <i>SettingsTab</i> geerbten Editor <i>TATableEditor</i>.</ul> 
	 *	<p>Es existiert nur ein einziger Listener, der <i>SettingsButtonListener</i>, welcher allen 
	 *	Knoepfen hinzugefuegt wird.</p>
	 *
	 *	@see SettingsTab
	 *	@see MyCellRenderer
	 *	@see PasswordCellRenderer
	 *	@see MyComboBoxEditor
	 *	@see MyComboBoxRenderer
	 */
	private void drawTab() {	
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		Box custInfoBoxWrapper_ref = new Box(BoxLayout.X_AXIS);
		Box custInfoBox_ref = new Box(BoxLayout.Y_AXIS);
		custInfoBox_ref.setBorder(new TitledBorder("Hinweis"));
		custInfoBox_ref.add(Box.createVerticalStrut(5));
		custInfoBox_ref.add(new JLabel("Hier können Sie die Einstellungen der Hosts vornehmen."));
		custInfoBox_ref.add(new JLabel("Vergessen Sie nicht, für jeden Host das richtige Betriebssystem auszuwählen."));
		custInfoBox_ref.add(Box.createVerticalStrut(5));
		custInfoBox_ref.add(new JLabel("MAC-Adressen werden nur für Wake-On-LAN genutzt."));
		custInfoBox_ref.add(new JLabel("Wenn Sie sich also \"root\" einloggen, benötigen Sie kein sudo-Passwort."));
		custInfoBox_ref.add(new JLabel());
		custInfoBox_ref.add(Box.createVerticalStrut(5));
		custInfoBoxWrapper_ref.add(custInfoBox_ref);
		custInfoBoxWrapper_ref.add(Box.createHorizontalStrut(10));
		ImageIcon hostIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/hostIcon.png"));
		custInfoBoxWrapper_ref.add(new JLabel(hostIcon_ref));
		custInfoBoxWrapper_ref.add(Box.createHorizontalStrut(10));
		this.add(custInfoBoxWrapper_ref);
		this.add(Box.createVerticalStrut(25));
		
		hostTable_ref = new JTable();
		hostTable_ref.setPreferredScrollableViewportSize(new Dimension(800,200));
		hostTableMod_ref = new DefaultTableModel(setGui_ref.getSettings()[0], HOST_TABLENAMES);
		hostTable_ref.setModel(hostTableMod_ref);
		hostTable_ref.setDefaultEditor(new Object().getClass(), new TATableEditor());
		refreshHostTableRenderers();
		JScrollPane tableScroller_ref = new JScrollPane(hostTable_ref);
		hostTable_ref.getColumnModel().getColumn(0).setPreferredWidth(55);
		hostTable_ref.getColumnModel().getColumn(1).setPreferredWidth(60);
		hostTable_ref.getColumnModel().getColumn(2).setPreferredWidth(40);
		hostTable_ref.getColumnModel().getColumn(3).setPreferredWidth(35);
		hostTable_ref.getColumnModel().getColumn(4).setPreferredWidth(35);
		hostTable_ref.getColumnModel().getColumn(6).setPreferredWidth(20);
		tableScroller_ref.setBorder(new TitledBorder("Verbindungs-Parameter"));
		tableScroller_ref.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		tableScroller_ref.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(tableScroller_ref);
		
		Box setADBox_ref = new Box(BoxLayout.X_AXIS);
		ImageIcon deleteIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/deleteIcon.png"));
		ImageIcon addIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/addIcon.png"));
		JButton addButton_ref = new JButton("Hinzufügen", addIcon_ref);
		addButton_ref.setToolTipText("Fügt einen neuen Eintrag zur Liste hinzu.");
		addButton_ref.addActionListener(new SettingsHostButtonListener());
		JButton delButton_ref = new JButton("Entfernen", deleteIcon_ref);
		delButton_ref.setToolTipText("Entfernt den ausgewählten Eintrag aus der Liste");
		delButton_ref.addActionListener(new SettingsHostButtonListener());
		ImageIcon setResetIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/resetIcon.png"));
		JButton setResetBut_ref = new JButton("Reset", setResetIcon_ref);
		setResetBut_ref.setToolTipText("Setzt die Tabelle zurück.");
		setResetBut_ref.addActionListener(new SettingsHostButtonListener());
		setADBox_ref.add(addButton_ref);
		setADBox_ref.add(Box.createHorizontalStrut(5));
		setADBox_ref.add(delButton_ref);
		setADBox_ref.add(Box.createHorizontalStrut(5));
		setADBox_ref.add(setResetBut_ref);
		this.add(setADBox_ref);
		this.add(Box.createVerticalStrut(5));
	} //endmethod drawTab
	
	/**
	 *	Liefert eine Referenz auf das gehaltene DefaultTableModel <i>hostTableMod_ref</i> zurueck. Dieses beinhaltet
	 *	die Einstellungen fuer alle Hosts.
	 *
	 *	@return Referenz auf das DefaultTableModel der Tabelle mit Host-Einstellungen.
	 */
	DefaultTableModel getHostTabMod() {
		return hostTableMod_ref;
	} //endmethod getHostTabMod
	
	/**
	 *	Setzt den Tabelleninhalt  auf einen einzigen Eintrag mit Standardwerten zurueck und erneuert die Renderer der Tabelle 
	 *	durch Aufruf der Methode <i>refreshHostTableRenderers()</i>.
	 *	Folgende Standardwerte werden dabei genutzt:<ul>
	 *		<li>Hostname="Host 1"</li> 
	 *		<li>IPAdresse="0.0.0.0"</li> 
	 *		<li>Bentzername="root"</li> 
	 *		<li>Passwort=""</li> 
	 *		<li>Sudo-Passwort=""</li>
	 *		<li>MACAdresse="00:00:00:00:00:00"</li>
	 *		<li>OS="Debian"</li>
	 *
	 *	@see #refreshHostTableRenderers()
	 */
	void resetHostSettings() {
		String[][] tempA_ref = new String[1][6];
		tempA_ref[0][0] = "Host 1";
		tempA_ref[0][1] = "0.0.0.0";
		tempA_ref[0][2] = "root";
		tempA_ref[0][3] = "";
		tempA_ref[0][4] = "";
		tempA_ref[0][5] = "00:00:00:00:00:00";
		
		hostTableMod_ref.setDataVector(tempA_ref, HOST_TABLENAMES);
		refreshHostTableRenderers();
		hostTableMod_ref.fireTableDataChanged();
	} //endmethod resetHostSettings
	
	/**
	 *	<p>Erneuert den Passwort-CellRenderer und die Renderer der ComboBoxen des Host-Einstellungs-Tabs,
	 *	um diese nach Aenderungen der Tabellendaten wieder neu zu maskieren bzw. zu dekorieren/zu fuellen.</p>
	 *	<p>Alle anderen Felder bekommen den <i>MyCellRenderer</i>.</p>
	 *	<p>Dies ist immer dann noetig, wenn der Tabelleninhalt geaendert wurde.</p>
	 *
	 *	@see MyCellRenderer
	 *	@see PasswordCellRenderer
	 * 	@see MyComboBoxEditor
	 * 	@see MyComboBoxRenderer
	 */
	private void refreshHostTableRenderers() {
		TableColumnModel tcm_ref = hostTable_ref.getColumnModel();
		for (int i=0; i<hostTable_ref.getColumnCount(); i++) {
			TableColumn tc_ref = tcm_ref.getColumn(i);
			if (i !=3 && i !=4 & i !=6) {
				tc_ref.setCellRenderer(new MyCellRenderer());
			} else if (i == 3 || i == 4) {
				tc_ref.setCellRenderer(new PasswordCellRenderer());
			} else if (i == 6) {
				tc_ref.setCellEditor(new MyComboBoxEditor(setGui_ref.getGUI().getOSValues()));
				tc_ref.setCellRenderer(new MyComboBoxRenderer(setGui_ref.getGUI().getOSValues()));
			} //endif
		} //endfor
	} //endmethod refreshHostTableRenderers
	
	// --- Listener
	/**
	 *	Listener fuer alle Knoepfe der Host-Tab des Einstellungs-GUIs.
	 *	Bietet folgende Moeglichkeiten:
	 *	<ul>
	 *		<li>Hinzufuegen<ul>
	 *			<li>Fuegt der Tabelle einen neuen Eintrag mit Standardwerten hinzu.</li></ul>
	 *		</li>
	 *		<li>Entfernen<ul>
	 *			<li>Entfernt den ausgewaehlten Eintrag aus der Liste.</li></ul>
	 *		</li>
	 *		<li>Reset<ul>
	 *			<li>Ruft die <i>resetSettings()</i>-Methode auf.</li></ul>
	 *		</li>
	 *	</ul>
	 *
	 * 	@version 0.2 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class SettingsHostButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev_ref) {
			if (ev_ref.getActionCommand().equals("Hinzufügen")) {
				int nRow = hostTableMod_ref.getRowCount(), nCol = hostTableMod_ref.getColumnCount();
			    String[][] tempData_ref = new String[nRow+1][nCol];
			    for (int i=0; i<tempData_ref.length; i++) {
			        for (int j=0; j<tempData_ref[i].length; j++) {
			        	if (i < (tempData_ref.length -1)) {
			        		tempData_ref[i][j] = (String)hostTableMod_ref.getValueAt(i,j);
			        	} else {
			        		tempData_ref[i][j] = "";
			        	} //endif
			        } //endfor
			    } //endfor
			    tempData_ref[tempData_ref.length -1][0] = "Host "+tempData_ref.length;
			    tempData_ref[tempData_ref.length -1][1] = "0.0.0.0";
			    tempData_ref[tempData_ref.length -1][2] = "root";
			    tempData_ref[tempData_ref.length -1][3] = "";
			    tempData_ref[tempData_ref.length -1][4] = "";
			    tempData_ref[tempData_ref.length -1][5] = "00:00:00:00:00:00";
			    tempData_ref[tempData_ref.length -1][6] = "Debian";
			  
			    hostTableMod_ref.setDataVector(tempData_ref, HOST_TABLENAMES);
			    refreshHostTableRenderers();
				hostTableMod_ref.fireTableDataChanged();
			} else if (ev_ref.getActionCommand().equals("Entfernen")) {
				int nRow = hostTableMod_ref.getRowCount(), nCol = hostTableMod_ref.getColumnCount();
			    String[][] tempData_ref = new String[nRow][nCol];
			    for (int i=0; i<tempData_ref.length; i++) {
			        for (int j=0; j<tempData_ref[i].length; j++) {
			        	tempData_ref[i][j] = (String)hostTableMod_ref.getValueAt(i,j);
			        } //endfor
			    } //endfor
				int index = hostTable_ref.getSelectedRow();
				if (index > -1 && tempData_ref[0] != null) {
					String[][] tableTemp_ref = new String[tempData_ref.length - 1][];
					tempData_ref[index][0] = null;
					int counter = 0;
					for (int i = 0; i < tempData_ref.length; i++) {
						if (tempData_ref[i][0] != null && counter < tableTemp_ref.length) {
							tableTemp_ref[counter] = tempData_ref[i];
							counter++;
						} //endif
					} //endfor
					hostTableMod_ref.setDataVector(tableTemp_ref, HOST_TABLENAMES);
					refreshHostTableRenderers();
					hostTableMod_ref.fireTableDataChanged();
				} //endif
			} else if (ev_ref.getActionCommand().equals("Reset")) {
				Object[] options_ref = {"Ja", "Nein"};
				int n = JOptionPane.showOptionDialog(setGui_ref.getFrame(), "Sind Sie sicher, dass Sie alle Einstellungen verwerfen möchten?", "Einstellungen zurücksetzen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options_ref, options_ref[1]);
				if (n == 0) {
					resetHostSettings();
				} //endif
			} //endif
		} //endmethod actionPerformed
	} //endclass SettingsHostButtonListener
	
	// --- Innere Klassen
	/**
	 * 	Eigener <i>TableCellRenderer</i> fuer die Tabelle des Host-Einstellungs-Tabs.
	 *	Sorgt dafuer, dass die Zellen, fuer die dieser Renderer ausgewaehlt wurde, als
	 *	JTextField gerendert werden. Hier wird auch das optische Verhalten dieser 
	 *	TextFelder geregelt.
	 *
	 * 	@version 0.2 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class MyCellRenderer extends JTextField implements TableCellRenderer {
		private static final long serialVersionUID = 1253309849735733317L;
		
		private MyCellRenderer() {
			super();
			this.setHorizontalAlignment(SwingConstants.CENTER);
		} //endconstructor

		public Component getTableCellRendererComponent(JTable table_ref, Object value_ref, boolean isSelected, boolean hasFocus, int row, int column) {
			if (hasFocus) {
				this.setBorder(BorderFactory.createLineBorder(new Color(99, 130, 191), 1));
			} else {
				this.setBorder(null);
			} //endif
			
			if (isSelected) {
				this.setBackground(new Color(184,  207, 229));
			} else {
				this.setBackground(Color.WHITE);
			} //endif
		
			this.setText((String)table_ref.getValueAt(row, column));
			return this;
		} //endmethod getTableCellRendererComponent
	} //endclass MyCellRenderer
	
	/**
	 * 	<p>Eigener <i>TableCellRenderer</i> fuer die Passwortzellen der Tabelle des Host-Einstellungs-Tabs.
	 *	Sorgt dafuer, dass beide Passwortspalten in Form eines <i>JPasswordFields</i> gerendert werden.</p>
	 *	<p>Eintraege werden so mit echo-Characters maskiert: Die angezeigte Laenge der echo-Chars ist dabei 
	 *	immer gleich und wird durch einen Standard-String (<i>supersecret</i>) symbolisiert. Es werden also immer 
	 *	11 "Sterne"(*) angezeigt.</p>
	 *	<p>Leider gehen dadurch die optischen Standardeigenschaften der Zelle verloren und so
	 *	wird hier auch festgelegt welche Hintergrundfarbe und Umrandung die Zelle in den
	 *	jeweiligen Stadien (ausgewaehlt, nicht ausgewaehlt) erhaelt.</p>
	 *	<p>Dieses optische Verhalten sollte identisch mit dem <i>MyCellRenderer</i> sein, um die Optik homogen zu halten.</p>
	 *
	 *	@see MyCellRenderer
	 *
	 * 	@version 0.2 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class PasswordCellRenderer extends JPasswordField implements TableCellRenderer {
		private static final long serialVersionUID = 1253309849735733317L;
		
		private PasswordCellRenderer() {
			super();
			this.setHorizontalAlignment(SwingConstants.CENTER);
		} //endconstructor

		public Component getTableCellRendererComponent(JTable table_ref, Object value_ref, boolean isSelected, boolean hasFocus, int row, int column) {
			if (hasFocus) {
				this.setBorder(BorderFactory.createLineBorder(new Color(99, 130, 191), 1));
			} else {
				this.setBorder(null);
			} //endif
			
			if (isSelected) {
				this.setBackground(new Color(184,  207, 229));
			} else {
				this.setBackground(Color.WHITE);
			} //endif
			
			if ((table_ref.getValueAt(row, column)).equals("")) {
				this.setText("");
			} else {
				this.setText("supersecret");
			} //endif
			
			return this;
		} //endmethod getTableCellRendererComponent
	} //endclass PasswordCellRenderer
	
	/**
	 *	Renderer fuer die <i>JComboBoxes</i> der Tabelle, welche das <i>OS</i>-Attribut fassen.
	 *	Moegliche Auswahlmoeglichkeiten werden dem Objekt bei seiner Erstellung durch das Array
	 *	<i>items_ref</i> uebergeben und an die Super-Klasse <i>JComoBox</i> weitergegeben.
	 *
	 * 	@version 0.2 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
		private static final long serialVersionUID = 1258611399135235317L;
		
	    private MyComboBoxRenderer(String[] items_ref) {
	        super(items_ref);
	    } //endconstructor

	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {
	        if (isSelected) {
	            setForeground(table.getSelectionForeground());
	            super.setBackground(table.getSelectionBackground());
	        } else {
	            setForeground(table.getForeground());
	            setBackground(table.getBackground());
	        } //endif

	        setSelectedItem(value);
	        return this;
	    } //endmethod getTableCellRendererComponent
	} //endclass MyComboBoxRenderer

	/**
	 *	Eigener Editor fuer die <i>JComboBoxes</i> der Tabelle um deren optisches Verhalten
	 *	genauso zu gestalten, wie ausserhalb einer Tabelle, indem eine <i>JComboBox</i> als
	 *	Zellentyp an die Superklasse <i>DefaultCellEditor</i> uebergeben wird.
	 *
	 * 	@version 0.2 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class MyComboBoxEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1238611378137331217L;
		
	    private MyComboBoxEditor(String[] items_ref) {
	        super(new JComboBox(items_ref));
	    } //endconstructor
	} //endclass MyComboBoxEditor
	
} //endclass SettingsTabHosts