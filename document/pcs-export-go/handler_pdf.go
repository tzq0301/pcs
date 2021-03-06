package main

import (
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"os/exec"
)

type Handler struct {
	body string

	patientName string
	doctorName  string
	date        string

	markdownPath string
	pdfPath      string
	pdfLink      string
}

func createTmpFile(pattern string) *os.File {
	tmpFile, err := ioutil.TempFile(TMP_DIR, pattern)
	if err != nil {
		log.Fatalln("error on TempFile: ", err.Error())
	}
	return tmpFile
}

// parse h.body then generateRecords markdown
func (h *Handler) parse() error {
	var mdContent string
	var err error
	h.patientName, h.doctorName, h.date, mdContent, err = parse(h.body)
	if err != nil {
		return err
	}

	// generate a markdown file and set h.markdownPath
	tmpFile := createTmpFile("tmp.*.md")
	h.markdownPath = tmpFile.Name()

	_, err = tmpFile.WriteString(mdContent)
	if err != nil {
		log.Fatalln("error on WriteString: ", err.Error())
	}
	return nil
}

// convert markdown to pdf
func (h *Handler) convert() *Handler {
	defer func(name string) {
		err := os.Remove(name)
		if err != nil {
			log.Println("os.Remove error for", err.Error())
		}
	}(h.markdownPath)
	log.Println("Handler::convert(), markdownPath=", h.markdownPath)

	tmpFile := createTmpFile("tmp.*.pdf")
	h.pdfPath = tmpFile.Name()

	// convert h.markdownPath to h.pdfPath
	cmd := exec.Command(LUTE_PATH, "-patientName", h.patientName, "-doctorName", h.doctorName, "--date", h.date,
		"-mdPath", h.markdownPath, "-savePath", h.pdfPath)
	err := cmd.Run()
	if err != nil {
		log.Fatalln("error on Run: ", err.Error(), "cmd:", cmd.String())
	}
	return h
}

// upload pdf to oss
func (h *Handler) upload() *Handler {
	defer func(name string) {
		err := os.Remove(name)
		if err != nil {
			log.Println("os.Remove error for", err.Error())
		}
	}(h.pdfPath)
	log.Println("Handler::upload(), pdfPath=", h.pdfPath)
	h.pdfLink = uploadFile(h.pdfPath)
	return h
}

// response oss link of pdf
func (h *Handler) response(w http.ResponseWriter) {
	log.Println("Handler::response(), pdfLink=", h.pdfLink)
	w.WriteHeader(200)
	_, err := w.Write([]byte(fillLink(h.pdfLink)))
	if err != nil {
		log.Println("error on write: ", err.Error())
	}
}

func (h *Handler) responseError(w http.ResponseWriter) {
	w.WriteHeader(400)
}

func export_pdf(w http.ResponseWriter, r *http.Request) {
	var buf []byte
	buf, _ = ioutil.ReadAll(r.Body)
	str := string(buf)
	log.Println("in export_pdf, http request: ", r.Method, r.URL)
	log.Println("receive num of bytes: ", len(buf))
	log.Println("http body: ", str)
	h := Handler{body: str}

	if h.parse() != nil {
		h.responseError(w)
		return
	}
	h.convert().upload().response(w)
}