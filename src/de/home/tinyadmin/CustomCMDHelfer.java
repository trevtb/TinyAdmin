package de.home.tinyadmin;

// --- Importe
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
--	CLASS: CustomCMDHelfer 															--
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
*	<p>Ein GUI-Hilfsmodul zum schnellen ausfuehren eigener Kommandos auf fremden Hosts.
*	Erzeugt wird dieses Objekt vom TinyAdminGUI, welches anschliessend die drawGUI()-Methode aufruft. 
*	Der Ausloeser hierfuer ist das Klicken des "Custom"-Buttons durch den Benutzer.</p> 
*	<p>Hier ist der Code fuer das Erzeugen des GUIs zur Abfrage des Kommandos in der <i>drawGUI</i>-Methode 
*	implementiert und beim Klick auf den Ok-Button wird die <i>performAction()</i>-Methode des Hauptprogramms
*	aufgerufen und der durch das GUI abgefragte Befehl wird dort zur Verarbeitung an ein erzeugtes SSHModule
*	weitergereicht. Zudem wird eine entsprechende Statusmeldung fuer das HauptGUI generiert.</p>
*
*	@see TinyAdminC#performAction(String, boolean, String[][], int)
*	@see TinyAdminGUI#setStatusText(String)
*
* 	@version 0.3 von 06.2011
*
* 	@author Tobias Burkard
*/
class CustomCMDHelfer {
	// --- Attribute
	private JFrame customFrame_ref;	// Fenster (JFrame) des Custom-GUI
	private TinyAdminGUI gui_ref;	// Referenz auf das HauptGUI
	private JTextField kommando_ref; // Referenz auf das JTextField, welches den Befehl fasst
	private JCheckBox asroot_ref;	// Hierrueber legt der Benutzer fest, ob der Befehl als root ausgefuehrt
									// werden soll
	
	/**	
	*	Setzt die uebergebene Referenz auf das Haupt-GUI und ruft die <i>drawGUI()</i>-Methode 
	*	zum letztendlichen Zeichnen des GUIs auf.
	*
	*	@see #drawGUI()
	*	@see TinyAdminGUI
	*/
	// --- Konstruktoren
	CustomCMDHelfer (TinyAdminGUI gui_ref) {
		this.gui_ref = gui_ref;
		drawGUI();
	} //endconstructor
	
	// --- Methoden
	/**
	 *	<p>Diese Methode baut das GUI zum Abfragen des Befehls vom Benutzer.
	 *	Es wird ein einfaches GUI mit einem JTextField <i>kommando_ref</i>,zwei
	 *	JButtons (Ok, Abbrechen) und einer JCheckBox <i>asroot_ref</i> erzeugt.</p>
	 *	<p>Das Textfeld erhaelt einen KeyListener <i>CustomKeyListener</i>, um die
	 *	Ausfuehrung durch das Drucken der "Enter"-Taste auszuloesen.</p>
	 *	<p>Die beiden Buttons bekommen einen <i>CustomButtonListener</i>, der sich um
	 *	die weitere Verarbeitung kuemmert.</p>
	 *
	 *	@see CustomKeyListener
	 *	@see CustomButtonListener
	 */
	void drawGUI() {
		customFrame_ref = new JFrame("Kommando-Eingabe");
		customFrame_ref.addWindowListener(new WindowEventListener());
		customFrame_ref.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		customFrame_ref.setAlwaysOnTop(true);
		ImageIcon customFrameIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/customFrameIcon.png"));
		customFrame_ref.setIconImage(customFrameIcon_ref.getImage());
		Box bgBox_ref = new Box(BoxLayout.Y_AXIS);
		bgBox_ref.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		Box infoLabBox_ref = new Box(BoxLayout.X_AXIS);
		ImageIcon customCommandIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/cCommandIcon.png"));
		JLabel infoLabel1_ref = new JLabel("Bitte geben Sie den Befehl ein, der ausgeführt werden soll:");
		infoLabBox_ref.add(new JLabel(customCommandIcon_ref));
		infoLabBox_ref.add(Box.createHorizontalStrut(10));
		infoLabBox_ref.add(infoLabel1_ref);
		infoLabBox_ref.add(Box.createGlue());
		bgBox_ref.add(infoLabBox_ref);
		bgBox_ref.add(Box.createVerticalStrut(10));
		
		Box kommandoBox_ref = new Box(BoxLayout.X_AXIS);
		JLabel kommandoLab_ref = new JLabel("Befehl:");
		kommandoBox_ref.add(kommandoLab_ref);
		kommandoBox_ref.add(Box.createHorizontalStrut(3));
		kommando_ref = new JTextField(25);
		kommando_ref.addKeyListener(new CustomKeyListener());
		kommandoBox_ref.add(kommando_ref);
		asroot_ref = new JCheckBox("Als root ausführen?");
		kommandoBox_ref.add(Box.createHorizontalStrut(5));
		kommandoBox_ref.add(asroot_ref);
		bgBox_ref.add(kommandoBox_ref);
		bgBox_ref.add(Box.createVerticalStrut(10));
		
		JPanel buttonPan_ref = new JPanel();
		ImageIcon okButIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/testIcon.png"));
		ImageIcon cancelButIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/cancelIcon.png"));
		JButton okBut_ref = new JButton("Ok", okButIcon_ref);
		JButton cancelBut_ref = new JButton("Abbrechen", cancelButIcon_ref);
		okBut_ref.addActionListener(new CustomButtonListener());
		cancelBut_ref.addActionListener(new CustomButtonListener());
		buttonPan_ref.add(okBut_ref);
		buttonPan_ref.add(Box.createHorizontalStrut(2));
		buttonPan_ref.add(cancelBut_ref);
		buttonPan_ref.setAlignmentX(SwingConstants.CENTER);
		bgBox_ref.add(buttonPan_ref);
		customFrame_ref.getContentPane().add(bgBox_ref);
		customFrame_ref.pack();
		customFrame_ref.setResizable(false);
		customFrame_ref.setVisible(true);
		Point pos_ref = gui_ref.getFrame().getLocationOnScreen();
		customFrame_ref.setLocation(pos_ref.x + 30, pos_ref.y + 250);
	} //endmethod drawGUI
	
	/**
	 *	<p>Baut unter Zuhilfenahme der <i>getActionData()</i>-Methode des Haupt-GUIs eine Matrix,
	 *	welche an die <i>performAction()</i>-Methode des Hauptprogramms weitergegeben werden kann.</p>
	 *	<p>Dies geschieht nur dann, wenn der Benutzer auch wirklich einen Befehl eingegeben hat: Ist das
	 *	Textfeld leer, macht die Methode garnichts</p>
	 *	<p>Die zu uebergebende Matrix erhaelt eine 10. Spalte, in der sich der an die Methode uebergebene Befehl 
	 *	<i>action_ref</i> befindet.</p>
	 *	<p>Der <i>performAction()</i> Methode wird auch mitgeteilt, ob der Benutzer die Ausfuehrung als 
	 *	<i>root</i> wuenscht oder nicht. Dies wird ueber das 2. Argument (<i>boolean sudo</i>) gesteuert, 
	 *	welches auf <i>true</i> gesetzt wird, falls der Benutzer die CheckBox zur Ausfuehrung als root
	 *	markiert hat.</p>
	 *	<p>Der Aktionstyp wird auf "custom" gesetzt.</p>
	 * 
	 * 	@param action_ref Der vom Benutzer eingegebene Befehl.
	 * 	@see TinyAdminGUI#getActionData()
	 * 	@see TinyAdminC#performAction(String, boolean, String[][], int)
	 * 	
	 */
	void doAction(String action_ref) {
		if (!action_ref.equals("")) {
			customFrame_ref.dispose();
			customFrame_ref = null;
			gui_ref.setStatusText("Eigenes Kommando: Eingabe erfasst, beginne Verarbeitung...\n");
			String[][] actionDataOld_ref = gui_ref.getActionData();
			String[][] actionData_ref = new String[actionDataOld_ref.length][10];
			for (int i=0; i<actionData_ref.length; i++) {
				for (int j=0; j<actionData_ref[i].length; j++) {
					if (j<9) {
						actionData_ref[i][j] = actionDataOld_ref[i][j];
					} else {
						actionData_ref[i][j] = action_ref;
					} //endif
				} //endfor
			} //endfor
			boolean sudo = asroot_ref.isSelected();
			
			gui_ref.getMainC().performAction("custom", sudo, actionData_ref, gui_ref.getProcesses());
		} //endif
	} //endmethod doAction
	
	// --- Innere Klassen
	
	// --- Listener
	/**
	 *	Listener fuer die "Ok"- und "Abbrechen"-Buttons.
	 *	<ul><li>"Ok" ruft die <i>doAction()</i>-Methode auf und uebergibt ihr dabei den vom Benutzer in das
	 *	JTextField <i>kommando_ref</i> eingegebenen Befehl.</li>
	 *	<li>"Abbrechen" laesst den Frame <i>customFrame_ref</i> verschwinden, setzt dessen Referenz
	 *	gleich <i>null</i> und setzt eine entsprechende Statusmeldung im HauptGUI ab.</li></ul>
	 *
	 *	@see TinyAdminGUI
	 *	@see CustomCMDHelfer#doAction(String)
	 *
	 * 	@version 0.3 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class CustomButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev_ref) {
			if (ev_ref.getActionCommand().equals("Ok")) {
				doAction(kommando_ref.getText());
			} else if (ev_ref.getActionCommand().equals("Abbrechen")) {
				gui_ref.setStatusText("Aktion vom Benutzer abgebrochen.");
				customFrame_ref.dispose();
				customFrame_ref = null;
			} //endif
		} //endmethod actionPerformed
	} //endclass CustomButtonListener
	
	/**
	 *	<p>Faengt das Druecken der "Enter"-Taste ab, waehrend der Benutzer sich im TextFeld <i>kommando_ref</i>
	 *	befindet, um so das gleiche Ergebnis wie durch einen Druck auf den "Ok"-Button zu erzielen.
	 *	Ruft die <i>doAction()</i>-Methode auf und uebergibt ihr dabei den vom Benutzer in das
	 *	JTextField <i>kommando_ref</i> eingegebenen Befehl.</p>
	 *
	 *	@see CustomCMDHelfer#doAction(String)
	 *
	 * 	@version 0.3 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class CustomKeyListener implements KeyListener {
		public void keyTyped(KeyEvent ev_ref) {}
		public void keyReleased(KeyEvent ev_ref) {}
		public void keyPressed(KeyEvent ev_ref) {
			int key = ev_ref.getKeyCode();
			if (key == KeyEvent.VK_ENTER) {
				doAction(kommando_ref.getText());
		     } //endif
		} //endmethod keyPressed
	} //endclass CustomKeyListener
	
	/**
	 *	Listener fuer den JFrame <i>customFrame_ref</i> (Das Fenster des Custom-Command-GUIs),
	 *	um vor dem Schliessen noch eine Statusmeldung im Haupt-GUI absetzen zu koennen.
	 * 
	 *	@version 0.3 von 06.2011
	 *
	 *	@author Tobias Burkard
	 */
	private class WindowEventListener extends WindowAdapter {
		public void windowClosing(WindowEvent ev_ref) {
			gui_ref.setStatusText("Aktion vom Benutzer abgebrochen.");
			customFrame_ref.dispose();
			customFrame_ref = null;
		} //endmethod windowClosing
	} //endclass WindowEventListener
	
} //endclass CustomCMDHelfer