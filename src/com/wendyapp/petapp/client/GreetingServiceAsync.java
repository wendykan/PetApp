package com.wendyapp.petapp.client;

import com.wendyapp.petapp.shared.Pet;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
//	void greetServer(Pet p, AsyncCallback<String> callback)
//			throws IllegalArgumentException;
	void createPet(Pet p, AsyncCallback<String> callback) throws IllegalArgumentException;
	void readPet(String owner, AsyncCallback<ArrayList<Pet>> callback) throws IllegalArgumentException;
	void updatePet(String KeyString, Pet p, AsyncCallback<String> callback) throws IllegalArgumentException;
	void deletePet(String keyString, AsyncCallback<String> callback) throws IllegalArgumentException;

}
