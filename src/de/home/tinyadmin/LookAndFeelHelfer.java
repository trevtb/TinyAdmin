package de.home.tinyadmin;

//--- Importe
import javax.swing.UIManager;

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
--	CLASS: LookAndFeelHelfer 														--
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
*	Helfer-Klasse fuer das setzen des Look&Feels aller GUI-Klassen. Stellt Werkzeuge
*	zum setzen eines nativen- oder Java-Look&Feels bereit.
*
* 	@version 0.3 von 06.2011
*
* 	@author Tobias Burkard
*/
class LookAndFeelHelfer {
	// --- Statische Methoden
	/**
	 *	Setzt das Look&Feel der Anwendung auf einen nativen Look und wirft eine <i>Exception</i>,
	 *	falls dies nicht gelingt.
	 *
	 *	@throws Exception Das native Look&Feel konnte nicht gesetzt werden.
	 */
	static void setNativeLookAndFeel() throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} //endmethod setNativeLookAndFeel

	/**
	 *	Setzt das Look&Feel der Anwendung auf den standard Java-Swing-Look und wirft eine <i>Exception</i>,
	 *	falls dies nicht gelingt.
	 *
	 *	@throws Exception Das Java-Swing-Look&Feel konnte nicht gesetzt werden.
	 */
	static void setJavaLookAndFeel() throws Exception {
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	} //endmethod setJavaLookAndFeel
	
} //endclass LookAndFeelHelfer