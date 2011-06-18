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
*	<p>Es wird zudem sichergestellt, dass alle Einstellungen zur richtigen Zeit vom <i>KryptoHelfer</i>-Objekt
*	ent- bzw. verschluesselt werden. Falls keine Einstellungsdatei existiert, oder diese fehlerhaft ist, wird eine neue
*	angelegt.</p>
*
*	@see KryptoHelfer
*
* 	@version 0.3 von 06.2011
*
* 	@author Tobias Burkard
*/
class IOHelfer {
	// --- Attribute
	private final String DATA_FILE = "tinyadmin.data";	// Dateiname der Datei, in welcher die Einstellungen
														// gespeichert werden
    
    // --- Methoden
	/**
	 *	Ueberprueft, ob es sich bei den eingelesenen Einstellungen um fuer die aktuelle Version
	 *	gueltige Daten handelt. Ist die Datei nicht kompatibel, liefert die Methode <i>false</i>
	 *	zurueck.
	 */
    boolean validateSettings() {
		boolean retVal = true;
		String[][][] settings_ref = null;
		@SuppressWarnings("unused")
		String pwdHash_ref = null;
		
		try {
			FileInputStream fis_ref = new FileInputStream(DATA_FILE);
			ObjectInputStream ois_ref = new ObjectInputStream(fis_ref);
			settings_ref = (String[][][]) ois_ref.readObject();
			pwdHash_ref = (String) ois_ref.readObject();
			ois_ref.close();
			if (settings_ref[0][0].length != 9 || settings_ref[0] == null || settings_ref[1] == null) {
				retVal = false;
			} //endif
		} catch (Exception ex_ref) {
			retVal = false;
		} //endcatch
		
		return retVal;
	} //endmethod validateSettings
    
    /**
	 *	<p>Liest die bereits auf der Festplatte gespeicherte Datei mit Einstellungen ein und entschluesselt die
	 *	Einstellungen mit Hilfe eines <i>KryptoHelfer</i>-Objekts.
	 *	<p>Die gespeicherte Matrix ist dreidimensional und besteht aus 2 zweidimensionalen Matritzen: <i>String[2][][]</i>.
	 *	<ul><li>Die erste dieser Matrizen fasst die Tabelle mit Host-Einstellungen des Einstellungs-GUIs.</li>
	 *	<li>Die zweite Matrix fasst die die einzelnen OS-Unterbefehle fuer die vom Benutzer spezifizierten Kommandos,
	 *	sowie die Namen dieser Kommandos im jeweils ersten Feld (<i>String[1][i][0]</i>).</li></ul></p>
	 *	<p>Zum Einlesen der Daten muss der Methode das Passwort uebergeben werden, mit dem die Daten verschluesselt wurden.
	 *	Ist das Paswort nicht korrekt, lieft die Methode <i>null</i> zurueck.</p>
	 *
	 *	@param pwd_ref Das Passwort, mit dem die Daten verschluesselt wurden.
	 *	@return Die Matrix mit Einstellungen (Host-Einstellungen und selbst definierte Kommandos).
	 *	@see KryptoHelfer
	 */
	String[][][] readSettings(String pwd_ref) {
		String[][][] decryptedSettings_ref = null;
		String pwdHash_ref = null;
		
		try {
			FileInputStream fis_ref = new FileInputStream(DATA_FILE);
			ObjectInputStream ois_ref = new ObjectInputStream(fis_ref);
			decryptedSettings_ref = (String[][][]) ois_ref.readObject();
			pwdHash_ref = (String) ois_ref.readObject();
			ois_ref.close();
		} catch (Exception ex_ref) {
			ex_ref.printStackTrace();
		} //endcatch
		
		KryptoHelfer decrypter_ref = new KryptoHelfer(pwd_ref);
		if (!decrypter_ref.hashMatchesPWD(pwdHash_ref)) {
			return null;
		} //endif
		
		for (int i=0; i<decryptedSettings_ref[0].length; i++) {
			for (int j=0; j<decryptedSettings_ref[0][i].length; j++) {
				decryptedSettings_ref[0][i][j] = decrypter_ref.decrypt(decryptedSettings_ref[0][i][j]);
			} //endfor
		} //endfor
		for (int i=0; i<decryptedSettings_ref[1].length; i++) {
			for (int j=0; j<decryptedSettings_ref[1][i].length; j++) {
				decryptedSettings_ref[1][i][j] = decrypter_ref.decrypt(decryptedSettings_ref[1][i][j]);
			} //endfor
		} //endfor
		
		return decryptedSettings_ref;
	} //endmethod readSettings
	
	/**
	 *	<p>Speichert die uebergebenen Einstellungen unter dem in <i>DATA_FILE</i> angegebenen Dateinamen auf
	 *	der Festplatte, verschluesselt jedoch zuvor jeden String in der Einstellungs-Matrix mit Hilfe eines
	 *	<i>KryptoHelfer</i>-Objekts.</p>
	 *	<p>Die gespeicherte Matrix ist dreidimensional und besteht aus 2 zweidimensionalen Matritzen (<i>String[2][][]</i>):
	 *	<ul><li>Die erste dieser Matrizen fasst die Tabelle mit Host-Einstellungen des Einstellungs-GUIs.</li>
	 *	<li>Die zweite Matrix fasst die die einzelnen OS-Unterbefehle fuer die vom Benutzer spezifizierten Kommandos,
	 *	sowie die Namen dieser Kommandos im jeweils ersten Feld (<i>String[1][i][0]</i>).</li></ul></p>
	 *	<p>Zusaetzlich wird auch der Hash des vom Benutzer gewaehlten Passworts in der Datei gespeichert. Dies ermoeglicht
	 *	eine schnelle Verifizierung.</p>
	 *
	 *	@param settings_ref Matrix der Einstellungen bestehend aus Hosteinstellungen und eigenen Kommandos.
	 *	@param pwd_ref Das vom Benutzer gewaehlte Passwort.
	 *	@see KryptoHelfer
	 */
	void writeSettings(String[][][] settings_ref, String pwd_ref) {
		String[][][] toSave_ref = new String[2][][];
		for (int i=0; i<settings_ref.length; i++) {
			toSave_ref[i] = new String[settings_ref[i].length][];
			for (int j=0; j<settings_ref[i].length; j++) {
				toSave_ref[i][j] = (String[])settings_ref[i][j].clone();
			} //endfor
		} //endfor
	
		KryptoHelfer encrypter_ref = new KryptoHelfer(pwd_ref);
		for (int i=0; i<toSave_ref[0].length; i++) {
			for (int j=0; j<toSave_ref[0][i].length; j++) {
				toSave_ref[0][i][j] = encrypter_ref.encrypt(toSave_ref[0][i][j]);
			} //endfor
		} //endfor
		for (int i=0; i<toSave_ref[1].length; i++) {
			for (int j=0; j<toSave_ref[1][i].length; j++) {
				toSave_ref[1][i][j] = encrypter_ref.encrypt(toSave_ref[1][i][j]);
			} //endfor
		} //endfor
	
		try {
			FileOutputStream fos_ref = new FileOutputStream(DATA_FILE);
			ObjectOutputStream ous_ref = new ObjectOutputStream(fos_ref);
			ous_ref.writeObject(toSave_ref);
			ous_ref.writeObject(encrypter_ref.getPWDHash());
			ous_ref.close();
		} catch (Exception ex_ref) {
			ex_ref.printStackTrace();
		} //endcatch
	} //endmethod writeSettings
	
} //endclass IOHelfer