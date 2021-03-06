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

package eu.esdihumboldt.hale.ui.service.schema.util;

import java.util.Collection;

import javax.xml.XMLConstants;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import eu.esdihumboldt.hale.common.schema.model.TypeDefinition;
import eu.esdihumboldt.hale.common.schema.model.constraint.type.MappableFlag;

/**
 * This tree content provider showing a Collection of TypeDefinitions grouped by
 * their namespace. Only types with a disabled HasValueFlag are returned.
 * 
 * @author Kai Schwierczek
 */
public class NSTypeTreeContentProvider implements ITreeContentProvider {

	private ListMultimap<String, TypeDefinition> nameSpaces;

	/**
	 * To make extending this class possible.
	 */
	public NSTypeTreeContentProvider() {
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// do nothing
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// do nothing
	}

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement == null) {
			nameSpaces = null;
			return new Object[0];
		}

		@SuppressWarnings("unchecked")
		Collection<? extends TypeDefinition> types = (Collection<? extends TypeDefinition>) inputElement;
		nameSpaces = ArrayListMultimap.create();
		for (TypeDefinition type : types) {
			// only show types with mappable flag
			if (!type.getConstraint(MappableFlag.class).isEnabled())
				continue;
			String ns = type.getName().getNamespaceURI();
			if (XMLConstants.NULL_NS_URI.equals(ns))
				ns = "(no namespace)";
			nameSpaces.put(ns, type);
		}

		return nameSpaces.keySet().toArray();
	}

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof String)
			return nameSpaces.get((String) parentElement).toArray();
		return null;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		if (element instanceof TypeDefinition) {
			String ns = ((TypeDefinition) element).getName().getNamespaceURI();
			if (XMLConstants.NULL_NS_URI.equals(ns))
				ns = "(no namespace)";
			return ns;
		}

		return null;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		return element instanceof String && !nameSpaces.get((String) element).isEmpty();
	}
}
