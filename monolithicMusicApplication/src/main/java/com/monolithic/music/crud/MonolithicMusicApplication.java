package com.monolithic.music.crud;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;


public class MonolithicMusicApplication {
	static MongoClient mongoClient;
	static MongoDatabase database;
	static MongoCollection<Document> collection;
	
	//Establish MongoDB Connection
	public static void MongoDBConnection() {
		// Creating a Mongo client
		mongoClient = new MongoClient("localhost",27017);
		
		// Creating database
		database = mongoClient.getDatabase("Music");
		System.out.println("Database name: " + database.getName());
		
		collection = database.getCollection("Songs");
	}
	

	//Uploading documents
	public static void Upload(String Song_Title, String Artist_Name, String Genre, Float Song_Length, String Song_Uploader, LocalDate Upload_Date, LocalTime Upload_Time) {
		MongoDBConnection();
		
		//Inserting sample records by creating documents.
		Document song = new Document("Title",Song_Title);
		song.append("Artist",Artist_Name);  
		song.append("Genre", Genre);
		song.append("Length",Song_Length);  
		song.append("Uploader",Song_Uploader);  
		song.append("Upload Date",Upload_Date);  
		song.append("Upload Time",Upload_Time);  
		collection.insertOne(song);
		System.out.println(Song_Title + " uploaded successfully");
	}
	

	//Searching record by Uploader Name
	public static void Search(String Uploader_Name)
	{
		MongoDBConnection();
		
		//specific document retrieving in a collection
		BasicDBObject searchSong = new BasicDBObject();
		searchSong.put("Uploader", Uploader_Name);
		System.out.println("Retrieving all songs uploaded by " + Uploader_Name + "...");
		MongoCursor<Document> cursor = collection.find(searchSong).iterator();
		while (cursor.hasNext()) {
		    System.out.println(cursor.next());
		}
	}
	

	//Updating documents
	public static void Update(String Uploader_Name, String Song_Name, String Field_Name, String New_fieldValue) {
		MongoDBConnection();

		//Update
		collection.updateOne(Filters.and(Filters.eq("Uploader", Uploader_Name), Filters.eq("Title", Song_Name)), Updates.set(Field_Name, New_fieldValue));       
		System.out.println("Document updated successfully...");  	
	}
	

	//Deleting records
	public static void Delete(String Uploader_Name, String Song_Name)
	{
		MongoDBConnection();
		
		//Deletion
		collection.deleteMany(Filters.and(Filters.eq("Uploader", Uploader_Name), Filters.eq("Title", Song_Name))); 
		System.out.println("Document deleted successfully...");
	}
	

	//Deleting the entire collection
	public static void Drop()
	{
		MongoDBConnection();

		// Dropping a Collection 
	    collection.drop(); 
	    System.out.println("Collection dropped successfully");
	}
	

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("--------------------------Welcome to the Music World--------------------------");
		System.out.println();
		while(true) {
			System.out.println("\n1. Upload Song\t 2. Search Song\t 3. Update Song\t 4. Delete Song\t 5. Drop collection\t 6. Exit");
			System.out.println("\nEnter your choice: ");
			int choice = Integer.parseInt(scan.nextLine());
			
			switch(choice) {
				case 1: System.out.println("\nEnter Song Details: ");
						System.out.println("\nTitle: ");
						String song_name = scan.nextLine();
						System.out.println("\nArtist name: ");
						String artist_name = scan.nextLine();
						System.out.println("\nGenre: ");
						String genre = scan.nextLine();
						System.out.println("\nLength: ");
						float song_length = Float.parseFloat(scan.nextLine());
						System.out.println("\nUploader name: ");
						String uploader_name = scan.nextLine();
						LocalDate upload_date = LocalDate.now();
						LocalTime upload_time = LocalTime.now();
						Upload(song_name, artist_name, genre, song_length, uploader_name, upload_date, upload_time);
						break;
				case 2: System.out.println("\nSearch by Uploader Name");
						System.out.println("\nUploader Name: ");
						String Uploader_name = scan.nextLine();
						Search(Uploader_name);
						break;
				case 3: System.out.println("\nUploader Name: ");
						String uploader_Name = scan.nextLine();
						System.out.println("\nSong Name: ");
						String song_Name = scan.nextLine();
						System.out.println("\nEnter field to be updated: Title, Artist, Genre, Length");
						String Field_Name = scan.nextLine();
						System.out.println("\nEnter new " + Field_Name +": ");
						String New_fieldValue = scan.nextLine();
						Update(uploader_Name, song_Name, Field_Name, New_fieldValue);		
						break;
				case 4: System.out.println("\nUploader Name: ");
						String Uploader_Name = scan.nextLine();
						System.out.println("\nSong Name: ");
						String Song_Name = scan.nextLine();
						Delete(Uploader_Name, Song_Name);
						break;
				case 5: Drop();
						break;
				case 6: System.out.println("Thanks for using our Music Application. Visit again");
						System.exit(0);
						break;
				default: System.out.println("Incorrect input!!! Please re-enter choice from our menu.");
			}
		}
	}	
}