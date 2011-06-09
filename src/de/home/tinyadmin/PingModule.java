package de.home.tinyadmin;

// --- Importe
import java.net.InetAddress;

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
--	CLASS: PingModule 																--
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
*	<p>Ein <i>ActionModule</i> zum Messen der Latenzzeiten von Hosts. Getragen wird dieses Objekt von einem 
*	<i>TaskRunnable</i>.</p>
*	<p>Hier in der <i>doAction()</i>-Methode ist der Code fuer das Aufbauen einer 
*	Verbindung und Messen der Antwortzeit implementiert und es wird auch eine entsprechende Statusmeldung 
*	fuer das Haupt-GUI generiert.</p>
*
* 	@version 0.3 von 06.2011
*
* 	@author Tobias Burkard
*/
class PingModule extends ActionModule {
	// --- Konstruktoren
	/**
	 *	Initialisiert die uebergebenen Referenzen auf die <i>Nachrichtenfabrik</i> und <i>TestHelfer-Klasse</i>
	 *	durch Uebergabe an die Superklasse <i>ActionModule</i>.
	 * 
	 *	@see ActionModule
	 */
	PingModule(MessageFacility msgFac_ref, TestHelfer test_ref) {
		super(msgFac_ref, test_ref);
	} //endconstructor
	
	// --- Methoden
	/**
	 *	<p>Objekteigene Implementierung der in <i>ActionModule</i> festgelegten <i>doAction()</i>-Methode.
	 *	Hier ist der eigentliche Code, welcher das Pingen des Hosts durchfuehrt, implementiert.</p>
	 *	<p>Es wird das gleiche Verfahren wie in der <i>TestModule</i>-Klasse verwendet, das Verfahren wird
	 *	jedoch oefter wiederholt und die Latzenzzeiten werden gemessen. Ist die Variable <i>abbort</i> im 
	 *	Hauptprogramm auf <i>true</i> gesetzt, so bricht die Methode ab.</p>
	 *	
	 *	@param ip_ref Der zu pingende Host bzw. die zu pingende IP-Adresse.
	 *	@return Statusmeldung ueber Erfolg/Misserfolg des Ping-Versuchs.
	 */
	String doAction(String ip_ref) {
		String retVal_ref = "Führe Ping durch für "+ip_ref+":";
		int iterations = 5;
		boolean[] reachable = new boolean[iterations];
		long millicounter = 0L;
		
		for (int i=0; i<iterations; i++) {
			if (TinyAdminC.abbort) {
				return "Vorgang abgebrochen ...";
			} //endif
			retVal_ref += "\nVersuch " + (i+1) + ": ";
			long millis = 0L;
			
			try {
				millis = System.currentTimeMillis();
				reachable[i] = InetAddress.getByName(ip_ref).isReachable(5000);
				millis = System.currentTimeMillis() - millis;
				if (reachable[i]) {
					retVal_ref += "Antwort von " + ip_ref + ", Zeit= " + millis + "ms";
					millicounter += millis;
				} else {
					retVal_ref += "Keine Verbindung zu "+ ip_ref + ", Zeit abgelaufen.";
				} //endif
			} catch (Exception ex_ref) {
				return "FEHLER: Die Host- oder IP-Adresse ist ungültig.";
			} //endtry
		} //endfor
		
		boolean allValid = true;
		for (int i=0; i<reachable.length; i++) {
			if (!reachable[i]) {
				allValid = false;
			} //endif
		} //endfor
		if (!allValid) {
			retVal_ref += "\nFEHLER: "+ ip_ref + " ist nicht erreichbar.";
		} else {
			retVal_ref += "\nDurschnittliche Latenzzeit: " + millicounter/iterations + " ms";
		} //endif
        
		return retVal_ref;
	} //endmethod doAction

} //endclass PingModule