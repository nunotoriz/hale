/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                 01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2010.
 */
package eu.esdihumboldt.hale.ui.style;

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import de.cs3d.util.logging.ALogger;
import de.cs3d.util.logging.ALoggerFactory;
import eu.esdihumboldt.hale.ui.style.internal.Messages;
import eu.esdihumboldt.hale.ui.style.service.StyleService;

/**
 * Action that loads styles from a SLD file
 * 
 * @author Simon Templer
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 */
public class LoadStylesAction extends Action {
	
	private static final ALogger log = ALoggerFactory.getLogger(LoadStylesAction.class);
	
	/**
	 * Creates an action that loads styles from a SLD file
	 */
	public LoadStylesAction() {
		super(Messages.LoadStylesAction_SuperTitle, AS_PUSH_BUTTON);
	}

	/**
	 * @see Action#run()
	 */
	@Override
	public void run() {
		StyleService styles = (StyleService) PlatformUI.getWorkbench().getService(StyleService.class);
		
		if (styles != null) {
			FileDialog files = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
			
			String[] extensions = new String[2]; 
			extensions[0]= "*.sld"; //$NON-NLS-1$
			extensions[1]= "*.*"; //$NON-NLS-1$
			files.setFilterExtensions(extensions);
			
			String filename = files.open();
			File file = new File(filename);
			
			try {
				styles.addStyles(file.toURI().toURL());
			} catch (MalformedURLException e) {
				log.error("Error loading SLD file", e); //$NON-NLS-1$
			}
		}
	}

}