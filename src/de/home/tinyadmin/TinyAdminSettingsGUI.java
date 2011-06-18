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
*	vorhandenen, gespeicherten Hosteinstellungen verschaffen, bzw. diese an die einzelnen Tabs weitergeben.</p>
*
* 	@version 0.3 von 06.2011
*
* 	@author Tobias Burkard
*/
class TinyAdminSettingsGUI {
	// --- Attribute
	private JFrame settingsFrame_ref;	// Fenster (JFrame) des Settings-GUI
	private TinyAdminGUI gui_ref;	// Referenz auf das HauptGUI
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
		drawGUI();
	} //endconstructor
	
	// --- Methoden
	/**
	 *	<p>Zeichnet das Einstellungs-GUI, bestehend aus einer <i>JTabbedPane</i>, welche
	 *	2 Tabs fasst: <i>SettingsTabHosts</i> und <i>SettingsTabKommandos</i>.</p>
	 *	<p>Beide Tab-Objekte erben von JPanel und somit lassen sich beide Objekte nahtlos in eine 
	 *	TabbedPane einfuegen.</p>
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
		ImageIcon setCloseIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/exitIcon.png"));
		JButton setCloseBut_ref = new JButton("Schließen", setCloseIcon_ref);
		setCloseBut_ref.setToolTipText("Schließt das Einstellungs-Menü.");
		setCloseBut_ref.addActionListener(new SettingsButtonListener());
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
	 *	Liefert eine Referenz auf das Haupt-GUI <i>TinyAdminGUI</i> zurueck.
	 *
	 *	@return Referenz auf das Haupt-GUI-Objekt.
	 */
	TinyAdminGUI getGUI() {
		return gui_ref;
	} //endmethod getGUI
	
	/**
	 *	Liefert eine Referenz auf das Host-Tab der TabbedPane zurueck.
	 * 
	 * 	@return Referenz auf das Host-Tab.
	 */
	SettingsTabHosts getHostTab() {
		return hostTab_ref;
	} //endmethod getHostTab
	
	/**
	 *	Liefert eine Referenz auf das Fenster (den <i>JFrame</i>) des Einstellungs-GUIs zurueck.
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
	
	// --- Listener
	/**
	 *	Listener fuer die unteren Knoepfe des Einstellungs-GUIs.
	 *	Bietet folgende Moeglichkeiten:
	 *	<ul>
	 *		<li>Speichern<ul>
	 *			<li>Speichert alle getaetigten Einstellungen auf der Festplatte.</li></ul>
	 *		</li>
	 *		<li>Schliessen<ul>
	 *			<li>Blendet das Einstellungs-Menue-Fenster aus.</li></ul>
	 *		</li>
	 *	</ul>
	 *
	 * 	@version 0.3 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class SettingsButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev_ref) {
			if (ev_ref.getActionCommand().equals("Schließen")) {
				settingsFrame_ref.dispose();
				settingsFrame_ref = null;
			} //endif
		} //endmethod actionPerformed
	} //endclass SettingsButtonListener
	
} //endclass TinyAdminSettingsGUI