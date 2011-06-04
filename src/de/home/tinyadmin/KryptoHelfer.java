package de.home.tinyadmin;

//--- Importe
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

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
*	Erstellung eines Objektes dieser Klasse uebergeben.</p> <p>Das Salt, welches mit dem 
*	Passwort gemixt wird und sozusagen als zweiter Schluessel dient, ist bereits fest
*	in diese Klasse integriert.</p>
*	<p>Als Verschluesselungsmethode dient <i>PBE</i> mit <i>MD5</i> und <i>DES</i>.</p>
*
* 	@version 0.2 von 06.2011
*
* 	@author Tobias Burkard
*/
class KryptoHelfer {
	// --- Attribute
    private Cipher ecipher_ref;			// Cipherobjekt fuer das Verschluesseln
    private Cipher dcipher_ref;			// Cipherobjekt fuer das Entschluesseln
    private byte[] salt_ref = {(byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,(byte)0x56, (byte)0x35, 
    							(byte)0xE3, (byte)0x03};	// Salt fuer die Kryptographie
    private int iterationCount = 19;	// Anzahl der Iterationen fuer die Verschluesselung
    
    // --- Konstruktoren
    /**	
	*	Initialisiert alle fuer die Kryptographie noetigen Variablen anhand des
	*	uebergebenen Keys. Der Schluessel wird durch ein <i>IOHelfer</i>-Objekt
	*	uebergeben.
	*
	*	@see IOHelfer
	*/
    KryptoHelfer(String passPhrase_ref) {
        try {
            KeySpec keySpec_ref = new PBEKeySpec(passPhrase_ref.toCharArray(), salt_ref, iterationCount);
            SecretKey key_ref = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec_ref);
            ecipher_ref = Cipher.getInstance(key_ref.getAlgorithm());
            dcipher_ref = Cipher.getInstance(key_ref.getAlgorithm());
            AlgorithmParameterSpec paramSpec_ref = new PBEParameterSpec(salt_ref, iterationCount);
            ecipher_ref.init(Cipher.ENCRYPT_MODE, key_ref, paramSpec_ref);
            dcipher_ref.init(Cipher.DECRYPT_MODE, key_ref, paramSpec_ref);
        } catch (Exception ex_ref) {
        	ex_ref.printStackTrace();
        } //endtry
    } //endconstructor
    
    // --- Methoden
    /**
	 *	Verschluesselt den uebergebenen String.
	 * 
	 *	@param str_ref Der zu verschluesselnde String.
	 *	@return Der verschluesselte String.
	 */
    @SuppressWarnings("restriction")
	String encrypt(String str_ref) {
        try {
            byte[] utf8_ref = str_ref.getBytes("UTF8");
            byte[] enc_ref = ecipher_ref.doFinal(utf8_ref);
            return new sun.misc.BASE64Encoder().encode(enc_ref);
        } catch (Exception ex_ref) {
        	ex_ref.printStackTrace();
        } //endtry
        
        return null;
    } //endmethod encrypt

    /**
	 *	Entschluesselt den uebergebenen String.
	 * 
	 *	@param str_ref Der zu entschluesselnde String.
	 *	@return Der entschluesselte String.
	 */
    @SuppressWarnings("restriction")
	String decrypt(String str_ref) {
        try {
            byte[] dec_ref = new sun.misc.BASE64Decoder().decodeBuffer(str_ref);
            byte[] utf8_ref = dcipher_ref.doFinal(dec_ref);
            return new String(utf8_ref, "UTF8");
        } catch (Exception ex_ref) {
        	ex_ref.printStackTrace();
        } //endtry
        
        return null;
    } //endmethod decrypt
    
} //endclass KryptoHelfer