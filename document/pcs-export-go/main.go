package main

import (
	"context"
	"flag"
	"golang.org/x/sys/unix"
	"log"
	"net"
	"net/http"
	"path"
	"strconv"
	"syscall"
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
	// cmd args
	keyId := flag.String("id", "", "阿里云OSS KeyId")
	keySecret := flag.String("secret", "", "阿里云OSS KeySecret")
	bucketName = flag.String("bucket", "", "阿里云OSS BucketName")
	port := flag.Int("port", 12345, "监听端口")
	flag.Parse()

	bucket = initOss(*keyId, *keySecret, *bucketName)

	// tcp
	lc := net.ListenConfig{
		Control: func(network, address string, conn syscall.RawConn) error {
			var operr error
			if err := conn.Control(func(fd uintptr) {
				operr = syscall.SetsockoptInt(int(fd), unix.SOL_SOCKET, unix.SO_REUSEPORT, 1)
			}); err != nil {
				return err
			}
			return operr
		},
	}

	ln, err := lc.Listen(context.Background(), "tcp", "0.0.0.0:" + strconv.Itoa(*port))
	if err != nil {
		log.Println("启动服务失败", err.Error())
		return
	}

 	// http
	http.HandleFunc("/export/pdf", export_pdf)
	http.HandleFunc("/export/csv", export_csv)
	http.HandleFunc("/export/zip", export_zip)

	log.Println("服务启动...")

	err = http.Serve(ln, nil)
	if err != nil {
		log.Println("启动服务失败", err.Error())
		return
	}
}
