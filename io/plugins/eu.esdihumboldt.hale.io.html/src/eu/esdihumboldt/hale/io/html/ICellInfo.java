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

package eu.esdihumboldt.hale.io.html;


/**
 * The Interface for representing information about a Cell
 * 
 * @author Kevin Mais
 */
public interface ICellInfo {
	
	/**
	 * Gets the location of the image for a Cell
	 * 
	 * @return the image location
	 */
	public String getImageLocation();
	
	/**
	 * Get an explanation for the cell.
	 * @return the explanation or <code>null</code> if none is available
	 */
	public String getExplanation();
	
	/**
	 * Get the explanation in html format for the given cell
	 * @return the cell explanation in html format
	 */
	public String getExplanationAsHtml();

}