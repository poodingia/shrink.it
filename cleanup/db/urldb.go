package db

import (
	"context"
	"log"
	"sync"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

var lock = &sync.Mutex{}

var UrlMongoCLient *mongo.Client;

func UrlDbconnect() *mongo.Client {

	if (UrlMongoCLient == nil) {
		// Set client options
		lock.Lock()
		defer lock.Unlock()
		if UrlMongoCLient == nil {
			log.Print("Connecting to URL DB!")
			clientOptions := options.Client().ApplyURI("mongodb+srv://poodingia1212:aNRdjveBiFSSLFGD@tom.vmdqr.mongodb.net/Tom")
	
			// Connect to MongoDB
			client, err := mongo.Connect(context.TODO(), clientOptions)
			if err != nil {
				log.Fatal(err)
			}

			UrlMongoCLient = client
		
			// Check the connection
			err = UrlMongoCLient.Ping(context.TODO(), nil)
		
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

	return UrlMongoCLient
}
