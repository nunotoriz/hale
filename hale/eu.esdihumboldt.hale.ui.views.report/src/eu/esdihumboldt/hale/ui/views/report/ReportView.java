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

package eu.esdihumboldt.hale.ui.views.report;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyList;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyViewer;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabItem;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import eu.esdihumboldt.hale.core.report.Message;
import eu.esdihumboldt.hale.core.report.Report;
import eu.esdihumboldt.hale.ui.service.report.ReportListener;
import eu.esdihumboldt.hale.ui.service.report.ReportService;

/**
 * The Transformation Report View.
 * 
 * @author Andreas Burchert
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 */
public class ReportView extends ViewPart implements ReportListener<Report<Message>, Message> {

	/**
	 * The view ID
	 */
	public static String ID = "eu.esdihumboldt.hale.ui.views.report";
	
	/**
	 * Displays all warnings and errors which occurred in the
	 * parse process.
	 */
	private TreeViewer viewer = null;
	
	/**
	 * Contains all selectable reports with timestamp and some information.
	 */
	private Combo combo = null;
	
	/**
	 * Innternal list with all reports.
	 */
	private ArrayList<Report<Message>> reports = new ArrayList<Report<Message>>();
	
	@Override
	public void createPartControl(Composite parent) {
		Composite page = new Composite(parent, SWT.NONE);
		
		// get ReportService and add listener
		ReportService repService = (ReportService) PlatformUI.getWorkbench().getService(ReportService.class);
		repService.addReportListener(this);
		
		// bar and TreeView
		page.setLayout(new GridLayout(2, true));
	
		combo = new Combo(page, SWT.READ_ONLY | SWT.FILL);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		viewer = new TreeViewer(page);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		viewer.setContentProvider(new ReportContentProvider());
		viewer.setLabelProvider(new ReportLabelProvider());
		
		combo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// gets the requested Report entry
				viewer.setInput(new ReportModel(reports.get(combo.getSelectionIndex())));
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				/* nothing */
			}
		});
		
		//
//		CTabFolder tabFolder = new CTabFolder(page, SWT.LEFT);
		TabFolder tabFolder = new TabFolder(page, SWT.LEFT);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		TabItem tabItem = new TabItem(tabFolder, SWT.LEFT);
		tabItem.setText("Summary");
		tabItem.setControl(null);
		
		TabItem tabItem2 = new TabItem(tabFolder, SWT.LEFT);
		tabItem2.setText("Detailed Report");
		tabItem2.setControl(null);
		
//		TabbedPropertyList list = new TabbedPropertyList(page, new TabbedPropertySheetWidgetFactory());
//		TabbedPropertySheetPage testPage = new TabbedPropertySheetPage()
//		ITabItem[] listItems = new ReportTabItem[3];
//		listItems[0] = new ReportTabItem();
//		listItems[1] = new ReportTabItem();
//		listItems[2] = new ReportTabItem();
//		
//		PropertySheet sheet1 = new PropertySheet();
//		PropertySheetPage page1 = new PropertySheetPage();
		
//		TabbedPropertyViewer tViewer = new TabbedPropertyViewer(list);
//		tViewer.setContentProvider(new ContentProvider());
		
		ReportViewer test = new ReportViewer("myid", "Report Test");
		test.createPartControl(page);
	}

	@Override
	public void setFocus() {
		/* nothing */
	}

	/**
	 * @see eu.esdihumboldt.hale.ui.service.report.ReportListener#getReportType()
	 */
	@Override
	public Class getReportType() {
		return Report.class;
	}

	/**
	 * @see eu.esdihumboldt.hale.ui.service.report.ReportListener#getMessageType()
	 */
	@Override
	public Class getMessageType() {
		return Message.class;
	}

	/**
	 * @see eu.esdihumboldt.hale.ui.service.report.ReportListener#reportAdded(eu.esdihumboldt.hale.ui.service.report.Report)
	 */
	@Override
	public void reportAdded(final Report<Message> report) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try{
					// create new ReportModel and set it as input
					viewer.setInput(new ReportModel(report));
					
					// add label to the combo box
					// TODO maybe add the current project to the label?
					combo.add("["+report.getTimestamp()+"] "+report.getTaskName()+" -- "+report.getSummary());
					
					// select current item
					combo.select(combo.getItemCount()-1);
					
					// add report to internal list
					reports.add(report);
				} catch (NullPointerException e) {
					// TODO remove this or add proper Exception handling
					System.err.println("NullPointer... "+report.getSummary());
					e.printStackTrace();
				}
			}
		});
	}

}

/**
 * Testing
 */
class ContentProvider implements IStructuredContentProvider {

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		System.err.println("ContentProvider.inputChanged()");
	}

	/**
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		System.err.println("ContentProvider.getElements()");
		return null;
	}
	
}

class ReportTabItem implements ITabItem {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabItem#getImage()
	 */
	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabItem#getText()
	 */
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return "lololol";
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabItem#isSelected()
	 */
	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabItem#isIndented()
	 */
	@Override
	public boolean isIndented() {
		// TODO Auto-generated method stub
		return false;
	}
	
}