package de.home.tinyadmin;

// --- Importe
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
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
--	CLASS: PWDHelfer 															--
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
 *	Helfer-Klasse fuer das Abfragen des Passwortes beim Programmstart, bzw. das Aendern des
 *	vorhandenen Passwortes von innerhalb der Anwendung. Fuer beide Faelle stellt diese
 *	Klasse GUIs und Werkzeuge bereit. Zur Ausfuehrung benoetigt ein Objekt dieser Klasse
 *	jedoch eine Referenz auf das Hauptprogramm.
 *
 *	@version 0.3 von 06.2011
 *
 * 	@author Tobias Burkard
 */
class PWDHelfer {
	// --- Attribute
	private JFrame frame_ref;				// Fenster fuer die Passwortabfrage beim Programmstart
	private JFrame setFrame_ref;			// Fenster fuer das Neusetzen des Passwortes
	private JPasswordField pwdField_ref;	// Passwortfeld in frame_ref
	private TinyAdminC main_ref;			// Referenz auf das Hauptprogramm
	private JPasswordField pwdField1_ref;	// Passwortfeld im setFrame_ref
	private JPasswordField pwdField2_ref;	// Bestaetigungsfeld im setFrame_ref
	private JButton okBut_ref;				// Ok-Button im frame_ref
	
	// --- Klassenattribute
	private static boolean lock;			// Sperrt das Programm fuer weitere Aktionen, um das doppelte
											// Absenden von Befehlen zu verhindern
	private ImageIcon appIcon_ref;			// Das TinyAdmin-Logo
	private ImageIcon pwdIcon_ref;			// Passwort-Icon fuer alle GUIs dieses Objektes
	
	/**
	 *	Initialisiert ein neues <i>PWDHelfer</i>-Objekt und setzt die uebergebene Referenz auf
	 *	das Hauptprogramm. Zudem wird das Logo der Anwendung initialisiert.
	 *
	 *	@param main_ref Referenz auf das Hauptprogramm.
	 */
	PWDHelfer(TinyAdminC main_ref) {
		this.main_ref = main_ref;
		appIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/appIcon.png"));
		pwdIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/pwdIcon.png"));
	} //endconstructor
	
	// --- Methoden
	/**
	 *	Baut das GUI fuer die Passwortabfrage beim Programmstart zusammen. Das GUI besteht aus einem
	 *	simplen TextFeld, in das der Benutzer sein Passwort eingeben kann, sowie einem <i>Verlassen-</i>
	 *	und einem <i>Ok-Button</i>. Beide Buttons erhalten einen <i>ButtonListener</i>.
	 *
	 *	@see ButtonListener
	 */
	void drawGUI() {
		PWDHelfer.lock = false;
		frame_ref = new JFrame("TinyAdmin v0.3");
		frame_ref.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame_ref.setIconImage(appIcon_ref.getImage());
	
		Box pwdBox_ref = new Box(BoxLayout.Y_AXIS);
		pwdBox_ref.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		Box infoBoxWrapper_ref = new Box(BoxLayout.X_AXIS);
		Box infoBox_ref = new Box(BoxLayout.Y_AXIS);
		infoBox_ref.setBorder(new TitledBorder("Hinweis"));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Bitte geben Sie zum Entschlüsseln Ihr Passwort ein."));
		infoBox_ref.add(new JLabel("Falls Sie das Kennwort vergessen haben, müssen Sie leider"));
		infoBox_ref.add(new JLabel("die Datei tinyadmin.data löschen und das Programm neu starten."));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBoxWrapper_ref.add(infoBox_ref);
		infoBoxWrapper_ref.add(Box.createHorizontalStrut(10));
		infoBoxWrapper_ref.add(new JLabel(pwdIcon_ref));
		infoBoxWrapper_ref.add(Box.createHorizontalStrut(5));
		pwdBox_ref.add(infoBoxWrapper_ref);
		
		pwdBox_ref.add(Box.createVerticalStrut(15));
		
		Box pwdSubBox_ref = new Box(BoxLayout.X_AXIS);
		JLabel pwdLab_ref = new JLabel("Passwort:");
		pwdField_ref = new JPasswordField(5);
		pwdField_ref.addKeyListener(new PWDKeyListener());
		pwdSubBox_ref.add(pwdLab_ref);
		pwdSubBox_ref.add(Box.createHorizontalStrut(2));
		pwdSubBox_ref.add(pwdField_ref);
		pwdSubBox_ref.add(Box.createHorizontalStrut(65));
		pwdBox_ref.add(pwdSubBox_ref);
		
		pwdBox_ref.add(Box.createVerticalStrut(10));
		
		Box buttonBox_ref = new Box(BoxLayout.X_AXIS);
		ImageIcon exitIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/exitIcon.png"));
		JButton exitBut_ref = new JButton("Verlassen", exitIcon_ref);
		exitBut_ref.addActionListener(new ButtonListener());
		buttonBox_ref.add(exitBut_ref);
		buttonBox_ref.add(Box.createHorizontalStrut(5));
		
		ImageIcon okIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/testIcon.png"));
		okBut_ref = new JButton("Ok", okIcon_ref);
		okBut_ref.addActionListener(new ButtonListener());
		buttonBox_ref.add(okBut_ref);
		pwdBox_ref.add(buttonBox_ref);
		
		frame_ref.getContentPane().add(pwdBox_ref);
		frame_ref.pack();
		frame_ref.setVisible(true);
		frame_ref.setResizable(false);
	} //endmethod drawGUI
	
	/**
	 *	Aendert den Cursor in einen Wartecursor oder stellt den normalen Cursor wieder her.
	 *	Dies geschieht in Abhaengigkeit des uebergebenen <i>int onOff</i>.
	 *
	 * 	@param onOff 0=Normaler Cursor, 1=Wartecursor
	 */
	void initWaitCursor(int onOff) {
		if (onOff == 1) {
			frame_ref.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else if (onOff == 0) {
			frame_ref.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} //endif
	} //endmethod initWaitCursor
	
	/**
	 *	Zeichnet das GUI zum setzten eines neuen Passwortes. Dieses besteht aus zwei Textfeldern
	 *	(eines fuer das Passwort, eines zur Bestaetigung), sowie eines <i>Abbrechen-</i> und
	 *	eines <i>Ok-Buttons</i>. Beide Buttons erhalten einen <i>SetButtonListener</i>.
	 *
	 *	@see SetButtonListener
	 */
	void drawPWDSet() {
		PWDHelfer.lock = false;
		setFrame_ref = new JFrame("Passwort neu setzen");
		setFrame_ref.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setFrame_ref.setIconImage(appIcon_ref.getImage());
		
		JPanel pwdPan_ref = new JPanel();
		BoxLayout lay_ref = new BoxLayout(pwdPan_ref, BoxLayout.Y_AXIS);
		pwdPan_ref.setLayout(lay_ref);
		pwdPan_ref.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		Box infoBoxWrapper_ref = new Box(BoxLayout.X_AXIS);
		Box infoBox_ref = new Box(BoxLayout.Y_AXIS);
		infoBox_ref.setBorder(new TitledBorder("Hinweis"));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Bitte geben Sie ihr neues Passwort ein:"));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBoxWrapper_ref.add(infoBox_ref);
		infoBoxWrapper_ref.add(Box.createHorizontalStrut(10));
		infoBoxWrapper_ref.add(new JLabel(pwdIcon_ref));
		infoBoxWrapper_ref.add(Box.createHorizontalStrut(5));
		pwdPan_ref.add(infoBoxWrapper_ref);
		
		pwdPan_ref.add(Box.createVerticalStrut(15));
		
		Box pwdBox_ref = new Box(BoxLayout.Y_AXIS);
		pwdBox_ref.setBorder(new TitledBorder("Passworteingabe"));
		
		Box pwdBox1_ref = new Box(BoxLayout.X_AXIS);
		JLabel pwdLab1_ref = new JLabel("Passwort:");
		pwdField1_ref = new JPasswordField(5);
		pwdBox1_ref.add(pwdLab1_ref);
		pwdBox1_ref.add(Box.createHorizontalStrut(2));
		pwdBox1_ref.add(pwdField1_ref);
		pwdBox1_ref.add(Box.createHorizontalStrut(65));
		pwdBox_ref.add(pwdBox1_ref);
		pwdBox_ref.add(Box.createVerticalStrut(10));
		
		Box infBox_ref = new Box(BoxLayout.X_AXIS);
		JLabel infL_ref = new JLabel("Geben Sie das Passwort zur Bestätigung bitte erneut ein:");
		infL_ref.setAlignmentX(SwingConstants.LEFT);
		infBox_ref.add(infL_ref);
		infBox_ref.add(Box.createGlue());
		pwdBox_ref.add(infBox_ref);
		pwdBox_ref.add(Box.createVerticalStrut(5));
		
		Box pwdBox2_ref = new Box(BoxLayout.X_AXIS);
		JLabel pwdLab2_ref = new JLabel("Passwort:");
		pwdField2_ref = new JPasswordField(5);
		pwdBox2_ref.add(pwdLab2_ref);
		pwdBox2_ref.add(Box.createHorizontalStrut(2));
		pwdBox2_ref.add(pwdField2_ref);
		pwdBox2_ref.add(Box.createHorizontalStrut(65));
		pwdBox_ref.add(pwdBox2_ref);
		pwdPan_ref.add(pwdBox_ref);
		
		pwdPan_ref.add(Box.createVerticalStrut(10));
		Box buttonBox_ref = new Box(BoxLayout.X_AXIS);
		ImageIcon cancelIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/cancelIcon.png"));
		JButton cancelBut_ref = new JButton("Abbrechen", cancelIcon_ref);
		cancelBut_ref.addActionListener(new SetButtonListener());
		buttonBox_ref.add(cancelBut_ref);
		buttonBox_ref.add(Box.createHorizontalStrut(5));
		ImageIcon doneIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/testIcon.png"));
		JButton doneBut_ref = new JButton("Ok", doneIcon_ref);
		doneBut_ref.addActionListener(new SetButtonListener());
		buttonBox_ref.add(doneBut_ref);
		pwdPan_ref.add(buttonBox_ref);
		
		setFrame_ref.getContentPane().add(pwdPan_ref);
		setFrame_ref.pack();
		setFrame_ref.setVisible(true);
		setFrame_ref.setResizable(false);
	} //endmethod drawPWDSet
	
	/**
	 *	<p>Hier ist der eigentliche Code zur Ueberpruefung des beim Programmstart
	 *	vom Benutzer eingegebenen Passwortes implementiert.</p>
	 *	<p>Zunaechst wird das GUI fuer weitere Aktionen (ausser Schliessen) gesperrt
	 *	und anschliessend das Passwort ueberprueft.</p>
	 *	<p>Ist das Passwort korrekt, dann startet das GUI: Die Methode <i>paintGUI()</i>
	 *	des Hauptprogramms wird aufgerufen. Ist das Passwort falsch,
	 *	wird ein neuer Thread mit einem <i>MyLocker</i>-Objekt erstellt. Dieses kuemmert sich
	 *	um die Sperrung des GUIs fuer 3 Sekunden. So soll Bruteforce-Attacken entgegengewirkt
	 *	werden. Natuerlich wird auch eine Fehlermeldung generiert.</p>
	 *	<p>Am Schluss wird das GUI wieder fuer Aktionen freigegeben.</p>
	 *
	 *	@see MyLocker
	 *	@see TinyAdminC#paintGUI()
	 */
	void doAction() {
		if ((new String(pwdField_ref.getPassword())).equals("")) {
			PWDHelfer.lock = false;
			JOptionPane.showMessageDialog(frame_ref, "Sie müssen ihr Passwort eingeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
		} else {
			boolean isAscii = main_ref.getTestHelfer().isAscii(new String(pwdField_ref.getPassword()));
			TinyAdminGUI.pwd_class = new String(pwdField_ref.getPassword());
			if (isAscii && main_ref.getIOHelfer().readSettings(TinyAdminGUI.pwd_class) != null) {
				main_ref.getGUI().initializeSettings(main_ref.getIOHelfer().readSettings(TinyAdminGUI.pwd_class));
				main_ref.paintGUI();
				frame_ref.dispose();
				frame_ref = null;
			} else {
				Thread lock_ref = new Thread(new MyLocker());
				lock_ref.start();
			} //endif
		} //endif
	} //endmethod doAction
	
	// --- Listener
	/**
	 * 	<p>Listener der Buttons des Fensters <i>frame_ref</i>, also dem
	 * 	Passwortdialog beim Start der Anwendung. Bietet folgende Optionen:</p>
	 * 	<ul>
	 * 		<li>Verlassen:<ul>
	 * 			<li>Schliesst die Anwendung</li></ul>
	 * 		</li>
	 * 		<li>Ok:<ul>
	 * 			<li>Sperrt das GUI fuer weitere Aktionen und ruft die <i>doAction()</i>-Methode auf.
	 * 			All dies geschieht natuerlich nur dann, wenn das GUI nicht gesperrt ist.</li></ul>
	 * 		</li>
	 * 	</ul>
	 * 
	 *	@version 0.3 von 06.2011
	 *
	 *	@author Tobias Burkard
	 */
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev_ref) {
			if (ev_ref.getActionCommand().equals("Verlassen")) {
				System.exit(0);
			} else if (ev_ref.getActionCommand().equals("Ok") && !PWDHelfer.lock) {
				PWDHelfer.lock = true;
				doAction();
			} //endif
		} //endmethod actionPerformed
	} //endclass ButtonListener
	
	/**
	 * 	<p>Listener der Buttons des Fensters <i>setFrame_ref</i>, also dem
	 * 	GUI zum Setzen eines neuen Passwortes. Bietet folgende Optionen:</p>
	 * 	<ul>
	 * 		<li>Abbrechen:<ul>
	 * 			<li>Schliesst das Fenster zum Setzen eines neuen Passwortes.</li></ul>
	 * 		</li>
	 * 		<li>Ok:<ul>
	 * 			<li>Sperrt das GUI fuer weitere Aktionen und ueberprueft das eingegebene Passwort. Tritt ein
	 * 			Fehler auf, z.B. weil die Passwoerter nicht uebereinstimmen, wird eine entsprechende
	 * 			Fehlermeldung generiert. All dies geschieht nur dann, wenn das GUI nicht gesperrt ist.</li></ul>
	 * 		</li>
	 * 	</ul>
	 * 
	 *	@version 0.3 von 06.2011
	 *
	 *	@author Tobias Burkard
	 */
	class SetButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev_ref) {
			if (ev_ref.getActionCommand().equals("Abbrechen")) {
				setFrame_ref.dispose();
				setFrame_ref = null;
			} else if (ev_ref.getActionCommand().equals("Ok") && !PWDHelfer.lock) {
				PWDHelfer.lock = true;
				boolean isAscii = main_ref.getTestHelfer().isAscii(new String(pwdField1_ref.getPassword()));
				if (!isAscii) {
					JOptionPane.showMessageDialog(setFrame_ref, "Bitte verwenden Sie nur ASCII-Zeichen:\n\n" +
							"!\"#$%&'()*+,-./0123456789:;<=>?\n" +
							"@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\n" +
							"`abcdefghijklmnopqrstuvwxyz{|}~", "Fehler", JOptionPane.ERROR_MESSAGE);
					PWDHelfer.lock = false;
				} else if ((new String(pwdField1_ref.getPassword())).equals("")) {
					JOptionPane.showMessageDialog(setFrame_ref, "Sie müssen ein Passwort eingeben!", "Fehler", JOptionPane.ERROR_MESSAGE);
					PWDHelfer.lock = false;
				} else if (!(new String(pwdField1_ref.getPassword())).equals("") &&
						(new String(pwdField2_ref.getPassword())).equals("")) {
					JOptionPane.showMessageDialog(setFrame_ref, "Bitte geben Sie das Passwort zur Bestätigung erneut ein!", "Fehler", JOptionPane.ERROR_MESSAGE);
					PWDHelfer.lock = false;
				} else if (!(new String(pwdField1_ref.getPassword())).equals("") && 
						!(new String(pwdField1_ref.getPassword())).equals(new String(pwdField2_ref.getPassword()))) {
					JOptionPane.showMessageDialog(setFrame_ref, "Die Passwörter stimmen nicht überein!", "Fehler", JOptionPane.ERROR_MESSAGE);
					PWDHelfer.lock = false;
				} else {
					TinyAdminGUI.pwd_class = new String(pwdField1_ref.getPassword());
					main_ref.getGUI().initializeSettings(main_ref.getGUI().getSettings());
					PWDHelfer.lock = false;
					setFrame_ref.dispose();
					setFrame_ref = null;
				} //endif
			} //endif
		} //endmethod actionPerformed
	} //endclass SetButtonListener
	
	/**
	 *	Listener fuer das Textfeld des Fensters <i>frame_ref</i>, dem Dialog zum Abfragen 
	 *	des Passwortes beim Start der Anwendung. Betaetigt der Benutzer die <i>Enter</i>-Taste
	 *	waehrend er sich im TextFeld befindet, hat dies die gleiche Wirkung wie ein Klick
	 *	auf den <i>Ok-Button</i>: Das GUI wird gesperrt und es wird die <i>doAction()</i>-Methode
	 *	aufgerufen.
	 *
	 *	@version 0.3 von 06.2011
	 *
	 *	@author Tobias Burkard
	 */
	private class PWDKeyListener implements KeyListener {
		public void keyTyped(KeyEvent ev_ref) {}
		public void keyReleased(KeyEvent ev_ref) {}
		public void keyPressed(KeyEvent ev_ref) {
			int key = ev_ref.getKeyCode();
			if (key == KeyEvent.VK_ENTER && !PWDHelfer.lock) {
				PWDHelfer.lock = true;
				doAction();
		     } //endif
		} //endmethod keyPressed
	} //endclass CustomKeyListener
	
	// --- Innere Klassen
	/**
	 *	<p>Dieses <i>Runnable</i>-Objekt sperrt das GUI optisch durch Deaktivierung des
	 *	</i>OK-Buttons</i> und Aufrufen der <i>initWaitCursor()</i>-Methode zum
	 *	Setzen eines Wartecursors. Anschliessend wird der Prozess fuer
	 *	3 Sekunden schlafen geschickt. Sind die 3 Sekunden vorbei, wird alles
	 *	wieder rueckgaengig gemacht.</p>
	 *	<p>Dieses Runnable wird erzeugt, wenn der Benutzer beim Programmstart
	 *	ein falsches Passwort eingegeben hat: So sollen Bruteforce-Attacken
	 *	erschwert werden.</p>
	 *
	 *	@version 0.3 von 06.2011
	 *
	 *	@author Tobias Burkard
	 */
	private class MyLocker implements Runnable {
		public void run() {
			okBut_ref.setEnabled(false);
			pwdField_ref.setEnabled(false);
			initWaitCursor(1);
			try {
				Thread.sleep(3000);
			} catch (Exception ex_ref) {
				ex_ref.printStackTrace();
			} //endtry
			initWaitCursor(0);
			okBut_ref.setEnabled(true);
			pwdField_ref.setEnabled(true);
			PWDHelfer.lock = false;
			JOptionPane.showMessageDialog(frame_ref, "Das Passwort war falsch, bitte versuchen Sie es erneut.", "Fehler", JOptionPane.ERROR_MESSAGE);
		} //endmethod run	
	} //endclass MyLocker
	
} //endclass PWDHelfer