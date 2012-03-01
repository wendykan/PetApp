package com.wendyapp.petapp.server;

import java.util.Date;

import com.wendyapp.petapp.shared.Pet;
import com.wendyapp.petapp.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import java.util.ArrayList;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	
	DatastoreService datastore;
	
	// creat a new pet on server
	public String createPet(Pet p) throws IllegalArgumentException{
		
		try{
            if (datastore == null) {
                datastore = DatastoreServiceFactory.getDatastoreService();
            }

            Entity pet = new Entity("Pet");
            pet.setProperty("petName",p.getPetName());
            pet.setProperty("petType",p.getPetType());
            pet.setProperty("petColor",p.getPetColor());
            pet.setProperty("petAge", p.getPetAge());
            pet.setProperty("petGender", p.getPetGender());
            pet.setProperty("petOwner", p.getOwnerName());
            
            pet.setProperty("dateUpdated", new Date());

		
			datastore.put(pet);
			return "Hello, "+p.getOwnerName()+", you've requested to create a pet named "+p.getPetName();
		}
		catch(Exception e){
            e.printStackTrace();
            return e.toString();
		}
		
		
	}
	
	// read a new pet from server
	public ArrayList<Pet> readPet(String owner) throws IllegalArgumentException{

		try{
            if (datastore == null) {
                datastore = DatastoreServiceFactory.getDatastoreService();
            }
            
			ArrayList<Pet> pets = new ArrayList<Pet>();

			// queries to fill in the arraylist
			Query q = new Query("Pet");

			q.addSort("petOwner");
			
			PreparedQuery pq = datastore.prepare(q);

			for (Entity result : pq.asIterable()) {
				Pet p = new Pet((String)result.getProperty("petName"), (String)result.getProperty("petType"),
						(String)result.getProperty("petColor"), (String)result.getProperty("petAge"),
						(String)result.getProperty("petGender"), (String)result.getProperty("petOwner"),
						(String)KeyFactory.keyToString(result.getKey()));
				pets.add(p);
			}


			return pets;
		}
		catch(Exception e){
            e.printStackTrace();
            return null;
		}
	}
	
	// update a pet on server with a unique petID (keyString)
	public String updatePet(String keyString, Pet p) throws IllegalArgumentException{
		try{
            if (datastore == null) {
                datastore = DatastoreServiceFactory.getDatastoreService();
            }

            // convert the key string to Key
            Key petKey = KeyFactory.stringToKey(keyString);
            
            // then, get the entity from that key
            Entity petEntity = datastore.get(petKey);
            
            petEntity.setProperty("petName",p.getPetName());
            petEntity.setProperty("petType",p.getPetType());
            petEntity.setProperty("petColor",p.getPetColor());
            petEntity.setProperty("petAge", p.getPetAge());
            petEntity.setProperty("petGender", p.getPetGender());
            petEntity.setProperty("petOwner", p.getOwnerName());
            
            petEntity.setProperty("dateUpdated", new Date());
            
            datastore.put(petEntity);
            
    		return "You have updated the pet.";

		}
		catch(Exception e){
            e.printStackTrace();
            return e.toString();
		}
		
	}
	
	// delete the pet with a petID (keyString)
	public String deletePet(String keyString) throws IllegalArgumentException{
		
		try{
            if (datastore == null) {
                datastore = DatastoreServiceFactory.getDatastoreService();
            }            

            Key petKey = KeyFactory.stringToKey(keyString);

            datastore.delete(petKey);
    		return "You have deleted the pet.";

		}
		catch(Exception e){
            e.printStackTrace();
            return e.toString();
		}
	}
	
}
