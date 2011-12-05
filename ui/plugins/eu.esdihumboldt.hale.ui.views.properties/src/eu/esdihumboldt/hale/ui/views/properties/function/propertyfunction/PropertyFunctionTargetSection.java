/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                 01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2011.
 */

package eu.esdihumboldt.hale.ui.views.properties.function.propertyfunction;

import eu.esdihumboldt.hale.common.align.extension.function.PropertyParameter;

/**
 * Property function section with target information
 * @author Patrick Lieb
 */
public class PropertyFunctionTargetSection extends AbstractPropertyFunctionSection<PropertyParameter>{

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#refresh()
	 */
	@Override
	public void refresh() {
		abstractRefresh(getFunction().getTarget(), "Target");
	}
}