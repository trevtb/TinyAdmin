package de.home.tinyadmin;

//--- Importe
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

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
--	CLASS: SettingsTab 																--
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
 *	Abstrakte Klasse fuer Tabs der JTabbedPane des <i>TinyAdminSettingsGUI</i>.
 *	Diese Klasse stellt die innere Klasse <i>TATableEditor</i> bereit, die von allen
 *	Tab-Unterobjekten der <i>JTabbedPane</i> benoetigt wird. Die Klasse selbst
 *	erbt von einem <i>JPanel</i>.
 *
 * 	@version 0.2 von 06.2011
 *
 * 	@author Tobias Burkard
 */
abstract class SettingsTab extends JPanel {
	// --- Klassenvariablen
	static final long serialVersionUID = 321880983615147591L; // Eigene serialVersionUID
	
	// --- Innere Klassen
	/**
	 * 	<p>Eigener <i>DefaultCellEditor</i>: Sorgt dafuer, dass der gesamte Zellinhalt beim 
	 * 	Sprung in eine neue Zelle selektiert wird.</p>
	 *	<p>Dies ermoeglicht das sofortige Ueberschreiben beim Sprung in eine neue Zelle.
	 *	Beim Standardverhalten wuerde der neue Text zum bereits existierenden hinzuaddiert.</p>
	 *	<p>Zusaetzlich wird die Zelle wieder farblich neu formatiert. Der Editor sollte also nicht
	 *	verwendet werden, wenn das durch <i>LookAndFeelHelfer</i> gesetzte Look&Feel
	 *	fuer die Tabelle beibehalten werden soll soll.</p>
	 *
	 *	@see LookAndFeelHelfer
	 *
	 * 	@version 0.2 von 06.2011
	 *
	 * 	@author Tobias Burkard
	 */
	class TATableEditor extends DefaultCellEditor {
		static final long serialVersionUID = 199880983615777317L;
		JTextField tf_ref;

		TATableEditor() {
			super(new JTextField());
		} //endconstructor

		public Component getTableCellEditorComponent(JTable table_ref, Object value_ref, boolean isSelected, int row, int column) {
			Component c_ref = super.getTableCellEditorComponent(table_ref, value_ref, isSelected, row, column);
			
			if (c_ref instanceof JTextField) {
				tf_ref = ((JTextField)c_ref);
				tf_ref.selectAll();
				tf_ref.setBorder(BorderFactory.createLineBorder(new Color(99, 130, 191), 2));
				tf_ref.setBackground(Color.WHITE);
				if (isSelected) {
					tf_ref.setBackground(new Color(184,  207, 229));
				} else {
					tf_ref.setBackground(Color.WHITE);
				} //endif
				c_ref = tf_ref;
			} //endif

			return c_ref;
		} //endmethod getTableCellEditor
	} //endclass TATableEditor
	
} //endclass SettingsTab