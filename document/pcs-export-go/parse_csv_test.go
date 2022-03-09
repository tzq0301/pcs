package main

import (
	"fmt"
	"os"
	"os/exec"
	"testing"
)

func TestParseCSV(t *testing.T) {
	// initOss
	os.Chdir("/Users/peter/GoLandProjects/dd_project/csv_test")
	body := "[\n    {\n        \"name\": \"属走研指之计\",\n        \"gender\": \"elit labore\",\n        \"phone\": \"13642101989\",\n        \"email\": \"e.vpjop@qq.com\",\n        \"numOfPeople\": 53,\n        \"numOfTime\": 42\n    },\n    {\n        \"name\": \"你万展代平西算\",\n        \"gender\": \"cillum aliquip fugiat nostrud ut\",\n        \"phone\": \"18105898357\",\n        \"email\": \"b.ibvo@qq.com\",\n        \"numOfPeople\": 60,\n        \"numOfTime\": 21\n    },\n    {\n        \"name\": \"期或整信且\",\n        \"gender\": \"nisi\",\n        \"phone\": \"13211541421\",\n        \"email\": \"t.eirrl@qq.com\",\n        \"numOfPeople\": 45,\n        \"numOfTime\": 70\n    },\n    {\n        \"name\": \"且置界火非\",\n        \"gender\": \"eu mollit veniam deserunt\",\n        \"phone\": \"18601141283\",\n        \"email\": \"t.vmlcsyi@qq.com\",\n        \"numOfPeople\": 69,\n        \"numOfTime\": 19\n    }\n]"
	var res string
	fmt.Println(parse_and_generate(body, &res), res)
	exec.Command("open", "-W", res).Run()
	os.Remove(res)
}