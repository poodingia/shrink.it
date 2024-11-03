package main

import (
	"context"
	"log"
	"time"

	"go.mongodb.org/mongo-driver/bson"
	"uetmydinh.com/cleanup/db"
)

type CleanupRequest struct {
    AgeInDays int `json:"age_in_days"`
}

type URL struct {
    URL string `json:"URL"`
    CreatedAt time.Time `json:"createdAt"`
}

func main() {
    mongoClient := db.Dbconnect()
    mongoClient.Database("Tom").Collection("Url")
    scheduleCleanup()
}

func cleanupOldRecords() error {
    db := db.MongoCLient
    currentTime := time.Now()
    expire := time.Duration(1 * 14 * 60 * 60)
	filter := bson.M{
		"$expr": bson.M{
			"$lt": []interface{}{
				bson.M{"$add": []interface{}{"$createdAt", expire}},
				currentTime,
			},
		},
	}	
    result, err := db.Database("Tom").Collection("Url").DeleteMany(context.TODO(), filter)
    log.Printf("Deleted %v documents", result.DeletedCount)
    return err
}

func scheduleCleanup() {
    for {
        cleanupOldRecords();   
        time.Sleep(5 * time.Second)
    }
}