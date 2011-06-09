package de.home.tinyadmin;

// --- Importe
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

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
--	CLASS: AddHostGUI 														--
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
 *	<p>Stellt das GUI bereit, mit dem neue Hosteintraege hinzugefuegt werden koennen.
 *	Die einzelnen GUI-Komponenten (JTextFields etc.) werden in der Matrix <i>guiComponents_ref</i>
 *	gespeichert.</p>
 *	<p>Die Buttons erhalten einen <i>ButtonListener</i>.</p>
 *	
 *	@see ButtonListener
 *
 * 	@version 0.3 von 06.2011
 *
 * 	@author Tobias Burkard
 */
class AddHostGUI {
	// --- Attribute
	private TinyAdminGUI mainGUI_ref;	// Referenz auf das Haupt-GUI
	private JFrame addHostFrame_ref;	// Fenster (JFrame) des AddHost-GUIs
	private Object[] guiComponents_ref;	// Matrix der GUI-Komponenten (z.B. JTextField) fuer jeden fieldName
	private String[] entry_ref;			// Liste der Parameter fuer den gewaehlten Hosteintrag
	private int pos;					// Position des Hosteintrages in der Matrix. -1, falles es ein neuer Eintrag ist.
	private JFileChooser fc_ref;		// Dateiauswahlmenue
	
	// --- Finale Attribute
	private final String[] fieldNames_ref = {"Name:", "IP-Adresse/Hostname:", "SSH-Port:", "Benutzername:", "Passwort:", 
											"Sudo-Passwort:", "KeyFile:", "MAC-Adresse:", "Betriebssystem:"}; 
											// Namen fuer die JLabels
	
	// --- Konstruktoren
	/**	
	*	Setzt die Referenz auf das uebergebene <i>TinyAdminGUI</i>-Objekt <i>mainGui_ref</i>,
	*	um Zugang zu den Vorhandenen Einstellungen und dessen Methode zu bekommen und
	*	initialisiert alle noetigen Variablen und Objekte.
	*/
	AddHostGUI(int pos, TinyAdminGUI mainGui_ref) {
		this.mainGUI_ref = mainGui_ref;
		entry_ref = new String[fieldNames_ref.length];
		this.pos = pos;
		guiComponents_ref = new Object[fieldNames_ref.length];
		fc_ref = new JFileChooser();
		fc_ref.setFileHidingEnabled(false);
	} //endconstructor
	
	// --- Methoden
	/**
	 *	<p>Zeichnet das GUI zum Hinzufuegen/Bearbeiten eines Hosteintrages.
	 *	Es wird ein Liste aus Labels (Namen des Parameters, z.B. "IP-Adresse") 
	 *	und der entsprechenden GUI-Komponente erstellt, in die der Benutzer die Einstellungen
	 *	eintragen kann.</p>
	 *	<p>Die GUI-Komponenten werden in der Matrix <i>guiComponents_ref</i> gespeichert.
	 *	Die Textfelder werden mit der am Anfang im Konstruktor uebergebenen Matrix gefuellt.</p>
	 */
	void drawGUI() {
		createFieldValues();
		if (pos != -1) {
			addHostFrame_ref = new JFrame("Hosteintrag bearbeiten");
		} else { 
			addHostFrame_ref = new JFrame("Neuen Hosteintrag erstellen");
		} //endif
		addHostFrame_ref.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addHostFrame_ref.setAlwaysOnTop(true);
		ImageIcon addHostIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/addHostFrameIcon.png"));
		addHostFrame_ref.setIconImage(addHostIcon_ref.getImage());
		Box backgroundBox_ref = new Box(BoxLayout.Y_AXIS);
		backgroundBox_ref.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		Box infoBox_ref = new Box(BoxLayout.Y_AXIS);
		infoBox_ref.setBorder(new TitledBorder("Hinweis"));
		infoBox_ref.add(Box.createVerticalStrut(5));
		if (pos != -1) {
			infoBox_ref.add(new JLabel("Hier können Sie den gewählten Hosteintrag bearbeiten."));
		} else {
			infoBox_ref.add(new JLabel("Hier können Sie einen neuen Hosteintrag erstellen."));
		} //endif
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Falls Sie ein KeyFile auswählen, wird dieses mit dem angegebenen"));
		infoBox_ref.add(new JLabel("Passwort verwendet."));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Wenn Sie kein Passwort benötigen, lassen Sie das Feld einfach frei."));
		infoBox_ref.add(new JLabel(""));
		infoBox_ref.add(Box.createVerticalStrut(5));
		Box infoBoxWrapper_ref = new Box(BoxLayout.X_AXIS);
		infoBoxWrapper_ref.add(infoBox_ref);
		infoBoxWrapper_ref.add(Box.createHorizontalStrut(10));
		infoBoxWrapper_ref.add(new JLabel(addHostIcon_ref));
		infoBoxWrapper_ref.add(Box.createHorizontalStrut(10));
		backgroundBox_ref.add(infoBoxWrapper_ref);
		backgroundBox_ref.add(Box.createVerticalStrut(25));
		
		JPanel paramPan_ref = new JPanel();
		GridLayout paramLayout_ref = new GridLayout(fieldNames_ref.length, 2);
		paramPan_ref.setLayout(paramLayout_ref);
		paramPan_ref.setBorder(new TitledBorder("Host-Parameter"));
		backgroundBox_ref.add(paramPan_ref);
		for (int i=0; i<fieldNames_ref.length; i++) {
			paramPan_ref.add(new JLabel(fieldNames_ref[i]));
			if (i!=4 && i!=5 && i!=6 && i!=8) {
				guiComponents_ref[i] = new JTextField(10);
				((JTextField)guiComponents_ref[i]).setText(entry_ref[i]);
				paramPan_ref.add((JTextField)guiComponents_ref[i]);
			} else if (i==4 || i==5) {
				guiComponents_ref[i] = new JPasswordField(10);
				((JPasswordField)guiComponents_ref[i]).setText(entry_ref[i]);
				paramPan_ref.add((JPasswordField)guiComponents_ref[i]);
			} else if (i==6) {
				Box chooserBox_ref = new Box(BoxLayout.X_AXIS);
				guiComponents_ref[i] = new JTextField(5);
				Font chooserFont_ref = new Font(((JTextField)guiComponents_ref[i]).getFont().getName(), 
						Font.ITALIC, ((JTextField)guiComponents_ref[i]).getFont().getSize());  
				((JTextField)guiComponents_ref[i]).setFont(chooserFont_ref);
				((JTextField)guiComponents_ref[i]).setEditable(false);
				((JTextField)guiComponents_ref[i]).setEnabled(false);
				((JTextField)guiComponents_ref[i]).setText(entry_ref[i]);
				chooserBox_ref.add((JTextField)guiComponents_ref[i]);
				ImageIcon chooseIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/chooseIcon.png"));
				JButton chooseBut_ref = new JButton(chooseIcon_ref);
				chooseBut_ref.setToolTipText("Hier können Sie ein SSH-Keyfile auswählen.");
				chooseBut_ref.addActionListener(new ButtonListener());
				chooseBut_ref.setActionCommand("chooser");
				chooserBox_ref.add(chooseBut_ref);
				paramPan_ref.add(chooserBox_ref);
			} else if (i==8) {
				guiComponents_ref[i] = new JComboBox();
				paramPan_ref.add((JComboBox)guiComponents_ref[i]);
				for (int j=0; j<mainGUI_ref.getOSValues().length; j++) {
					((JComboBox)guiComponents_ref[i]).addItem((mainGUI_ref.getOSValues())[j]);
				} //endfor
				((JComboBox)guiComponents_ref[i]).setSelectedItem(entry_ref[i]);
			} //endif
		} //endfor
		backgroundBox_ref.add(Box.createVerticalStrut(25));
		
		Box buttonBox_ref = new Box(BoxLayout.X_AXIS);
		ImageIcon savButIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/saveIcon.png"));
		ImageIcon cancButIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/cancelIcon.png"));
		JButton savBut_ref;
		JButton cancBut_ref = new JButton("Schließen", cancButIcon_ref);
		if (pos != -1) {
			savBut_ref = new JButton("Übernehmen", savButIcon_ref);
			savBut_ref.setToolTipText("Übernimmt die Änderungen am ausgewählten Eintrag.");
			cancBut_ref.setToolTipText("Schließt das Fenster zum Verändern der Einstellungen.");
		} else {
			savBut_ref = new JButton("Speichern", savButIcon_ref);
			savBut_ref.setToolTipText("Speichert den neu erstellten Eintrag.");
			cancBut_ref.setToolTipText("Schließt das Fenster zum Erstellen eines neuen Eintrags.");
		} //endif
		savBut_ref.setActionCommand("save");
		savBut_ref.addActionListener(new ButtonListener());
		cancBut_ref.addActionListener(new ButtonListener());
		buttonBox_ref.add(savBut_ref);
		buttonBox_ref.add(Box.createHorizontalStrut(10));
		buttonBox_ref.add(cancBut_ref);
		backgroundBox_ref.add(buttonBox_ref);
		
		addHostFrame_ref.getContentPane().add(backgroundBox_ref);
		addHostFrame_ref.pack();
		addHostFrame_ref.setResizable(false);
		addHostFrame_ref.setVisible(true);
	} //endmethod drawGUI
	
	/**
	 *	<p>Fuellt die GUI-Komponenten mit Werten, in Abhaengigkeit von der am Anfang
	 *	durch den Konstruktor gesetzten Variable <i>int pos</i>:</p>
	 *	<ul>
	 *		<li>pos = -1:<ul>
	 *	 		<li>Es wird ein leerer, neuer Eintrag erstellt.</li></ul>
	 *		</li>
	 * 		<li>pos > -1:<ul>
	 * 			<li>Pos dient als index fuer die im Haupt-GUI gespeicherte 
	 * 			Einstellungs-Matrix <i>settings_ref</i> und das GUI wird mit dem
	 * 			entsprechenden Eintrag aus der Matrix gefuellt. Die Matrix kann ueber
	 * 			die Methode <i>getSettings()</i> des Haupt-GUIs bezogen werden.</li></ul>
	 * 		</li>
	 * 	</ul>
	 * 
	 * 	@see TinyAdminGUI#getSettings()
	 */
	private void createFieldValues() {
		if (pos == -1) {
			entry_ref[0] = "Host #" + ((mainGUI_ref.getSettings())[0].length + 1);
			entry_ref[1] = "0.0.0.0";
			entry_ref[2] = "22";
			entry_ref[3] = "root";
			entry_ref[4] = "";
			entry_ref[5] = "";
			entry_ref[6] = "Wählen Sie ein Keyfile aus.";
			entry_ref[7] = "00:00:00:00:00:00";
			entry_ref[8] = "Debian";
		} else {
			entry_ref = (mainGUI_ref.getSettings())[0][pos];
			if (entry_ref[6].equals("-----")) {
				entry_ref[6] = "Wählen Sie ein Keyfile aus.";
			}
		} //endif
	} //endmethod createFieldValues
	
	/**
	 *	Zeigt den uebergebenen String als Text einer Fehlermeldung mit Hilfe 
	 *	der <i>JOptionPane</i> an.
	 * 
	 * @param message_ref Der Fehlertext.
	 */
	void displayError(String message_ref) {
		JOptionPane.showMessageDialog(addHostFrame_ref, message_ref, "Fehler", JOptionPane.ERROR_MESSAGE);
	} //endmethod displayError
	
	/**
	 * 	Zeigt eine Informationsnachricht mit Hilfe der <i>JOptionPane</i> an.
	 * 
	 * 	@param message_ref Die Meldung, welche angezeigt werden soll.
	 */
	private void displayMsg(String message_ref) {
		JOptionPane.showMessageDialog(addHostFrame_ref, message_ref, "Hinweis", JOptionPane.INFORMATION_MESSAGE);
	} //endmethod displayMsg
	
	/**
	 *	Liefert eine Referenz auf das Fenster (den <i>JFrame</i>) des AddHost-GUIs zurueck.
	 *	Dies ist nuetzlich, um ihn z.B. von ausserhalb zu schliessen.
	 *
	 *	@return Referenz auf das Fenster des AddHost-GUIs.
	 */
	JFrame getFrame() {
		return addHostFrame_ref;
	} //endmethod getFrame
	
	// --- Listener
	/**
	 *	<p>Listener fuer alle Buttons dieses GUIs, bietet folgende Moeglichkeiten:</p>
	 *	<ul>
	 *		<li>chooser:<ul>
	 *			<li>Oeffnet den Dateiauswahl-Dialog mit Hilfe des JFileChoosers
	 *			<i>fc_ref</i></li></ul>
	 *		</li>
	 *		<li>Schliessen:<ul>
	 *			<li>Ruft die <i>dispose()</i>-Methode auf dem <i>JFrame</i> dieses GUIs auf
	 *				und schliesst ihn so.</li></ul>
	 *		</li>
	 *		<li>save:</ul>
	 *			<li>Speichert die vom Benutzer getaetigten Einstellungen, in Abhängigkeit
	 *			der Variablen <i>int pos</i>, entweder als neuen Eintrag, oder ueberschreibt
	 *			den Vorhandenen.</li></ul>
	 *		</li>
	 *	</ul>
	 *	<p>Zusaetzlich werden auch alle getaetigten Einstellungen auf Gueltigkeit
	 *	ueberprueft: Sind alle oder Teile davon inkorrekt, wird eine entsprechende
	 *	(Fehler-)meldung mit der Methode <i>displayMsg()</i> bzw. <i>displayError()</i>
	 *	erzeugt.
	 *	</p>
	 *	<p>Zum letztlichen Speichern dient die Methode <i>initializeSettings()</i> aus dem
	 *	Haupt-GUI.</p>
	 *
	 *	@see #displayMsg(String message_ref)
	 *	@see #displayError(String message_ref)
	 *	@see TinyAdminGUI#initializeSettings(String[][][])
	 * 
	 *	@version 0.3 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev_ref) {
			if (ev_ref.getActionCommand().equals("chooser")) {
				int returnVal = fc_ref.showOpenDialog(addHostFrame_ref);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file_ref = fc_ref.getSelectedFile();
		            try {
			            ((JTextField)guiComponents_ref[6]).setText(file_ref.getCanonicalPath());
			            entry_ref[6] = file_ref.getCanonicalPath();
		            } catch (Exception ex_ref) {
		            	ex_ref.printStackTrace();
		            } //endtry
				} //endif
			} else if (ev_ref.getActionCommand().equals("Schließen")) {
				addHostFrame_ref.dispose();
				addHostFrame_ref = null;
			} else if (ev_ref.getActionCommand().equals("save")) {
				boolean hasInvalidNames = false;
				boolean hasInvalidMACs = false;
				boolean hasNoUser = false;
				boolean hasNoPWDs = false;
				boolean sudoNoPWD = false;
				boolean hasInvalidHosts = false;
				boolean hasInvalidPort = false;
				
				for (int i=0; i<entry_ref.length; i++) {
					if (i!=4 && i!=5 && i!=8) {
						entry_ref[i] = (((JTextField)guiComponents_ref[i]).getText()) + "";
					} else if (i==4 || i==5) {
						entry_ref[i] = (new String(((JPasswordField)guiComponents_ref[i]).getPassword())) + "";
					} else if (i==8) {
						entry_ref[i] = (String)(((JComboBox)guiComponents_ref[i]).getSelectedItem()) + "";
					} //endif
				} //endfor	
				
				if ((((JTextField)guiComponents_ref[0]).getText().toCharArray().length > 12) || (((JTextField)guiComponents_ref[0]).getText().equals(""))) {
			    	hasInvalidNames = true;
		        } //endif
				
				if (!mainGUI_ref.getTestHelfer().isValidPort(((JTextField)guiComponents_ref[2]).getText())) {
						hasInvalidPort = true;
						entry_ref[2] = "22";
				} //endif	
					
				if (((JTextField)guiComponents_ref[3]).getText().equals("")) {
						hasNoUser = true;
				} else {
					if ((!mainGUI_ref.getTestHelfer().isRootLogin(((JTextField)guiComponents_ref[3]).getText())) &&
								(new String(((JPasswordField)guiComponents_ref[5]).getPassword())).equals("")) {
							sudoNoPWD = true;
					} //endif	
				} //endif	
					
				if (new String(((JPasswordField)guiComponents_ref[4]).getPassword()).equals("")) {
					hasNoPWDs = true;
				} //endif
					
				if (!((JTextField)guiComponents_ref[7]).getText().equals("")) {
		        	if (!mainGUI_ref.getTestHelfer().isValidMAC(((JTextField)guiComponents_ref[7]).getText())) {
						hasInvalidMACs = true;
					} //endif
		        } //endif
					
				if (((JTextField)guiComponents_ref[1]).getText().equals("")) {
						System.out.println("Wir haben invalide Hosts!");
						hasInvalidHosts = true;
						entry_ref[1] = "0.0.0.0";
				} //endif
				
				for (int i=0; i<mainGUI_ref.getSettings()[0].length; i++) {
					if (i!=pos && pos!=-1 && (mainGUI_ref.getSettings())[0][i][0].equals( ((JTextField)guiComponents_ref[0]).getText() ) ) {
						hasInvalidNames = true;
					} //endif
				} //endfor
			    
			    String errorMsg_ref = "";
			    String infoMsg_ref = "";
				if (!hasInvalidNames && !hasInvalidMACs && !hasInvalidHosts) {
					if (hasNoUser) {
						infoMsg_ref += "ACHTUNG: Sie haben keinen Benutzernamen angegeben.\n" + 
								"Ihnen werden daher die Funktionen Update, Reboot und Shutdown für diese Hosts\n" + 
								"nicht zur verfügung stehen.\n\n";
					} //endif
					if (hasNoPWDs && ((JTextField)guiComponents_ref[6]).getText().equals("Wählen Sie ein Keyfile aus.")) {
						infoMsg_ref += "HINWEIS: Sie haben kein Passwort angegeben, nutzen aber kein Keyfile.\n" + 
										"Falls auf der Zielmaschine zum Login kein Passwort benötigt wird,\n" +
										"so sollten Sie den Systemadministrator darum bitten, dieses Verhalten aus\n"+
										"Sicherheitsgründen zu ändern, oder ein Keyfile zu benutzen.\n\n";
					} //endif
					if (sudoNoPWD) {
						infoMsg_ref += "HINWEIS: Sie wollen sich als normaler Benutzer anmelden,\n" + 
										"geben aber kein Sudo-Passwort an. Falls das Sudo-Passwort auf dem Fremdrechner wirklich nicht gesetzt ist,\n" +
										"so sollten Sie den Systemadministrator darum bitten, dies aus Sicherheitsgründen zu setzen.\n" +
										"Falls das Passwort nicht leer ist, sie es aber nicht kennen, so werden sie weder Update-, noch\n" +
										"Reboot-, noch Shutdown-Aktionen durchführen können.\n\n";
					} //endif
					if (hasInvalidPort) {
						infoMsg_ref += "HINWEIS: Sie haben keinen oder einen ungültigen Port angegeben,\n verwende stattdessen den Standardport 22.\n\n";
					} //endif
					if (!infoMsg_ref.equals("")) {
						infoMsg_ref += "Die Einstellungen wurden dennoch gespeichert.";
					} //endif
					String[][][] tempData_ref = new String[2][][];
				    tempData_ref[1] = mainGUI_ref.getSettings()[1];
				    
				    if (entry_ref[6].equals("Wählen Sie ein Keyfile aus.")) {
			    		entry_ref[6] = "-----";
			    	} //endif
				    
				    if (pos != -1) {
				    	tempData_ref[0] = mainGUI_ref.getSettings()[0];
				    	tempData_ref[0][pos] = entry_ref;
				    } else {
				    	tempData_ref[0] = new String [(mainGUI_ref.getSettings())[0].length + 1][9];
				    	for (int i=0; i<(mainGUI_ref.getSettings())[0].length; i++) {
					        for (int j=0; j<(mainGUI_ref.getSettings())[0][i].length; j++) {
					            tempData_ref[0][i][j] = mainGUI_ref.getSettings()[0][i][j];
					        } //endfor
					    } //endfor
				    	tempData_ref[0][tempData_ref[0].length-1] = entry_ref;
				    } //endif	
				   
					if (!infoMsg_ref.equals("")) {
						displayMsg(infoMsg_ref);
					} //endif
					mainGUI_ref.initializeSettings(tempData_ref);
					if (mainGUI_ref.getSettingsGUI() != null) {
						mainGUI_ref.getSettingsGUI().getHostTab().refreshTableData();
					} //endif
					addHostFrame_ref.dispose();
					addHostFrame_ref = null;
				} else {
					if (hasInvalidNames) {
						errorMsg_ref += "FEHLER: Die eingegebene Hostbezeichnung ist ungültig.\n" + 
										"Die Bezeichnung muss (kleiner/gleich 12 Zeichen) sein\n" +
										"und darf nicht bereits von einem anderen Eintrag verwendet werden..";
					} //endif
					if (hasInvalidMACs) {
						if (!errorMsg_ref.equals("")) {
							errorMsg_ref += "\n\n";
						} //endif
						errorMsg_ref += "FEHLER: Die eingegebene MAC-Adresse. ist ungültig,\n" +
										"geben Sie bitte entweder keine oder aber eine gültige Adresse an.";
					} //endif
					if (hasInvalidHosts) {
						if (!errorMsg_ref.equals("")) {
							errorMsg_ref += "\n\n";
						} //endif
						errorMsg_ref += "FEHLER: Sie haben keine(n) IP-Adresse/Hostnamen angegeben.";
					} //endif
					displayError(errorMsg_ref);
				} //endif
			} //endif
		} //endmethod actionPerformed
	} //endclass ButtonListener
	
} //endclass AddHostGUI