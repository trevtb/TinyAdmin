package de.home.tinyadmin;

// --- Importe
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
--	CLASS: FirstStartGUI 															--
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
 *	<p>Stellt das GUI bereit, das dem Benutzer beim ersten Start der Anwendung bezueglich der
 *	Einrichtung des Passwortes und der Erstellung eines ersten Hosteintrages behilflich ist.</p>
 *	<p>Die Erstellung des GUIs ist in der Methode <i>drawGUI()</i> implementiert.</p>
 *	
 *	@see #drawGUI()
 *
 * 	@version 0.3 von 06.2011
 *
 * 	@author Tobias Burkard
 */
class FirstStartGUI {
	// --- Attribute
	private TinyAdminGUI mainGui_ref;		// Referenz auf das HauptGUI-Objekt der Anwendung
	private JFrame frame_ref;				// Das Fenster des Wizzards
	private int num;						// Legt fest, welcher Teil des Wizzards gerade angezeigt wird
	private JPasswordField pwdField1_ref;	// Textfeld fuer das Passwort
	private JPasswordField pwdField2_ref;	// Das Bestaetigungsfeld fuer das Passwort
	private JPanel addHostPan_ref;			// Das vom AddHostGUI zur Laufzeit bezogene Panel
	private AddHostGUI addHost_ref;			// Referenz auf das AddHostGUI
	
	// --- Konstruktoren
	/**
	 *	Initialisiert die Objektvariable <i>gui_ref</i> mit der uerbergebenen Referenz
	 *	auf das HauptGUI, sowie alle sonst fuer den Wizzard noetigen Variablen und Objekte.
	 *
	 *	@param mainGui_ref Referenz auf das Haupt-GUI der Anwendung.
	 */
	FirstStartGUI(TinyAdminGUI mainGui_ref) {
		this.mainGui_ref = mainGui_ref;
		num = 0;
		frame_ref = null;
	} //endconstructor
	
	// --- Methoden
	/**
	 *	<p>Zeichnet den Wizzard fuer den ersten Start der Anwendung in Abhaengigkeit der Variablen
	 *	<i>int num</i>. Ueber die Methoden <i>drawStart()</i>, <i>drawEnd()</i> und <i>drawPWD()</i>, 
	 *	sowie ueber die Klasse <i>AddHostGUI</i> koennen die einzelnen Panels fuer die Teilschritte 
	 *	des Wizzards bezogen werden.</p>
	 *	<p>Die Belegung der Variablen <i>int num</i> ist wie folgt:</p>
	 *	<ul>
	 *		<li>0 - dies ist der Ausgangspunkt, das Panel wird ueber die Methode <i>getStart()</i> bezogen.</li>
	 *		<li>1 - der zweite Schritt, das Panel wird ueber die Methode <i>getPWD()</i> bezogen.</li>
	 *		<li>2 - der dritte Schritt, das Panel wird ueber die Methode <i>getPanel()</i> der Klasse
	 *			<i>AddHostGUI</i> bezogen</li>
	 *		<li>3 - der letzte Schritt: das Panel wird ueber die Methode <i>drawEnd()</i> bezogen.</li>
	 *	</ul>
	 *	<p>Zusaetzlich wird dem so bezogenen Panel noch eine Box mit Buttons hinzugefuegt. Hierfuer
	 *	ist die Methode <i>getButtonBox()</i> zustaendig.</p>
	 *
	 *	@see #drawStart()
	 *	@see #drawPWD()
	 *	@see AddHostGUI#getPanel(int type)
	 *	@see #drawEnd()
	 *	@see #getButtonBox(int)
	 */
	void drawGUI() {
		boolean isNew;
		
		if (frame_ref == null) {
			isNew = true;
			frame_ref = new JFrame("TinyAdmin v0.3");
			frame_ref.addWindowListener(new FrameListener());
			frame_ref.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			ImageIcon appIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/appIcon.png"));
			frame_ref.setIconImage(appIcon_ref.getImage());
		} else {
			isNew = false;
			frame_ref.getContentPane().removeAll();
		} //endif
		
		JPanel currentPan_ref = null;
		if (num == 0) {
			currentPan_ref = drawStart();
		} else if (num == 1) {
			currentPan_ref = drawPWD();
			currentPan_ref.add(Box.createVerticalStrut(10));
			currentPan_ref.add(getButtonBox(-1));
		} else if (num == 2) {
			if (addHostPan_ref == null) {
				addHost_ref = new AddHostGUI(0, mainGui_ref);
				addHostPan_ref = addHost_ref.getPanel(2);
				currentPan_ref = addHostPan_ref;
				currentPan_ref.add(Box.createVerticalStrut(10));
				currentPan_ref.add(getButtonBox(-1));
			} else {
				currentPan_ref = addHostPan_ref;
			}
		} else if (num == 3) {
			currentPan_ref = drawEnd();
		} //endif
		
		frame_ref.getContentPane().add(currentPan_ref);
		frame_ref.pack();
		if (isNew) {
			frame_ref.setVisible(true);
			frame_ref.setResizable(false);
		} else {
			frame_ref.validate();
			frame_ref.repaint();
		} //endif
	} //endmethod drawGUI
	
	/**
	 *	Zeichnet das Begruessungsfenster des Wizzards. Hier wird der Benutzer kurz
	 *	ueber das weitere vorgehen informiert. Die Buttons werden, wie bei jedem
	 *	Teilschritt des Wizzards, ueber die Methode <i>getButtons()</i> erzeugt.
	 *
	 *	@return Panel, welches das Begruessungs-GUI fuer den Wizzards beinhaltet.
	 *	@see #getButtonBox(int pos)
	 */
	JPanel drawStart() {
		JPanel startPan_ref = new JPanel();
		BoxLayout lay_ref = new BoxLayout(startPan_ref, BoxLayout.Y_AXIS);
		startPan_ref.setLayout(lay_ref);
		startPan_ref.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		Box headBox_ref = new Box(BoxLayout.X_AXIS);
		JLabel titleLab_ref = new JLabel("Willkommen bei TinyAdmin!", JLabel.CENTER);
		titleLab_ref.setFont(new Font(titleLab_ref.getFont().getFamily(), Font.PLAIN, 32));
		headBox_ref.add(titleLab_ref);
		headBox_ref.add(Box.createHorizontalStrut(1));
		ImageIcon firstStartIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/firstStartIcon.png"));
		headBox_ref.add(new JLabel(firstStartIcon_ref));
		startPan_ref.add(headBox_ref);
		
		startPan_ref.add(Box.createVerticalStrut(15));
		
		Box infoBoxWrapper_ref = new Box(BoxLayout.X_AXIS);
		infoBoxWrapper_ref.setBorder(new TitledBorder("Hinweis"));
		Box infoBox_ref = new Box(BoxLayout.Y_AXIS);
		infoBox_ref.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		infoBox_ref.add(new JLabel("Dieser Wizzard wird ihnen dabei helfen, das Programm für die erste Verwendung einzurichten."));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Nehmen Sie sich daher bitte kurz die Zeit um ein Passwort für die Verschlüsselung zu wählen"));
		infoBox_ref.add(new JLabel("und ihren ersten Hosteintrag zu erstellen."));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Natürlich können Sie später weitere Einträge über das Einstellungsmenü hinzufügen,"));
		infoBox_ref.add(new JLabel("dort können Sie dann auch eigene Kommandos erstellen."));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Wenn Sie bereit sind, klicken Sie bitte auf 'Weiter'."));
		infoBoxWrapper_ref.add(infoBox_ref);
		startPan_ref.add(infoBoxWrapper_ref);
		
		startPan_ref.add(Box.createVerticalStrut(15));
		Box buttonBox_ref = getButtonBox(0);
		startPan_ref.add(buttonBox_ref);
		
		return startPan_ref;
	} //endmethod drawStart
	
	/**
	 *	Zeichnet das Abschiedsfenster des Wizzards: Dies schliesst die Erstkonfiguration ab. 
	 *	Die Buttons werden, wie bei jedem Teilschritt des Wizzards, ueber die Methode <i>getButtons()</i> erzeugt.
	 *
	 *	@return Panel, welches das Abschieds-GUI fuer den Wizzards beinhaltet.
	 *	@see #getButtonBox(int pos)
	 */
	JPanel drawEnd() {
		JPanel endPan_ref = new JPanel();
		BoxLayout lay_ref = new BoxLayout(endPan_ref, BoxLayout.Y_AXIS);
		endPan_ref.setLayout(lay_ref);
		endPan_ref.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		Box headBox_ref = new Box(BoxLayout.X_AXIS);
		JLabel titleLab_ref = new JLabel("Vielen Dank!", JLabel.CENTER);
		titleLab_ref.setFont(new Font(titleLab_ref.getFont().getFamily(), Font.PLAIN, 32));
		headBox_ref.add(titleLab_ref);
		headBox_ref.add(Box.createHorizontalStrut(1));
		ImageIcon firstStartIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/firstStartIcon.png"));
		headBox_ref.add(new JLabel(firstStartIcon_ref));
		endPan_ref.add(headBox_ref);
		
		endPan_ref.add(Box.createVerticalStrut(15));
		
		Box infoBoxWrapper_ref = new Box(BoxLayout.X_AXIS);
		infoBoxWrapper_ref.setBorder(new TitledBorder("Hinweis"));
		Box infoBox_ref = new Box(BoxLayout.Y_AXIS);
		infoBox_ref.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		infoBox_ref.add(new JLabel("Sie haben die Ersteinrichtung von TinyAdmin erfolgreich abgeschlossen!"));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Vergessen Sie nicht ihr Passwort zu notieren, dies muss bei jedem Start"));
		infoBox_ref.add(new JLabel("der Software eingegeben werden.")); 
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Momentan existiert leider noch keine Hilfefunktion,"));
		infoBox_ref.add(new JLabel("falls Sie also Fragen haben, dann schreiben Sie eine E-Mail an admin@tinyadmin.org"));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Vielen Dank, dass Sie sich die Zeit genommen haben und nun viel Spass"));
		infoBox_ref.add(new JLabel("mit der Software!"));
		infoBoxWrapper_ref.add(infoBox_ref);
		endPan_ref.add(infoBoxWrapper_ref);
		
		endPan_ref.add(Box.createVerticalStrut(15));
		Box buttonBox_ref = getButtonBox(1);
		endPan_ref.add(buttonBox_ref);
		
		return endPan_ref;
	} //endmethod drawEnd
	
	/**
	 *	<p>Zeichnet das Fenster zum Einrichten des Passwortes fuer die Verschluesselung.
	 *	Hier kann der Benutzer ein neues Passwort festlegen, welches dann zum Verschluesseln
	 *	aller Host- und MAC-Adressen, sowie der Passwoerter verwendet wird.</p>
	 *	<p>Die Buttons werden, wie bei jedem Teilschritt des Wizzards, ueber die Methode 
	 *	<i>getButtons()</i> erzeugt.</p>
	 *
	 *
	 *	@return Panel, welches das GUI fuer das Einstellen des Krypto-Passwortes fasst.
	 *	@see #getButtonBox(int pos)
	 */
	JPanel drawPWD() {
		JPanel pwdPan_ref = new JPanel();
		BoxLayout lay_ref = new BoxLayout(pwdPan_ref, BoxLayout.Y_AXIS);
		pwdPan_ref.setLayout(lay_ref);
		pwdPan_ref.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		Box infoBoxWrapper_ref = new Box(BoxLayout.X_AXIS);
		Box infoBox_ref = new Box(BoxLayout.Y_AXIS);
		infoBox_ref.setBorder(new TitledBorder("Hinweis"));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Der erste Schritt besteht darin, ein Passwort für die Verschlüsselung ihrer"));
		infoBox_ref.add(new JLabel("persönlichen Einstellungen festzulegen."));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Wählen Sie also nun bitte ein Passwort und geben Sie es zur Verifizierung"));
		infoBox_ref.add(new JLabel("erneut ein. "));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBox_ref.add(new JLabel("Dieses Passwort wird bei jedem Start abgefragt: Merken Sie es sich daher gut."));
		infoBox_ref.add(Box.createVerticalStrut(5));
		infoBoxWrapper_ref.add(infoBox_ref);
		infoBoxWrapper_ref.add(Box.createHorizontalStrut(10));
		ImageIcon pwdIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/pwdIcon.png"));
		infoBoxWrapper_ref.add(new JLabel(pwdIcon_ref));
		infoBoxWrapper_ref.add(Box.createHorizontalStrut(5));
		pwdPan_ref.add(infoBoxWrapper_ref);
		
		pwdPan_ref.add(Box.createVerticalStrut(15));
		
		Box pwdBox_ref = new Box(BoxLayout.Y_AXIS);
		pwdBox_ref.setBorder(new TitledBorder("Passworteingabe"));
		Box pwdBox1_ref = new Box(BoxLayout.X_AXIS);
		JLabel pwdLab1_ref = new JLabel("Passwort:");
		if (pwdField1_ref == null) {
			pwdField1_ref = new JPasswordField(5);
		} //endif
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
		if (pwdField2_ref == null) {
			pwdField2_ref = new JPasswordField(5);
		} //endif
		pwdBox2_ref.add(pwdLab2_ref);
		pwdBox2_ref.add(Box.createHorizontalStrut(2));
		pwdBox2_ref.add(pwdField2_ref);
		pwdBox2_ref.add(Box.createHorizontalStrut(65));
		pwdBox_ref.add(pwdBox2_ref);
		
		pwdPan_ref.add(pwdBox_ref);
		
		return pwdPan_ref;
	} //endmethod drawPWD
	
	/**
	 *	<p>Erstellt die Buttons fuer das Wizzard-GUI in Abhaengigkeit der uebergebenen Variablen
	 *	<i>int pos</i>. Das Verhalten ist wie folgt geregelt:</p>
	 *	<ul>
	 *		<li>-1:<ul>
	 *			<li>Erstellt einen <i>Abbrechen-</i>, <i>Zurueck-</i> und <i>Weiter-Button</i>.</li></ul>
	 *		</li>
	 *		<li>0:<ul>
	 *			<li>Erstellt einen <i>Abbrechen-</i> und einen <i>Weiter-Button</i>.</li></ul>
	 *		</li>
	 *		<li>1:<ul>
	 *			<li>Erstellt einen <i>Zurueck-</i> und einen <i>Fertig-Button</i>.</li></ul>
	 *		</li>
	 *	</ul>
	 *
	 * 	@return Die Box, welche die Buttons fuer das Wizzard-GUI fasst.
	 */
	private Box getButtonBox(int pos) {
		Box buttonBox_ref = new Box(BoxLayout.X_AXIS);
		
		if (pos == -1) {
			ImageIcon cancelIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/cancelIcon.png"));
			JButton cancelBut_ref = new JButton("Abbrechen", cancelIcon_ref);
			cancelBut_ref.addActionListener(new ButtonListener());
			buttonBox_ref.add(cancelBut_ref);
			buttonBox_ref.add(Box.createHorizontalStrut(5));
			
			ImageIcon backIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/backIcon.png"));
			JButton backBut_ref = new JButton("Zurück", backIcon_ref);
			backBut_ref.addActionListener(new ButtonListener());
			buttonBox_ref.add(backBut_ref);
			buttonBox_ref.add(Box.createHorizontalStrut(5));
			
			ImageIcon nextIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/nextIcon.png"));
			JButton nextBut_ref = new JButton("Weiter", nextIcon_ref);
			nextBut_ref.addActionListener(new ButtonListener());
			buttonBox_ref.add(nextBut_ref);
		} else if (pos == 0) {
			ImageIcon cancelIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/cancelIcon.png"));
			JButton cancelBut_ref = new JButton("Abbrechen", cancelIcon_ref);
			cancelBut_ref.addActionListener(new ButtonListener());
			buttonBox_ref.add(cancelBut_ref);
			buttonBox_ref.add(Box.createHorizontalStrut(5));
			
			ImageIcon nextIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/nextIcon.png"));
			JButton nextBut_ref = new JButton("Weiter", nextIcon_ref);
			nextBut_ref.addActionListener(new ButtonListener());
			buttonBox_ref.add(nextBut_ref);
		} else if (pos == 1) {
			ImageIcon backIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/backIcon.png"));
			JButton backBut_ref = new JButton("Zurück", backIcon_ref);
			backBut_ref.addActionListener(new ButtonListener());
			buttonBox_ref.add(backBut_ref);
			buttonBox_ref.add(Box.createHorizontalStrut(5));
			
			ImageIcon doneIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/testIcon.png"));
			JButton doneBut_ref = new JButton("Fertig", doneIcon_ref);
			doneBut_ref.addActionListener(new ButtonListener());
			buttonBox_ref.add(doneBut_ref);
		} //endif
		
		return buttonBox_ref;
	} //endmethod getButtonBox
	
	/**
	 *	Erzeugt ein Dialog-Fenster mit Hilfe der <i>JOptionPane</i>, welches den Benutzer fragt,
	 *	ob er den Wizzard wirklich beenden moechte.
	 */
	private void doExit() {
		Object[] options = {"Ja", "Nein"};
    	int n = JOptionPane.showOptionDialog(frame_ref, "Sie müssen den Wizzard abschließen, um die Software " +
    										"verwenden zu können:\n\n" + "Wollen Sie wirklich abbrechen?", 
    										"Beenden", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
    										null, options, options[1]);
    	if (n == 0) {
    		System.exit(0);
    	} //endif
	} //endmethod doExit
	
	/**
	 *	Liefert eine Referenz auf das Fenster (den JFrame) zurueck.
	 * 	
	 * 	@return Referenz auf das Fenster des Wizzards.
	 */
	JFrame getFrame() {
		return frame_ref;
	} //endmethod getFrame
	
	/**
	 *	Erzeugt eine neue Einstellungs-Matrix mit Standardeintraegen und liefert diese zurueck.
	 *	Es wird sowohl ein Standard-Hosteintrag, als auch ein Standard-Kommando erstellt.
	 *
	 * 	@return Einstellungs-Matrix mit Standardeinstellungen.
	 */
	String[][][] getDefaultSettings() {
		String[][][] retVal_ref = new String[2][1][];
		retVal_ref[0][0] = new String[]{"Host #1", "0.0.0.0", "22", "root", "", "", "-----", "0:0:0:0:0:0", "Debian"};
		retVal_ref[1][0] = new String[mainGui_ref.getOSValues().length + 1];
		for (int i=0; i<retVal_ref[1][0].length; i++) {
			retVal_ref[1][0][i] = "";
		} //endfor
		retVal_ref[1][0][0] = "Kommando #1";
		
		return retVal_ref;
	} //endmethod getDefaultSettings
	
	// --- Listener
	/**
	 *	<p>Listener fuer alle verwendeten Buttons, bietet folgende Moeglichkeiten:</p>
	 *	<ul>
	 *		<li>Abbrechen:<ul>
	 *			<li>Ruft die Methode <i>doExit()</i> auf.</li></ul>
	 *		</li>
	 *
	 *		<li>Weiter:<ul>
	 *			<li>Setzt den Einstellungswizzard fort. Die Variable <i>int num</i> wird inkrementiert und
	 *			anschliessend die <i>drawGUI()</i> Methode erneut aufgerufen. Zuvor werden noch alle
	 *			im aktuellen Schritt getaetigten Einstellungen gespeichert und notfalls eine Fehlermeldung
	 *			erzeugt</li></ul>
	 *		</li>
	 *
	 *		<li>Zurueck:<ul>
	 *			<li>Springt einen Schritt zurueck: die Variable <i>int num</i> wird dekrementiert und
	 *			anschliessend die <i>drawGUI()</i> Methode erneut aufgerufen.</li></ul>
	 *
	 *		<li>Fertig:<ul>
	 *			<li>Schliesst den Wizzard.</li></ul>
	 *		</li>
	 *	</ul>
	 *
	 *	@version 0.3 von 06.2011
	 *
	 *	@author Tobias Burkard
	 */
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev_ref) {
			if (ev_ref.getActionCommand().equals("Abbrechen")) {
				doExit();
			} else if (ev_ref.getActionCommand().equals("Weiter")) {
				if (num == 1) {
					boolean oneIsAscii = mainGui_ref.getMainC().getTestHelfer().isAscii(new String(pwdField1_ref.getPassword()));
					boolean twoIsAscii = mainGui_ref.getMainC().getTestHelfer().isAscii(new String(pwdField2_ref.getPassword()));
					if (!oneIsAscii || !twoIsAscii) {
						JOptionPane.showMessageDialog(frame_ref, "Bitte verwenden Sie nur ASCII-Zeichen:\n\n" +
								"!\"#$%&'()*+,-./0123456789:;<=>?\n" +
								"@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\n" +
								"`abcdefghijklmnopqrstuvwxyz{|}~", "Fehler", JOptionPane.ERROR_MESSAGE);
					} else if ((new String(pwdField1_ref.getPassword())).equals("")) {
						JOptionPane.showMessageDialog(frame_ref, "Sie müssen ein Passwort eingeben!", "Fehler", JOptionPane.ERROR_MESSAGE);
					} else if (!(new String(pwdField1_ref.getPassword())).equals("") &&
							(new String(pwdField2_ref.getPassword())).equals("")) {
						JOptionPane.showMessageDialog(frame_ref, "Bitte geben Sie das Passwort zur Bestätigung erneut ein!", "Fehler", JOptionPane.ERROR_MESSAGE);
					} else if (!(new String(pwdField1_ref.getPassword())).equals("") && 
							!(new String(pwdField1_ref.getPassword())).equals(new String(pwdField2_ref.getPassword()))) {
						JOptionPane.showMessageDialog(frame_ref, "Die Passwörter stimmen nicht überein!", "Fehler", JOptionPane.ERROR_MESSAGE);
					} else {
						TinyAdminGUI.pwd_class = new String(pwdField1_ref.getPassword());
						mainGui_ref.initializeSettings(getDefaultSettings());
						num++;
						drawGUI();
					} //endif
				} else if (num == 0) {
					num++;
					drawGUI();
				} else if (num == 2) {
					String[][][] settings_ref = addHost_ref.getContent(frame_ref);
					mainGui_ref.initializeSettings(settings_ref);
					num++;
					drawGUI();
				} //endif
			} else if (ev_ref.getActionCommand().equals("Zurück")) {
				num--;
				drawGUI();
			} else if (ev_ref.getActionCommand().equals("Fertig")) {
				mainGui_ref.drawGUI();
				frame_ref.dispose();
				frame_ref = null;
			} //endif
		} //endmethod actionPerformed
	} //endclass ButtonListener
	
	/**
	 *	<p>Listener fuer das Fenster: ueberschreibt die Methode, welche bei einem Klick auf den 
	 *	Schliessen-Fensterbutton ausgeloest wird. So kann beim Schliessen ein Dialog-Fenster
	 *	generiert werden, das den Benutzer erneut fragt, ob er den Wizzard wirklich schliessen moechte.</p>
	 *	<p>Hierfuer wird die Methode <i>doExit()</i> aufgerufen.</p>
	 *	
	 *	@see FirstStartGUI#doExit()
	 *
	 *	@version 0.3 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class FrameListener implements WindowListener {
		public void windowActivated(WindowEvent ev_ref) {
        } //endmethod windowActivated
		
        public void windowClosed(WindowEvent ev_ref) {
        } //endmethod windowClosed
        
        public void windowClosing(WindowEvent ev_ref) {
        	doExit();
        } //endmethod windowClosing
        
        public void windowDeactivated(WindowEvent ev_ref) {
        } //endmethod windowDeactivated
        
        public void windowDeiconified(WindowEvent ev_ref) {
        } //endmethod windowDeiconified
        
        public void windowIconified(WindowEvent ev_ref) {
        } //endmethod windowIconified
        
        public void windowOpened(WindowEvent ev_ref) {
        } //endmethod windowOpened
	} //endclass FrameListener
	
} //endclass FirstStartGUI