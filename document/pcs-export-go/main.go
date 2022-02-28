package main

import (
	"flag"
	"net/http"
)

const (
	TMP_DIR = "/tmp"
	MARKDOWN_TEMPLATE_PATH = "/Users/peter/GoLandProjects/dd_project/pcs-export-go/markdown_template.txt"
	LUTE_PATH = "/Users/peter/GoLandProjects/dd_project/pcs-export-go/lute-pdf"
	OSS_WEBSITE = "https://oss-cn-hangzhou.aliyuncs.com"
	OSS_DIR = "pcs/pdf"
)

func main() {
	keyId := flag.String("id", "", "阿里云OSS KeyId")
	keySecret := flag.String("secret", "", "阿里云OSS KeySecret")
	bucketName := flag.String("bucket", "", "阿里云OSS BucketName")
	flag.Parse()

	bucket = initOss(*keyId, *keySecret, *bucketName)

	http.HandleFunc("/export", export)

	err := http.ListenAndServe(":10000", nil)
	if err != nil {
		return
	}
}
