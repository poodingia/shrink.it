package db

import (
	"context"
	"log"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

var KeyMongoCLient *mongo.Client;

func KeyDbconnect() *mongo.Client {

	if (KeyMongoCLient == nil) {
		// Set client options
		lock.Lock()
		defer lock.Unlock()
		if KeyMongoCLient == nil {
			log.Print("Connecting to Key DB!")
			clientOptions := options.Client().ApplyURI("mongodb+srv://poodingia1212:aNRdjveBiFSSLFGD@tom.vmdqr.mongodb.net/Tom")
	
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
