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
	ID string `bson:"_id"`
    URL string `bson:"URL"`
    CreatedAt time.Time `bson:"createdAt"`
}

func main() {
    urlDb := db.UrlDbconnect()
    urlDb.Database("Tom").Collection("Url")
    keyDb := db.KeyDbconnect()
    keyDb.Database("Tom").Collection("Key")
    scheduleCleanup()
}

func cleanupOldRecords() ([]string, error) {
    db := db.UrlMongoCLient
    currentTime := time.Now()
    expire := time.Duration(0 * 14 * 60 * 60)
    log.Printf("Current time: %v", currentTime)
	filter := bson.M{
		"$expr": bson.M{
			"$lt": []interface{}{
				bson.M{"$add": []interface{}{"$createdAt", expire}},
				currentTime,
			},
		},
	}	
    cursor, err := db.Database("Tom").Collection("Url").Find(context.TODO(), filter)
	if err != nil {
		return nil, err
	}
	defer cursor.Close(context.TODO())
	var results []string
	for cursor.Next(context.TODO()) {
		var doc URL
		if err := cursor.Decode(&doc); err != nil {
			log.Printf("Error decoding document: %v", err)
			continue
		}
		results = append(results, doc.ID)
	}

	if err := cursor.Err(); err != nil {
		return nil, err
	}
	resultLength := len(results)

	for _, doc := range results {
		log.Printf("Found document with ID : %v", doc)
	}

	log.Printf("Total documents found: %d", resultLength)
    db.Database("Tom").Collection("Url").DeleteMany(context.TODO(), filter)
    if resultLength == 0 {
        return []string{""}, nil
    }
	return results, nil
}

func returnExpiredKeys(idList []string) error {
    db := db.KeyMongoCLient
    filter := bson.M{
		"_id": bson.M{
			"$in": idList,
		},
	}
    updateFilter := bson.M{
        "$set": bson.M{
            "isUsed": false,
        },
    }
    result, err := db.Database("Tom").Collection("Key").UpdateMany(context.TODO(), filter, updateFilter)

    log.Printf("The number of modified documents: %d\n", result.ModifiedCount)
    return err
}



func scheduleCleanup() {
    for {
        idList, _ := cleanupOldRecords();   
        err := returnExpiredKeys(idList);
        if err != nil {
            log.Fatal(err)
        }
        time.Sleep(5 * time.Second)
    }
}