package db

import (
	"context"
	"os"
	"log"
    "github.com/joho/godotenv"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

var KeyMongoCLient *mongo.Client;

func KeyDbconnect() *mongo.Client {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	  }

	if (KeyMongoCLient == nil) {
		// Set client options
		lock.Lock()
		defer lock.Unlock()
		if KeyMongoCLient == nil {
			log.Print("Connecting to Key DB!")
			clientOptions := options.Client().ApplyURI(os.Getenv("KEYDB_URL"))
	
			// Connect to MongoDB
			client, err := mongo.Connect(context.TODO(), clientOptions)
			if err != nil {
				log.Fatal(err)
			}

			KeyMongoCLient = client
		
			// Check the connection
			err = KeyMongoCLient.Ping(context.TODO(), nil)
		
			if err != nil {
				log.Fatal(err)
			}
		} else {
            log.Print("Connection to MongoDB already created.")
        }

		log.Print("Connected to MongoDB!")
	} else {
		log.Print("Connection to MongoDB already created.")
	}

	return KeyMongoCLient
}
