package de.home.tinyadmin;

// --- Importe
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

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
--	The Icons are taken from the Humanity Icon Theme found in Ubuntu.				--
-- 	The Humanity Icon Theme is also licensed under the GPL v2.						--
-- 	Parts of the work are based on the Tango icons, which are released under the	--
-- 	public domain.																	--	
-- 	Whereas the main application icon is taken from the "Soft Scraps Icons"-Set		--
-- 	made by Deleket (JoJo Mendoza) and distributed under the						--
-- 	CC Attribution-Noncommercial-No Derivate 3.0 License which can be found at		--
-- 	http://creativecommons.org/licenses/by-nc-nd/3.0/								--
-- 	The homepage of the author can be found at http://www.deleket.com				--
--																					--
--	You should have received a copy of the GNU General Public License				--
--	along with this program; if not, write to the Free Software						--
--	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.	--
--																					--
--	Also included in the class documentation of this file is the copyright notice	--
--	for the Ganymed SSH-2 Library which is being used by this program.				--
--	The copyright of the library is not being held by the author of this program and--
--	the library is not being distributed under the terms of the GPL. If you want to	--
--	use the library in a redistribution of this program then you will also have to 	--
--	accept the library's own license apart from accepting this program's GPLv2 		--
--	license. You should also have recieved a copy of the license along with this	--
--	program; if not, go to http://www.cleondris.ch/ssh2								--											--
-- 																					--
--	If you have problems or questions regarding the software, contact the author by	--
--	e-mail, write to: tobias.burkard (at) gmail (dot) com							--
--------------------------------------------------------------------------------------
-- 																					--
--	CLASS: SSHModule 																--
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
*	<p>Ein <i>ActionModule</i> zum Ausfuehren von Kommandos auf fremden Hostcomputern mittels SSH2.	
*	Getragen wird dieses Objekt von einem <i>TaskRunnable</i>.</p> <p>Hier in der <i>doAction()</i>-Methode 
*	ist der Code fuer das Aufbauen einer Verbindung und Ausfuehren des Kommandos auf dem Fremdrechner implementiert
*	und es wird auch eine entsprechende Statusmeldung generiert. Sowohl der <i>stdout</i>, als auch <i>stderr</i>
*	werden abgefangen und in die Statusmeldung integriert.</p>
*	<p>Dieser Code nutzt Ganymed SSH-2 for Java - build 250, eine Library, welche SSH2 mit vielen Features in Java 
*	implementiert. Die Website lautet http://www.cleondris.ch/ssh2 und um die Software hier verwenden zu duerfen, 
*	ist folgender Copyrighteintrag notwendig:</p>
*	<br /><p>Copyright (c) 2006 - 2010 Christian Plattner. All rights reserved.</p>
*	<p>Redistribution and use in source and binary forms, with or without modification, are permitted provided 
*	that the following conditions are met:<ol>
*	<li>Redistributions of source code must retain the above copyright notice, this list of conditions and 
*	the following disclaimer.</li>
*	<li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and 
*	the following disclaimer in the documentation and/or other materials provided with the distribution.</li>
*	<li>Neither the name of Christian Plattner nor the names of its contributors may be used to endorse or 
*	promote products derived from this software	without specific prior written permission.</li></ol></p>
*	<p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED 
*	WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
*	PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR 
*	ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
*	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
*	OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
*	LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
*	EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.</p><br />
*
* 	@version 0.2 von 06.2011
*
* 	@author Tobias Burkard
*/
class SSHModule extends ActionModule {
	// --- Attribute
	private Connection connection_ref;
	private Session session_ref;
	
	// --- Konstruktoren
	/**
	 *	Initialisiert die uebergebenen Referenzen auf die <i>Nachrichtenfabrik</i> und <i>TestHelfer-Klasse</i>
	 *	durch Uebergabe an die Superklasse <i>ActionModule</i>. Zudem werden die Session- und Verbindungs-
	 *	Variablen initialisiert.
	 * 
	 * 	@see ActionModule
	 */
	SSHModule(MessageFacility msgFac_ref, TestHelfer test_ref) {
		super(msgFac_ref, test_ref);
		connection_ref = null;
		session_ref = null;
	} //endconstructor
	
	// --- Methoden
	/**
	 *	Schliesst Verbindung und Session mit dem Fremdrechner, falls diese vorhanden sind.
	 */
	void die() {
		if (session_ref != null) {
			session_ref.close();
			connection_ref.close();
			session_ref = null;
			connection_ref = null;
		} //endif
	} //endmethod die
	
	/**
	 *	<p>Objekteigene Implementierung der in <i>ActionModule</i> festgelegten <i>doAction()</i>-Methode.
	 *	Hier ist der eigentliche Code, welcher das Kommando auf dem fremden Host ausfuehrt, implementiert.</p>
	 *	<p>Es wird mit <i>Ganymed SSH-2 for Java</i> eine SSH2 Verbindung zu einem Fremdrechner aufgebaut und 
	 *	ein Befehl ausgefuert. Moeglich macht dies der uerbergebene Parameter, welcher neben Verbindungsinformationen, 
	 *	dem Benutzernamen, dem Passwort, dem Sudo-Passwort, und dem Aktionstyp, auch den noetigen Befehl speichert.
	 *	Als Trennzeichen der einzelnen Informationen dient <i>#D3l1M3t3R#</i>.</p>
	 *	<p>Ist die Variable <i>abbort</i> im Hauptprogramm auf <i>true</i> gesetzt, so bricht die Methode ab. Zur
	 *	Sicherheit ruft das <i>TaskRunnable</i> welche dieses Objekt traegt, noch die Methode <i>die()</i> auf,
	 *	welche alle laufenden Verbindungen beendet.</p>
	 *
	 *	@see #die()	
	 *
	 *	@param param_ref Der Parameter mit Verbindungsinformationen.
	 *	@return Statusmeldung ueber Erfolg/Misserfolg der Operation.
	 */
	String doAction(String param_ref) {
		if (TinyAdminC.abbort) {
			return "Vorgang abgebrochen ...";
		} //endif
		String retVal_ref = "";
		String[] t_ref = param_ref.split("#D3l1M3t3R#");
		String ip_ref = t_ref[0];
		String user_ref = t_ref[1];
		String pass_ref = t_ref[2];
		String cmd_ref = "";
		if (t_ref.length != 4) {
			return "Kein Befehl zur Ausführung für dieses Betriebssystem angegeben.";
		} else {
			cmd_ref = t_ref[3];
		} //endif
		boolean reachable = false;
		try {
			reachable = InetAddress.getByName(ip_ref).isReachable(5000);
		} catch (Exception ex_ref) {
        	return "FEHLER: Die Host- oder IP-Adresse ist ungültig.";
		} //endtry	
		
		if (reachable) {
			try {
				connection_ref = new Connection(ip_ref);
				connection_ref.connect();
				boolean isLogin = connection_ref.authenticateWithPassword(user_ref, pass_ref);
				if (isLogin == false) {
					return "Authentifizierung gescheitert.";
				} //endif
	
				session_ref = connection_ref.openSession();
				session_ref.execCommand(cmd_ref);
	
				InputStream stdout_ref = new StreamGobbler(session_ref.getStdout());
				InputStream stderr_ref = new StreamGobbler(session_ref.getStderr());
				
				BufferedReader bread1_ref = new BufferedReader(new InputStreamReader(stdout_ref));
				BufferedReader bread2_ref = new BufferedReader(new InputStreamReader(stderr_ref));
				
				String line_ref = "";
				String err_ref = "";
				int count = 0;
				while (!TinyAdminC.abbort) {
					line_ref = bread1_ref.readLine();
					if (line_ref == null) {
						break;
					} //endif
					if (count == 0) {
						if (!line_ref.equals("")) {
							retVal_ref += line_ref;
						} //endif
						count++;
					} else {
						if (!line_ref.equals("")) {
							retVal_ref += "\n"+line_ref;
						} //endif
					} //endif
				} //endwhile
				
				while (!TinyAdminC.abbort) {
					err_ref = bread2_ref.readLine();
					if (err_ref == null) {
						break;
					} //endif
					if (!err_ref.equals("")) {
						retVal_ref += "\n"+err_ref;
					} //endif
				} //endwhile
				
				bread1_ref.close();
				bread2_ref.close();
				die();
			} catch (Exception ex_ref) {
				retVal_ref = ex_ref.getMessage();
			} //endtry
		} else {
			retVal_ref += "Der Host \""+ip_ref+"\" ist nicht erreichbar.";
		} //endif
		
		return retVal_ref;
	} //endmethod doAction
	
} //endclass SSHModule