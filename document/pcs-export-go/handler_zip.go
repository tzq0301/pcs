package main

import (
	"encoding/json"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"os/exec"
	"sync"
)

func export_zip(w http.ResponseWriter, r *http.Request) {
	var buf []byte
	buf, _ = ioutil.ReadAll(r.Body)
	str := string(buf)
	log.Println("in export_zip, http request: ", r.Method, r.URL)
	log.Println("receive num of bytes: ", len(buf))
	log.Println("http body: ", str)

	// 解析json
	var pdfs []PdfReportData
	if err := json.Unmarshal([]byte(str), &pdfs); err != nil {
		responseError(w)
		return
	}
	if len(pdfs) == 0 {
		responseError(w)
		return
	}

	pdfPaths := make([]string, len(pdfs))
	isAllOk := true
	wg := sync.WaitGroup{}
	wg.Add(len(pdfs))

	// 分配任务，每个json数组的对象成员都被一个routine处理，生成多个pdf
	for i := 0; i < len(pdfs); i++ {
		go func(i int) {
			defer wg.Done()
			ni_str, err := json.Marshal(pdfs[i])
			if err != nil {
				isAllOk = false
				return
			}
			h := Handler{body: string(ni_str)}
			if h.parse() != nil {
				h.responseError(w)
				isAllOk = false
				return
			}
			h.convert()
			pdfPaths[i] = h.pdfPath
		}(i)
	}

	wg.Wait()
	if !isAllOk {
		responseError(w)
		return
	}
	// 打包， 响应link
	zipPath := pack(pdfPaths)
	defer func(name string) {
		err := os.Remove(name)
		if err != nil {
			log.Println("os.Remove error for", err.Error())
		}
	}(zipPath)
	response(w, uploadFile(zipPath))
}

func pack(paths []string) string {
	for _, path := range paths {
		defer func(name string) {
			err := os.Remove(name)
			if err != nil {
				log.Println("os.Remove error for", err.Error())
			}
		}(path)
	}
	zipFile := createTmpFile("tmp*.zip")
	os.Remove(zipFile.Name())
	cmd := exec.Command("zip")
	cmd.Args = append(cmd.Args, zipFile.Name())
	cmd.Args = append(cmd.Args, paths...)
	err := cmd.Run()
	if err != nil {
		log.Fatalln("error on Run: ", err.Error(), "cmd:", cmd.String())
	}
	return zipFile.Name()
}
