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
--	CLASS: ActionModule 															--
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
*	<p>Abstrakte Klasse fuer alle Aktions-Module: Eine Unterklasse eines <i>ActionModules</i> implementiert die Methode 
*	<i>doAction()</i>, welche den Code fuer die Ausfuehrung der momentanen Aufgabe enthaelt. 
*	Jede <i>TaskRunnable</i>, braucht ein <i>ActionModule</i>.</p> 
*	<p>Diese Klasse gewaehrleistet auch, dass alle Submodule Zugang zur <i>MessageFacility</i> und zum 
*	<i>TestHelfer</i> haben, um (Fehler)meldungen absetzen zu koennen, oder Eingabewerte zu ueberpruefen.</p>
*
*	@see TaskRunnable
*	@see MessageFacility
*	@see TestHelfer
*
* 	@version 0.3 von 06.2011
*
* 	@author Tobias Burkard
*/
abstract class ActionModule {
	// --- Attribute
	MessageFacility msgFac_ref;	// Referenz auf die Nachrichtenfabrik um Meldungen absetzen zu koennen
	TestHelfer test_ref;	// Referenz auf die Testhelfer-Klasse um IP-Adressen/Hostnamen ueberpruefen zu koennen
	
	// --- Konstruktoren
	/**	
	*	Initialisiert die <i>Nachrichtenfabrik msgFac_ref</i> und <i>TestHelfer</i>-Klasse <i>test_ref</i>.
	*
	*	@see MessageFacility
	*	@see TestHelfer
	*/
	ActionModule(MessageFacility msgFac_ref, TestHelfer test_ref) {
		this.msgFac_ref = msgFac_ref;
		this.test_ref = test_ref;
	} //endconstructor
	
	// --- Methoden
	/**
	 *	Abstrakte Methode, welche letztendlich den eigentlichen Code zur Bewaeltigung der, dem
	 * 	Prozess gegebenen, Aufgaben implementiert.
	 * 
	 * 	@param param_ref Der auszufuehrende Befehl bzw. Informationen ueber diesen.
	 * 	@see TinyAdminC#computeCommand(String, boolean, String[][])
	 */
	abstract String doAction(String param_ref);
	
} //endclass ActionModule