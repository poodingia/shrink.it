package main

import (
    "context"
    "log"
    "time"

    "uetmydinh.com/cleanup/db"
    "go.mongodb.org/mongo-driver/bson"
)

type CleanupRequest struct {
    AgeInDays int `json:"age_in_days"`
}

type URL struct {
    URL string `json:"URL"`

}

func main() {
    mongoClient := db.Dbconnect()
    mongoClient.Database("Tom").Collection("Url")
    scheduleCleanup("https://uet.vnu.edu.vn")
}



func cleanupOldRecords(name string) error {
    db := db.MongoCLient
	filter := bson.D{{Key: "URL", Value: name}}
	var gameInfo URL 
    deleteResult, err := db.Database("Tom").Collection("Url").DeleteMany(context.TODO(), filter)
    if err != nil {
        return err
    }
    log.Printf("Deleted %v documents", deleteResult.DeletedCount)
	log.Print(gameInfo)
    return err
}

func scheduleCleanup(name string) {
    for {
        cleanupOldRecords(name);   
        time.Sleep(5 * time.Second)
    }
}