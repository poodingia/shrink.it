package main

import (
	"context"
	"log"
	"time"
	"os"

	"go.mongodb.org/mongo-driver/bson"
	"uetmydinh.com/cleanup/db"
)

type URL struct {
	ID string `bson:"_id"`
    URL string `bson:"URL"`
    CreatedAt time.Time `bson:"createdAt"`
}

func main() {
    db.UrlDbconnect()
    db.KeyDbconnect()
    scheduleCleanup()
}

func cleanupOldRecords() ([]string, error) {
    db := db.UrlMongoCLient
    currentTime := time.Now()
    expire := time.Duration(31) * 24 * time.Hour
	expiredTime := currentTime.Add(-expire)
    log.Printf("Expired time: %v", expiredTime)
	filter := bson.M{
		"$expr": bson.M{
			"$gt": []interface{}{
				expiredTime,
				"$createdAt",
			},
		},
	}	
    cursor, err := db.Database(os.Getenv("URLDB_DB")).Collection(os.Getenv("URLDB_COLLECTION")).Find(context.TODO(), filter)
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
		log.Printf("Found document with ID : %v, expiredAt: %v", doc.ID, doc.CreatedAt)	
		
		results = append(results, doc.ID)
	}

	if err := cursor.Err(); err != nil {
		return nil, err
	}

	resultLength := len(results)

	log.Printf("Total documents found: %d", resultLength)

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
    result, err := db.Database(os.Getenv("KEYDB_DB")).Collection(os.Getenv("URLDB_COLLECTION")).UpdateMany(context.TODO(), filter, updateFilter)

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