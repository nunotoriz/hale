/*
 * Copyright (c) 2012 Data Harmonisation Panel
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     HUMBOLDT EU Integrated Project #030962
 *     Data Harmonisation Panel <http://www.dhpanel.eu>
 */

package eu.esdihumboldt.hale.common.align.extension.function;

/**
 * Function utility methods
 * 
 * @author Simon Templer
 */
public abstract class FunctionUtil {

	/**
	 * Get the function w/ the given identifier.
	 * 
	 * @param id the function ID
	 * @return the function or <code>null</code> if no function with the given
	 *         identifier was found
	 */
	public static AbstractFunction<?> getFunction(String id) {
		AbstractFunction<?> result = null;

		result = TypeFunctionExtension.getInstance().get(id);

		if (result == null) {
			result = PropertyFunctionExtension.getInstance().get(id);
		}

		return result;
	}

}
