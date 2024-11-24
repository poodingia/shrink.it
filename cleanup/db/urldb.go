package db

import (
	"context"
	"log"
	"sync"
	"os"
	"github.com/joho/godotenv"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

var lock = &sync.Mutex{}

var UrlMongoCLient *mongo.Client;

func UrlDbconnect() *mongo.Client {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	if (UrlMongoCLient == nil) {
		lock.Lock()
		defer lock.Unlock()
		if UrlMongoCLient == nil {
			log.Print("Connecting to URL DB!")
			clientOptions := options.Client().ApplyURI(os.Getenv("URLDB_URL"))
	
			client, err := mongo.Connect(context.TODO(), clientOptions)
			if err != nil {
				log.Fatal(err)
			}

			UrlMongoCLient = client
		
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
