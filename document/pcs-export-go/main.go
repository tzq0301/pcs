package main

import (
	"flag"
	"net/http"
	"path"
	"strconv"
)

const (
	TMP_DIR = "/tmp"
	MARKDOWN_TEMPLATE_PATH = "/root/markdown_template.txt"
	LUTE_PATH = "/root/lute-pdf"
	OSS_WEBSITE = "https://oss-cn-hangzhou.aliyuncs.com"
	OSS_DIR = "pcs/pdf"
)

var bucketName *string

func fillLink(link string) string {
	return "https://" + *bucketName + "." + path.Base(OSS_WEBSITE) + "/" + link;
}

func main() {
	keyId := flag.String("id", "", "阿里云OSS KeyId")
	keySecret := flag.String("secret", "", "阿里云OSS KeySecret")
	bucketName = flag.String("bucket", "", "阿里云OSS BucketName")
	port := flag.Int("port", 12345, "监听端口")
	flag.Parse()

	bucket = initOss(*keyId, *keySecret, *bucketName)

	http.HandleFunc("/export/pdf", export_pdf)
	http.HandleFunc("/export/csv", export_csv)
	http.HandleFunc("/export/zip", export_zip)

	err := http.ListenAndServe(":" + strconv.Itoa(*port), nil)
	if err != nil {
		return
	}
}
