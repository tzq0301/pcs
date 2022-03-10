package main

import (
	"encoding/json"
	"errors"
	"github.com/xuri/excelize/v2"
	"io/ioutil"
	"log"
	"math"
	"net/http"
	"os"
	"strconv"
)

type CSVReportData struct {
	Name        string
	Gender      string
	Phone       string
	Email       string
	NumOfPeople int
	NumOfTime   int
}

func parse_and_generate(body string, res_url *string) error {
	ds, err := parseCSV(body)
	if err != nil {
		return err
	}
	csvPath := generateCSV(ds)
	if csvPath == "" {
		return errors.New("csv file not exists")
	}
	defer func(name string) {
		err := os.Remove(name)
		if err != nil {
			log.Println("os.Remove error for", err.Error())
		}
	}(csvPath)
	*res_url = uploadFile(csvPath)
	return nil
}

func getSeries(ds []CSVReportData) (series string) {
	fillOne := func(name, categories, values string) string {
		return "{\n\"name\": \"" + name +
			"\",\n\"categories\": \"" + categories +
			"\",\n" + " \"values\": \"" + values + "\"\n}"
	}
	for i := 0; i < len(ds); i++ {
		series += fillOne("Sheet1!$A$"+strconv.Itoa(i+3), "Sheet1!$E$2:$F$2", "Sheet1!$E$"+strconv.Itoa(i+3)+":$F$"+strconv.Itoa(i+3))
		if i != len(ds)-1 {
			series += ","
		}
	}
	return
}

func getHeightAndWidth(ds []CSVReportData) (height, width string) {
	var maxP, minP, maxT, minT = math.MaxInt, math.MinInt, math.MaxInt, math.MinInt
	for _, data := range ds {
		if data.NumOfPeople > maxP {
			maxP = data.NumOfPeople
		}
		if data.NumOfPeople < minP {
			minP = data.NumOfPeople
		}
		if data.NumOfTime > maxT {
			maxT = data.NumOfTime
		}
		if data.NumOfTime < minT {
			minT = data.NumOfTime
		}
	}
	maxMul := maxP/minP + 1
	if maxT/minT+1 > maxMul {
		maxMul = maxT/minT + 1
	}
	height = strconv.Itoa(250 + len(ds)*25)
	width = strconv.Itoa(800 + maxMul*200)
	return
}

func parseCSV(body string) (ds []CSVReportData, err error) {
	err = json.Unmarshal([]byte(body), &ds)
	return
}

func generateCSV(ds []CSVReportData) (csvPath string) {
	categories := map[string]string{
		"A2": "姓名", "B2": "性别", "C2": "电话号", "D2": "电子邮件", "E2": "咨询人数", "F2": "咨询总时间"}
	f := excelize.NewFile()
	for k, v := range categories {
		err := f.SetCellValue("Sheet1", k, v)
		if err != nil {
			log.Println("err on SetCellValue ", err.Error())
			return
		}
	}
	for i, data := range ds {
		err := f.SetCellValue("Sheet1", "A"+strconv.Itoa(i+3), data.Name)
		err = f.SetCellValue("Sheet1", "B"+strconv.Itoa(i+3), data.Gender)
		err = f.SetCellValue("Sheet1", "C"+strconv.Itoa(i+3), data.Phone)
		err = f.SetCellValue("Sheet1", "D"+strconv.Itoa(i+3), data.Email)
		err = f.SetCellValue("Sheet1", "E"+strconv.Itoa(i+3), data.NumOfPeople)
		err = f.SetCellValue("Sheet1", "F"+strconv.Itoa(i+3), data.NumOfTime)
		if err != nil {
			log.Println("err on SetCellValue ", err.Error())
			return
		}
	}

	series := getSeries(ds)
	h, w := getHeightAndWidth(ds)

	if err := f.AddChart("Sheet1", "G1", `{
        "type": "bar",
        "series": [`+
		series+
		`],
        "format":
        {
            "x_scale": 1.0,
            "y_scale": 1.0,
            "x_offset": 15,
            "y_offset": 10,
            "print_obj": true,
            "lock_aspect_ratio": false,
            "locked": false
        },
		"dimension":
		{
			"height":`+h+`,
			"width": `+w+`
		},
        "legend":
        {
            "position": "left",
            "show_legend_key": false
        },
        "title":
        {
            "name": "工作量对比"
        },
        "plotarea":
        {
            "show_bubble_size": true,
            "show_cat_name": false,
            "show_leader_lines": false,
            "show_percent": true,
            "show_series_name": true,
            "show_val": true
        },
        "show_blanks_as": "zero"
    }`); err != nil {
		return
	}

	excelFile := createTmpFile("tmp.*.xlsx")
	if err := f.SaveAs(excelFile.Name()); err != nil {
		log.Println("err on excelizd SaveAs ", err.Error())
	}
	csvPath = excelFile.Name()
	return
}

func responseError(w http.ResponseWriter) {
	w.WriteHeader(400)
}

func response(w http.ResponseWriter, csvPath string) {
	log.Println("Handler_csv::response(), csvLink=", csvPath)
	w.WriteHeader(200)
	_, err := w.Write([]byte(fillLink(csvPath)))
	if err != nil {
		log.Println("error on write: ", err.Error())
	}
}

func export_csv(w http.ResponseWriter, r *http.Request) {
	var buf []byte
	buf, _ = ioutil.ReadAll(r.Body)
	str := string(buf)

	log.Println("in export_csv, http request: ", r.Method, r.URL)
	log.Println("receive num of bytes: ", len(buf))
	log.Println("http body: ", str)

	var res_url string
	if parse_and_generate(str, &res_url) != nil {
		responseError(w)
	} else {
		response(w, res_url)
	}
}
