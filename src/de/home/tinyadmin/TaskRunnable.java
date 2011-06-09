package de.home.tinyadmin;

// --- Importe
import java.util.ArrayList;

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
--	CLASS: TaskRunnable 															--
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
*	<p>Ein Prozess, an welchen ein <i>ActionModule</i> zur Ausfuehrung gekoppelt wird. Welches Modul genau 
*	erstellt wird, entscheidet ein Objekt dieser Klasse zur Laufzeit und erstellt es entsprechend bei Bedarf.</p>
*	<p>Zusaetzlich kuemmert sich ein Objekt dieser Klasse um die Weiterleitung und Aufbereitung von Nachrichten
*	an das GUI.</p>
*
*	@see TaskRunnable
*
* 	@version 0.3 von 06.2011
*
* 	@author Tobias Burkard
*/
class TaskRunnable implements Runnable {
	// --- Attribute
	private ArrayList<String[]> connectAR_ref = new ArrayList<String[]>();	// Enthaelt alle fuer diesen Prozess abzuarbeitende 
																	// Tabelleneintraege aus dem GUI. Ein String[] fuer jeden Eintrag
	private String[] realAR_ref;	// Fasst den Eintrag, der zur Laufzeit gerade abgearbeitet wird
	private TinyAdminC main_ref;	// Referenz auf die Hauptprogramm-Klasse
	private ActionModule taskModule_ref;	// Das genutze ActionModule
	
	// --- Konstruktoren
	/**	
	*	Initialisiert die Referenz auf das Hauptprogramm mit der uebergebenen Referenz.
	*
	*	@see TinyAdminC
	*/
	TaskRunnable(TinyAdminC main_ref) {
		this.main_ref = main_ref;
	} //endconstructor
	
	// --- Methoden
	/**	
	*	Fuegt dem Array <i>connectAR_ref</i>, welches alle abzuarbeitenden Eintraege enthaelt, einen neuen Eintrag hinzu.
	*	
	*	@param entry_ref Der neue Eintrag.
	*/
	void addEntry(String[] entry_ref) {
		connectAR_ref.add(entry_ref);
	} //endmethod addEntry
	
	/**	
	*	Prueft ob dieses Objekt Eintraege zur Verarbeitung enthaelt.
	*	
	*	@return <i>true</i>, wenn es Eintraege enthaelt - <i>false</i>, wenn nicht.
	*/
	boolean hasEntries() {
		if (!connectAR_ref.isEmpty()) {
			return true;
		} else {
			return false;
		} //endif
	} //endmethod hasEntries
	
	/**	
	*	Liefert das komplette Array mit allen zur Verarbeitung anstehenden Eintraegen zurueck.
	*	
	*	@return Das komplette Array mit allen zur Verarbeitung anstehenden Eintraegen.
	*/
	ArrayList<String[]> getEntries() {
		return connectAR_ref;
	} //endmethod getEntries
	
	/**
	 *	<p>Schliesst den Prozess ab, indem es den Prozesszaehler der Hauptanwendung dekrementiert.
	 *	Falls der Zaehler 0 erreicht, es sich also um das Ende des letzten Prozesses handelt,
	 *	so werden die (Fehler)meldungen alle Prozesse durch die Methode <i>computeErrorMsg()</i> ausgewertet.</p>
	 *	<p>Im Anschluss wird <i>doProcessEnd()</i> aufgerufen um das finale Ergebnis aller 
	 *	Prozessoperation auszuwerten und danach <i>closeAction()</i>, um alle Prozessobjekte wieder auf
	 *	<i>null</i> zu setzen.</p> 
	 *	<p>So tauchen letztlich im HauptGUI alle relevanten Information auf und der Benutzer wird so ueber 
	 *	das Ergebnis informiert.</p>
	 *
	 *	@see MessageFacility#computeErrorMsg()
	 *	@see MessageFacility#doProcessEnd(String)
	 *	@see TinyAdminC#closeAction()
	 */
	private void closeTask() {
		main_ref.decrementPSCount();
		if (main_ref.getPSCount() == 0) {
			main_ref.getMsgFacility().computeErrorMsg();
			main_ref.getMsgFacility().doProcessEnd((connectAR_ref.get(0))[2]);
			main_ref.closeAction();
		} //endif
	} //endmethod closeTask
	/**
	 *	<p>Entscheidet darueber, welches <i>Actionmodule</i> genutzt wird und erzeugt dieses bei Bedarf.</p>
	 *	<p>Auch wird am Ende eine entsprechende Statusmeldung an das Haupt-GUI weitergereicht und die 
	 *	<i>closeTask()</i>-Methode der Superklasse aufgerufen. Wurde die statische Variable <i>abbort</i> im
	 *	Hauptprogramm gesetzt, so wird die Methode <i>kill()</i> aufgerufen, welche Alle Verbindungen
	 *	bestehender <i>SSHModule</i> beendet. Andere Module bleiben unberuehrt, da diese selbststaendig
	 *	abbrechen koennen.</p>
	 *
	 *	@see #closeTask()
	 *	@see #kill()
	 */
	public void run() {
		for (int i=0; i<connectAR_ref.size(); i++) {
			if (!TinyAdminC.abbort) {
				realAR_ref = connectAR_ref.get(i);
				String statusText_ref = main_ref.getMsgFacility().getStartMsg(realAR_ref);
				String line_ref = "";
				if (realAR_ref[2].equals("test")) {
					taskModule_ref = new TestModule(main_ref.getMsgFacility(), main_ref.getTestHelfer());
					line_ref = taskModule_ref.doAction(realAR_ref[1]);
				} else if (realAR_ref[2].equals("wol")) {
					taskModule_ref = new WOLModule(main_ref.getMsgFacility(), main_ref.getTestHelfer());
					line_ref = taskModule_ref.doAction(realAR_ref[1]);
				} else if (realAR_ref[2].equals("ping")) {
					taskModule_ref = new PingModule(main_ref.getMsgFacility(), main_ref.getTestHelfer());
					line_ref = taskModule_ref.doAction(realAR_ref[1]);
				} else if (realAR_ref[2].equals("update") || realAR_ref[2].equals("reboot") || realAR_ref[2].equals("shutdown") 
						|| realAR_ref[2].equals("custom") || realAR_ref[2].equals("customC")) {
					taskModule_ref = new SSHModule(main_ref.getMsgFacility(), main_ref.getTestHelfer());
					line_ref = taskModule_ref.doAction(realAR_ref[1]);
				} //endif
				
				statusText_ref += line_ref;
				main_ref.getMsgFacility().computeErrors(line_ref, realAR_ref);
				statusText_ref += main_ref.getMsgFacility().getEndMsg(realAR_ref);
				main_ref.getGUI().setStatusText(statusText_ref);
			} else {
				break;
			} //endif
		} //enfor
		closeTask();
	} //endmethod run
	
	/**
	 *	Ruft die Methode <i>die()</i> auf allen bestehenden <i>SSHModulen</i> auf um so deren
	 *	Verbindungen zu beenden.
	 *
	 *	@see SSHModule#die()
	 */
	void kill() {
		if (taskModule_ref.getClass().getCanonicalName().equals("de.home.tinyadmin.SSHModule")) {
			((SSHModule)taskModule_ref).die();
		} //endif
	} //endmethod kill
	
} //endclass TaskRunnable