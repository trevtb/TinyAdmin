package de.home.tinyadmin;

//--- Importe
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
--	CLASS: MessageFacility 															--
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
*	<p>Ein Objekt dieser Klasse generiert (Fehler)meldungen fuer alle zur Laufzeit
*	der einzelnen Prozesse aufgetretenen Ereignisse. Eine von den Prozessen generierte Liste 
*	dieser Ereignisse wird hier verarbeitet und die Ergebnisse werden dann auf dem HauptGUI ausgegeben.</p> 
*	<p>In dieser Klasse werden auch alle Fehlermeldungen definiert und Meldungen 
*	ueber Erfolg, Misserfolg und Status der einzelnen Ereignisse generiert.</p>
*
*	@see TinyAdminGUI
*
* 	@version 0.3 von 06.2011
*
* 	@author Tobias Burkard
*/
class MessageFacility {
	// --- Attribute
	private Object[][] errorMatrix_ref;	// Haelt fuer jede moegliche Fehlermeldung eine ArrayList, welche die Hosts fasst, 
										// die diesen Fehler gemeldet haben. Die Zweite Zeile fasst die Bezeichner der
										// Fehlermeldungen
	private int errorCount;	// Zaehler fuer Fehlermeldungen
	private String errorMsg_ref;	// Behaelter fuer erfasste Fehlermeldungen. Trennzeichen ist "\n"
	private final String[] ERRNAMES = {"CommandNotFoundOnHost", "SudoWrongPWD", "SSHHostsNotReachable", "SSHNoConnectionOnPort22", 
								"SSHInvalidLogin", "TestHostsNotReachable", "WOLHostsNotReachable",
								"WOLMacAddressProblems", "InvalidSyntax", "SSHAuthProblems", "KeyNotFound"}; // Namen der moeglichen Fehlermeldungen 
	private TinyAdminGUI gui_ref;	// Referenz auf das HauptGUI-Objekt um (Fehler)meldungen anzuzeigen.
	private TinyAdminC main_ref;	// Referenz auf das Hauptprogramm
	// --- Konstruktoren
	/**	
	*	<p>Setzt die <i>main_ref</i> Referenz auf das uebergebene <i>TinyAdminC</i>-Objekt und
	*	verschafft sich ueber dieses Zugang zum <i>TinyAdminGUI</i>. So koenen (Fehler)meldungen 
	*	ausgeben werden.</p> 
	*	<p>Ueber die <i>renewFacility()</i>-Methode werden zudem alle noetigen Objektvariablen initialisiert.</p>
	*
	*	@see #renewFacility()
	*	@see TinyAdminC
	*	@see TinyAdminGUI
	*/
	MessageFacility(TinyAdminC main_ref) {
		this.main_ref = main_ref;
		this.gui_ref = main_ref.getGUI();
		renewFacility();
	} //endconstructor
	
	// --- Methoden
	/**	
	*	Initialisiert alle Objektvariablen mit Standardwerten.
	*/
	void renewFacility() {
		errorMatrix_ref = new Object[2][];
		errorMatrix_ref[0] = new ArrayList[ERRNAMES.length];
		errorMatrix_ref[1] = new String[ERRNAMES.length];
		for (int i=0; i<ERRNAMES.length; i++) {
			errorMatrix_ref[0][i] = new ArrayList<String>();
			errorMatrix_ref[1][i] = ERRNAMES[i];
		} //endfor
		errorMsg_ref = "";
		errorCount = 0;
	} //endmethod renewFacility
	
	/**
	 *	Generiert die Startnachricht eines jeden Prozesseintrages fuer das Status-Textfeld des HauptGUIs.
	 *	Je nachdem um welche Art von Prozessaufgabe es sich handelt, wird die Startnachricht angepasst.
	 *
	 *	@param entry_ref Vom Prozess abzuarbeitender Eintrag.
	 *	@return Die dem Prozesseintrag entsprechende Startnachricht.
	 */
	String getStartMsg(String[] entry_ref) {
		if (entry_ref[2].equals("update")) {
			return new String("\n----- FÜHRE SOFTWARE-UPDATE FÜR \""+ (entry_ref[0]) + "\" DURCH: -----\n");
		} else if (entry_ref[2].equals("reboot")) {
			return new String("\n----- FÜHRE REBOOT FÜR \""+ (entry_ref[0]) + "\" DURCH: -----\n");
		} else if (entry_ref[2].equals("shutdown")) {
			return new String("\n----- FÜHRE SHUTDOWN FÜR \""+ (entry_ref[0]) + "\" DURCH: -----\n");
		} else if (entry_ref[2].equals("test")) {
			return new String("\n----- FÜHRE TEST FÜR \""+ (entry_ref[0]) + "\" DURCH: -----\n");
		} else if (entry_ref[2].equals("ping")) {
			return new String("\n----- FÜHRE PING FÜR \""+ (entry_ref[0]) + "\" DURCH: -----\n");
		} else if (entry_ref[2].equals("wol")) {
			return new String("\n----- FÜHRE AUFWECKEN (WOL) FÜR \""+ (entry_ref[0]) + "\" DURCH: -----\n");
		} else if (entry_ref[2].equals("custom")) {
			return new String("\n----- FÜHRE EIGENES KOMMANDO AUF \""+ (entry_ref[0]) + "\" AUS: -----\n");
		} else if (entry_ref[2].equals("customC")) {
			return new String("\n----- FÜHRE \"" + gui_ref.getRunCommand() +"\" AUF \""+ (entry_ref[0]) + "\" AUS: -----\n");
		} else {
			return null;
		} //endif
	} //endmethod getStartMsg
	
	/**
	 *	Generiert die Endnachricht eines jeden Prozesseintrages fuer das Status-Textfeld des HauptGUIs.
	 *	Je nachdem um welche Art von Prozessaufgabe es sich handelt, wird die Endnachricht angepasst.
	 *
	 *	@param entry_ref Vom Prozess abzuarbeitender Eintrag.
	 *	@return Die dem Prozesseintrag entsprechende Endnachricht.
	 */
	String getEndMsg(String[] entry_ref) {
		if (entry_ref[2].equals("update")) {
			return new String("\n----- SOFTWARE-UPDATE VON \"" + (entry_ref[0]) + "\" BEENDET -----\n");
		} else if (entry_ref[2].equals("reboot")) {
			return new String("\n----- REBOOT VON \"" + (entry_ref[0]) + "\" WURDE AUSGELÖST -----\n");
		} else if (entry_ref[2].equals("shutdown")) {
			return new String("\n----- SHUTDOWN VON \"" + (entry_ref[0]) + "\" WURDE AUSGELÖST -----\n");
		} else if (entry_ref[2].equals("test")) {
			return new String("\n----- TEST VON \""+ (entry_ref[0]) + "\" BEENDET -----\n");
		} else if (entry_ref[2].equals("ping")) {
			return new String("\n----- PING VON \""+ (entry_ref[0]) + "\" ABGESCHLOSSEN -----\n");
		} else if (entry_ref[2].equals("wol")) {
			return new String("\n----- AUFWECKEN VON \""+ (entry_ref[0]) + "\" ABGESCHLOSSEN -----\n");
		} else if (entry_ref[2].equals("custom")) {
			return new String("\n----- AUSFÜHREN DES KOMMANDOS ABGESCHLOSSEN -----\n");
		} else if (entry_ref[2].equals("customC")) {
			return new String("\n----- AUSFÜHREN VON \"" + gui_ref.getRunCommand() + "\" ABGESCHLOSSEN -----\n");
		} else {		
			return null;
		} //endif
	} //endmethod getEndMsg
	
	/**
	 *	<p>Komplettiert die Ausgabe aller Statusmeldungen fuer das Status-Textfeld des Haupt-GUIs.
	 *	Es werden zudem Fehler ausgewertet und gegebenenfalls Fehlermeldungen im Haupt-GUI generiert.</p> 
	 *	<p>War die Aktion erfolgreich, so wird der Benutzer darueber informiert.
	 *	War sie es nicht, so wird eine Liste aller Hosteintraege angezeigt, fuer die der Vorgang nicht
	 *	erfolgreich abgeschlossen werden konnte.</p> 
	 *	<p>Am Ende wird die <i>closeAction()</i>-Methode des Hauptprogramms aufgerufen, um alle Prozessobjekte
	 *	aus dem Speicher zu nehmen, also auf <i>null</i> zu setzen. Zudem wird der Wartecursor im HauptGUI 
	 *	wieder entfernt und die Aktionsknoepfe wieder zugaenglich gemacht. 
	 *	Hierzu werden die Methoden <i>initWaitCursor(int)</i> und <i>setButtonStatus(int)</i> des Haupt-GUIs verwendet.</p>
	 *
	 *	@param type_ref Die Art von Aktion (update, reboot, shutdown, test, ping, wol, custom, customC) um die es sich handelte.
	 *	@see TinyAdminC#closeAction()
	 *	@see TinyAdminGUI#initWaitCursor(int)
	 *	@see TinyAdminGUI#setButtonStatus(int)
	 */
	void doProcessEnd(String type_ref) {
		gui_ref.setStatusText("\n----- VORGANG ABGESCHLOSSEN -----");
		
		if (errorCount > 0) {
			gui_ref.displayError(errorMsg_ref);
		} else if (errorCount == 0 && !TinyAdminC.abbort) {
			if (type_ref.equals("reboot")) {
				gui_ref.displayMsg("Reboot aller Hosts erfolgreich eingeleitet.");
			} else if (type_ref.equals("update")) {
				gui_ref.displayMsg("Software-Update aller Hosts erfolgreich abgeschlossen.");
			} else if (type_ref.equals("shutdown")) {
				gui_ref.displayMsg("Shutdown aller Hosts erfolgreich eingeleitet.");
			} else if (type_ref.equals("test")) {
				gui_ref.displayMsg("Test aller Hosts erfolgreich abgeschlossen.");
			} else if (type_ref.equals("ping")) {
				gui_ref.displayMsg("Ping aller Hosts erfolgreich abgeschlossen.");
			} else if (type_ref.equals("wol")) {
				gui_ref.displayMsg("Alle WOL 'Magic Packets' wurden erfolgreich versandt.");
			} else if (type_ref.equals("custom")) {
				gui_ref.displayMsg("Das Kommando wurde auf allen Hosts ausgeführt.\n" +
						"Es traten keine der Anwendung bekannten Fehler auf.");
			} else if (type_ref.equals("customC")) {
				gui_ref.displayMsg("Das Kommando \"" + gui_ref.getRunCommand() + "\" wurde auf allen Hosts ausgeführt.\n" +
						"Es traten keine der Anwendung bekannten Fehler auf.");
			} //endif
		} //endif
		
		if (TinyAdminC.abbort) {
			gui_ref.displayMsg("Der Vorgang wurde abgebrochen.");
		} //endif
		
		main_ref.closeAction();
		gui_ref.initWaitCursor(0);
		gui_ref.setButtonStatus(1);
	} //endmethod doProcessEnd
	
	/**
	 *	<p>Vergleicht die uebergebene Statusmeldung <i>inputLine_ref</i> mit allen moeglichen Statusmeldungen, welche
	 *	in der Matrix patterMatrix_ref gehalten werden.</p> <p>Falls es einen Treffer gibt, wird der Hosteintrag,
	 *	der diese Statusmeldung ausgeloest hat, zu dem der Fehlermeldung entsprechenden Array der <i>errorMatrix_ref</i>
	 *	hinzugefuegt.</p> <p>So kann spaeter genau nachempfunden werden, welcher Host welche Statusmeldung ausgegeben hat.</p>
	 * 
	 *	@param inputLine_ref Die vom Prozess ausgeloeste Statusmeldung fuer den Hosteintrag, der gerade bearbeitet wurde.
	 *	@param listEntry_ref Der Hosteintrag, der diese Statusmeldung ausgeloest hat.
	 *	@return die <i>errorMatrix</i>: ein Array mit Hostnamen fuer jede moegliche Fehlermeldung.
	 */
	@SuppressWarnings("unchecked")
	Object[][] computeErrors (String inputLine_ref, String[]listEntry_ref) {
		String[][] patternMatrix_ref = {{"Kommando nicht gefunden", "command not found"},
							{"Sorry, try again"},
							{"problem while connecting"},
							{"Connection refused"},
							{"Permission denied", "Authentifizierung gescheitert"},
							{"ist nicht erreichbar"},
							{"IP-Adresse ist ungültig"},
							{"keine MAC-Adresse"},
							{"syntax error", "Syntax Fehler"},
							{"Authentication failed"},
							{"Keyfile konnte nicht gefunden werden."}};
		for (int i=0; i<patternMatrix_ref.length; i++) {
			int counter = 0;
			for (int j=0; j<patternMatrix_ref[i].length; j++) {
				if (!patternMatrix_ref[i][j].equals("")) {
					counter++;
				} //endif
			} //endfor
			Pattern[] patterns_ref = new Pattern[counter];
			counter = 0;
			for (int j=0; j<patternMatrix_ref[i].length; j++) {
				if (!patternMatrix_ref[i][j].equals("")) {
					patterns_ref[counter] = Pattern.compile(patternMatrix_ref[i][j]);
					counter++;
				} //endif
			} //endfor
			Matcher[] matchers_ref = new Matcher[patterns_ref.length];
			for (int j=0; j<matchers_ref.length; j++) {
				matchers_ref[j] = patterns_ref[j].matcher(inputLine_ref);
			} //endfor
			
			boolean found = false;
			for (int j=0; j<matchers_ref.length; j++) {
				if (matchers_ref[j].find()) {
					found = true;
				} //endif
			} //endfor
			
			if (found) {
				((ArrayList<String>)errorMatrix_ref[0][i]).add(listEntry_ref[0]);
			} //endif
		} //endfor
		
		return errorMatrix_ref;
	} //endmethod computeErrors
	
	/**
	 *	<p>Baut die Fehlermeldung zur Anzeige im Haupt-GUI anhand der durch die Methode
	 *	<i>computeErrors()</i> errechneten Fehler-Arrays zusammen.</p>
	 *	<p>Es wird zudem sichergestellt, dass Fehlermeldungen nicht ueber 20 Eintraege wachsen. 
	 *	Dies kann leicht geschehen, wenn beispielsweise viele Hosts (>20) nicht erreichbar sind.<br>
	 *	So soll verhindert werden, dass die Groesse der Fehlermeldung die Anzeigeflaeche des Bildschirms 
	 *	uebersteigt.</p>
	 * 
	 *	@return Die errechnete Fehlermeldung zur Anzeige im Haupt-GUI.
	 */
	@SuppressWarnings("unchecked")
	String computeErrorMsg() {
		String[] errorMessages_ref = {"FEHLER: Auf folgenden Hosts wurde der auszuführende Befehl nicht gefunden:\n",
									"FEHLER: Sie haben für einige Hosts ein falsches Sudo-Passwort angegeben:\n",
									"FEHLER: Folgende Hosts konnten nicht gefunden werden:\n",
									"FEHLER: SSH Verbindung auf Port 22 abgelehnt, von folgenden Hosts:\n",
									"FEHLER: Benutzernamen und/oder Passwort folgender Hosts ist falsch,\noder der Nutzer ist für den Fernzugriff gesperrt:\n",
									"FEHLER: Folgende Hosts sind nicht erreichbar:\n",
									"FEHLER: Die Host- oder IP-Adresse folgender Hosts ist ungültig:\n",
									"FEHLER: Sie haben für folgende Hosts keine MAC-Adresse angegeben:\n",
									"FEHLER: Der eingegebene Befehl erzeugte auf folgenden Hosts einen Sytnax-Error:\n",
									"FEHLER: Folgende Hosts scheinen einen Login ohne interaktive Passworteingabe nicht zu unterstützen:\n",
									"FEHLER: Für folgende Hosts wurde das Keyfile nicht gefunden, überprüfen Sie den Pfad:\n"
		};
		
		if (errorMsg_ref.isEmpty()) {
			errorMsg_ref = "Es traten Probleme bei der Verarbeitung auf:\n";
		} //endif
		
		for (int i=0; i<errorMatrix_ref[0].length; i++) {
			if (!((ArrayList<String>)errorMatrix_ref[0][i]).isEmpty()) {
				errorMsg_ref += "\n" + errorMessages_ref[i];
				errorCount++;
				int count = 0, count2 = 0;
				for (int j=0; j<((ArrayList<String>)errorMatrix_ref[0][i]).size(); j++) {
					if (count2 <= 20) {
						if (count < 10) {
							errorMsg_ref += "\""+((ArrayList<String>)errorMatrix_ref[0][i]).get(j) + "\", ";
						} else {
							errorMsg_ref += "\n";
							count = 0;
						} //endif
					} //endif
					count++;
					count2++;
				} //endfor
				if (count2 >=20) {
					errorMsg_ref += "...(zu viele Hosts, es werden max. 20 angezeigt.)... ";
				} //endif
				char[] errorMsgChar_ref = errorMsg_ref.toCharArray();
				char[] newErrorMsgChar_ref = new char[errorMsgChar_ref.length -1];
				for (int j=0; j<newErrorMsgChar_ref.length; j++) {
					newErrorMsgChar_ref[j] = errorMsgChar_ref[j];
				} //endfor
				newErrorMsgChar_ref[newErrorMsgChar_ref.length -1] = '.';
				errorMsg_ref = new String(newErrorMsgChar_ref);
				errorMsg_ref += "\n";
			} //endif
		} //endfor
		
		return errorMsg_ref;
	} //endmethod computeErrorMsg
	
} //endclass MessageFacility