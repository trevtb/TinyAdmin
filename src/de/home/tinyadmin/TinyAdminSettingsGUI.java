package de.home.tinyadmin;

//--- Importe
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

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
--	CLASS: TinyAdminSettingsGUI 													--
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
*	<p>Dies stellt das Einstellungs-GUI der Anwendung dar und kuemmert sich um die Verwaltung und grafische
*	Darstellung der Host- und Kommandoeinstellungen. Die Hauptmethode bildet <i>drawGUI()</i>, welche das tatsaechliche Zeichnen 
*	uebernimmt.</p> 
*	<p>Zudem hat ein Objekt dieser Klasse Zugriff auf alle Methoden des Haupt-GUIs <i>TinyAdminGUI</i> 
*	ueber eine beim Erzeugen uebergebene Referenz auf dieses und kann sich somit Zugang zu den bereits, falls
*	vorhandenen, gespeicherten Hosteinstellungen verschaffen.</p>
*
* 	@version 0.2 von 06.2011
*
* 	@author Tobias Burkard
*/
class TinyAdminSettingsGUI {
	// --- Attribute
	private JFrame settingsFrame_ref;	// Fenster (JFrame) des Settings-GUI
	private TinyAdminGUI gui_ref;	// Referenz auf das HauptGUI
	private String[][][] settings_ref;	// Matrix mit Einstellungen: die erste Zeile fasst die Hosteinstellungen,
										// die zweite Zeile die Kommando-Einstellungen
	private SettingsTabHosts hostTab_ref;	// Referenz auf das Host-Tab
	private SettingsTabKommandos comTab_ref;	// Referenz auf das Kommando-Tab
	
	// --- Konstruktoren
	/**	
	*	<p>Setzt die uebergebene Referenz auf das Haupt-GUI und verschafft dem Objekt
	*	somit Zugang zu den, falls vorhanden, bereits gespeicherten Einstellungen.</p> 
	*	<p>Anschliessend wird die <i>drawGUI()</i>-Methode aufgerufen um die TabbedPane und
	*	die noetigen Buttons zu erstellen.</p>
	*
	*	@see TinyAdminGUI
	*	@see #drawGUI()
	*/
	TinyAdminSettingsGUI(TinyAdminGUI gui_ref) {
		this.gui_ref = gui_ref;
		this.settings_ref = gui_ref.getSettings();
		drawGUI();
	} //endconstructor
	
	// --- Methoden
	/**
	 *	<p>Zeichnet das Einstellungs-GUI, bestehend aus einer <i>JTabbedPane</i>, welche
	 *	2 Tabs fasst: <i>SettingsTabHosts</i> und <i>SettingsTabKommandos</i>.</p>
	 *	<p>Beide Tab-Objekte sind Unterklassen eines <i>SettingsTab</i>, welches wiederum
	 *	von JPanel erbt. Somit lassen sich beide Objekte nahtlos in eine TabbedPane einfuegen.</p>
	 *	<p>Zusaetzlich werden noch zwei Buttons gezeichnet (Speichern und Schliessen). Beide erhalten
	 *	einen <i>SettingsButtonListener</i>.</p>
	 *
	 *	@see SettingsTabHosts
	 *	@see SettingsTabKommandos
	 *	@see SettingsButtonListener
	 */
	void drawGUI() {
		settingsFrame_ref = new JFrame("Einstellungen");
		settingsFrame_ref.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		settingsFrame_ref.setAlwaysOnTop(true);
		ImageIcon setFrameIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/setFrameIcon.png"));
		settingsFrame_ref.setIconImage(setFrameIcon_ref.getImage());
		Box backgroundBox_ref = new Box(BoxLayout.Y_AXIS);
		JTabbedPane tabbedPane_ref = new JTabbedPane();
		backgroundBox_ref.add(tabbedPane_ref);
		
		hostTab_ref = new SettingsTabHosts(this);
		ImageIcon setTabIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/tabHosts.png"));
		tabbedPane_ref.addTab("Hosts", setTabIcon_ref, hostTab_ref, "Zeigt die Hosteinstellungen an.");
		tabbedPane_ref.setMnemonicAt(0, KeyEvent.VK_1);
		
		comTab_ref = new SettingsTabKommandos(this);
		ImageIcon customTabIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/tabCustomC.png"));
		tabbedPane_ref.addTab("Kommandos", customTabIcon_ref, comTab_ref, "Lässt den Benutzer eigene Kommandos definieren.");
		tabbedPane_ref.setMnemonicAt(1, KeyEvent.VK_2);
		
		Box setButBox_ref = new Box(BoxLayout.X_AXIS);
		ImageIcon setSaveIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/saveIcon.png"));
		JButton setSaveBut_ref = new JButton("Speichern", setSaveIcon_ref);
		setSaveBut_ref.setToolTipText("Speichert die Tabelle auf der Festplatte.");
		setSaveBut_ref.addActionListener(new SettingsButtonListener());
		ImageIcon setCloseIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/exitIcon.png"));
		JButton setCloseBut_ref = new JButton("Schließen", setCloseIcon_ref);
		setCloseBut_ref.setToolTipText("Schließt das Einstellungs-Menü.");
		setCloseBut_ref.addActionListener(new SettingsButtonListener());
		setButBox_ref.add(setSaveBut_ref);
		setButBox_ref.add(Box.createHorizontalStrut(2));
		setButBox_ref.add(setCloseBut_ref);
		
		backgroundBox_ref.add(Box.createVerticalStrut(5));
		backgroundBox_ref.add(setButBox_ref);
		backgroundBox_ref.add(Box.createVerticalStrut(5));
		
		settingsFrame_ref.getContentPane().add(backgroundBox_ref);
		settingsFrame_ref.pack();
		settingsFrame_ref.setResizable(false);
		settingsFrame_ref.setVisible(true);
	} //endmethod drawGUI

	/**
	 *	Liefert eine Referenz auf die von diesem Objekt gehaltene Referenz auf die
	 *	Einstellungs-Matrix zurueck.
	 *
	 *	@return Referenz auf die Einstellungs-Matrix.
	 */
	String[][][] getSettings() {
		return settings_ref;
	} //endmethod getSettings
	
	/**
	 *	Setzt die Einstellung-Matrix auf die neu uebergebene Matrix.
	 *
	 *	@param settings_ref Referenz auf die neue Einstellungs-Matrix.
	 */
	void setSettings(String[][][] settings_ref) {
		this.settings_ref = settings_ref;
	} //endmethod setSettings
	
	/**
	 *	Liefert eine Referenz auf das Haupt-GUI <i>TinyAdminGUI</i> zurueck.
	 *
	 *	@return Referenz auf das Haupt-GUI-Objekt.
	 */
	TinyAdminGUI getGUI() {
		return gui_ref;
	} //endmethod getGUI
	
	/**
	 *	Liefert eine Referenz auf das Fenster (den <i>JFrame</i>) des Einstellungs-Menues zurueck.
	 *	Dies ist nuetzlich, um ihn z.B. von ausserhalb zu schliessen.
	 *
	 *	@return Referenz auf das Fenster des Einstellungs-GUIs.
	 */
	JFrame getFrame() {
		return settingsFrame_ref;
	} //endmethod getFrame
	
	/**
	 *	Zeigt den uebergebenen String als Text einer Fehlermeldung im SettingsGUI-Fenster mit Hilfe 
	 *	der <i>JOptionPane</i> an.
	 * 
	 * @param message_ref Der Fehlertext.
	 */
	void displayError(String message_ref) {
		JOptionPane.showMessageDialog(settingsFrame_ref, message_ref, "Fehler", JOptionPane.ERROR_MESSAGE);
	} //endmethod displayError
	
	/**
	 * 	Zeigt eine Informationsnachricht im Settings-GUI mit Hilfe der <i>JOptionPane</i> an.
	 * 
	 * 	@param message_ref Die Meldung, welche angezeigt werden soll.
	 */
	private void displayMsg(String message_ref) {
		JOptionPane.showMessageDialog(settingsFrame_ref, message_ref, "Hinweis", JOptionPane.INFORMATION_MESSAGE);
	} //endmethod displayMsg
	
	// --- Listener
	/**
	 *	Listener fuer die unteren Knoepfe des Einstellungs-GUIs.
	 *	Bietet folgende Moeglichkeiten:
	 *	<ul>
	 *		<li>Speichern<ul>
	 *		<li>Speichert alle getaetigten Einstellungen auf der Festplatte.</li></ul>
	 *		</li>
	 *		<li>Schliessen<ul>
	 *				<li>Blendet das Einstellungs-Menue-Fenster aus.</li></ul>
	 *		</li>
	 *	</ul>
	 *
	 * 	@version 0.2 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class SettingsButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev_ref) {
			if (ev_ref.getActionCommand().equals("Speichern")) {
				boolean hasInvalidNames = false;
				boolean hasInvalidMACs = false;
				boolean hasNoUser = false;
				boolean hasNoPWDs = false;
				boolean sudoNoPWD = false;
				boolean hasInvalidHosts = false;
				
				int nRow = hostTab_ref.getHostTabMod().getRowCount(), nCol = hostTab_ref.getHostTabMod().getColumnCount();
				
				for (int i=0; i<nRow; i++) {
					if ((((String)hostTab_ref.getHostTabMod().getValueAt(i,0)).toCharArray().length > 12) || (((String)hostTab_ref.getHostTabMod().getValueAt(i,0)).equals(""))) {
			    		hasInvalidNames = true;
		        	} //endif
					
					if (((String)hostTab_ref.getHostTabMod().getValueAt(i,2)).equals("")) {
						hasNoUser = true;
					} else {
						if (!gui_ref.getTestHelfer().isRootLogin((String)hostTab_ref.getHostTabMod().getValueAt(i,2)) &&
								((String)hostTab_ref.getHostTabMod().getValueAt(i,4)).equals("")) {
							sudoNoPWD = true;
						} //endif	
					} //endif	
					
					if (((String)hostTab_ref.getHostTabMod().getValueAt(i,3)).equals("")) {
						hasNoPWDs = true;
					} //endif
					
					if (!((String)hostTab_ref.getHostTabMod().getValueAt(i,5)).equals("")) {
		        		if (!gui_ref.getTestHelfer().isValidMAC((String)hostTab_ref.getHostTabMod().getValueAt(i,5))) {
							hasInvalidMACs = true;
						} //endif
		        	} //endif
					
					if (((String)hostTab_ref.getHostTabMod().getValueAt(i,1)).equals("")) {
						hasInvalidHosts = true;
					} //endif
				} //endfor
				
			    String[][][] tempData_ref = new String[2][][];
			    tempData_ref[1] = settings_ref[1];
			    tempData_ref[0] = new String [nRow][nCol];
			    for (int i=0; i<nRow; i++) {
			        for (int j=0; j<nCol; j++) {
			            tempData_ref[0][i][j] = (String)hostTab_ref.getHostTabMod().getValueAt(i,j);
			        } //endfor
			    } //endfor
			    
			    for (int i=0; i<nRow; i++) {
			    	for (int j=0; j<nRow; j++) {
			    		if ((i != j) && tempData_ref[0][i][0].equals(tempData_ref[0][j][0])) {
			    			hasInvalidNames = true;
			    		} //endif
			    	} //endfor
			    } //endfor
			    
			    String errorMsg_ref = "";
			    String infoMsg_ref = "";
				if (!hasInvalidNames && !hasInvalidMACs && !hasInvalidHosts) {
					if (hasNoUser) {
						infoMsg_ref += "ACHTUNG: Ein oder mehrere der Einträge verfügen über keinen Benutzernamen.\n" + 
								"Ihnen werden daher die Funktionen Update, Reboot und Shutdown für diese Hosts\n" + 
								"nicht zur verfügung stehen.\n\n";
					} //endif
					if (hasNoPWDs) {
						infoMsg_ref += "HINWEIS: Sie haben bei ein oder mehreren Hosts kein Passwort angegeben.\n" + 
										"Falls auf der Zielmaschine zum Login kein Passwort benötigt wird,\n" +
										"so sollten Sie den Systemadministrator darum bitten, dieses Verhalten aus\n"+
										"Sicherheitsgründen zu ändern.\n\n";
					} //endif
					if (sudoNoPWD) {
						infoMsg_ref += "HINWEIS: Bei ein oder mehreren Hosts wollen sie sich als normaler Benutzer anmelden,\n" + 
										"geben aber kein Sudo-Passwort an. Falls das Sudo-Passwort auf dem Fremdrechner wirklich nicht gesetzt ist,\n" +
										"so sollten Sie den Systemadministrator darum bitten, dies aus Sicherheitsgründen zu setzen.\n" +
										"Falls das Passwort nicht leer ist, sie es aber nicht kennen, so werden sie weder Update-, noch\n" +
										"Reboot-, noch Shutdown-Aktionen durchführen können.\n\n";
					} //endif
					if (!infoMsg_ref.equals("")) {
						infoMsg_ref += "Die Einstellungen wurden dennoch gespeichert.";
					} //endif
					gui_ref.initializeSettings(tempData_ref);
					if (!infoMsg_ref.equals("")) {
						displayMsg(infoMsg_ref);
					} //endif
				} else {
					if (hasInvalidNames) {
						errorMsg_ref += "FEHLER: Eine oder mehrere der angegebenen Host-Bezeichnungen sind ungültig.\n" + 
										"Sie müssen für jeden Eintrag eine Bezeichnung (kleiner/gleich 12 Zeichen) vergeben\n" +
										"und keine darf doppelt vorkommen.";
					} //endif
					if (hasInvalidMACs) {
						if (!errorMsg_ref.equals("")) {
							errorMsg_ref += "\n\n";
						} //endif
						errorMsg_ref += "FEHLER: Ein oder mehrere Einträge besitzen eine ungütlige MAC-Adresse.\n" +
										"Geben Sie bitte entweder keine oder aber eine gültige Adresse an.";
					} //endif
					if (hasInvalidHosts) {
						if (!errorMsg_ref.equals("")) {
							errorMsg_ref += "\n\n";
						} //endif
						errorMsg_ref += "FEHLER: Ein oder mehrere der Einträge besitzen keine IP-Adresse/Hostnamen.";
					} //endif
					displayError(errorMsg_ref);
				} //endif
			} else if (ev_ref.getActionCommand().equals("Schließen")) {
				settingsFrame_ref.dispose();
				settingsFrame_ref = null;
			} //endif
		} //endmethod actionPerformed
	} //endclass SettingsButtonListener
	
} //endclass TinyAdminSettingsGUI