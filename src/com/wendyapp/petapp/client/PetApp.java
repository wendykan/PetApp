package com.wendyapp.petapp.client;

import java.util.ArrayList;

import com.wendyapp.petapp.shared.Pet;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PetApp implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	
	
	/* Initialize class parameters */
	private DialogBox dialogBox;
	private HTML serverResponseLabel;
	private Button closeButton;
	private Label statusLabel;
	private FlexTable petsFlexTable;
	private Button refreshButton;
	private Button openNewPetDialogButton;
	
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		// define controls
		final VerticalPanel mainPanel = new VerticalPanel();
		final HorizontalPanel ownerPanel = new HorizontalPanel();
		final Label lastUpdatedLabel = new Label();

		refreshButton = new Button("Refresh All Pets");

		openNewPetDialogButton = new Button("Create a Pet");

		statusLabel = new Label();
		petsFlexTable = new FlexTable();
		
		
		// header for pets table		
	    petsFlexTable.setText(0, 0, "Owner");
	    petsFlexTable.setText(0, 1, "Pet Name");
	    petsFlexTable.setText(0, 2, "Type");
	    petsFlexTable.setText(0, 3, "Color");
	    petsFlexTable.setText(0, 4, "Age");
	    petsFlexTable.setText(0, 5, "Gender");
		petsFlexTable.setText(0, 6, "Edit");
	    petsFlexTable.setText(0, 7, "Remove");

	    // apply formatting for the table
	    petsFlexTable.getRowFormatter().addStyleName(0, "petListHeader");
	    petsFlexTable.addStyleName("petList");
	    
	    ownerPanel.add(openNewPetDialogButton);
	    ownerPanel.add(refreshButton);

	    mainPanel.add(ownerPanel);
	    mainPanel.add(petsFlexTable);
	    mainPanel.add(lastUpdatedLabel);
	    mainPanel.add(statusLabel);


	    // Associate the Main panel with the HTML host page.
	    RootPanel.get("petList").add(mainPanel);
	    
	    
		// Create the popup dialog box
		dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		

		// list all pets in the beginning
		readPet();
		
		
		
		
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
	    
	    // handler to refresh list of pets
	    refreshButton.addClickHandler(new ClickHandler(){
	    	public void onClick(ClickEvent event){
	    		readPet();	    		
	    	}
	    }
	    );


	    
		// dialog to create a new pet
		openNewPetDialogButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				// Create the popup dialog box

				newPetDialogBox addPetDialog = new newPetDialogBox(null);
				addPetDialog.showDialog(null);
				
				openNewPetDialogButton.setEnabled(false);
				refreshButton.setEnabled(false);
				
			}
			
		}
		);

		


		
	}
	
	
	// request pet information from the server, and display them in petsFlexTable
	private void readPet(){
	    greetingService.readPet("", new AsyncCallback<ArrayList<Pet>>() {
			
	    	public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				dialogBox
						.setText("Remote Procedure Call - Failure");
				serverResponseLabel
						.addStyleName("serverResponseLabelError");
				serverResponseLabel.setHTML(SERVER_ERROR);
				dialogBox.center();
				closeButton.setFocus(true);
			}

			public void onSuccess(final ArrayList<Pet> pets) {
				
			    int row = 1;			    
			    
			    // each row is one pet
			    for(final Pet p:pets){

				    petsFlexTable.setText(row, 0, p.getOwnerName());
				    petsFlexTable.setText(row, 1, p.getPetName());
				    petsFlexTable.setText(row, 2, p.getPetType());
				    petsFlexTable.setText(row, 3, p.getPetColor());
				    petsFlexTable.setText(row, 4, p.getPetAge());
				    petsFlexTable.setText(row, 5, p.getPetGender());
				    
				    
				    // adding the remove button in each row
				    Button removePetButton = new Button("x");
				    removePetButton.addClickHandler(new ClickHandler() {
				      public void onClick(ClickEvent event) {
				    	
				        int removedIndex = pets.indexOf(p);
				        removePet(p.petID);
				        pets.remove(removedIndex);     
				        petsFlexTable.removeRow(removedIndex + 1);
				        
				      }
				    });
				    petsFlexTable.setWidget(row, 7, removePetButton);	
				    
				    //format table
				    petsFlexTable.getCellFormatter().addStyleName(row, 0, "petListColumn");
				    petsFlexTable.getCellFormatter().addStyleName(row, 1, "petListColumn");
				    petsFlexTable.getCellFormatter().addStyleName(row, 2, "petListColumn");
				    petsFlexTable.getCellFormatter().addStyleName(row, 3, "petListColumn");
				    petsFlexTable.getCellFormatter().addStyleName(row, 4, "petListColumn");
				    petsFlexTable.getCellFormatter().addStyleName(row, 5, "petListColumn");
				    petsFlexTable.getCellFormatter().addStyleName(row, 6, "petListRemoveColumn");
				    petsFlexTable.getCellFormatter().addStyleName(row, 7, "petListRemoveColumn");
				    
				    
				    // adding the edit button in each row
				    Button editPetButton = new Button("Edit");
				    
				    editPetButton.addClickHandler(new ClickHandler() {
				      public void onClick(ClickEvent event) {
				    	
						newPetDialogBox editPetDialog = new newPetDialogBox(p);
						editPetDialog.showDialog(p);
						
						openNewPetDialogButton.setEnabled(false);
						refreshButton.setEnabled(false);

				        
				      }
				    });
				    
				    petsFlexTable.setWidget(row, 6, editPetButton);		    
				    
				    
				    
				    
				    row++;
			    }


			}
		});
	}
	
	// request server to remove a pet
	private void removePet(String keyString){
		
		greetingService.deletePet(keyString,
				new AsyncCallback<String>() {
	    			public void onFailure(Throwable caught) {
	    				// Show the RPC error message to the user
	    				dialogBox
	    						.setText("Remote Procedure Call - Failure");
	    				serverResponseLabel
	    						.addStyleName("serverResponseLabelError");
	    				serverResponseLabel.setHTML(SERVER_ERROR);
	    				dialogBox.center();
	    				closeButton.setFocus(true);
	    			}

	    			public void onSuccess(String s) {
	    				dialogBox.setText("Server confirmation");
	    				serverResponseLabel.setHTML(s);
	    				dialogBox.center();
	    				closeButton.setFocus(true);
	    			}
		});
	}
	
	

	// the class pet dialog which enables users to create or update a pet
	public class newPetDialogBox extends DialogBox {
				
	    public newPetDialogBox(final Pet p) {
	    	Button newPetButton;
	    		    	
			setAnimationEnabled(true);
			Button cancelButton = new Button("Cancel");

			final FlexTable newPetFlexTable = new FlexTable();
			final TextBox newOwnerNameField = new TextBox();
			final TextBox newPetNameField = new TextBox();
			final TextBox newPetTypeField = new TextBox();
			final TextBox newPetColorField = new TextBox();
			final TextBox newPetAgeField = new TextBox();
			final ListBox newPetGenderField = new ListBox();
			
			// Populate the gender listbox
			ArrayList<String> gender_list = new ArrayList<String>();
			gender_list.add("Male");
			gender_list.add("Female");
			gender_list.add("Unknown");
			for(String s:gender_list){
				newPetGenderField.addItem(s);
			}

			newPetFlexTable.setText(0, 0, "Owner Name:");
			newPetFlexTable.setWidget(0, 1, newOwnerNameField);
			newPetFlexTable.setText(1, 0, "Pet Name:");
			newPetFlexTable.setWidget(1, 1, newPetNameField);
			newPetFlexTable.setText(2, 0, "Pet Type:");
			newPetFlexTable.setWidget(2, 1, newPetTypeField);
			newPetFlexTable.setText(3, 0, "Pet Color:");
			newPetFlexTable.setWidget(3, 1, newPetColorField);
			newPetFlexTable.setText(4, 0, "Pet Age:");
			newPetFlexTable.setWidget(4, 1, newPetAgeField);
			newPetFlexTable.setText(5, 0, "Pet Gender:");
			newPetFlexTable.setWidget(5, 1, newPetGenderField);

			newPetFlexTable.setWidget(6, 1, cancelButton);
			//		final Label textToServerLabel = new Label();
//			serverResponseLabel = new HTML();
			VerticalPanel newPetVPanel = new VerticalPanel();
			newPetVPanel.add(newPetFlexTable);			

			
			cancelButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					hide();
					refreshButton.setEnabled(true);
					openNewPetDialogButton.setEnabled(true);
				}
			});
			
			// handle difference between create or edit a pet

	    	if(p == null){
	    		// create a new pet
				newPetButton = new Button("Create Pet");
				setHTML("Create a new pet");
				newPetButton.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event){
						
						
						Pet newp = new Pet(newPetNameField.getText(), newPetTypeField.getText(), newPetColorField.getText(), newPetAgeField.getText(), newPetGenderField.getItemText(newPetGenderField.getSelectedIndex()), newOwnerNameField.getText());
						callAsyncCreatePet(newp);
						hide();
						refreshButton.setEnabled(true);
						openNewPetDialogButton.setEnabled(true);
			    		readPet();
					}
				}
				);
	    	}
			
	    	else{
	    		// edit existing pet
	    		newPetButton = new Button("Update Pet");
				setHTML("Update an existing pet");
				
				newOwnerNameField.setText(p.getOwnerName());
				newPetNameField.setText(p.getPetName());
				newPetTypeField.setText(p.getPetType());
				newPetColorField.setText(p.getPetColor());
				newPetAgeField.setText(p.getPetAge());
				newPetGenderField.setSelectedIndex(gender_list.indexOf(p.getPetGender()));
				
				newPetButton.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event){
						
						
						Pet updatedp = new Pet(newPetNameField.getText(), newPetTypeField.getText(), newPetColorField.getText(), newPetAgeField.getText(), newPetGenderField.getItemText(newPetGenderField.getSelectedIndex()), newOwnerNameField.getText());
						callAsyncUpdatePet(p.petID, updatedp);
						hide();
						refreshButton.setEnabled(true);
						openNewPetDialogButton.setEnabled(true);
						readPet();
					}
				}
				);

	    	}
			
			newPetFlexTable.setWidget(6, 0, newPetButton);			
			setWidget(newPetVPanel);
			newPetFlexTable.setSize("100%", "100%");
			
	    }
	    
	    private newPetDialogBox dialog;
	    
	    // show the petDialogBox
	    public void showDialog(Pet p){
	    	if (dialog == null) {
	    		dialog = new newPetDialogBox(p);
	    		dialog.setSize("300px", "100px");
	    	}
	    	dialog.center();
	    }
	    
	    
	    // request server to create a pet 
	    private void callAsyncCreatePet(Pet p){
			greetingService.createPet(p,
					new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							dialogBox
									.setText("Remote Procedure Call - Failure");
							serverResponseLabel
									.addStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML(SERVER_ERROR);
							dialogBox.center();
							closeButton.setFocus(true);
						}

						public void onSuccess(String result) {
							dialogBox.setText("Server confirmation");
							serverResponseLabel
									.removeStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML(result);
							dialogBox.center();
							closeButton.setFocus(true);
						}
					});

	    }
	    
	    // request server to update pet info 
	    private void callAsyncUpdatePet(String KeyString, Pet p){
			greetingService.updatePet(KeyString, p,
					new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							dialogBox
									.setText("Remote Procedure Call - Failure");
							serverResponseLabel
									.addStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML(SERVER_ERROR);
							dialogBox.center();
							closeButton.setFocus(true);
						}

						public void onSuccess(String result) {
							dialogBox.setText("Server confirmation");
							serverResponseLabel
									.removeStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML(result);
							dialogBox.center();
							closeButton.setFocus(true);
						}
					});

	    }

	}
}
