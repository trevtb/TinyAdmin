package de.home.tinyadmin;

//--- Importe
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
--	CLASS: IOHelfer	 																--
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
*	<p>Helfer-Klasse fuer die Datei Ein- und Ausgabe der gespeicherten Einstellungen.</p>
*	<p>Es wird zudem sichergestellt, dass die Passwortfelder der Host-Einstellungs-Tabelle <i>unter Angabe der Variablen
*	<b>KLO</b>, welche den geheimen Schluessel enthaelt</i>, zur richtigen Zeit vom <i>KryptoHelfer</i>-Objekt
*	ent- bzw. verschluesselt werden. Falls keine Einstellungsdatei existiert, oder diese fehlerhaft ist, wird eine neue
*	angelegt.</p>
*	<p>Der geheime Schluessel, welcher in der fertig kompilierten, downloadbaren Version der Anwendung Verwendung findet,
*	wird vom Autor aus Sicherheitsgruenden <i>NICHT</i> mit dem Sourcecode ausgeliefert. Es steht aber jedem frei, die
*	Variable <i>KLO</i> selbst mit einem beliebigen Schluessel zu fuellen.</p>
*	<p>Wird dies nicht getan, entsteht beim Ausfuehren der Anwendung ein Fehler (eine Exception tritt auf).</p>
*
*	@see KryptoHelfer
*
* 	@version 0.2 von 06.2011
*
* 	@author Tobias Burkard
*/
class IOHelfer {
	// --- Attribute
	private TinyAdminC main_ref;	// Referenz auf das Hauptprogramm
	private final String DATA_FILE = "tinyadmin.data";	// Dateiname der Datei, in welcher die Einstellungen
														// gespeichert werden
    private final String KLO = "AA3F40126067DD3B9313B5424C5716" + 
    							"CF89DDB35EDE85BE682582A11BCDD2935F";	// Schluessel fuer die Kryptographie
    
    // --- Konstruktoren
    /**
     *	Setzt die Variable <i>main_ref</i> auf die uebergebene Referenz des Hauptprogramms.
     */
    IOHelfer(TinyAdminC main_ref) {
    	this.main_ref = main_ref;
    } //endconstructor
	
    // --- Methoden
    /**
	 *	<p>Liest die bereits auf der Festplatte gespeicherte Datei mit Einstellungen ein und entschluesselt die
	 *	Passwort-Spalten der Host-Einstellungen mit Hilfe eines <i>KryptoHelfer</i>-Objekts. Ist keine Datei 
	 *	vorhanden oder ist diese fehlerhaft, wird die <i>makeNewSettings()</i>-Methode aufgerufen um eine neue 
	 *	zu generieren und diese zu speichern.</p>
	 *	<p>Die gespeicherte Matrix ist dreidimensional und besteht aus 2 zweidimensionalen Matritzen: <i>String[2][][]</i>.
	 *	<ul><li>Die erste dieser Matrizen fasst die Tabelle mit Host-Einstellungen des Einstellungs-GUIs.</li>
	 *	<li>Die zweite Matrix fasst die die einzelnen OS-Unterbefehle fuer die vom Benutzer spezifizierten Kommandos,
	 *	sowie die Namen dieser Kommandos im jeweils ersten Feld (<i>String[1][i][0]</i>).</li></ul></p>
	 *	<p>HINWEIS: Der geheime Schluessel <i>KLO</i> ist hier nicht enthalten und muss beim Kompilieren der
	 *	Software selbst hinzugefuegt werden.</p>
	 *
	 *	@return Die Matrix mit Einstellungen (Host-Einstellungen und selbst definierte Kommandos).
	 *	@see #makeNewSettings()
	 *	@see KryptoHelfer
	 */
	String[][][] readSettings() {
		String[][][] decryptedSettings_ref = null;
		
		try {
			FileInputStream fis_ref = new FileInputStream(DATA_FILE);
			ObjectInputStream ois_ref = new ObjectInputStream(fis_ref);
			decryptedSettings_ref = (String[][][]) ois_ref.readObject();
			ois_ref.close();
		} catch (Exception ex_ref) {
			makeNewSettings();
			decryptedSettings_ref = readSettings();
			return decryptedSettings_ref;
		} //endcatch
		
		if (decryptedSettings_ref[0][0].length != 7 || decryptedSettings_ref[0] == null || decryptedSettings_ref[1] == null) {
			makeNewSettings();
			decryptedSettings_ref = readSettings();
			return decryptedSettings_ref;
		} //endif
		for (int i=0; i<decryptedSettings_ref[0].length; i++) {
			try {
				KryptoHelfer encrypter_ref = new KryptoHelfer(KLO);
				decryptedSettings_ref[0][i][3] = encrypter_ref.decrypt(decryptedSettings_ref[0][i][3]);
				decryptedSettings_ref[0][i][4] = encrypter_ref.decrypt(decryptedSettings_ref[0][i][4]);
			} catch (Exception ex_ref) {
				ex_ref.printStackTrace();
			} //endtry
		} //endfor
		
		return decryptedSettings_ref;
	} //endmethod readSettings
	
	/**
	 *	Speichert die uebergebenen Einstellungen unter dem in <i>DATA_FILE</i> angegebenen Dateinamen auf
	 *	der Festplatte, verschluesselt jedoch zuvor die Passwort-Spalten der Hosteinstellungs-Matrix mit Hilfe eines
	 *	<i>KryptoHelfer</i>-Objekts.
	 *	<p>Die gespeicherte Matrix ist dreidimensional und besteht aus 2 zweidimensionalen Matritzen (<i>String[2][][]</i>):
	 *	<ul><li>Die erste dieser Matrizen fasst die Tabelle mit Host-Einstellungen des Einstellungs-GUIs.</li>
	 *	<li>Die zweite Matrix fasst die die einzelnen OS-Unterbefehle fuer die vom Benutzer spezifizierten Kommandos,
	 *	sowie die Namen dieser Kommandos im jeweils ersten Feld (<i>String[1][i][0]</i>).</li></ul></p>
	 *	<p>HINWEIS: Der geheime Schluessel <i>KLO</i> ist hier nicht enthalten und muss beim Kompilieren der
	 *	Software selbst hinzugefuegt werden.</p>
	 *
	 *	@param settings_ref Matrix der Einstellungen bestehend aus Hosteinstellungen und eigenen Kommandos.
	 *	@see KryptoHelfer
	 */
	void writeSettings(String[][][] settings_ref) {
		String[][][] toSave_ref = new String[2][][];
		for (int i=0; i<settings_ref.length; i++) {
			toSave_ref[i] = new String[settings_ref[i].length][];
			for (int j=0; j<settings_ref[i].length; j++) {
				toSave_ref[i][j] = (String[])settings_ref[i][j].clone();
			} //endfor
		} //endfor
		for (int i=0; i<toSave_ref[0].length; i++) {
			try {
			    KryptoHelfer encrypter_ref = new KryptoHelfer(KLO);
			    toSave_ref[0][i][3] = encrypter_ref.encrypt(toSave_ref[0][i][3]);
			    toSave_ref[0][i][4] = encrypter_ref.encrypt(toSave_ref[0][i][4]);
			} catch (Exception ex_ref) {
				ex_ref.printStackTrace();
			} //endtry
		} //endfor
		
		try {
			FileOutputStream fos_ref = new FileOutputStream(DATA_FILE);
			ObjectOutputStream ous_ref = new ObjectOutputStream(fos_ref);
			ous_ref.writeObject(toSave_ref);
			ous_ref.close();
		} catch (Exception ex_ref) {
			ex_ref.printStackTrace();
		} //endcatch
	} //endmethod writeSettings
	
	/**
	 *	<p>Erzeugt eine neue Einstellungs-Matrix mit einem Standardeintrag.</p>
	 *	<p>Der Standardeintrag fuer die Hosteinstellungen hat folgenden Inhalt:<ul>
	 *		<li>Hostname="Host 1"</li>
	 *		<li>IPAdresse="0.0.0.0"</li>
	 *		<li>Benutzername="root"</li> 
	 *		<li>Passwort=""</li>
	 *		<li>Sudo-Passwort=""</li>
	 *		<li>MAC-Adresse="00:00:00:00:00:00"</li>
	 *		<li>OS="Debian"</li></ul>
	 *	</p>
	 *	<p>Der Standardeintrag fuer die vom Benutzer selbst erstellbaren Kommandos hat folgenden Inhalt:<ul>
	 *	<li>Name="Neues Kommando #1"</li>
	 *	<li>alle restlichen Felder (fuer jedes Betriebssystem eines), werden mit <i>""</i> aufgefuellt, bleiben
	 *	also leer.</li></ul>
	 *	</p>
	 *	<p>Die Matrix ist dreidimensional und besteht aus diesen beiden, 2-dimensionalen Untermatrizen: hat also
	 *	zwei Felder.</p>
	 *	<p>Anschliessend wird die Matrix mit Hilfe der writeSettings()-Methode auf die
	 *	Festplatte geschrieben.</p>
	 *
	 *	@see #writeSettings(String[][][])
	 */
	private void makeNewSettings() {
		String[][][] settings_ref = new String[2][][];
		
		String[][] hostSettings_ref = new String[1][7];
		hostSettings_ref[0][0] = "Host 1";
		hostSettings_ref[0][1] = "0.0.0.0";
		hostSettings_ref[0][2] = "root";
		hostSettings_ref[0][3] = "";
		hostSettings_ref[0][4] = "";
		hostSettings_ref[0][5] = "00:00:00:00:00:00";
		hostSettings_ref[0][6] = "Debian";
		
		settings_ref[0] = hostSettings_ref;
		
		String[][] commandSettings_ref = new String[1][main_ref.getGUI().getOSValues().length + 1];
		for (int i=0; i<commandSettings_ref[0].length; i++) {
			commandSettings_ref[0][i] = "";
		} //endfor
		commandSettings_ref[0][0] = "Neues Kommando #1";
		settings_ref[1] = commandSettings_ref;
		
		writeSettings(settings_ref);
	} //endmethod makeNewSettings
	
} //endclass IOHelfer