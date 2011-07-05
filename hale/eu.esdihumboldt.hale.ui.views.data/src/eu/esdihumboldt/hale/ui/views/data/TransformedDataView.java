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

package eu.esdihumboldt.hale.ui.views.data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.WorkbenchPart;

import eu.esdihumboldt.hale.ui.service.schema.SchemaService.SchemaType;
import eu.esdihumboldt.hale.ui.views.data.internal.DataViewPlugin;
import eu.esdihumboldt.hale.ui.views.data.internal.Messages;
import eu.esdihumboldt.hale.ui.views.data.internal.filter.InstanceServiceSelector;

/**
 * Table for viewing transformed data
 *
 * @author Simon Templer
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 */
public class TransformedDataView extends AbstractDataView {
	
	/**
	 * The view id
	 */
	public static final String ID = "eu.esdihumboldt.hale.ui.views.data.transformed"; //$NON-NLS-1$
	
	private Image instanceImage;
	
	private Image sampleImage;
	
	private Image mapImage;
	
	private InstanceServiceSelector instanceSelector;
	
	private SampleTransformFeatureSelector sampleSelector;
	
	private MapFeatureSelector mapSelector;

	/**
	 * Default constructor
	 */
	public TransformedDataView() {
		super(new SampleTransformFeatureSelector());
		
		instanceSelector = new InstanceServiceSelector(SchemaType.TARGET);
		// another selector based on the reference sample service
		sampleSelector = (SampleTransformFeatureSelector) getFeatureSelector();
		// selector base on the map selection
		mapSelector = new MapFeatureSelector(SchemaType.TARGET);
	}

	/**
	 * @see AbstractDataView#provideCustomControls(Composite)
	 */
	@Override
	protected void provideCustomControls(Composite parent) {
		GridLayout layout = new GridLayout(2, true);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		parent.setLayout(layout);
		
		final Button instanceButton = new Button(parent, SWT.RADIO);
		if (instanceImage == null) {
			instanceImage = DataViewPlugin.getImageDescriptor("icons/random.gif").createImage(); //$NON-NLS-1$
		}
		instanceButton.setImage(instanceImage);
		instanceButton.setToolTipText(Messages.TransformedTableView_0); //$NON-NLS-1$
		instanceButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setInstanceSelector(instanceSelector);
			}
			
		});
		
		final Button sampleButton = new Button(parent, SWT.RADIO);
		if (sampleImage == null) {
			sampleImage = DataViewPlugin.getImageDescriptor("icons/table.gif").createImage(); //$NON-NLS-1$
		}
		sampleButton.setImage(sampleImage);
		sampleButton.setSelection(true);	
		sampleButton.setToolTipText(Messages.TransformedTableView_SynchToolTipText);
		sampleButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setInstanceSelector(sampleSelector);
			}
			
		});
		
		final Button mapButton = new Button(parent, SWT.RADIO);
		if (mapImage == null) {
			mapImage = DataViewPlugin.getImageDescriptor("icons/map.gif").createImage(); //$NON-NLS-1$
		}
		mapButton.setImage(mapImage);
		mapButton.setToolTipText(Messages.TransformedTableView_1); //$NON-NLS-1$
		mapButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setInstanceSelector(mapSelector);
			}
			
		});
	}

	/**
	 * @see WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		instanceImage.dispose();
		sampleImage.dispose();
		mapImage.dispose();
		
		super.dispose();
	}

}