package main

import (
	"github.com/aliyun/aliyun-oss-go-sdk/oss"
	"log"
	"os"
	"path/filepath"
	"strconv"
	"time"
)

var bucket *oss.Bucket

func initOss(keyId, keySecret, bucketName string) *oss.Bucket {
	client, err := oss.New(OSS_WEBSITE, keyId, keySecret)
	if err != nil {
		log.Println("Error:", err)
		os.Exit(-1)
	}

	bucket, err = client.Bucket(bucketName)
	if err != nil {
		log.Println("Error:", err)
		os.Exit(-1)
	}

	return bucket
}

func uploadFile(filePath string) (fileLink string) {
	timestamp := time.Now().Unix()
	fileLink = OSS_DIR + "/" + strconv.FormatInt(timestamp, 10) + filepath.Base(filePath)
	err := bucket.PutObjectFromFile(fileLink, filePath)
	if err != nil {
		log.Println("Error on uploadFile:", err)
		os.Exit(-1)
	}
	return
}