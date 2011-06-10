package de.home.tinyadmin;

//--- Importe
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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
 *	Das Objekt selbst ist bzw. erbt von einem JPanel.</p>
 *	<p>Das Objekt wird mit einer Referenz auf das EinstellungsGUI <i>TinyAdminSettingsGUI</i>
 *	erzeugt und baut sich (das JPanel) dann selbststaendig zusammen. Diese Klasse enthaelt
 *	alle fuer dieses Tab noetigen Listener und GUI-Komponenten, sowie Methoden.</p>
 *
 * 	@version 0.3 von 06.2011
 *
 * 	@author Tobias Burkard
 */
class SettingsTabHosts extends JPanel {
	// --- Attribute
	private final String[] HOST_TABLENAMES = {"Name", "IP-Adresse/Hostname", "SSH-Port", "Benutzername", 
			"KeyFile", "MAC-Adresse", "OS"};	// Tabellenueberschriften
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
		hostTableMod_ref = new CustomTableModel(null, HOST_TABLENAMES);
		drawTab();
	} //endconstructor
	
	// --- Methoden
	/**
	 *	<p>Diese Methode baut das JPanel (also das Objekt selbst, da es von JPanel erbt)
	 *	zusammen. Dieses fasst die Tabelle mit Host-Einstellungen und diverse GUI-Elemente.</p>
	 *	<p>Die Tabelle wird mit den Werten aus der im Haupt-GUI gehaltenen Matrix gefuettert. Es werden
	 *	jedoch nicht alle Felder der Matrix genutzt, da beispielsweise das Anzeigen von Passwoertern keinen Sinn
	 *	macht, da diese maskiert dargestellt werden. Fuer das Generieren der Tabelledaten ist die Methode
	 *	<i>generateTableData()</i> zustaendig.</p>
	 *	<p>Es existiert nur ein einziger Listener, der <i>SettingsButtonListener</i>, welcher allen 
	 *	Knoepfen hinzugefuegt wird. Die Tabelle erhaelt zudem ein CustomTableModel, um sie vor Zugriffen zu
	 *	schuetzen.</p>
	 *
	 *	@see #generateTableData()
	 */
	private void drawTab() {	
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		Box custInfoBoxWrapper_ref = new Box(BoxLayout.X_AXIS);
		Box custInfoBox_ref = new Box(BoxLayout.Y_AXIS);
		custInfoBox_ref.setBorder(new TitledBorder("Hinweis"));
		custInfoBox_ref.add(Box.createVerticalStrut(5));
		custInfoBox_ref.add(new JLabel("Hier können Sie neue Hosts hinzufügen und entfernen, sowie deren Einstellungen ändern."));
		custInfoBox_ref.add(new JLabel("Nutzen Sie hierfür die entsprechenden Buttons."));
		custInfoBoxWrapper_ref.add(custInfoBox_ref);
		custInfoBoxWrapper_ref.add(Box.createHorizontalStrut(10));
		ImageIcon hostIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/hostIcon.png"));
		custInfoBoxWrapper_ref.add(new JLabel(hostIcon_ref));
		custInfoBoxWrapper_ref.add(Box.createHorizontalStrut(10));
		this.add(custInfoBoxWrapper_ref);
		
		hostTable_ref = new JTable();
		hostTable_ref.setPreferredScrollableViewportSize(new Dimension(800,250));
		hostTableMod_ref = new CustomTableModel(generateTableData(), HOST_TABLENAMES);
		hostTable_ref.setModel(hostTableMod_ref);
		hostTable_ref.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		JScrollPane tableScroller_ref = new JScrollPane(hostTable_ref);
		hostTable_ref.getColumnModel().getColumn(0).setPreferredWidth(60);
		hostTable_ref.getColumnModel().getColumn(1).setPreferredWidth(70);
		hostTable_ref.getColumnModel().getColumn(2).setPreferredWidth(10);
		hostTable_ref.getColumnModel().getColumn(3).setPreferredWidth(35);
		hostTable_ref.getColumnModel().getColumn(4).setPreferredWidth(5);
		hostTable_ref.getColumnModel().getColumn(6).setPreferredWidth(20);
		((DefaultTableCellRenderer)hostTable_ref.getDefaultRenderer(hostTable_ref.getColumnClass(0))).setHorizontalAlignment(SwingConstants.CENTER);
		tableScroller_ref.setBorder(new TitledBorder("Verbindungs-Parameter"));
		tableScroller_ref.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		tableScroller_ref.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(tableScroller_ref);
		
		Box setADBox_ref = new Box(BoxLayout.X_AXIS);
		ImageIcon deleteIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/deleteIcon.png"));
		ImageIcon addIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/addIcon.png"));
		ImageIcon setResetIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/resetIcon.png"));
		ImageIcon setEditIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/customIcon.png"));
		JButton addButton_ref = new JButton("Hinzufügen", addIcon_ref);
		addButton_ref.setToolTipText("Fügt einen neuen Eintrag zur Liste hinzu.");
		addButton_ref.addActionListener(new SettingsHostButtonListener());
		JButton editButton_ref = new JButton("Bearbeiten", setEditIcon_ref);
		editButton_ref.setToolTipText("Öffnet den ausgewählten Eintrag zum Bearbeiten.");
		editButton_ref.addActionListener(new SettingsHostButtonListener());
		JButton delButton_ref = new JButton("Entfernen", deleteIcon_ref);
		delButton_ref.setToolTipText("Entfernt den ausgewählten Eintrag aus der Liste");
		delButton_ref.addActionListener(new SettingsHostButtonListener());
		JButton setResetBut_ref = new JButton("Reset", setResetIcon_ref);
		setResetBut_ref.setToolTipText("Setzt die Tabelle zurück.");
		setResetBut_ref.addActionListener(new SettingsHostButtonListener());
		setADBox_ref.add(addButton_ref);
		setADBox_ref.add(Box.createHorizontalStrut(5));
		setADBox_ref.add(editButton_ref);
		setADBox_ref.add(Box.createHorizontalStrut(5));
		setADBox_ref.add(delButton_ref);
		setADBox_ref.add(Box.createHorizontalStrut(5));
		setADBox_ref.add(setResetBut_ref);
		this.add(setADBox_ref);
		this.add(Box.createVerticalStrut(5));
	} //endmethod drawTab
	
	/**
	 *	Liefert eine Referenz auf das gehaltene CustomTableModel <i>hostTableMod_ref</i> zurueck. Dieses beinhaltet
	 *	die in der Tabelle gehaltenen Einstellungen fuer alle Hosts.
	 *
	 *	@return Referenz auf das DefaultTableModel der Tabelle mit Host-Einstellungen.
	 *	@see CustomTableModel
	 */
	DefaultTableModel getHostTabMod() {
		return hostTableMod_ref;
	} //endmethod getHostTabMod
	
	/**
	 *	Generiert die <i>String[][]</i>-Matrix fuer die Tabelle. Die Daten werden ueber
	 *	die Methode <i>getSettings()</i> des Haupt-GUIs bezogen und dann gekuerzt: 
	 *	Die Passwoerter werden nicht uebernommen, da diese fuer den Benutzer
	 *	uninteressant sind und nur Platz in der Tabelle beanspruchen.
	 *
	 *	@return Die Tabellenmatrix.
	 *	@see TinyAdminGUI#getSettings()
	 */
	String[][] generateTableData() {
		String[][] matrixOld_ref = (setGui_ref.getGUI().getSettings())[0];
		String[][] matrixNew_ref = new String[matrixOld_ref.length][7];
		
		for (int i=0; i<matrixNew_ref.length; i++) {
			matrixNew_ref[i][0] = matrixOld_ref[i][0] + "";
			matrixNew_ref[i][1] = matrixOld_ref[i][1] + "";
			matrixNew_ref[i][2] = matrixOld_ref[i][2] + "";
			matrixNew_ref[i][3] = matrixOld_ref[i][3] + "";
			matrixNew_ref[i][4] = matrixOld_ref[i][6] + "";
			matrixNew_ref[i][5] = matrixOld_ref[i][7] + "";
			matrixNew_ref[i][6] = matrixOld_ref[i][8] + "";
		} //endfor
		
		return matrixNew_ref;
	} //endmethod generateTableModel
	
	/**
	 *	Generiert eine neue Tabellenmatrix mit Hilfe der Methode
	 *	<i>generateTableData()<i> und aktuallisiert die Tabelle mit dieser Matrix.
	 */
	void refreshTableData() {
		hostTableMod_ref.setDataVector(generateTableData(), HOST_TABLENAMES);
		hostTableMod_ref.fireTableDataChanged();
	} //endmethod refreshTableData
	
	/**
	 *	<p>Erzeugt einen neuen Eintrag mit Standardwerten und uebergibt ihn an die Methode
	 *	<i>initializeSettings()</i> des Haupt-GUIs.</p>
	 *	<p>Durch Aufruf der Methode <i>refreshTableData()</i> wird im Anschluss die 
	 *	Tabelle aktuallisiert</p>
	 *	<p>Folgende Standardwerte werden dabei genutzt:<ul>
	 *		<li>Hostname="Host 1"</li> 
	 *		<li>IP-Adresse="0.0.0.0"</li> 
	 *		<li>Port="22"</li>
	 *		<li>Bentzername="root"</li> 
	 *		<li>Passwort=""</li> 
	 *		<li>Sudo-Passwort=""</li>
	 *		<li>Keyfile="-----"</li>
	 *		<li>MACAdresse="00:00:00:00:00:00"</li>
	 *		<li>OS="Debian"</li></ul>
	 *	</p>
	 *
	 *	@see #refreshTableData()
	 */
	void resetHostSettings() {
		String[][][] tempA_ref = new String[2][][];
		tempA_ref[0] = new String[1][9];
		tempA_ref[1] = (setGui_ref.getGUI().getSettings())[1];
		tempA_ref[0][0][0] = "Host #1";
		tempA_ref[0][0][1] = "0.0.0.0";
		tempA_ref[0][0][2] = "22";
		tempA_ref[0][0][3] = "root";
		tempA_ref[0][0][4] = "";
		tempA_ref[0][0][5] = "";
		tempA_ref[0][0][6] = "-----";
		tempA_ref[0][0][7] = "00:00:00:00:00:00";
		tempA_ref[0][0][8] = "Debian";
		
		setGui_ref.getGUI().initializeSettings(tempA_ref);
		refreshTableData();
	} //endmethod resetHostSettings
	
	// --- Listener
	/**
	 *	Listener fuer alle Knoepfe der Host-Tab des Einstellungs-GUIs.
	 *	Bietet folgende Moeglichkeiten:
	 *	<ul>
	 *		<li>Hinzufuegen<ul>
	 *			<li>Fuegt der Tabelle einen neuen Eintrag hinzu, in dem er ein <i>AddHostGUI</i>-Objekt
	 *			mit Hilfe der Methode <i>createAddHost()</i> des Haupt-GUIs erzeugt.</li></ul>
	 *		</li>
	 *		<li>Entfernen<ul>
	 *			<li>Entfernt den ausgewaehlten Eintrag aus der Liste.</li></ul>
	 *		</li>
	 *		<li>Reset<ul>
	 *			<li>Ruft die <i>resetHostSettings()</i>-Methode auf.</li></ul>
	 *		</li>
	 *	</ul>
	 *
	 *	@see TinyAdminGUI#createAddHost(int)
	 *	@see #resetHostSettings()
	 *	@see TinyAdminGUI#createAddHost(int)
	 *
	 * 	@version 0.3 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class SettingsHostButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev_ref) {
			if (ev_ref.getActionCommand().equals("Hinzufügen")) {
			    setGui_ref.getGUI().createAddHost(-1);
			} else if (ev_ref.getActionCommand().equals("Bearbeiten")) {
				int selection = hostTable_ref.getSelectedRow();
				if (selection != -1) {
					setGui_ref.getGUI().createAddHost(hostTable_ref.getSelectedRow());
				} else {
					setGui_ref.displayError("Sie haben keinen Eintrag zum Bearbeiten ausgewählt.");
				} //endif
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
					hostTableMod_ref.fireTableDataChanged();
					String[][][] newSettings_ref = new String[2][][];
					newSettings_ref[0] = new String[(setGui_ref.getGUI().getSettings())[0].length - 1][];
					(setGui_ref.getGUI().getSettings())[0][index] = null;
					counter = 0;
					for (int i=0; i<(setGui_ref.getGUI().getSettings())[0].length; i++) {
						if ((setGui_ref.getGUI().getSettings())[0][i] != null && counter < newSettings_ref[0].length) {
							newSettings_ref[0][counter] = (setGui_ref.getGUI().getSettings())[0][i];
							counter++;
						} //endif
					} //endfor
					newSettings_ref[1] = (setGui_ref.getGUI().getSettings())[1];
					setGui_ref.getGUI().initializeSettings(newSettings_ref);
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
	 *	Eigenes Tabellenmodell: Setzt alle Zellen auf nicht-editierbar.
	 *
	 * 	@version 0.3 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	class CustomTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 100110981666322347L;
		
		public CustomTableModel(Object[][] werte_ref, Object[] bezeichner_ref) {
			super(werte_ref, bezeichner_ref);
		} //endconstructor
		
		public boolean isCellEditable(int row, int column) {
				return false;
		} //endmethod isCellEditable
	} //endclass CustomTableModel
	
} //endclass SettingsTabHosts