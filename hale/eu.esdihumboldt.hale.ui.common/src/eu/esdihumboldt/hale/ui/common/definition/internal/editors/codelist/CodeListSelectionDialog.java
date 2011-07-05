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
package eu.esdihumboldt.hale.ui.common.definition.internal.editors.codelist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import eu.esdihumboldt.hale.codelist.CodeList;
import eu.esdihumboldt.hale.ui.common.internal.Messages;

/**
 * Dialog showing the properties of a schema item
 * 
 * @author Simon Templer
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 */
public class CodeListSelectionDialog extends TitleAreaDialog {
	
	private static final int NONE_ID = IDialogConstants.CLIENT_ID + 1;

	private CodeList codeList;
	
	private final List<CodeListSelector> selectors = new ArrayList<CodeListSelector>();
	
	private TabFolder tabFolder;
	
	private final String message;
	
	/**
	 * Constructor
	 * 
	 * @param parentShell the parent shell
	 * @param codeList the current code list
	 * @param message the message
	 */
	public CodeListSelectionDialog(Shell parentShell, CodeList codeList, String message) {
		super(parentShell);
		
		this.codeList = codeList;
		this.message = message;
	}
	
	/**
	 * @see TitleAreaDialog#createContents(Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		
		//setMessage("");
		setTitle(Messages.CodeListSelectionDialog_0); //$NON-NLS-1$
		
		return control;
	}

	/**
	 * @see Window#configureShell(Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText(Messages.CodeListSelectionDialog_1); //$NON-NLS-1$
	}

	/**
	 * @see TitleAreaDialog#createDialogArea(Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage(message, IMessageProvider.INFORMATION);
		
		Composite page = new Composite(parent, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = 600;
		page.setLayoutData(data);
		
		GridLayout pageLayout = new GridLayout(2, false);
		pageLayout.marginLeft = 0;
		pageLayout.marginTop = 0;
		pageLayout.marginLeft = 0;
		pageLayout.marginBottom = 0;
		page.setLayout(pageLayout);
		
		tabFolder = new TabFolder(page, SWT.TOP);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// list
		TabItem listItem = new TabItem(tabFolder, SWT.NONE);
		listItem.setText(Messages.CodeListSelectionDialog_2); //$NON-NLS-1$
		ListSelector listSelector = new ListSelector(tabFolder);
		listItem.setControl(listSelector.getControl());
		selectors.add(listSelector);
		
		// file
		TabItem fileItem = new TabItem(tabFolder, SWT.NONE);
		fileItem.setText(Messages.CodeListSelectionDialog_3); //$NON-NLS-1$
		FileSelector fileSelector = new FileSelector(tabFolder);
		fileItem.setControl(fileSelector.getControl());
		selectors.add(fileSelector);
		if (codeList != null) {
			fileSelector.setLocation(codeList.getLocation());
		}
		
		// initial state
		if (listSelector.selectCodeList(codeList)) {
			tabFolder.setSelection(listItem);
		}
		else {
			tabFolder.setSelection(fileItem);
		}
		
		return page;
	}
	
	/**
	 * @see Dialog#createButtonsForButtonBar(Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL,
				false);
		
		createButton(parent, NONE_ID, Messages.CodeListSelectionDialog_4, //$NON-NLS-1$
				false);
	}

	/**
	 * @see Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		CodeListSelector selector = selectors.get(tabFolder.getSelectionIndex());
		
		CodeList codeList = selector.getCodeList();
		
		if (codeList != null) {
			this.codeList = codeList;
			
			super.okPressed();
		}
		else {
			//setErrorMessage("Invalid code list selection");
			MessageDialog.openError(getShell(), Messages.CodeListSelectionDialog_5, Messages.CodeListSelectionDialog_6); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * @see Dialog#buttonPressed(int)
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case NONE_ID:
			this.codeList = null;
			setReturnCode(OK);
			close();
			break;
		default:
			super.buttonPressed(buttonId);
		}
	}

	/**
	 * @return the codeList
	 */
	public CodeList getCodeList() {
		return codeList;
	}

}