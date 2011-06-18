package de.home.tinyadmin;

// --- Importe
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

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
--	CLASS: KryptoHelfer 															--
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
*	<p>Helfer-Klasse fuer die Kryptographie. 
*	Wird zum Ent- bzw. Verschluesseln von Strings genutzt. Das Passwort wird bei der
*	Erstellung eines Objektes dieser Klasse uebergeben.</p>
*	<p>Strings werden mit folgendem Algorithmus verschluesselt:
*	<i>"PBEWithMD5AndDES/CBC/PKCS5Padding"</i>
*	Der Passwort-Hash fuer das Benutzerpasswort verwendet hingegen <i>"SHA-1"</i>.</p>
*	
* 	@version 0.3 von 06.2011
*
* 	@author Tobias Burkard
*/
class KryptoHelfer {
	// --- Attribute
	private Cipher ecipher_ref;						// Haupt-Objekt fuer die Entschluesselung
	private Cipher dcipher_ref;						// Haupt-Objekt fuer die Verschluesselung
	@SuppressWarnings("restriction")
	private sun.misc.BASE64Encoder encoder_ref;		// Encodiert Strings in Bytefolgen
	@SuppressWarnings("restriction")
	private sun.misc.BASE64Decoder decoder_ref;		// Decodiert Bytefolgen in String
	private String pwdString_ref;					// Das Passwort
	
	// --- Konstruktoren
	/**
	 *	Initialisiert ein neues KryptoHelfer-Objekt mit dem uebergebenen Passwort. Hier wird
	 *	auch das Salt und die Anzahl der Iterationen festgelegt. Zur Initialisierung dient
	 *	die Methode <i>init()</i>.
	 *
	 *	@param pwd_ref Passwort fuer die Verschluesselung.
	 *	@see #init(char[], byte[], int)
	 */
	@SuppressWarnings("restriction")
	KryptoHelfer(String pwd_ref) {
		pwdString_ref = pwd_ref;
		encoder_ref = new sun.misc.BASE64Encoder();
		decoder_ref = new sun.misc.BASE64Decoder();
		
		byte[] salt_ref = {(byte) 0xa3, (byte) 0x21, (byte) 0x24, (byte) 0x2c,
				(byte) 0xf2, (byte) 0xd2, (byte) 0x3e, (byte) 0x19};
		int iterations = 16;
		
		init(pwd_ref.toCharArray(), salt_ref, iterations);
	} //endconstructor
	
	/**
	 * 	Initialisiert das Kryptohelfer-Objekt mit den uebergebenen Werten.
	 * 	Es werden alle fuer die Verschluesselung noetigen Variablen
	 * 	und Objekte initialisiert/erstellt.
	 * 
	 * @param pass_ref	Das Passwort fuer die Verschluesselung.
	 * @param salt_ref	Das Salt fuer die Verschluesselung.
	 * @param iterations Die Anzahl der Iterationen fuer die Verschluesselung.
	 */
	private void init(char[] pass_ref, byte[] salt_ref, int iterations) {
		try {
			PBEParameterSpec ps_ref = new PBEParameterSpec(salt_ref, 20);
			SecretKeyFactory kf_ref = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey k_ref = kf_ref.generateSecret(new PBEKeySpec(pass_ref));
			ecipher_ref = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
			ecipher_ref.init(Cipher.ENCRYPT_MODE, k_ref, ps_ref);
			dcipher_ref = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
			dcipher_ref.init(Cipher.DECRYPT_MODE, k_ref, ps_ref);
		} catch (Exception ex_ref) {
			ex_ref.printStackTrace();
		} //endtry
	} //endmethod init
    
	// --- Methoden
	/**
	 *	Verschluesselt den uebergebenen String und liefert
	 *	das Ergebnis (ebenfalls als String) zurueck.
	 *
	 *	@param msg_ref Der zu verschluesselnde String.
	 */
    @SuppressWarnings("restriction")
	String encrypt(String msg_ref) {
    	try {
			byte[] utf8_ref = msg_ref.getBytes("UTF8");
			byte[] enc_ref = ecipher_ref.doFinal(utf8_ref);
			return encoder_ref.encode(enc_ref);
		} catch (Exception ex_ref) {
			ex_ref.printStackTrace();
		} //endtry
		
		return null;
    } //endmethod encrypt

    /**
     *	Entschluesselt den uebergebenen String und gibt
     * 	das Ergebnis (ebenfalls als String) zurueck.
     * 
     * 	@param msg_ref Der zu entschluesselnde String.
     * 	@return Der entschluesselte String.
     */
    @SuppressWarnings("restriction")
	String decrypt(String msg_ref) {
    	try {
			byte[] dec_ref = decoder_ref.decodeBuffer(msg_ref);
			byte[] utf8_ref = dcipher_ref.doFinal(dec_ref);
			return new String(utf8_ref, "UTF8");
		} catch (Exception ex_ref) {
			ex_ref.printStackTrace();
		} //endtry
		
		return null;
    } //endmethod decrypt
    
    /**
     *	Laesst den SHA-1 Hash des gesetzten Passwortes durch die Methode
     *	<i>computeHash()</i> ermitteln und wandelt das so gewonnene
     *	<i>byte[]</i>-Array mit der Methode <i>byteArrayToHexString()</i> 
     *	wieder in einen String um. Dieser wird dann zurueckgegeben.
     *	
     * 	@return Der Hash des vom Benutzer gesetzten Passwortes in String-Form.
     * 	@see #computeHash(String)
     * 	@see #byteArrayToHexString(byte[])
     */
    String getPWDHash() {
    	return byteArrayToHexString(computeHash(pwdString_ref));
    } //endmethod getPWDHash
    
    /**
     *	Vergleicht den uebergebenen Passwort-Hash mit dem durch
     *	die Methode <i>getPWDHash()</i> ermittelten Hash, welcher
     *	das momentan gesetzte Passwort repraesentiert.	
     *
     * 	@param hash_ref	Der zu pruefende Hash.
     * 	@return	<i>true</i> falls die Hashes uebereinstimmen, <i>false</i> wenn nicht.
     */
    boolean hashMatchesPWD(String hash_ref) {
    	String pwdHash_ref = getPWDHash();
    	if (pwdHash_ref.equals(hash_ref)) {
    		return true;
    	} else {
    		return false;
    	} //endif
    } //endmethod hashMatchesPWD
    
    /**
     *	Erzeugt einen SHA-1 Hash fuer den uebergebenen String und gibt diesen als 
     *	<i>byte[]</i>-Array zurueck.
     *
     * 	@param str_ref Der String fuer den ein SHA-1 Hash erstellt werden soll.
     * 	@return Der ermittelte Hash fuer den uebergebenen String.
     */
    private byte[] computeHash(String str_ref) {
       try {
    	   MessageDigest md_ref = null;
    	   md_ref = MessageDigest.getInstance("SHA-1");
	       md_ref.reset();
	       md_ref.update(str_ref.getBytes());
	       return  md_ref.digest();
       } catch (Exception ex_ref) {
    	   ex_ref.printStackTrace();
       } //endtry
       
       return null;
    } //endmethod computeHash
    
    /**
     *	Rechnet die Byte-Werte im uebergebenen <i>byte[]</i>-Array, welche
     *	einzelne Zeichen repraesentieren, wieder in einen String um.
     *
     * 	@param b_ref Das Array, von dem eine String-Repraesentation erstellt werden soll.
     * 	@return Der String, der das Array repraesentiert.
     */
    private String byteArrayToHexString(byte[] b_ref){
        StringBuffer sb_ref = new StringBuffer(b_ref.length * 2);
        for (int i=0; i<b_ref.length; i++){
          int v = b_ref[i] & 0xff;
          if (v < 16) {
            sb_ref.append('0');
          } //endif
          sb_ref.append(Integer.toHexString(v));
        } //endfor
        
        return sb_ref.toString().toUpperCase();
     } //endmethod byteArrayToHexString
    
} //endclass KryptoHelfer