package de.home.tinyadmin;

//--- Importe
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.Utilities;

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
--	CLASS: TinyAdminGUI 															--
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
*	<p>Dies stellt das Haupt-GUI der Anwendung dar und kuemmert sich um die Interaktion mit dem Benutzer.
*	Die Hauptmethode bildet <i>drawGUI()</i>, welche das tatsaechliche Zeichnen uebernimmt.</p> 
*	<p>Beim Erzeugen eines Objektes dieser Klassen wird versucht, die bereits gespeicherten Einstellungen zu laden, 
*	falls diese vorhanden sind. Zudem hat ein Objekt dieser Klasse Zugriff auf alle Methoden des
*	Hauptprogramms <i>TinyAdminC</i> ueber eine beim Erzeugen uebergebene Referenz auf dieses.</p>
*
*	@see TinyAdminSettingsGUI
*
* 	@version 0.2 von 06.2011
*
* 	@author Tobias Burkard
*/
class TinyAdminGUI {
	// --- Attribute
	private TinyAdminC tAShell_ref;							// Referenz auf die Programm-Hauptklasse
	private IOHelfer helfer_ref;							// Referenz auf die Input-/Outputhelfer Klasse
	private TestHelfer test_ref;							// Referenz auf die Testhelfer Klasse
	private Object[][] mainHosts_ref;						// Beinhaltet Namen und JCheckBoxes der Hosts
	private JButton[] actionButtons_ref;					// Die Hauptfunktions-JButtons des HauptGUIs
	private JComboBox eCBox_ref;							// Auswahl der Hostnamen in der "Springe-Zu" Funktion
	private JComboBox psCBox_ref;							// Auswahl der Prozessanzahl		
	private JFrame mainFrame_ref;							// Der JFrame des HauptGUIs
	private JPanel mainBPan_ref;							// Einziges Panel von mainFrame_ref (HauptGUI)
	private JScrollPane scroller_ref;						// Fasst die JTextArea statusField_ref des HauptGUI
	private JTextArea statusField_ref;						// Das Statustextfeld des HauptGUIs
	private Box mainSelBox_ref;								// Fasst die Hostauswahl und den "alle"-JButton
	private Box mainBBox_ref;								// Fasst die Hostauswahl
	private Box topBox_ref;									// Fasst die mainSelBox_ref und das Icon
	private TinyAdminSettingsGUI settingsGUI_ref;			// Referenz auf das Einstellungs-GUI
	private String[][][] settings_ref;						// Die Host-Tabellenmatrix in String-Form
	private HighlightPainter painter_ref;					// Das Zeichenobjekt zum markieren von Text in statusField_ref
															// siehe highlightText(int start, int end)
	@SuppressWarnings("unused")
	private Object highlight_ref;							// Das momentan gesetzte Highlight in statusField_ref
	private JMenuItem einstellungenMenuIt_ref;				// Der Hauptmenueeintrag fuer das Einstellungsmenue
	private JComboBox ccCombo_ref;							// Fasst die vom Benutzer selbst hinzugefuegten Kommandos
	private String lastCmd_ref;								// Das zuletzt ausgefuehrte, selbst über das Settings-GUI 
															// erstellte Kommando.
	private JButton ccBut_ref;								// "Ausfuehren"-Button fuer die eigenen Aktionen
	private JCheckBox asroot_ref;							// Legt fest ob der gewaehlte Custom-Command als root ausgefuehrt
															// werden soll
	
	// --- Konstruktoren
	/**	
	*	<p>Setzt die uebergebene Referenz auf das Hauptprogramm und initialisiert
	*	alle Hilfsobjekt wie z.B. die zur Datei Ein-/Ausgabe. Es werden die bereits gespeicherten Einstellungen, 
	*	falls vorhanden, geladen.</p> <p>Es werden auch alle, der Anwendung bekannten Aktionsknoepfe fuer die
	*	einzelnen Funktionen generiert.</p>
	*/
	TinyAdminGUI(TinyAdminC tAShell_ref) {
		this.tAShell_ref = tAShell_ref;
		this.helfer_ref = tAShell_ref.getIOHelfer();
		this.test_ref = tAShell_ref.getTestHelfer();
		actionButtons_ref = createActionButtons();
	} //endconstructor 
	
	// --- Methoden
	/**
	 *	<p>Diese Methode versucht zunaechst, ein natives Look&Feel mit Hilfe der <i>tryNativeLook()</i>-Methode
	 *	zu setzen. Im Anschluss daran wird das GUI gebaut und mit Hilfe der durch den <i>IOHelfer</i> eingelesenen
	 *	Werte gefuettert.</p> <p>Die Aktionsknoepfe im unteren Teil, werden durch eine eigene Methode,
	 *	<i>createActionButtons()</i>, generiert. Das Status-Textfeld wird auf den Standardtext mit der 
	 *	<i>setStandardText()</i>-Methode gesetzt.</p> <p>Der Hauptframe erhaelt eine Menubar, deren Elementen jeweils
	 *	ein <i>MenuListener</i> hinzugefuegt wird. Die Knoepfe erhalten jeweils einen <i>ButtonListener</i>.</p>
	 *
	 *	@see #tryNativeLook()
	 *	@see #createActionButtons()
	 *	@see #setStandardText()
	 *	@see MenuListener
	 *	@see ButtonListener
	 *	@see IOHelfer
	 */
	void drawGUI() {
		settings_ref = helfer_ref.readSettings();
		tryNativeLook();
		mainHosts_ref = new Object[settings_ref[0].length][2];
		for (int i=0; i<mainHosts_ref.length; i++) {
			mainHosts_ref[i][0] = new JLabel(settings_ref[0][i][0]);
			mainHosts_ref[i][1] = new JCheckBox();
			((JCheckBox)mainHosts_ref[i][1]).setSelected(true);
		} //endfor
		mainFrame_ref = new JFrame("TinyAdmin v0.2");
		ImageIcon frameIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/appIcon.png"));
		mainFrame_ref.setIconImage(frameIcon_ref.getImage());
		mainFrame_ref.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainBPan_ref = new JPanel();
		mainBBox_ref = new Box(BoxLayout.Y_AXIS);
		mainSelBox_ref = new Box(BoxLayout.X_AXIS);
		topBox_ref = new Box(BoxLayout.X_AXIS);
		ImageIcon appIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/appIcon.png"));
		JLabel appIconLab_ref = new JLabel(appIcon_ref);
		topBox_ref.add(mainSelBox_ref);
		topBox_ref.add(Box.createVerticalStrut(1));
		topBox_ref.add(appIconLab_ref);
		topBox_ref.add(Box.createVerticalStrut(1));
		
		JMenuBar menuBar_ref = new JMenuBar();
		JMenu dateiMenu_ref = new JMenu("Datei");
		JMenuItem beendenMenuIt_ref = new JMenuItem("Beenden");
		beendenMenuIt_ref.addActionListener(new MenuListener());
		dateiMenu_ref.add(beendenMenuIt_ref);
		JMenu bearbeitenMenu_ref = new JMenu("Bearbeiten");
		JMenuItem resetMenuIt_ref = new JMenuItem("Reset");
		resetMenuIt_ref.addActionListener(new MenuListener());
		einstellungenMenuIt_ref = new JMenuItem("Einstellungen");
		einstellungenMenuIt_ref.addActionListener(new MenuListener());
		bearbeitenMenu_ref.add(resetMenuIt_ref);
		bearbeitenMenu_ref.add(einstellungenMenuIt_ref);
		JMenu hilfeMenu_ref = new JMenu("Hilfe");
		JMenuItem infoMenuIt_ref = new JMenuItem("Über");
		infoMenuIt_ref.addActionListener(new MenuListener());
		hilfeMenu_ref.add(infoMenuIt_ref);
		menuBar_ref.add(dateiMenu_ref);
		menuBar_ref.add(bearbeitenMenu_ref);
		menuBar_ref.add(hilfeMenu_ref);
		mainFrame_ref.setJMenuBar(menuBar_ref);
		
		Box seleBox_ref = new Box(BoxLayout.Y_AXIS);
		int count = mainHosts_ref.length / 4;
		double cOne = ((double)mainHosts_ref.length) / 4.0;
		cOne = cOne - (int)cOne;
		if (cOne > 0.0) {
			count++;
		} //endif
		
		int co = 0;
		for (int i=0; i<count; i++) {
			Box lineBox_ref = new Box(BoxLayout.X_AXIS);
			int exEl = 0;
			if ((settings_ref[0].length % 4) > 0) {
				exEl = settings_ref[0].length % 4;
			} //endif
			
			if ((exEl > 0) && (i != (count-1))) {
				for (int j=0; j<4; j++) {
					lineBox_ref.add((JLabel)mainHosts_ref[co][0]);
					lineBox_ref.add((JCheckBox)mainHosts_ref[co][1]);
					
					if (j != 3) {
						lineBox_ref.add(new JLabel("  |  "));
					} //endif
					co++;
				} //endfor
			} else if ((exEl > 0) && (i == (count-1))) {
				for (int j=0; j<exEl; j++) {
					lineBox_ref.add((JLabel)mainHosts_ref[co][0]);
					lineBox_ref.add((JCheckBox)mainHosts_ref[co][1]);
					
					if (j != (exEl -1)) {
						lineBox_ref.add(new JLabel("  |  "));
					} //endif
					co++;
				} //endfor
			} else if (exEl == 0) {
				for (int j=0; j<4; j++) {
					if (co < mainHosts_ref.length) {
						lineBox_ref.add((JLabel)mainHosts_ref[co][0]);
						lineBox_ref.add((JCheckBox)mainHosts_ref[co][1]);
						
						if (j != 3) {
							lineBox_ref.add(new JLabel("  |  "));
						} //endif
					} //endif
					co++;
				} //endfor
			} //endif
			seleBox_ref.add(lineBox_ref);
		} //endfor
		JScrollPane seleScrollPane_ref = new JScrollPane(seleBox_ref);
		seleScrollPane_ref.setPreferredSize(new Dimension(600, 50));
		seleScrollPane_ref.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		seleScrollPane_ref.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		mainSelBox_ref.add(seleScrollPane_ref);
		mainSelBox_ref.add(Box.createHorizontalStrut(15));
		ImageIcon allSIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/allSIcon.png"));
		JButton allBut_ref = new JButton("alle", allSIcon_ref);
		allBut_ref.setToolTipText("Alle Elemente auswählen/abwählen.");
		allBut_ref.addActionListener(new ButtonListener());
		mainSelBox_ref.add(allBut_ref);
		
		mainSelBox_ref.setBorder(new TitledBorder("Host-Auswahl"));
		
		Box statusBox_ref = new Box(BoxLayout.Y_AXIS);
		statusField_ref = new JTextArea(20, 85);
		statusField_ref.setFont(new Font("Monospaced", Font.PLAIN, 12)); 
		setStandardText();
		statusField_ref.setEditable(false);
		statusField_ref.setLineWrap(true);
		statusField_ref.setWrapStyleWord(true);
		scroller_ref = new JScrollPane(statusField_ref);  
		scroller_ref.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller_ref.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		statusBox_ref.add(scroller_ref);
		ImageIcon resetIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/resetIcon.png"));
		JButton rsButton_ref = new JButton("Lösche Statusfeld", resetIcon_ref);
		rsButton_ref.setToolTipText("Setzt das Statusfeld zurück.");
		rsButton_ref.setActionCommand("delTA");
		rsButton_ref.addActionListener(new ButtonListener());
		Box subButtonBox_ref = new Box(BoxLayout.X_AXIS);
		subButtonBox_ref.add(rsButton_ref);
		subButtonBox_ref.add(Box.createHorizontalStrut(230));
		ImageIcon jumpIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/jumpIcon.png"));
		JButton eBut_ref = new JButton("Springe zu", jumpIcon_ref);
		eBut_ref.setToolTipText("Springt zum ausgewählten Eintrag im Statusfeld.");
		eBut_ref.setActionCommand("jump");
		eBut_ref.addActionListener(new ButtonListener());
		eCBox_ref = new JComboBox();
		for (int i=0; i<settings_ref[0].length; i++) {
			eCBox_ref.addItem(settings_ref[0][i][0]);
		} //endfor
		
		subButtonBox_ref.add(Box.createHorizontalStrut(170));
		subButtonBox_ref.add(eCBox_ref);
		subButtonBox_ref.add(Box.createHorizontalStrut(3));
		subButtonBox_ref.add(eBut_ref);
		
		statusBox_ref.add(Box.createVerticalStrut(3));
		statusBox_ref.add(subButtonBox_ref);
		statusBox_ref.add(Box.createVerticalStrut(5));
		statusBox_ref.setBorder(new TitledBorder("Status"));
		
		Box buttonBox_ref = new Box(BoxLayout.X_AXIS);
		
		Box psBox_ref = new Box(BoxLayout.X_AXIS);
		ImageIcon psIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/psIcon.png"));
		JLabel psLab_ref = new JLabel(psIcon_ref);
		psLab_ref.setToolTipText("Anzahl der Prozesse, die gleichzeitig gestartet werden sollen.");
		psCBox_ref = new JComboBox();
		for (int i=0; i<10; i++) {
			psCBox_ref.addItem((i+1)+"");
		} //endfor
		psCBox_ref.setSelectedIndex(2);
		psBox_ref.add(psLab_ref);
		psBox_ref.add(Box.createHorizontalStrut(3));
		psBox_ref.add(psCBox_ref);
		psBox_ref.add(Box.createHorizontalStrut(8));
		Box psHolderBox_ref = new Box(BoxLayout.Y_AXIS);
		psHolderBox_ref.add(Box.createVerticalStrut(15));
		psHolderBox_ref.add(psBox_ref);
		psHolderBox_ref.setBorder(new TitledBorder("Prozesse"));
		psHolderBox_ref.add(Box.createVerticalStrut(20));
		buttonBox_ref.add(psHolderBox_ref);
		buttonBox_ref.add(Box.createHorizontalStrut(5));
		JPanel functionPan_ref = new JPanel();
		GridLayout grid1_ref = new GridLayout(2,4);
		grid1_ref.setVgap(5);
		grid1_ref.setHgap(5);
		functionPan_ref.setLayout(grid1_ref);
		functionPan_ref.setBorder(new TitledBorder("Aktionen"));
		for (int i=0; i<actionButtons_ref.length; i++) {
			functionPan_ref.add(actionButtons_ref[i]);
		} //endfor
		functionPan_ref.add(new JLabel(""));
		buttonBox_ref.add(functionPan_ref);
		
		buttonBox_ref.add(Box.createHorizontalStrut(5));
		Box customCBox_ref = new Box(BoxLayout.Y_AXIS);
		customCBox_ref.setBorder(new TitledBorder("Eigene Kommandos"));
		customCBox_ref.add(Box.createHorizontalStrut(3));
		ccCombo_ref = new JComboBox();
		ccCombo_ref.setPreferredSize(new Dimension(170, ccCombo_ref.getPreferredSize().height));
		for (int i=0; i<settings_ref[1].length; i++) {
			ccCombo_ref.addItem(settings_ref[1][i][0]);
		} //endfor
		customCBox_ref.add(ccCombo_ref);
		customCBox_ref.add(Box.createVerticalStrut(5));
		Box actButBox_ref = new Box(BoxLayout.X_AXIS);
		customCBox_ref.add(actButBox_ref);
		ImageIcon actIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/wolIcon.png"));
		ccBut_ref = new JButton("Ausführen", actIcon_ref);
		ccBut_ref.setToolTipText("<html>Führt die gewählte Aktion aus.<br>Über das <i>Kommando-Tab</i> des Einstellungs-GUIs<br>" +
								"können neue Aktion definiert werden.");
		ccBut_ref.addActionListener(new ButtonListener());
		actButBox_ref.add(ccBut_ref);
		asroot_ref = new JCheckBox("als root ausführen?");
		actButBox_ref.add(asroot_ref);
		
		buttonBox_ref.add(customCBox_ref);
		
		Box cancQuitButtBox_ref = new Box(BoxLayout.X_AXIS);
		ImageIcon cancelIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/cancelIcon.png"));
		JButton cancelButton_ref = new JButton("Abbrechen", cancelIcon_ref);
		cancelButton_ref.setToolTipText("Bricht die momentan laufende Aktion ab.");
		cancelButton_ref.addActionListener(new ButtonListener());
		ImageIcon quitIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/exitIcon.png"));
		JButton qtButton_ref = new JButton("Verlassen", quitIcon_ref);
		qtButton_ref.setToolTipText("Das Programm verlassen.");
		qtButton_ref.addActionListener(new ButtonListener());
		cancQuitButtBox_ref.add(cancelButton_ref);
		cancQuitButtBox_ref.add(Box.createHorizontalStrut(5));
		cancQuitButtBox_ref.add(qtButton_ref);
		
		mainBBox_ref.add(topBox_ref);
		mainBBox_ref.add(Box.createVerticalStrut(2));
		mainBBox_ref.add(statusBox_ref);
		mainBBox_ref.add(Box.createVerticalStrut(5));
		mainBBox_ref.add(buttonBox_ref);
		mainBBox_ref.add(Box.createVerticalStrut(5));
		mainBBox_ref.add(cancQuitButtBox_ref);
		mainBBox_ref.add(Box.createVerticalStrut(5));
		
		mainBPan_ref.add(mainBBox_ref);
		
		mainFrame_ref.getContentPane().add(mainBPan_ref);
		mainFrame_ref.pack();
		mainFrame_ref.setResizable(false);
		mainFrame_ref.setVisible(true);
	} //endmethod drawGUI
	
	/**
	 *	<p>Versucht dem GUI ein natives Look&Feel zu verleihen. Hierzu macht es gebrauch von der statischen
	 *	Methode <i>setNativeLookAndFeel()</i> der Klasse <i>LookAndFeelHelfer</i>.</p> <p>Gelingt dies nicht, wird 
	 *	die ebenfalls statische Methode <i>setJavaLookAndFeel()</i> dieser Klasse aufgerufen und das GUI 
	 *	erhaelt den standard Java-Swing-Look.</p>
	 *
	 *	@see LookAndFeelHelfer
	 *	@see LookAndFeelHelfer#setNativeLookAndFeel()
	 *	@see LookAndFeelHelfer#setJavaLookAndFeel()
	 */
	private void tryNativeLook() {
		try {
			LookAndFeelHelfer.setNativeLookAndFeel();
		} catch (Exception ex_ref) {
			try {
				LookAndFeelHelfer.setJavaLookAndFeel();
			} catch (Exception e_ref) {
				e_ref.printStackTrace();
			} //endtry
		} //endtry
	} //endmethod tryNativeLook
	
	/**
	 *	Erzeugt ein <i>JButton[]</i>-Array, welches die im unteren GUI enthaltenen Aktions-Knoepfe
	 *	enthaelt. Die Knoepfe werden hier generiert, um das Programm so leichter erweiterbar zu machen.
	 *	Jeder Knopf erhaelt einen <i>Namen</i>, ein <i>Icon</i> und einen <i>Tooltip</i>. 
	 * 
	 * @return Array mit den Aktionsknoepfen fuer das untere Haupt-GUI.
	 */
	private JButton[] createActionButtons() {
		String[] names_ref = {"Update", "Reboot", "Shutdown", "Custom", "Test", "Ping", "WOL"};
		String[] icons_ref = {"updateIcon.png", "rebootIcon.png", "shutdownIcon.png", "customIcon.png",
								"testIcon.png", "pingIcon.png", "wolIcon.png"};
		String[] tooltips_ref = {"<html>Führt ein Software-Update für alle ausgewählten<br>Hosts durch.</html>",
								"Führt einen Neustart aller ausgewählten Hosts durch.",
								"Fährt alle ausgewählten Hosts herunter.",
								"<html>Öffnet ein Fenster, in dem ein eigener Befehl zur<br>Ausführung eingegeben werden kann.</html>",
								"<html>Testet den Online-/Offline-Status aller<br>ausgewählten Hosts.</html>",
								"Pingt alle ausgewählten Hosts an.",
								"<html>Wake-On-LAN: Versendet ein Magic-Packet,<br>um die ausgewählten Hosts zu starten/aufzuwecken.</html>"};
		
		JButton[] buttonArray_ref = new JButton[names_ref.length];
		for (int i=0; i<buttonArray_ref.length; i++) {
			ImageIcon icon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/"+icons_ref[i]));
			buttonArray_ref[i] = new JButton(names_ref[i], icon_ref);
			buttonArray_ref[i].setToolTipText(tooltips_ref[i]);
			buttonArray_ref[i].addActionListener(new ButtonListener());
		} //endfor
		
		return buttonArray_ref;
	} //endmethod createActionButtons
	
	/**
	 *	Setzt die Hosteinstellungen auf die uebergebene Matrix, speichert diese mit dem <i>IOHelfer</i>-Objekt
	 *	und ueberprueft mit Hilfe der Methode <i>validateSettings()</i>, ob das GUI auf Grund einer 
	 *	Veraenderung der Hostanzahl neu gezeichnet werden muss.
	 *	
	 *	@param settings_ref Die zu initialisierenden Settings.
	 *	@see IOHelfer#writeSettings(String[][][])
	 * 	@see #validateSettings()
	 */
	void initializeSettings(String[][][] settings_ref) {
		this.settings_ref = settings_ref;
		helfer_ref.writeSettings(settings_ref);
		validateSettings();
	} //endmethod initializeSettings
	
	/**
	 *	Ueberprueft ob das GUI auf Grund einer Veraenderung der Anzahl der Hosts neu gezeichnet werden muss
	 *	und tut dies bei Bedarf. Falls es keine Veraenderung der Anzahl gab, aber die Namen der Hosts oder
	 *	Benutzer-eigenen Befehle dennoch geaendert werden muessen, geschieht dies ohne das GUI neu zu zeichnen.
	 */
	private void validateSettings() {
		if (mainHosts_ref.length != settings_ref[0].length) {
			mainFrame_ref.dispose();
			drawGUI();
		} else {
			eCBox_ref.removeAllItems();
			for (int i=0; i<settings_ref[0].length; i++) {
				eCBox_ref.addItem(settings_ref[0][i][0]);
			} //endfor
			for (int i=0; i<settings_ref[0].length; i++) {
				for (int j=0; j<settings_ref[0][i].length; j++) {
					((JLabel)mainHosts_ref[i][0]).setText(settings_ref[0][i][0]);
				} //endfor
			} //endfor
		} //endif
	
		ccCombo_ref.removeAllItems();
		for (int i=0; i<settings_ref[1].length; i++) {
			ccCombo_ref.addItem(settings_ref[1][i][0]);
		} //endfor
	} //endmethod validateSettings
	
	/**
	 *	Selektiert oder Deselektiert die <i>JCheckBoxes</i> der Hostauswahl entsprechend einiger Kriterien.<ul>
	 *	<li>Sind mehr oder gleich viele Boxen selektiert wie nicht selektiert, so werden alle nicht selektiert.</li>
	 *	<li>Sind mehr nicht selektiert als selektiert, so werden alle selektiert.</li></ul>
	 */
	private void selectAll() {
		int nsel = 0;
		int sel = 0;
		
		for (int i=0; i<mainHosts_ref.length; i++) {
			if (((JCheckBox)mainHosts_ref[i][1]).isSelected()) {
				sel++;
			} else {
				nsel++;
			} //endif
		} //endfor
		
		if (sel >= nsel) {
			for (int i=0; i<mainHosts_ref.length; i++) {
				((JCheckBox)mainHosts_ref[i][1]).setSelected(false);
			} //endfor
		} else if (nsel > sel) {
			for (int i=0; i<mainHosts_ref.length; i++) {
				((JCheckBox)mainHosts_ref[i][1]).setSelected(true);
			} //endfor
		} //endif
	} //endmethod selectAll
	
	/**
	 *	<p>Scrollt die <i>JScrollPane</i>, welche das Status-Textfeld <i>statusField_ref</i> fasst, ganz nach unten.
	 *	So wird die Ansicht immer auf neu hinzugefuegte Informationen gesetzt und der Benutzer
	 *	muss nicht selbst nach unten scrollen.</p>
	 *	<p>Dies wird realisiert, indem die Stelle im Text auswaehlt wird, die ganz am Ende steht.</p>
	 */
	private void autoScroll() {
		statusField_ref.selectAll();
		statusField_ref.select(statusField_ref.getSelectionEnd(), statusField_ref.getSelectionEnd());
	} //endmethod autoScroll
	
	/**
	 *	Setzt den GUI-Cursor auf "warten".
	 *	<ul><li>1 steht fuer "warten"</li>
	 *	<li>0 steht fuer standard</li></ul>
	 *
	 * @param onOff "warten" Ein/Aus
	 */
	void initWaitCursor(int onOff) {
		if (onOff == 1) {
			mainFrame_ref.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			closeOpenWindows();
		} else if (onOff == 0) {
			mainFrame_ref.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} //endif
	} //endmethod initWaitCursor
	
	/**
	 * 	Zeigt eine Fehlermeldung im Haupt-GUI mit Hilfe der <i>JOptionPane</i> an.
	 * 
	 * 	@param message_ref Die Fehlermeldung, welche angezeigt werden soll.
	 */
	void displayError(String message_ref) {
		JOptionPane.showMessageDialog(mainFrame_ref, message_ref, "Fehler", JOptionPane.ERROR_MESSAGE);
	} //endmethod displayError
	
	/**
	 * 	Zeigt eine Informationsnachricht im Haupt-GUI mit Hilfe der <i>JOptionPane</i> an.
	 * 
	 * 	@param message_ref Die Meldung, welche angezeigt werden soll.
	 */
	void displayMsg(String message_ref) {
		JOptionPane.showMessageDialog(mainFrame_ref, message_ref, "Hinweis", JOptionPane.INFORMATION_MESSAGE);
	} //endmethod displayMsg
	
	/**
	 *	Wertet aus, welche <i>JCheckBoxes</i> der einzelnen Hosts im HauptGUI vom Benutzer ausgewaehlt wurden.
	 *	Dementsprechend wird ein <i>String[][]</i>-Array generiert, welches diesen Zustand abbildet.
	 *
	 *	@return Array der ausgewaehlten Hosteintraege im Haupt-GUI.
	 */
	String[][] getActionData() {
		int amount = 0;
		for (int i=0; i<mainHosts_ref.length; i++) {
			if (((JCheckBox)mainHosts_ref[i][1]).isSelected()) {
				amount++;
			} //endif
		} //endfor
		
		String[][] action_ref = new String[amount][7];
		int pos = 0;
		for (int i=0; i<mainHosts_ref.length; i++) {
			if (((JCheckBox)mainHosts_ref[i][1]).isSelected()) {
				action_ref[pos][0] = settings_ref[0][i][0];
				action_ref[pos][1] = settings_ref[0][i][1];
				action_ref[pos][2] = settings_ref[0][i][2];
				action_ref[pos][3] = settings_ref[0][i][3];
				action_ref[pos][4] = settings_ref[0][i][4];
				action_ref[pos][5] = settings_ref[0][i][5];
				action_ref[pos][6] = settings_ref[0][i][6];
				pos++;
			} //endif
		} //enfor
		
		return action_ref;
	} //endmethod getActionData
	
	/**
	 *	Liefert eine Referenz auf die momentan vom Haupt-GUI gehaltene Matrix der Einstellungen zurueck.
	 * 
	 *	@return Matrix der Einstellungen.
	 */
	String[][][] getSettings() {
		return settings_ref;
	} //endmethod getSettings
	
	/**
	 *	Liefert den zuletzt ausgefuehrten, Benutzer-eigenen Befehl zurueck.
	 *
	 *	@return Die zu letzt ausgefuehrte, Benutzer-eigene Aktion.
	 */
	String getRunCommand() {
		return lastCmd_ref;
	} //endmethod getRunCommand
	
	/**
	 *	Liefert eine Referenz auf die momentan gehaltene Liste der unterstuetzten Betriebssysteme zurueck.
	 * 
	 *	@return Liste der unterstuetzten Betriebssysteme.
	 */
	String[] getOSValues() {
		return new String[]{"CentOS", "Debian", "Fedora", "MacOSX", "Mandriva", "RedHat"};
	} //endmethod getSettings
		
	/**
	 *	Liefert eine Referenz auf das <i>TestHelfer</i>-Objekt mit Pruefwerkzeugen zurueck.
	 * 
	 *	@return Referenz auf das <i>TestHelfer</i>-Objekt.
	 *	@see TestHelfer
	 */
	TestHelfer getTestHelfer() {
		return test_ref;
	} //endmethod getTestHelfer
	
	/**
	 *	Liefert eine Referenz auf das <i>TinyAdminC</i>-Objekt <i>tAShell_ref</i> zurueck.
	 * 
	 *	@return Referenz auf das <i>TinyAdminC</i>-Objekt.
	 *	@see TinyAdminC
	 */
	TinyAdminC getMainC() {
		return tAShell_ref;
	} //endmethod getMainC
	
	/**
	 *	Liefert eine Referenz auf das Hauptfenster der Anwendung, <i>mainFrame_ref</i> zurueck.
	 * 
	 *	@return Referenz auf das Hauptfenster der Anwendung.
	 */
	JFrame getFrame() {
		return mainFrame_ref;
	} //endmethod getFrame
	
	/**
	 *	Gibt die Anzahl der momentan vom Benutzer ausgewaehlten Anzahl an Prozessen zurueck.
	 *	Hierzu wird die Nummer des in <i>psCBox_ref</i> ausgewaehlten Elementes ausgelesen und
	 *	um 1 inkrementiert, da die Nummerierung mit 0 beginnt.
	 * 
	 * @return Anzahl der gewuenschten Prozesse.
	 */
	int getProcesses() {
		return (psCBox_ref.getSelectedIndex() + 1);
	} //endmethod getSelectedProcesses
	
	/**
	 *	Haengt den uebergebenen String an den im Status-Textfeld <i>statusField_ref</i> existierenden Text 
	 *	an und scrollt durch Aufruf der Methode <i>autoScroll()</i> ganz nach unten.
	 *
	 * 	@param s_ref Der anzuhaengende String.
	 * 	@see #autoScroll()
	 */
	void setStatusText(String s_ref) {
		statusField_ref.append(s_ref);
		autoScroll();
	} //endmethodsetStatusText
	
	/**
	 *	Setzt den Standardtext fuer das Status-Textfeld <i>statusField_ref</i>, welcher eine grobe 
	 *	Hilfestellung zur Bedienung der Anwendung gibt.
	 */
	private void setStandardText() {
		statusField_ref.setText("Bitte wählen Sie die Hosts aus, mit denen Sie arbeiten möchten.\n" +
				"Natürlich müssen Sie vorher die Verbindungs-Parameter über das Einstellungs-Menü festlegen: " +
				"Einer der Aktions-Buttons unten startet den Vorgang und " +
				"wenn Sie möchten, können Sie vorher noch die Anzahl der parallelen Prozesse festlegen.\n\n" + 
				"EIGENE BEFEHLE:\n\n" +
				"Über das Einstellungs-Menü können Sie eigene Kommandos definieren. Hierbei können Sie für " + 
				"jedes Betriebssystem einen Befehl angeben, der dann an dieses Kommando gekoppelt wird.\n" + 
				"Wählen Sie ihren selbst erstellten Befehl anschließend über das DropDown-Menü aus und betätigen " + 
				"Sie den \"Ausführen\"-Button.\n\n" + 
				"Natürlich können Sie auch über den Aktions-Knopf \"Custom\" ein eigenes Kommando ausführen: " + 
				"Die Software bittet Sie dann um die Eingabe eines Befehls zum Ausführen auf den gewählten Hosts.\n" + 
				"Hierbei wird aber das Betriebssystem der einzelnen Hosts nicht berückstichtigt, also vorsicht.\n\n"
				);
	} //endmethod setStandardText
	
	/**
	 *	Markiert die gesamte Zeile, in der die uebergebene Textposition <i>pos</i> steht.
	 * 	Die Farbe der Markierung wird am Anfang definiert.
	 * 
	 * @param pos Textposition, deren Zeile markiert werden soll.
	 */
	private void highlightText(int pos) {
		Color color_ref = new Color(184, 207, 229);
		statusField_ref.getHighlighter().removeAllHighlights();
    	highlight_ref = null;
    	Element elem_ref = Utilities.getParagraphElement(statusField_ref, pos);
    	int start = elem_ref.getStartOffset();
    	int end = elem_ref.getEndOffset();
		painter_ref = new DefaultHighlighter.DefaultHighlightPainter(color_ref);
		try {
			highlight_ref = statusField_ref.getHighlighter().addHighlight(start, end, painter_ref);
		} catch (Exception ex_ref) {
			ex_ref.printStackTrace();
		} //endtry
	} //endmethod highlightText
	
	/**
	 *	Setzt alle durch <i>createActionButtons()</i> erstellten Aktionsknoepfe, den Ausfuehren-Button 
	 *	und den Zugriff auf das Einstellungsmenue auf aktiv oder inaktiv. Dies ist noetig, nachdem der Benutzer 
	 *	eine Aktion eingeleitet hat, um ihn daran zu hindern, eine neue Aktion auszufuehren, waehrend die alte 
	 *	noch laeuft oder die Einstelungen im laufenden Betrieb zu veraendern.
	 *
	 * 	@param onOff <i>1</i> aktiviert die Knoepfe, <i>0</i> deaktiviert sie.
	 */
	void setButtonStatus(int onOff) {
		if (onOff == 1) {
			for (int i=0; i<actionButtons_ref.length; i++) {
				actionButtons_ref[i].setEnabled(true);
				einstellungenMenuIt_ref.setEnabled(true);
				ccBut_ref.setEnabled(true);
			} //endfor
		} else if (onOff == 0) {
			for (int i=0; i<actionButtons_ref.length; i++) {
				actionButtons_ref[i].setEnabled(false);
				einstellungenMenuIt_ref.setEnabled(false);
				ccBut_ref.setEnabled(false);
			} //endfor
		} //endif
	} //endmethod setButtonStatus
	
	/**
	 *	Ueberprueft ob das Einstellungs-Menue geoeffnet ist und schliesst es bei Bedarf.
	 */
	void closeOpenWindows() {
		if (settingsGUI_ref != null) {
			if (settingsGUI_ref.getFrame() != null) {
				settingsGUI_ref.getFrame().dispose();
			} //endif
			settingsGUI_ref = null;
		} //endif
	} //endmethod closeOpenWindows
	
	// --- Innere Klassen
	
	// --- Listener
	/**
	 *	Listener fuer die Menubar des Haupt-GUIs.
	 *	Moegliche Aktionen:
	 *	<ul>
	 *		<li>Beenden<ul>
	 *				<li>Beendet das Programm.</li></ul>
	 *		</li>
	 *		<li>Ueber<ul>
	 *				<li>Blendet das Info-Fenster mit Hilfe der <i>JOptionPane</i> ein.</li></ul>
	 *		</li>
	 *		<li>Reset<ul>
	 *				<li>Markiert alle Hosts, setzt das Status-Textfeld zurueck und setzt sowohl die
	 *				Prozess-Auswahl, als auch die Auswahl fuer das Benutzer-eigene Kommando
	 *				auf den Standardeintrag.</li></ul>
	 *		</li>
	 *		<li>Einstellungen<ul>
	 *				<li>Zeichnet das Einstellungs-GUI.</li></ul>
	 *		</li>
	 *	</ul>
	 *
	 * 	@version 0.2 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent ev_ref) {
			if (ev_ref.getActionCommand().equals("Beenden")) {
				System.exit(0);
			} else if (ev_ref.getActionCommand().equals("Über")) {
				ImageIcon infoIcon_ref = new ImageIcon(ClassLoader.getSystemResource("de/home/tinyadmin/resource/appIcon.png"));
				JOptionPane.showMessageDialog(mainFrame_ref, "TinyAdmin v0.2\n\n(c) Tobias Burkard 2011\n\n" + 
											"Paralleles Administrieren von UN*X (-ähnlichen) Systemen über das Netzwerk.\n\n" +
											"Diese Software steht unter der GPLv2-Lizenz und kommt OHNE JEGLICHE GEWÄHRLEISTUNG,\n" +
											"darf jedoch frei unter den Bedingungen dieser Lizenz vertrieben werden. Sie sollten eine\n" +
											"Kopie davon zusammen mit dieser Software erhalten haben.\n\n" +
											"Falls nicht, oder falls Sie nähere Informationen benötigen, besuchen Sie bitte \n" +
											"die Website des Autors unter http://www.tinyadmin.org .",
											"Über", JOptionPane.INFORMATION_MESSAGE, infoIcon_ref);
			} else if (ev_ref.getActionCommand().equals("Reset")) {
				for (int i=0; i<mainHosts_ref.length; i++) {
					((JCheckBox)mainHosts_ref[i][1]).setSelected(true);
				} //endfor
				setStandardText();
				eCBox_ref.setSelectedIndex(0);
				psCBox_ref.setSelectedIndex(2);
				ccCombo_ref.setSelectedIndex(0);
			} else if (ev_ref.getActionCommand().equals("Einstellungen")) {
				settingsGUI_ref = new TinyAdminSettingsGUI(tAShell_ref.getGUI());
			} //endif
		} //endmethod actionPerformed
	} //endclass MenuListener
	
	/**
	 *	Listener fuer alle Knoepfe des Haupt-GUIs.
	 *	Moegliche Aktionen:
	 *	<ul>
	 *		<li>Verlassen<ul>
	 *				<li>Beendet das Programm.</li></ul>
	 *		</li>
	 *		<li>Abbrechen<ul>
	 *				<li>Beendet alle laufenden Aktionen.</li></ul>
	 *		</li>
	 *		<li>alle<ul>
	 *				<li>Ruft die <i>selectAll()</i>-Methode auf.</li></ul>
	 *		</li>
	 *		<li>delTa (Status-TextFeld loeschen)<ul>
	 *				<li>ruft die <i>setStandardText()</i>-Methode auf.</li></ul>
	 *		</li>
	 *		<li>jump (Springe zu)<ul>
	 *				<li>Springt zum in der SelectionBox ausgewaehlten Hosteintrag.</li></ul>
	 *		</li>
	 *		<li>Update, Reboot, Shutdown, Test, Ping, WOL, Custom<ul>
	 *				<li>Fuehrt die entsprechende Aktion aus. Bei Custom wird ein neues <i>CustomCMDHelfer</i>-
	 *				Objekt erzeugt, welches den Befehl vom Benutzer abfragt.</li></ul>
	 *		</li>
	 *		<li>Ausfuehren<ul>
	 *			<li>Fuehrt das selektierte, Benutzer-eigene Kommando aus.</li></ul>
	 *		</li>
	 *	</ul>
	 *
	 * 	@version 0.2 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev_ref) {
			if (ev_ref.getActionCommand().equals("Verlassen")) {
				System.exit(0);
			} else if (ev_ref.getActionCommand().equals("Abbrechen")) {
				tAShell_ref.forceAbbort();
			} else if (ev_ref.getActionCommand().equals("alle")) {
				selectAll();
			} else if (ev_ref.getActionCommand().equals("delTA")) {
				setStandardText();
			} else if (ev_ref.getActionCommand().equals("jump")) {
				Pattern pattern_ref = Pattern.compile("--- FÜHRE.*" + (settings_ref[0][eCBox_ref.getSelectedIndex()][0]));
			    Matcher matcher_ref = pattern_ref.matcher(statusField_ref.getText());
			 
			    if (matcher_ref.find()) {
			    	int index = matcher_ref.start();
			    	statusField_ref.setCaretPosition(index);
			    	scroller_ref.getVerticalScrollBar().setValue(index);
			    	highlightText(index);
			    } //endif
			} else if (ev_ref.getActionCommand().equals("Update")) {
				if (mainHosts_ref.length > 0) {
					statusField_ref.setText("Bitte warten, führe Software-Update durch...\n");
					tAShell_ref.performAction("update", true, getActionData(), getProcesses());
				} else {
					displayError("Sie haben keine Hosts angegeben,\nbitte holen Sie dies in den Einstellungen nach.");
				} //endif
			} else if (ev_ref.getActionCommand().equals("Reboot")) {
				if (mainHosts_ref.length > 0) {
					statusField_ref.setText("Bitte warten, führe Reboot durch...\n");
					tAShell_ref.performAction("reboot", true, getActionData(), (psCBox_ref.getSelectedIndex()+1));
				} else {
					displayError("Sie haben keine Hosts angegeben,\nbitte holen Sie dies in den Einstellungen nach.");
				} //endif
			} else if (ev_ref.getActionCommand().equals("Shutdown")) {
				if (mainHosts_ref.length > 0) {
					statusField_ref.setText("Bitte warten, führe Shutdown durch...\n");
					tAShell_ref.performAction("shutdown", true, getActionData(), (psCBox_ref.getSelectedIndex()+1));
				} else {
					displayError("Sie haben keine Hosts angegeben,\nbitte holen Sie dies in den Einstellungen nach.");
				} //endif
			} else if (ev_ref.getActionCommand().equals("Test")) {
				if (mainHosts_ref.length > 0) {
					statusField_ref.setText("Bitte warten, führe Test durch...\n");
					tAShell_ref.performAction("test", false, getActionData(), (psCBox_ref.getSelectedIndex()+1));
				} else {
					displayError("Sie haben keine Hosts angegeben,\nbitte holen Sie dies in den Einstellungen nach.");
				} //endif
			} else if (ev_ref.getActionCommand().equals("Ping")) {
				if (mainHosts_ref.length > 0) {
					statusField_ref.setText("Bitte warten, führe Ping durch...\n");
					tAShell_ref.performAction("ping", false, getActionData(), (psCBox_ref.getSelectedIndex()+1));
				} else {
					displayError("Sie haben keine Hosts angegeben,\nbitte holen Sie dies in den Einstellungen nach.");
				} //endif
			} else if (ev_ref.getActionCommand().equals("WOL")) {
				if (mainHosts_ref.length > 0) {
					statusField_ref.setText("Bitte warten, führe Wake-On-LAN durch...\n");
					tAShell_ref.performAction("wol", false, getActionData(), (psCBox_ref.getSelectedIndex()+1));
				} else {
					displayError("Sie haben keine Hosts angegeben,\nbitte holen Sie dies in den Einstellungen nach.");
				} //endif
			} else if (ev_ref.getActionCommand().equals("Custom")) {
				if (mainHosts_ref.length > 0) {
					statusField_ref.setText("Eigenes Kommando: warte auf Benutzereingabe.\n");
					new CustomCMDHelfer(tAShell_ref.getGUI());
				} else {
					displayError("Sie haben keine Hosts angegeben,\nbitte holen Sie dies in den Einstellungen nach.");
				} //endif
			} else if (ev_ref.getActionCommand().equals("Ausführen")) {
				if (mainHosts_ref.length > 0) {
					lastCmd_ref = (String)ccCombo_ref.getItemAt(ccCombo_ref.getSelectedIndex());
					statusField_ref.setText("Führe eigenes Kommando \"" + lastCmd_ref + "\" für alle ausgewählten Hosts durch.\n");
					tAShell_ref.performCustomAction(settings_ref[1][ccCombo_ref.getSelectedIndex()], asroot_ref.isSelected(), getActionData(), (psCBox_ref.getSelectedIndex()+1));
				} else {
					displayError("Sie haben keine Hosts angegeben,\nbitte holen Sie dies in den Einstellungen nach.");
				} //endif
			} //endif
		} //endmethod actionPerformed
	} //endclass ButtonListener
	
} //endclass TinyAdminGUI