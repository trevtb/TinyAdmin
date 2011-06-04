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
--	CLASS: TestHelfer																--
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
*	Ein Objekt dieser Klasse stellt Werkzeuge bereit, um Werte zu validieren. Das Einstellungs-GUI
*	kann beispielsweise hierauf zurueckgreifen, um vom Benutzer eingegebene Werte zu ueberpruefen.
*
* 	@version 0.2 von 06.2011
*
* 	@author Tobias Burkard
*/
class TestHelfer {
	// --- Methoden
	/**	
	*	Ueberprueft ob ein uebergebener String gleich <i>"root"</i> ist.
	*
	*	@param user_ref Der uebergebene String.
	*	@return <i>true</i>, wenn es sich um <i>root</i> handelt - <i>false</i>, wenn nicht.
	*/
	boolean isRootLogin (String user_ref) {
		if (user_ref.equals("root")) {
			return true;
		} else {
			return false;
		} //endif
	} //endmethod isRootLogin
	
	/**
	 *	<p>Ueberprueft ob es sich bei dem uebergebenen String um eine gueltige MAC-Adresse handelt.
	 *	Es wird getestet, ob der String:<ul>
	 *	<li><i>6</i> durch <i>":"</i> getrennte Felder</li>
	 *	<li>mit gueltigen hexadezimal-Zahlen enthaelt.</li></ul></p>
	 *	<p>Ist all dies der Fall, liefert die Methode einen boolean mit dem Wert <i>true</i> zurueck.</p>
	 * 
	 * @param mac_ref Die zu pruefende MAC-Adresse.	
	 * @return <i>true</i>, falls die Adresse gueltig ist - <i>false</i>, wenn nicht.
	 */
	boolean isValidMAC(String mac_ref) {
		boolean retVal = true;
		byte[] macBytes_ref = new byte[6];
	    String[] hex_ref = mac_ref.split("(\\:|\\-)");
		if (hex_ref.length != 6) {
	    	retVal = false;
	    } else {
	    	try {
	    		for (int i=0; i<6; i++) {
	    			macBytes_ref[i] = (byte)Integer.parseInt(hex_ref[i], 16);
		        } //endfor
		    } catch (Exception ex_ref) {
		    	retVal = false;
		    } //endtry
	    } //endif
		
		return retVal;
	} //endmethod isValidMAC
	
	/**
	 *	<p>Ueberprueft, ob es sich bei dem uebergebenen String um eine gueltige IP-Adresse handelt.
	 *	Es wird getestet, ob der String:<ul>
	 *	<li><i>4</i> durch <i>"."</i> getrennte Felder aufweist,</li>
	 *	<li>welche alle aus ganzzahligen int-Werten <=255 bestehen.</li></ul></p>
	 *	<p>Ist all dies der Fall, liefert die Methode ein boolean mit dem Wert <i>true</i> zurueck.</p>
	 *
	 *	@param ip_ref Die zu pruefende IP-Adresse.
	 *	@return <i>true</i>, falls die Adresse gueltig ist - <i>false</i>, wenn nicht.
	 */
	boolean isValidIP(String ip_ref) {
		boolean retVal = true;
		String[] ipTemp_ref = ip_ref.split("\\.");
		if (ipTemp_ref.length != 4) {
			retVal = false;
		} else {
			for (int i=0; i<ipTemp_ref.length; i++) {
				try {
					int temp = Integer.parseInt(ipTemp_ref[i]);
					if (temp > 255) {
						retVal = false;
					} //endif
				} catch (Exception ex_ref) {
					retVal = false;
				} //endtry
			} //endfor
		} //endif
		return retVal;
	} //endmetho isValidIP
	
} //endclass TestHelfer