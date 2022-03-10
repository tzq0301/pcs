package main

import (
	"encoding/json"
	"io/ioutil"
	"log"
	"os"
	"runtime/debug"
	"strconv"
	"strings"
)

type PatientInfo struct {
	Name      string
	Gender    string
	Phone     string
	Email     string
	BirthDate string
}

type DoctorInfo struct {
	Name   string
	Gender string
	Phone  string
	Email  string
}

type ConsultRecords struct {
	Date     string
	Location string
	Content  string
}

type PdfReportData struct {
	Date       string
	Pinfo      PatientInfo `json:"patientInfo"`
	Dinfo      DoctorInfo  `json:"doctorInfo"`
	SelfCommon string
	Records    []ConsultRecords `json:"consultRecords"`
	Summary    string
}

func assert(expr bool) {
	if !expr {
		debug.PrintStack()
		panic("")
	}
}

// parse h.body to patientName, doctorName, date and markdown-format text
func parse(body string) (patientName, doctorName, date, content string, err error) {
	var data PdfReportData
	// Unmarshal规约：字段名和json中对象名要相同（忽略大小写），所有字段都要public（首字母大写），字段名不同时可令json tag为json对象名
	err = json.Unmarshal([]byte(body), &data)
	if err != nil {
		return "", "", "", "", err
	}
	patientName = data.Pinfo.Name
	doctorName = data.Dinfo.Name
	date = data.Date
	mdTemplate, err := os.Open(MARKDOWN_TEMPLATE_PATH)
	buf, err := ioutil.ReadAll(mdTemplate)
	if err != nil {
		log.Fatalln("err on Open:", err.Error())
	}

	builder := strings.Builder{}
	for i := 0; i < len(buf); i++ {
		if buf[i] == '$' {
			i++
			number := 0
			for i < len(buf) {
				if IsDigit(buf[i]) {
					number = number*10 + int(buf[i]-'0')
					i++
				} else {
					builder.WriteByte(buf[i])
					break
				}
			}
			assert(number != 0)
			builder.WriteString(mappingString(number, &data))
		} else {
			builder.WriteByte(buf[i])
		}
	}

	content = builder.String()
	return
}

func mappingString(number int, data *PdfReportData) string {
	r := ""
	switch number {
	case 1:
		r = data.Dinfo.Name
	case 11:
		r = data.Dinfo.Gender
	case 12:
		r = data.Dinfo.Phone
	case 13:
		r = data.Dinfo.Email
	case 2:
		r = data.Pinfo.Name
	case 21:
		r = data.Pinfo.Gender
	case 22:
		r = data.Pinfo.Phone
	case 23:
		r = data.Pinfo.Email
	case 24:
		r = data.Pinfo.BirthDate
	case 3:
		r = data.SelfCommon
	case 4:
		r = generateRecords(data.Records)
	case 5:
		r = data.Summary
	}
	return r
}

func generateRecords(records []ConsultRecords) string {
	builder := strings.Builder{}
	for i, record := range records {
		header := "第" + strconv.Itoa(i + 1) + "次心理咨询\t时间：" + record.Date + "\t地点：" + record.Location + "\n记录：" +
			record.Content + "\n\n"
		builder.WriteString(header)
	}
	return builder.String()
}

func IsDigit(token byte) bool {
	return '0' <= token && '9' >= token
}
