package de.home.tinyadmin;

//--- Importe
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
--	CLASS: TestModule 																--
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
*	<p>Ein <i>ActionModule</i> zum testen der Erreichbarkeit von Hostnamen und IP-Adressen.
*	Getragen wird dieses Objekt von einem <i>TaskRunnable</i>.</p> 
*	<p>Diese Klasse implementiert in der <i>doAction()</i>-Methode konkret den Code fuer den Test
*	des Hosts und es wird auch eine entsprechende Statusmeldung generiert.</p>
*
* 	@version 0.3 von 06.2011
*
* 	@author Tobias Burkard
*/
class TestModule extends ActionModule {
	/**	
	*	Uebergibt die uebergebenen, fuer die Verarbeitung benoetigten, Referenzen
	*	auf die <i>Nachrichtenfabrik</i> und das <i>TestHelfer</i>-Objekt an die Superklasse <i>ActionModule</i>.
	*
	*	@see ActionModule
	*/
	// --- Konstruktoren
	TestModule(MessageFacility msgFac_ref, TestHelfer test_ref) {
		super(msgFac_ref, test_ref);
	} //endconstructor
	
	// --- Methoden
	/**	
	*	Objekteigene Implementierung der in <i>ActionModule</i> festgelegten <i>doAction()</i>-Methode.
	*	Hier ist der eigentliche Code, welcher den Test des Hosts durchfuehrt, implementiert. Die Methode
	*	bricht ab, falls die <i>abbort</i>-Variable im Hauptprogramm auf <i>true</i> gesetzt wurde.
	*	
	*	@param ip_ref Der Hostname oder die IP-Adresse des zu testenden Hosts.
	*	@return Statusmeldung ueber Erfolg/Misserfolg des Erreichbarkeitstests.
	*/
	String doAction(String ip_ref) {
		if (TinyAdminC.abbort) {
			return "Vorgang abgebrochen ...";
		} //endif
		String retVal_ref = "";
		try {
            boolean reachable = InetAddress.getByName(ip_ref).isReachable(5000);
            if (reachable) {
            	retVal_ref += "Die Adresse "+ip_ref+" ist erreichbar.";
            } else {
            	retVal_ref += "FEHLER: Die Adresse "+ip_ref+" ist nicht erreichbar.";
            } //endif
        } catch (Exception ex_ref) {
        	return "FEHLER: Die Host- oder IP-Adresse ist ungültig.";
        } //endtry
		
        return retVal_ref;
	} //endmethod doAction
	
} //endclass TestModule