package de.home.tinyadmin;

//--- Importe
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
--	CLASS: WOLModule 																--
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
*	<p>Ein <i>ActionModule</i> zum versenden von Wake-On-LAN <i>Magic Packets</i> und somit zum Aufwecken von Hosts.
*	Getragen wird dieses Objekt von einem <i>TaskRunnable</i>.</p>
*	<p>Hier ist der Code fuer das Generieren und Versenden des <i>Magic Packets</i> in der <i>doAction()</i>-
*	Methode implementiert und es wird auch eine entsprechende Statusmeldung generiert.</p>
*
* 	@version 0.2 von 06.2011
*
* 	@author Tobias Burkard
*/
class WOLModule extends ActionModule {
	// --- Konstruktoren
	/**
	 *	Initialisiert die uebergebenen Referenzen auf die <i>Nachrichtenfabrik</i> und <i>TestHelfer-Klasse</i>
	 *	durch Uebergabe an die Superklasse <i>ActionModule</i>.
	 * 
	 * 	@see ActionModule
	 */
	WOLModule(MessageFacility msgFac_ref, TestHelfer test_ref) {
		super(msgFac_ref, test_ref);
	} //endconstructor
	
	// --- Methoden
	/**
	 *	Objekteigene Implementierung der in <i>ActionModule</i> festgelegten <i>doAction()</i>-Methode.
	 *	Hier ist der eigentliche Code, welcher das Aufwecken des Hosts durchfuehrt, implementiert.
	 *	Die Methode bricht ab, falls die <i>abbort</i>-Variable im Hauptprogramm auf <i>true</i> gesetzt wurde.
	 *	
	 *	@param param_ref Benoetigte Parameter in der Form "IP-Adresse/Host" + #D3l1M3t3R## + "MAC-Adresse".
	 *	@return Statusmeldung ueber Erfolg/Misserfolg des Versendens des Magic Packets.
	 */
	String doAction(String param_ref) {
		String retVal_ref = "";
		String[] ipMac_ref = param_ref.split("#D3l1M3t3R#");
		if (ipMac_ref.length < 2) {
			return "FEHLER: Es wurde keine MAC-Adresse angegeben.";
		} //endif
		byte[] bytes_ref = getMACBytes(ipMac_ref[1]);
		if (test_ref.isValidIP(ipMac_ref[0])) {
			String[] tempIP_ref = ipMac_ref[0].split("\\.");
			tempIP_ref[tempIP_ref.length-1] = "255";
			String holder_ref = "";
			for (int i=0; i<tempIP_ref.length; i++) {
				holder_ref += tempIP_ref[i];
				if (i != (tempIP_ref.length-1)) {
					holder_ref += ".";
				} //endif
			} //endfor
			ipMac_ref[0] = holder_ref;
		} //endif
        
	 	try {
	 		InetAddress	address_ref = InetAddress.getByName(ipMac_ref[0]);
		    DatagramPacket packet_ref = new DatagramPacket(bytes_ref, bytes_ref.length, address_ref, 9);
		    DatagramSocket socket_ref = new DatagramSocket();
		    socket_ref.send(packet_ref);
		    socket_ref.close();
		} catch (Exception ex_ref) {
			return "FEHLER: Die Host- oder IP-Adresse ist ungültig.";
		} //endtry
		  
	    retVal_ref += "Versenden des magic-packets an \""+ipMac_ref[1]+"\" über \""+ipMac_ref[0]+"\" erfolgreich.";
	    
	    return retVal_ref;
	} //endmethod doAction
	
	/**
	 *	Liefert die als String uebergebene MAC-Adresse als verwertbares <i>byte[6]</i>-Array zurueck.
	 *
	 *	@param mac_ref Die uebergebene MAC-Adresse.
	 *	@return Die MAC-Adresse als <i>byte[6]</i>-Array.
	 */
	private byte[] getMACBytes(String mac_ref) {
		byte[] macBytes_ref = new byte[6];
		String[] hex_ref = mac_ref.split("(\\:|\\-)");
		for (int i=0; i<6; i++) {
			macBytes_ref[i] = (byte)Integer.parseInt(hex_ref[i], 16);
		} //endfor
		byte[] bytes_ref = new byte[6 + 16 * macBytes_ref.length];
		for (int i=0; i<6; i++) {
			bytes_ref[i] = (byte)0xff;
	    } //endfor
	    for (int i=6; i<bytes_ref.length; i+=macBytes_ref.length) {
	    	System.arraycopy(macBytes_ref, 0, bytes_ref, i, macBytes_ref.length);
	    } //endfor
        
        return bytes_ref;
	} //endmethod getMACBytes
	
} //endclass WOLModule