package db

import (
	"context"
	"log"
	"sync"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

var lock = &sync.Mutex{}

var MongoCLient *mongo.Client;

func Dbconnect() *mongo.Client {

	if (MongoCLient == nil) {
		// Set client options
		lock.Lock()
		defer lock.Unlock()
		if MongoCLient == nil {
			log.Print("Connecting to MongoDB!")
			clientOptions := options.Client().ApplyURI("mongodb+srv://poodingia1212:aNRdjveBiFSSLFGD@tom.vmdqr.mongodb.net/Tom")
	
			// Connect to MongoDB
			client, err := mongo.Connect(context.TODO(), clientOptions)
			if err != nil {
				log.Fatal(err)
			}

			MongoCLient = client
		
			// Check the connection
			err = MongoCLient.Ping(context.TODO(), nil)
		
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

	return MongoCLient
}
