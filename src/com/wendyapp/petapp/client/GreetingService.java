package com.wendyapp.petapp.client;

import com.wendyapp.petapp.shared.Pet;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	
	String createPet(Pet p) throws IllegalArgumentException;
	
	ArrayList<Pet> readPet(String owner) throws IllegalArgumentException;

	String updatePet(String keyString, Pet p) throws IllegalArgumentException;
	
	String deletePet(String keyString) throws IllegalArgumentException;
}
