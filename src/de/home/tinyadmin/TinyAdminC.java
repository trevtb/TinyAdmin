package de.home.tinyadmin;

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
-- CLASS: TinyAdminC 																--
-- 																					--
--------------------------------------------------------------------------------------
-- 																					--
-- PROJECT: TinyAdmin 																--
-- 																					--
--------------------------------------------------------------------------------------
-- 																					--
-- SYSTEM ENVIRONMENT 																--
-- OS			Ubuntu 11.04 (Linux 2.6.38)											--
-- SOFTWARE 	JDK 1.6 															--
-- 																					--
--------------------------------------------------------------------------------------
------------------------------------------------------------------------------------*/

/**	
*	<p>Dies stellt die Hauptklasse der Anwendung dar und kuemmert sich um deren Start und
*	um die Erstellung aller noetigen Helferklassen, sowie des GUIs.</p> 
*	<p>Sie dient auch als Schnittstelle zwischen der vom Benutzer ueber das GUI ausgefuehrten Aktionen 
*	und der tatsaechlichen Verarbeitung durch die einzelnen Prozesse, welche auch hier generiert werden.</p>
*
* 	@version 0.2 von 06.2011
*
* 	@author Tobias Burkard
*/
public class TinyAdminC { 
	// --- Attribute
	private IOHelfer helfer_ref;	// Referenz auf die Input-/Outputhelfer Klasse
	private TinyAdminGUI gui_ref;	// Referenz auf das HauptGUI
	private MessageFacility msgFac_ref;	// Referenz auf die Nachrichtenfabrik
	private TestHelfer test_ref;	// Referenz auf die Testhelfer Klasse
	private int psCount;	// Anzahl der tatsaechlich aktiven Prozesse
	private Runnable[] taskRunArray_ref; // Liste der Runnable-Objekte
	
	// --- Klassenvariablen
	static boolean abbort;	// Laufende Aktionen bzw. deren Prozesse brechen ab, 
							// sobald diese Variable auf true gesetzt wird
	
	// --- Konstruktoren
	/**	
	*	Standardkonstruktor: Initialisiert alle wichtigen Hilfsobjekte/Variablen und das GUI-Objekt.
	*
	*	@see IOHelfer
	*	@see TestHelfer
	*	@see TinyAdminGUI
	*	@see MessageFacility
	*/
	public TinyAdminC() {
		helfer_ref = new IOHelfer(this);
		test_ref = new TestHelfer();
		gui_ref = new TinyAdminGUI(this);
		msgFac_ref = new MessageFacility(this);
		TinyAdminC.abbort = false;
	} //endconstructor
	
	// --- Methoden
	/**
	 *	Die <i>main()</i>-Methode der Anwendung. Es wird ein neues <i>TinyAdminC</i>-Objekt erstellt und
	 *	die <i>paintGUI()</i>-Methode darauf aufgerufen.
	 *
	 *	@see #paintGUI()
	 */
	public static void main(String[] args) {
		TinyAdminC main_ref = new TinyAdminC();
		main_ref.paintGUI();
	} //endmethod main
	
	/**
	 *	Liefert eine Referenz auf das Eingabe-/Ausgabe-Helfer Objekt, <i>IOHelfer</i>, zurueck.
	 *
	 * 	@return das <i>IOHelfer</i>-Objekt, welches Werkzeuge fuer die Datei Ein-/Ausgabe bereitstellt.
	 * 	@see IOHelfer
	 */
	IOHelfer getIOHelfer() {
		return helfer_ref;
	} //endmethod getIOHelfer
	
	/**
	 *	Liefert eine Referenz auf das Haupt-GUI Objekt, <i>TinyAdminGUI</i>, zurueck.
	 *
	 * 	@return das HauptGUI-Objekt.
	 * 	@see TinyAdminGUI
	 */
	TinyAdminGUI getGUI() {
		return gui_ref;
	} //endmethod getGUI
	
	/**
	 *	Liefert die Anzahl aller momentan aktiven oder kurz vor dem Start stehenden Prozesse zurueck.
	 *
	 * 	@return Anzahl der tatsaechlichen Prozesse.
	 */
	int getPSCount() {
		return psCount;
	} //endmethod getPSCount
	
	/**
	 *	<p>Beendet alle momentan laufenden Prozesse.</p>
	 *	<p>Jeder Prozess ueberprueft vor einer weiteren Iteration im Durchlauf seiner Programmschleife,
	 *	ob die statische Variable <i>abbort</i> der Klasse <i>TinyAdminC</i> noch auf <i>false</i> 
	 *	gesetzt ist.</p>
	 *	<p>Diese Methode macht sich dies zu nutze und setzt die <i>abbort</i>-Variable auf <i>true</i>
	 *	um so jede weitere Iteration aller laufenden Prozesse zu unterbinden.</p>
	 *	<p>Sobald der Benutzer eine neue Aktion ausfuehrt, wird der Status der Variable wieder
	 *	zurueck gesetzt. Es kann vorkommen, dass Verbindungen von SSHModulen festhaengen. Zur Sicherheit
	 *	wird deshalb noch die <i>kill()</i>-Methode auf jedem <i>TaskRunnable</i>-Objekt aufgerufen.</p>
	 */
	void forceAbbort() {
		if (taskRunArray_ref != null) {
			TinyAdminC.abbort = true;
			for (int i=0; i<taskRunArray_ref.length; i++) {
				((TaskRunnable)taskRunArray_ref[i]).kill();
			} //endfor
		} //endif
	} //endmethod forceAbbort
	
	/**
	 *	Liefert eine Referenz auf das <i>Nachrichtenfabrik</i>-Objekt, <i>MessageFacility</i> zurueck.
	 *
	 * 	@return das Nachrichtenfabrik-Objekt, <i>MessageFacility</i>.
	 *  @see MessageFacility
	 */
	MessageFacility getMsgFacility() {
		return msgFac_ref;
	} //endmethod getMsgFacility
	
	/**
	 *	Liefert eine Referenz auf das <i>TestHelfer</i>-Objekt, <i>TestHelfer</i> zurueck.
	 *
	 * 	@return das <i>TestHelfer</i>-Objekt.
	 * 	@see TestHelfer
	 */
	TestHelfer getTestHelfer() {
		return test_ref;
	} //endmethod getTestHelfer
	
	/**
	 *	Ruft auf der Haupt-GUI Referenz die <i>drawGUI()</i>-Methode auf um so das Zeichnen des GUIs auszuloesen.
	 *
	 *	@see TinyAdminGUI#drawGUI()
	 */
	private void paintGUI() {
		gui_ref.drawGUI();
	} //endmethod paintGUI
	
	/**
	 *	Setzt den Zaehler der tatsaechlich aktiven Prozesse um 1 herab.
	 */
	void decrementPSCount() {
		psCount--;
	} //endmethod decrementPSCount
	
	/**
	 *	<p>Initiiert den Wartecursor durch die <i>initWaitCursor()</i>-Methode, setzt alle Aktionsknoepfe 
	 *	mit Hilfe der <i>setButtonStatus()</i>-Methode auf inaktiv, der Prozesszaehler wird auf 0 gesetzt. 
	 *	Zuletzt wird sichergestellt, dass die <i>abbort</i>-Variable wieder auf <i>false</i> steht.</p>
	 *	<p>So wird die Anwendung auf das Ausfuehren einer Aktion vorbereitet.</p>
	 *
	 *	@see TinyAdminGUI#initWaitCursor(int)
	 *	@see TinyAdminGUI#setButtonStatus(int)
	 */
	private void prepareAction() {
		gui_ref.initWaitCursor(1);
		gui_ref.setButtonStatus(0);
		psCount = 0;
		TinyAdminC.abbort = false;
	} //endmethod initVariables
	
	/**
	 *	Dies ist die entscheidende Methode zur Ausfuehrung von allgemeinen Aktionen.<ul>
	 *	<li>Zuerst wird die <i>Nachrichtenfabrik</i> durch Aufruf von <i>renewFacility()</i> auf dem 
	 *	<i>MessageFacility</i>-Objekt geleert.</li>
	 *	<li>Im Anschluss wird die Aktion durch Aufruf der <i>prepareAction()</i>-Methode vorbereitet.</li>
	 *	<li>Nun wird durch Aufruf von <i>computeCommand()</i> fuer jeden Hosteintrag ein Kommando erstellt, 
	 *	welches vom Prozess ausgewertet werden kann und ihm mitteilt, was zu tun ist.</li>
	 *	<li>Der Name des Hosts, das errechnte Kommando, und die Art der Aktion <i>(update, reboot, shutdown, test, ping, wol, custom)</i>
	 *	werden von <i>computeCommand()</i> fuer jeden Hosteintrag in einer neuen Matrix gespeichert.</li>
	 *	<li>So gewonnene Matrizen werden durch Aufruf von <i>createTaskArray()</i> gleichmaessig auf alle von 
	 *	ihr erstellten Prozesse verteilt, bis keine Eintraege in <i>connectA_ref</i> zum Errechnen neuer Matrizen mehr uebrig sind.</li>
	 * 	<li>Der uebergebene <i>int processes</i> begrenzt die maximale Anzahl an Prozessen.</li> 
	 * 	<li>Die tatsaechliche Anzahl der zu aktivierenden Prozesse wird durch <i>countProcesses()</i> bestimmt und der 
	 * 	Prozesszaehler wird entsprechend gesetzt.</li></ul></p>
	 * 	<p>Ist all dies geschehen, wird die Methode <i>executeTaskArray()</i> aufgerufen, welche die fuer die
	 * 	TaskRunnables noetigen Prozesse erzeugt und startet.</p>
	 *	<p>Fuer Benutzer-eigene Kommandos gibt es die <i>performCustomAction()</i>-Methode.</p>
	 * 
	 * 	@param type_ref Die Art der Aktion (update, reboot, shutdown, test, ping, wol, custom).
	 * 	@param sudo Legt fest ob der Befehl als root ausgefuehrt werden soll.
	 * 	@param connectA_ref Die Matrix mit Hosteintraegen, fuer die diese Aktion durchgefuehrt werden soll.
	 * 	@param processes Die Anzahl vom Benutzer gewuenschter Prozesse.
	 * 	@see MessageFacility#renewFacility()
	 * 	@see #prepareAction()
	 * 	@see #computeCommand(String, boolean, String[][])
	 * 	@see #createTaskArray(String[][], int)
	 * 	@see #executeTaskArray(Runnable[], int)
	 * 	@see #performCustomAction(String[], boolean, String[][], int)
	 * 	@see TaskRunnable
	 */
	void performAction(String type_ref, boolean sudo, String[][] connectA_ref, int processes) {
		msgFac_ref.renewFacility();
		prepareAction();
		String[][] finalArray_ref = computeCommand(type_ref, sudo, connectA_ref);
		taskRunArray_ref = createTaskArray(finalArray_ref, processes);
		psCount = countProcesses(taskRunArray_ref);
		executeTaskArray(taskRunArray_ref, processes);
	} //endmethod performAction
	
	/**
	 *	<p>Dies ist die entscheidende Methode zur Ausfuehrung von Benutzer-eigenen Aktionen.<ul>
	 *	<li>Zuerst wird die <i>Nachrichtenfabrik</i> durch Aufruf von <i>renewFacility()</i> auf dem 
	 *	<i>MessageFacility</i>-Objekt geleert.</li>
	 *	<li>Im Anschluss wird die Aktion durch Aufruf der <i>prepareAction()</i>-Methode vorbereitet.</li>
	 *	<li>Nun wird fuer jeden Hosteintrag ein Kommando erstellt, welches vom Prozess ausgewertet werden kann 
	 *	und ihm mitteilt, was zu tun ist. Hierbei wird beruecksichtigt, welches Betriebssystem der Hosteintrag hat
	 *	und ob das Kommando als root ausgefuehrt werden soll: Dementsprechend wird das richtige Kommando zugeordnet/erstellt.</li>
	 *	<li>Der Name des Hosts, das errechnte Kommando, und die Art der Aktion (<i>customC</i>) werden fuer jeden Hosteintrag 
	 *	in einer neuen Matrix gespeichert.</li>
	 *	<li>So gewonnene Matrizen werden durch Aufruf von <i>createTaskArray()</i> gleichmaessig auf alle von 
	 *	ihr erstellten Prozesse verteilt, bis keine Eintraege in <i>connectA_ref</i> zum Errechnen neuer Matrizen mehr uebrig sind.</li>
	 * 	<li>Der uebergebene <i>int processes</i> begrenzt die maximale Anzahl an Prozessen.</li> 
	 * 	<li>Die tatsaechliche Anzahl der zu aktivierenden Prozesse wird durch <i>countProcesses()</i> bestimmt und der 
	 * 	Prozesszaehler wird entsprechend gesetzt.</li></ul></p>
	 * 	<p>Ist all dies geschehen, wird die Methode <i>executeTaskArray()</i> aufgerufen, welche die fuer die
	 * 	TaskRunnables noetigen Prozesse erzeugt und startet.</p> 
	 * 
	 * 	@param commands_ref Die Matrix mit spezifischen Kommandos fuer jedes Betriebssystem.
	 * 	@param sudo Bestimmt ob der Befehl als root ausgefuehrt werden soll.
	 * 	@param connectA_ref Die Matrix mit Hosteintraegen, fuer die diese Aktion durchgefuehrt werden soll.
	 * 	@param processes Die Anzahl vom Benutzer gewuenschter Prozesse.
	 * 	@see MessageFacility#renewFacility()
	 * 	@see #prepareAction()
	 * 	@see #createTaskArray(String[][], int)
	 * 	@see #executeTaskArray(Runnable[], int)
	 * 	@see TaskRunnable
	 */
	void performCustomAction(String[] commands_ref, boolean sudo, String[][] connectA_ref, int processes) {
		msgFac_ref.renewFacility();
		prepareAction();
		
		String[][] finalArray_ref = new String[connectA_ref.length][3];
		for (int i=0; i<connectA_ref.length; i++) {
			finalArray_ref[i][0] = connectA_ref[i][0];
			finalArray_ref[i][2] = "customC";
			String[] osNames_ref = gui_ref.getOSValues();
			
			for (int j=0; j<osNames_ref.length; j++) {
				if (connectA_ref[i][6].equals(osNames_ref[j])) {
					if (!test_ref.isRootLogin(connectA_ref[i][2])) {
						String command_ref = "";
						if (sudo) {
							String[] seps_ref = commands_ref[j+1].split("&&");
							command_ref = "echo \"" + connectA_ref[i][4] + "\" | sudo -S ";
							if (seps_ref.length > 1) {
								command_ref += seps_ref[0];
								for (int k=1; k<seps_ref.length; k++) {
									command_ref += "&& sudo " + seps_ref[k];
								} //endfor
							} else {
								command_ref += commands_ref[j+1];
							} //endif
						} else {
							command_ref = commands_ref[j+1];
						} //endif
						finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
												connectA_ref[i][3] + "#D3l1M3t3R#" + command_ref;
					} else if (test_ref.isRootLogin(connectA_ref[i][2])) {
						finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
												connectA_ref[i][3] + "#D3l1M3t3R#"+ commands_ref[j+1];
					} //endif
				} //endif
			} //endfor
		} //endfor
		
		taskRunArray_ref = createTaskArray(finalArray_ref, processes);
		psCount = countProcesses(taskRunArray_ref);
		executeTaskArray(taskRunArray_ref, processes);
	} //endmethod performCustomAction
	
	/**
	 *	Setzt das <i>Runnable[]</i>-Array auf <i>null</i> und nimmt somit alle Referenzen auf laufende Prozesse
	 *	aus dem Speicher.
	 */
	void closeAction() {
		taskRunArray_ref = null;
	} //endmethod closeAction
	
	/**
	 *	<p>Errechnet anhand des Typs der Aktion <i>(update, reboot, shutdown, test, ping, wol, custom)</i> fuer jeden Hosteintrag
	 *	in einer uebergebenen Matrix ein entsprechendes Kommando, welches vom Prozess verarbeitet werden kann.</p>
	 *	<p>Der Names des Hosteintrags, das errechnete Kommando und der Typ der Aktion werden in einer neuen
	 *	Matrix abgelegt und zurueckgegeben.</p>
	 *	<p>Handelt es sich um ein Benutzer-eigenes Schnell-Kommando, wird zudem ueberprueft ob es als root
	 *	ausgefuehrt werden soll.</p>
	 *
	 *	@param connectA_ref Die Matrix mit Hosteintraegen.
	 * 	@param type_ref Der Typ der Aktion (update, reboot, shutdown, test, ping, wol, custom).
	 * 	@return Matrix mit Name, Kommando und Aktionstyp jedes Hosteintrages der Eingangsmatrix.
	 */
	private String[][] computeCommand(String type_ref, boolean sudo, String[][] connectA_ref) {
		String[][] finalArray_ref = new String[connectA_ref.length][3];
		
		for (int i=0; i<connectA_ref.length; i++) {
			finalArray_ref[i][0] = connectA_ref[i][0];
			finalArray_ref[i][2] = type_ref;
			if (!test_ref.isRootLogin(connectA_ref[i][2])) {
				if (type_ref.equals("update")) {
					if (connectA_ref[i][6].equals("Debian")) {
						finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
												connectA_ref[i][3] + "#D3l1M3t3R#" + 
												"echo \"" + connectA_ref[i][4] + "\" | sudo -S apt-get update && " + 
												"echo \"" + connectA_ref[i][4] + "\" | sudo -S apt-get -y upgrade && " + 
												"echo \"" + connectA_ref[i][4] + "\" | sudo -S apt-get clean && exit";
					} else if (connectA_ref[i][6].equals("MacOSX")) {
						finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
						connectA_ref[i][3] + "#D3l1M3t3R#" + "echo \"" + connectA_ref[i][4] + 
						"\" | sudo -S softwareupdate -i -a && exit";
					} else if (connectA_ref[i][6].equals("RedHat") || connectA_ref[i][6].equals("CentOS") || connectA_ref[i][6].equals("Fedora")) {
						finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
						connectA_ref[i][3] + "#D3l1M3t3R#" + 
						"echo \"" + connectA_ref[i][4] + "\" | sudo -S yum -y update yum && " +
						"echo \"" + connectA_ref[i][4] + "\" | sudo -S yum -y update && exit";
					} else if (connectA_ref[i][6].equals("Mandriva")) {
						finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
						connectA_ref[i][3] + "#D3l1M3t3R#" + 
						"echo \"" + connectA_ref[i][4] + "\" | sudo -S urpmq --auto --auto-select && " + 
						"echo \"" + connectA_ref[i][4] + "\" | sudo -S urpmi --update --auto --auto-select && exit";
					} //endif
				} else if (type_ref.equals("reboot")) {
					finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
											connectA_ref[i][3] + "#D3l1M3t3R#" + "echo \"" + connectA_ref[i][4] + 
											"\" | sudo -S reboot";
				} else if (type_ref.equals("shutdown")) {
					finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
											connectA_ref[i][3] + "#D3l1M3t3R#" + "echo \"" + connectA_ref[i][4] + 
											"\" | sudo -S shutdown -h now";
				} else if (type_ref.equals("test")) {
					finalArray_ref[i][1] = connectA_ref[i][1];
				} else if (type_ref.equals("ping")) {
					finalArray_ref[i][1] = connectA_ref[i][1];
				} else if (type_ref.equals("wol")) {
					finalArray_ref[i][1] = connectA_ref[i][1] +"#D3l1M3t3R#"+ connectA_ref[i][5];
				} else if (type_ref.equals("custom")) {
					String cmd_ref = "";
					if (sudo) {
						cmd_ref = "echo \"" + connectA_ref[i][4] + "\" | sudo -S ";
						String[] seps_ref = connectA_ref[i][7].split("&&");
						if (seps_ref.length > 1) {
							cmd_ref += seps_ref[0];
							for (int k=1; k<seps_ref.length; k++) {
								cmd_ref += " && echo \"" + connectA_ref[i][4] + "\" | sudo -S " + seps_ref[k];
							} //endfor
						} else {
							cmd_ref += connectA_ref[i][7];
						} //endif
					} //endif
					if (!cmd_ref.equals("")) {
						connectA_ref[i][7] = cmd_ref;
					} //endif
					finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
					connectA_ref[i][3] + "#D3l1M3t3R#" + connectA_ref[i][7];
				} //endif
			} else if (test_ref.isRootLogin(connectA_ref[i][2])) {
				if (type_ref.equals("update")) {
					if (connectA_ref[i][6].equals("Debian")) {
						finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
											connectA_ref[i][3] + "#D3l1M3t3R#"+ "apt-get update && " + 
											"apt-get -y upgrade && apt-get clean && exit";
					} else if (connectA_ref[i][6].equals("MacOSX")) {
						finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
						connectA_ref[i][3] + "#D3l1M3t3R#"+ "softwareupdate -i -a && exit";
					} else if (connectA_ref[i][6].equals("RedHat") || connectA_ref[i][6].equals("CentOS") || connectA_ref[i][6].equals("Fedora")) {
						finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
						connectA_ref[i][3] + "#D3l1M3t3R#"+ "yum -y update yum && yum -y update && exit";
					} else if (connectA_ref[i][6].equals("Mandriva")) {
						finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
						connectA_ref[i][3] + "#D3l1M3t3R#"+ "urpmq --auto --auto-select && urpmi --update --auto --auto-select && exit";
					} //endif
				} else if (type_ref.equals("reboot")) {
					finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
											connectA_ref[i][3] + "#D3l1M3t3R#" + "reboot";
				} else if (type_ref.equals("shutdown")) {
					finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
					connectA_ref[i][3] + "#D3l1M3t3R#" + "shutdown -h now";
				} else if (type_ref.equals("test")) {
					finalArray_ref[i][1] = connectA_ref[i][1];
				} else if (type_ref.equals("ping")) {
					finalArray_ref[i][1] = connectA_ref[i][1];
				} else if (type_ref.equals("wol")) {
					finalArray_ref[i][1] = connectA_ref[i][1] +"#D3l1M3t3R#"+ connectA_ref[i][5];
				} else if (type_ref.equals("custom")) {
					finalArray_ref[i][1] = connectA_ref[i][1] + "#D3l1M3t3R#" + connectA_ref[i][2] + "#D3l1M3t3R#" + 
					connectA_ref[i][3] + "#D3l1M3t3R#" + connectA_ref[i][7];
				} //endif
			} //endif
		} //endfor
		
		return finalArray_ref;
	} //endmethod computeCommand
	
	/**
	 *	<p>Erstellt ein Array aus <i>Runnable</i>-Objekten, welche die einzelnen Prozesse repraesentieren.
	 *	Die Methode sorgt auch fuer die gleichmaessige Verteilung aller Hosteintraege auf die <i>Runnable</i>
	 *	Objekte.</p> 
	 *	<p>Am Ende gibt die Methode das erstellte Array <i>Runnable[]</i> zurueck.</p>
	 *
	 * 	@param processes Die vom Benutzer gewuenschte Anzahl an Prozessen.
	 * 	@param finalArray_ref Das durch <i>computeCommand()</i> errechnete Array aus Name, Kommando und Typ.
	 * 	@return Das Array aus <i>Runnable</i>-Objekten, auf welche die Hosteintraege gleichmaessig verteilt wurden.
	 */
	private Runnable[] createTaskArray(String[][] finalArray_ref, int processes) {
		Runnable[] taskRunArray_ref = null;
		int count = 0, ct = 0;
		taskRunArray_ref = new TaskRunnable[processes];
		try {
			for (int i=0; i<taskRunArray_ref.length; i++) {
				taskRunArray_ref[i] = new TaskRunnable(this);
			} //endfor
		} catch (Exception ex_ref) {
			ex_ref.printStackTrace();
		} //endtry
				
		while (count < finalArray_ref.length) {
			((TaskRunnable)taskRunArray_ref[ct]).addEntry(finalArray_ref[count]);
			if (ct == (processes-1)) {
				ct = 0;
			} else {
				ct++;
			} //endif
			count++;
		} //endwhile
		
		return taskRunArray_ref;
	} //endmethod createTaskArray
	
	/**
	 *	<p>Erstellt ein Array aus <i>Thread</i>-Objekten, welche die uebergebenen <i>TaskRunnable-Objekte</i> tragen und
	 *	startet diese.</p>
	 *	<p>Alle <i>TaskRunnables</i> die keine Eintraege enthalten, werden wieder gleich <i>null</i> gesetzt.</p>
	 *
	 *	@param array_ref Die Matrix aus TaskRunnable-Objekten.
	 *	@param processes Die Anzahl der gewuenschten Prozesse.
	 */
	private void executeTaskArray(Runnable[] array_ref, int processes) {
		Thread[] taskThrArray_ref = null;
		try {
			taskThrArray_ref = new Thread[processes];
			for (int i=0; i<taskThrArray_ref.length; i++) {
				taskThrArray_ref[i] = new Thread((TaskRunnable)taskRunArray_ref[i]);
			} //endfor
		} catch (Exception ex_ref) {
			ex_ref.printStackTrace();
		} //endtry
		
		for (int i=0; i<processes; i++) {
			if (((TaskRunnable)taskRunArray_ref[i]).hasEntries()) {
				taskThrArray_ref[i].start();
			} else {
				taskThrArray_ref[i] = null;
			} //endif
		} //endfor
	} //endmethod executeTaskArray
	
	/**
	 *	Zaehlt die tatsaechlich zu aktivierenden Prozesse, indem es das Daten-Array eines jeden
	 *	<i>TaskRunnable</i>-Objektes auf enthaltene Eintraege ueberprueft. Enthaelt es keine Eintraege,
	 *	so muss der zugehoerige Prozess nicht aktiviert werden, da er keine Aufgabe hat.
	 *
	 * 	@param taskRunArray_ref Das zurvor von <i>createTaskArray()</i> erstellte Array.
	 * 	@return Anzahl der tatsaechlich zu aktivierenden Prozesse.
	 */
	private int countProcesses(Runnable[] taskRunArray_ref) {
		int retVal = 0;
		for (int i=0; i<taskRunArray_ref.length; i++) {
			if (((TaskRunnable)taskRunArray_ref[i]).hasEntries()) {
				retVal++;
			} //endif
		} //endfor
		
		return retVal;
	} //endmethod countProcesses
	
} //endclass TinyAdminC