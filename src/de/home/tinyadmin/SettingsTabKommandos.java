package de.home.tinyadmin;

// --- Importe
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
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
--	CLASS: SettingsTabKommandos 													--
-- 																					--
--------------------------------------------------------------------------------------
-- 																					--
--	PROJECT: TinyAdmin 																--
-- 																					--
--------------------------------------------------------------------------------------
-- 																					--
--	SYSTEM ENVIRONMENT 																--
--	OS			Ubuntu 11.04 (Linux 2.6.38)											--
--	SOFTWARE 	JDK 1.6 															--
-- 																					--
--------------------------------------------------------------------------------------
------------------------------------------------------------------------------------*/

/**
 *	<p>Stellt das Kommando-Einstellungs-Tab des Einstellungs-GUIs bereit.
 *	Hier kann der Benutzer eigene Kommandos erstellen und deren, fuer jedes Betriebssystem
 *	individuellen Befehle; editieren, reseten und speichern.</p>
 *	<p>Das Objekt selbst erbt von einem JPanel und wird mit einer Referenz auf das EinstellungsGUI 
 *	<i>TinyAdminSettingsGUI</i> erzeugt. Ab da baut sich (das JPanel) dann selbststaendig durch Aufruf
 *	der <i>drawTab()</i>-Methode zusammen, aufgerufen durch den Konstruktor.</p>
 *	<p>Diese Klasse enthaelt alle fuer dieses Tab noetigen Listener und GUI-Komponenten, 
 *	sowie Methoden.</p>
 *
 * 	@version 0.3 von 06.2011
 *
 * 	@author Tobias Burkard
 */
class SettingsTabKommandos extends JPanel {
	// --- Attribute
	private TinyAdminSettingsGUI setGui_ref;	// Referenz auf das Einstellungs-GUI
	private JTable custTable_ref;	// Fasst die Kommandos der einzelnen Betriebssysteme fuer den momentan selektierten Eintrag
	private DefaultTableModel custTableMod_ref;	// Referenz auf das TableModel der Tabelle, um dieses bei Bedarf zu 
												// manipulieren, oder abzufragen
	private JTextField custNameTextF_ref;	// Fasst den vom Benutzer eingegebenen Namen fuer das selektierte Kommando
	private JComboBox cComBox_ref;	// Auswahl der vom Benutzer erstellten Kommandos
	
	// --- Klassenvariablen
	static final long serialVersionUID = 321270999911113371L; // Eigene serialVersionUID
	
	// --- Konstruktoren
	/**
	 *	<p>Setzt bei einer Erzeugung des Objekts die uebergebene Referenz des Einstellungs-GUIs
	 *	auf die Objektvariable <i>setGui_ref</i> und erzeugt ein neues, leeres Tabellen-Modell.</p>
	 *	<p>Im Anschluss wird die <i>drawTab()</i>-Methode aufgerufen, welche das Objekt (das JPanel)
	 *	mit Objekten und Werten fuellt, also es erstellt.</p>
	 *
	 *	@see #drawTab()
	 */
	SettingsTabKommandos(TinyAdminSettingsGUI setGui_ref) {
		this.setGui_ref = setGui_ref;
		custTableMod_ref = new CustomTableModel(null, new String[]{"OS", "Kommando"});
		drawTab();
	} //endconstructor
	
	// --- Methoden
	/**
	 *	<p>Diese Methode baut das JPanel (also das Objekt selbst, da es von JPanel erbt)
	 *	zusammen und fuellt es mit Werten. Dieses fasst die bereits vom Benutzer gespeicherten, eigenen Kommandos 
	 *	und erlaubt das Erstellen neuer oder Loeschen alter.</p>
	 *	<p>Es existiert nur ein einziger Listener fuer Buttons, der <i>SettingsCommandButtonListener</i>. 
	 *	Die Auswahl-ComboBox fuer die Kommandos bekommt ihren eigenen <i>ComboBoxListener</i>.</p>
	 *	<p>Die Tabelle erhaelt fuer die erste Spalte, welche die Namen der Betriebssysteme fasst und nicht 
	 *	veraendert werden darf, einen eigenen Renderer <i>CustomTableRenderer</i>. Zudem bekommt die gesamte
	 *	Tabelle ein eigenes DefaultTableModel <i>CustomTableModel</i>.</p>
	 *
	 *	@see SettingsCommandButtonListener
	 *	@see ComboBoxListener
	 *	@see CustomTableRenderer
	 *	@see CustomTableModel
	 */
	 private void drawTab() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		Box custInfoBoxWrapper_ref = new Box(BoxLayout.X_AXIS);
		Box custInfoBox_ref = new Box(BoxLayout.Y_AXIS);
		custInfoBox_ref.setBorder(new TitledBorder("Hinweis"));
		custInfoBox_ref.add(Box.createVerticalStrut(5));
		custInfoBox_ref.add(new JLabel("Hier können Sie eigene Kommandos mit einem Befehl für jedes Betriebssystem definieren."));
		custInfoBox_ref.add(new JLabel("Die Kommandos können dann über das Hauptinterface ausgewählt und ausgeführt werden."));
		custInfoBox_ref.add(Box.createVerticalStrut(10));
		custInfoBox_ref.add(new JLabel("Sie müssen nicht für jedes Betriebssystem ein Kommando definieren, für betroffene"));
		custInfoBox_ref.add(new JLabel("Einträge wird jedoch dann unter Umständen kein Befehl ausgeführt."));
		custInfoBox_ref.add(Box.createVerticalStrut(5));
		custInfoBoxWrapper_ref.add(custInfoBox_ref);
		custInfoBoxWrapper_ref.add(Box.createHorizontalStrut(10));
		ImageIcon commandIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/customFrameIcon.png"));
		custInfoBoxWrapper_ref.add(new JLabel(commandIcon_ref));
		custInfoBoxWrapper_ref.add(Box.createHorizontalStrut(10));
		this.add(custInfoBoxWrapper_ref);
		this.add(Box.createVerticalStrut(25));
		
		Box comSelBox_ref = new Box(BoxLayout.Y_AXIS);
		comSelBox_ref.setBorder(new TitledBorder("Kommandos"));
		Box comSelBoxSub1_ref = new Box(BoxLayout.X_AXIS);
		comSelBoxSub1_ref.add(new JLabel("Kommandos:"));
		cComBox_ref = new JComboBox();
		for (int i=0; i<(setGui_ref.getGUI().getSettings())[1].length; i++) {
			cComBox_ref.addItem((setGui_ref.getGUI().getSettings())[1][i][0]);
		} //endfor
		cComBox_ref.addItemListener(new ComboBoxListener());
		cComBox_ref.setPreferredSize(new Dimension(100,30));
		comSelBoxSub1_ref.add(Box.createHorizontalStrut(3));
		comSelBoxSub1_ref.add(cComBox_ref);
		comSelBoxSub1_ref.add(Box.createHorizontalStrut(450));
		comSelBoxSub1_ref.add(Box.createGlue());
		Box comSelBoxSub2_ref = new Box(BoxLayout.X_AXIS);
		ImageIcon custAddIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/addIcon.png"));
		ImageIcon custDelIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/deleteIcon.png"));
		JButton custAddButton_ref = new JButton("Neu", custAddIcon_ref);
		custAddButton_ref.setToolTipText("Fügt ein neues Kommando hinzu.");
		custAddButton_ref.addActionListener(new SettingsCommandButtonListener());
		JButton custDelButton_ref = new JButton("Löschen", custDelIcon_ref);
		custDelButton_ref.setToolTipText("Löscht das gewählte Kommando.");
		custDelButton_ref.addActionListener(new SettingsCommandButtonListener());
		comSelBoxSub2_ref.add(custAddButton_ref);
		comSelBoxSub2_ref.add(Box.createHorizontalStrut(5));
		comSelBoxSub2_ref.add(custDelButton_ref);
		comSelBoxSub2_ref.add(Box.createGlue());
		comSelBox_ref.add(Box.createVerticalStrut(5));
		comSelBox_ref.add(comSelBoxSub1_ref);
		comSelBox_ref.add(Box.createVerticalStrut(10));
		comSelBox_ref.add(comSelBoxSub2_ref);
		comSelBox_ref.add(Box.createVerticalStrut(5));
		this.add(comSelBox_ref);
		this.add(Box.createVerticalStrut(15));
		
		Box selectedItemBox_ref = new Box(BoxLayout.Y_AXIS);
		selectedItemBox_ref.setBorder(new TitledBorder("Ausgewähltes Kommando"));
		selectedItemBox_ref.add(Box.createVerticalStrut(5));
		Box nameBox_ref = new Box(BoxLayout.X_AXIS);
		nameBox_ref.add(new JLabel("Name:"));
		custNameTextF_ref = new JTextField(10);
		nameBox_ref.add(Box.createHorizontalStrut(5));
		nameBox_ref.add(custNameTextF_ref);
		nameBox_ref.add(Box.createHorizontalStrut(600));
		nameBox_ref.add(Box.createGlue());
		selectedItemBox_ref.add(nameBox_ref);
		selectedItemBox_ref.add(Box.createVerticalStrut(10));
		Box custTableBox_ref = new Box(BoxLayout.X_AXIS);
		custTable_ref = new JTable();
		custTable_ref.setPreferredScrollableViewportSize(new Dimension(600,75));
		JScrollPane custTableScroller_ref = new JScrollPane(custTable_ref);
		custTableScroller_ref.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		custTableScroller_ref.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		custTableMod_ref = new CustomTableModel(getSelTabMatrix(), new String[]{"OS", "Kommando"});
		custTable_ref.setModel(custTableMod_ref);
		custTable_ref.setDefaultEditor(new Object().getClass(), new TableEditor());
		refreshCustomTableRenderers();
		custTableBox_ref.add(custTableScroller_ref);
		custTableBox_ref.add(Box.createHorizontalStrut(10));
		selectedItemBox_ref.add(custTableBox_ref);
		Box selItemBoxButtons_ref = new Box(BoxLayout.Y_AXIS);
		ImageIcon custSaveIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/saveIcon.png"));
		ImageIcon custResetIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/resetIcon.png"));
		JButton custSaveButton_ref = new JButton("Speichern", custSaveIcon_ref);
		custSaveButton_ref.setToolTipText("Speichert die Veränderungen am ausgewählten Kommando.");
		custSaveButton_ref.addActionListener(new SettingsCommandButtonListener());
		JButton custResetButton_ref = new JButton("Reset", custResetIcon_ref);
		custResetButton_ref.setToolTipText("<html>Setzt das ausgewählte Kommando auf<br>Standardeinstellungen zurück.</html>");
		custResetButton_ref.addActionListener(new SettingsCommandButtonListener());
		selItemBoxButtons_ref.add(custSaveButton_ref);
		selItemBoxButtons_ref.add(Box.createHorizontalStrut(1));
		selItemBoxButtons_ref.add(custResetButton_ref);
		selItemBoxButtons_ref.add(Box.createVerticalStrut(20));
		custTableBox_ref.add(selItemBoxButtons_ref);
		
		this.add(selectedItemBox_ref);
		this.add(Box.createVerticalStrut(10));
		
		fillSelectedItemBox();
	} //endmethod drawTab
	 
	 /**
	  *	Nimmt den vom Nutzer ueber die JComboBox </i>cComBox_ref</i> gewaehlten Eintrag aus 
	  *	der Hauptmatrix des Haupt-GUIs, erhalten ueber dessen Methode <i>getSettings()</i>,
	  *	und formatiert ihn zusammen mit den von der Methode <i>getOSValues()</i> des Haupt-GUIs erhaltenen 
	  *	OS-Namen in eine fuer die Tabelle dieses Tabs geeignete Form.
	  * 
	  * @return DataVector fuer das Tabellenmodell der in diesem Objekt gehaltenen Tabelle.
	  *	@see TinyAdminGUI#getSettings()
	  *	@see TinyAdminGUI#getOSValues()
	  */
	 private String[][] getSelTabMatrix() {
			String[] osValues_ref = setGui_ref.getGUI().getOSValues();
			String[][] custTabMatrix_ref = new String[osValues_ref.length][2];
			int selectedIndex = cComBox_ref.getSelectedIndex();
			if (selectedIndex < 0) {
				cComBox_ref.setSelectedIndex(0);
				selectedIndex = 0;
			} //endif
			for (int i=0; i<custTabMatrix_ref.length; i++) {
				custTabMatrix_ref[i][0] = osValues_ref[i];
				custTabMatrix_ref[i][1] = (setGui_ref.getGUI().getSettings())[1][selectedIndex][i+1];
			} //endfor
			
			return custTabMatrix_ref;
		} //endmethod getSelTabMatrix
	 
	 /**
	  *	Erneuert die Tabelle mit einem frischen <i>CustomTableRenderer</i>.
	  */
	 private void refreshCustomTableRenderers() {
			custTable_ref.setDefaultRenderer(Object.class, new CustomTableRenderer());
	 } //endmethod refreshCustomTableRenderers
	 
	 /**
	  *	<p>Erneuert das TextFeld <i>custNameTextF_ref</i> und das DefaultTableModel der Tabelle
	  *	<i>custTable_ref</i> mit Werten, entsprechend dem durch die JComboBox <i>cComBox_ref</i>
	  *	vom Benutzer ausgewaehlten Kommando.</p>
	  *	<p>Die Werte werden aus der durch das Haupt-GUI erhaltenen Matrix ausgelesen, welche ueber 
	  *	dessen Methode <i>getSettings()</i> abgerufen werden kann.</p>
	  * <p>Um die Werte fuer die Tabelle korrekt zu formatieren, wird die Methode <i>getSelTabMatrix()</i> genutzt.</p>
	  *
	  *	@see #getSelTabMatrix()
	  *	@see TinyAdminGUI#getSettings()
	  */
	 private void fillSelectedItemBox() {
		int selectedIndex = cComBox_ref.getSelectedIndex();
		if (selectedIndex < 0) {
			cComBox_ref.setSelectedIndex(0);
			selectedIndex = 0;
		} //endif
		custNameTextF_ref.setText((setGui_ref.getGUI().getSettings())[1][selectedIndex][0]);
		custTableMod_ref.setDataVector(getSelTabMatrix(), new String[]{"OS", "Kommando"});
		custTable_ref.getColumnModel().getColumn(0).setPreferredWidth(60);
		custTable_ref.getColumnModel().getColumn(1).setPreferredWidth(540);
		custTableMod_ref.fireTableDataChanged();
	 } //endmethod fillSelectedItemBox
	 
	 /**
	  *	<p>Bei Veraenderungen an den Kommandonamen, oder Hinzufuegen/Entfernen von Kommandos, 
	  *	kann mit dieser Methode die JComboBox <i>cComBox_ref</i> erneuert (neu
	  *	befuellt) werden.</p>
	  *	<p>Zugriff auf die Kommandonamen erhaelt diese Methode ueber die <i>getSettings()</i>-
	  *	Methode des Haupt-GUIs.</p>
	  *
	  *	@see TinyAdminGUI#getSettings()
	  */
	 private void updateCommandBox() {
			cComBox_ref.removeAllItems();
			for (int i=0; i<(setGui_ref.getGUI().getSettings())[1].length; i++) {
				cComBox_ref.addItem((setGui_ref.getGUI().getSettings())[1][i][0]);
			} //endfor
	 } //endmethod updateCommandBox
	 
	 // --- Listener
	 /**
	  *	<p>ItemListener fuer die JComboBox zur Auswahl der Kommandos. Sobald der Benutzer
	  *	ein Element aus der ComboBox auswaehlt, wird automatisch das TextFeld und die
	  *	Tabelle zum Editieren des Kommandos mit den entsprechenden Werten befuellt.</p>
	  *	<p>Hierzu wird die Methode <i>fillSelectedItemBox()</i> aufgerufen.</p>
	  *
	  *	@see #fillSelectedItemBox()
	  *
	  * @version 0.3 von 06.2011
	  *
	  * @author Tobias Burkard
	  */
	 private class ComboBoxListener implements ItemListener {
			public void itemStateChanged(ItemEvent ev_ref) {
		        if (ev_ref.getStateChange() == ItemEvent.SELECTED) {
		           	fillSelectedItemBox();
		        } //endif
			} //endmethod actionPerformed
	 } //endclass ComboBoxListener
	 
	 /**
	  *	Listener fuer alle Knoepfe des Kommando-Tabs.
	  *	Bietet folgende Moeglichkeiten:
	  *	<ul>
	  *		<li>Neu<ul>
	  *				<li>Erstellt ein neues Kommando und laedt es in die Editor-Tabelle und das Textfeld.</li></ul>
	  *		</li>
	  *		<li>Loeschen<ul>
	  *				<li>Loescht das ausgewaehlte Kommando aus der List der gespeicherten Befehle.</li></ul>
	  *		</li>
	  *		<li>Uebernehmen<ul>
	  *				<li>Speichert die Veraenderungen am ausgewaehlten Kommando.</li></ul>
	  *		</li>
	  *		<li>Reset<ul>
	  *				<li>Loescht alle Felder der Editor-Tabelle und das Textfeld, fuer den ausgewaehlten Befehl.</li></ul>
	  *		</li>
	  *	</ul>
	  *
	  * 	@version 0.3 von 06.2011
	  *
	  * 	@author Tobias Burkard
	  */
	 private class SettingsCommandButtonListener implements ActionListener {
		 public void actionPerformed(ActionEvent ev_ref) {
				if (ev_ref.getActionCommand().equals("Neu")) {
					String[][][] newMatrix_ref = new String[2][][];
				    newMatrix_ref[0] = (setGui_ref.getGUI().getSettings())[0];
				    
				    String[] tempData_ref = new String[(setGui_ref.getGUI().getSettings())[1][0].length];
				    tempData_ref[0] = "Kommando #" + ((setGui_ref.getGUI().getSettings())[1].length+1);
				    for (int i=1; i<tempData_ref.length; i++) {
				    	tempData_ref[i] = "";
				    } //endfor
				    newMatrix_ref[1] = new String[(setGui_ref.getGUI().getSettings())[1].length + 1][];
				    
				    for (int i=0; i<(setGui_ref.getGUI().getSettings())[1].length; i++) {
				    	newMatrix_ref[1][i] = (setGui_ref.getGUI().getSettings())[1][i];
				    } //endfor
				    newMatrix_ref[1][newMatrix_ref[1].length-1] = tempData_ref;
				    setGui_ref.getGUI().initializeSettings(newMatrix_ref);
				    updateCommandBox();
				    cComBox_ref.setSelectedIndex(cComBox_ref.getItemCount() - 1);
				} else if (ev_ref.getActionCommand().equals("Löschen")) {
					if ((setGui_ref.getGUI().getSettings())[1].length > 1) {
						String[][][] newMatrix_ref = new String[2][][];
					    newMatrix_ref[0] = (setGui_ref.getGUI().getSettings())[0];
					    int toDelete = cComBox_ref.getSelectedIndex();
					    String[][] ccommandsOld_ref = (setGui_ref.getGUI().getSettings())[1];
					    ccommandsOld_ref[toDelete] = null;
					    newMatrix_ref[1] = new String[(setGui_ref.getGUI().getSettings())[1].length - 1][];
					    int count = 0;
					    for (int i=0; i<ccommandsOld_ref.length; i++) {
					    	if (ccommandsOld_ref[i] != null) {
					    		newMatrix_ref[1][count] = ccommandsOld_ref[i];
					    		count++;
					    	} //endif
					    } //endfor
					    setGui_ref.getGUI().initializeSettings(newMatrix_ref);
					    updateCommandBox();
					    int newIndex = 0;
					    if (toDelete != 0) {
					    	newIndex = toDelete - 1;
					    } //endif
					    cComBox_ref.setSelectedIndex(newIndex);
					} else {
						setGui_ref.displayError("Sie können nicht ihr einziges Kommando löschen.");
					} //endif
				} else if (ev_ref.getActionCommand().equals("Speichern")) {
					if (custNameTextF_ref.getText().length() > 15) {
						setGui_ref.displayError("Der Names Ihres Kommandos darf nicht größer als 15 Zeichen sein.");
					} else {
						int selectedIndex = cComBox_ref.getSelectedIndex();
					    for (int i=1; i<(setGui_ref.getGUI().getSettings())[1][selectedIndex].length; i++) {
					    	(setGui_ref.getGUI().getSettings())[1][selectedIndex][i] = (String)custTableMod_ref.getValueAt((i-1),1);
					    } //endfor
					    (setGui_ref.getGUI().getSettings())[1][selectedIndex][0] = custNameTextF_ref.getText();
					    updateCommandBox();
					    cComBox_ref.setSelectedIndex(selectedIndex);
					    setGui_ref.getGUI().initializeSettings(setGui_ref.getGUI().getSettings());
					} //endif
				} else if (ev_ref.getActionCommand().equals("Reset")) {
					String[] osValues_ref = setGui_ref.getGUI().getOSValues();
					String[][] commandMatrix_ref = new String[osValues_ref.length][2];
					for (int i=0; i<commandMatrix_ref.length; i++) {
						commandMatrix_ref[i][0] = osValues_ref[i];
						commandMatrix_ref[i][1] = "";
					} //endfor
					
					custNameTextF_ref.setText("Kommando #" + (cComBox_ref.getSelectedIndex() + 1));
					custTableMod_ref.setDataVector(commandMatrix_ref, new String[]{"OS", "Kommando"});
					custTable_ref.getColumnModel().getColumn(0).setPreferredWidth(60);
					custTable_ref.getColumnModel().getColumn(1).setPreferredWidth(540);
					custTableMod_ref.fireTableDataChanged();
				} //endif
		 } //endmethod actionPerformed
	} //endclass SettingsCommandButtonListener
	 
	// --- Innere Klassen
	/**
	 *	Eigenes Tabellenmodell: Setzt alle Zellen der ersten Spalte auf nicht-editierbar.
	 *
	 * 	@version 0.3 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	class CustomTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 100330981635722317L;
		
		public CustomTableModel(Object[][] werte_ref, Object[] bezeichner_ref) {
			super(werte_ref, bezeichner_ref);
		} //endconstructor
		
		public boolean isCellEditable(int row, int column) {
			if (column > 0) {
				return true;
			} else {
				return false;
			} //endif
		} //endmethod isCellEditable
	} //endclass CustomTableModel
	
	/**
	 *	<p>Eigener Tabellen-Renderer: Setzt eine breite Schrift auf alle Zellen
	 *	der ersten Spalte und deaktiviert diese, so dass sie wirklich
	 *	fuer jeglichen Zugriff gesperrt sind.</p>
	 *	<p>Alle anderen Zellen bleiben fuer Zugriffe offen.</p>
	 *
	 * 	@version 0.3 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	class CustomTableRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 100221981647722317L;
		
	    public Component getTableCellRendererComponent(JTable table,
	        Object value, boolean isSelected, boolean hasFocus,
	        int row, int column) {

	        @SuppressWarnings("unused")
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
			Font font_ref = new Font (this.getFont().getName(), Font.BOLD, this.getFont().getSize());
			
	        if (column < 1) {
	            this.setFont(font_ref);
	            this.setEnabled(false);
	            this.setFocusable(false);
	        } else {
	        	this.setEnabled(true);
	        	this.setFocusable(true);
	        } //endif
	        	
	        return this;
	    } //endmethod getTableCellRendererComponent
	} //endclass CustomTableCellRenderer
	
	/**
	 * 	<p>Eigener <i>DefaultCellEditor</i>: Sorgt dafuer, dass der gesamte Zellinhalt beim 
	 * 	Sprung in eine neue Zelle selektiert wird.</p>
	 *	<p>Dies ermoeglicht das sofortige Ueberschreiben beim Sprung in eine neue Zelle.
	 *	Beim Standardverhalten wuerde der neue Text zum bereits existierenden hinzuaddiert.</p>
	 *	<p>Zusaetzlich wird die Zelle wieder farblich neu formatiert. Der Editor sollte also nicht
	 *	verwendet werden, wenn das durch <i>LookAndFeelHelfer</i> gesetzte Look&Feel
	 *	fuer die Tabelle beibehalten werden soll.</p>
	 *
	 *	@see LookAndFeelHelfer
	 *
	 * 	@version 0.3 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	class TableEditor extends DefaultCellEditor {
		static final long serialVersionUID = 199880983615777317L;
		JTextField tf_ref;

		TableEditor() {
			super(new JTextField());
		} //endconstructor

		public Component getTableCellEditorComponent(JTable table_ref, Object value_ref, boolean isSelected, int row, int column) {
			Component c_ref = super.getTableCellEditorComponent(table_ref, value_ref, isSelected, row, column);
			
			if (c_ref instanceof JTextField) {
				tf_ref = ((JTextField)c_ref);
				tf_ref.selectAll();
				tf_ref.setBorder(BorderFactory.createLineBorder(new Color(99, 130, 191), 2));
				tf_ref.setBackground(Color.WHITE);
				if (isSelected) {
					tf_ref.setBackground(new Color(184,  207, 229));
				} else {
					tf_ref.setBackground(Color.WHITE);
				} //endif
				c_ref = tf_ref;
			} //endif

			return c_ref;
		} //endmethod getTableCellEditor
	} //endclass TableEditor
	
} //endclass SettingsTabKommandos