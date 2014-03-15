#!/usr/bin/python
import os
import random
import csv
import json
import cgi
import cgitb; cgitb.enable()

def returnjson():
	print "Content-type:text/html\r\n\r\n"
	print '<html>'
	print '<head>'
	print '<title>Sending stuff in json</title>'
	print '</head>'
	print '<body>'

	#g = open("queries","r")
	#lines2 = g.readlines()

	fileName = random.choice(os.listdir("/home/arindam/ML/ML-Project/DataFiles"))
	path = "/home/arindam/ML/ML-Project/DataFiles/"+str(fileName)
	f = open(path, 'r' )
	lines = f.readlines()
	keys = lines[0]
	del lines[0]
	reader = csv.reader(lines )
	dic = {}
	fileName = str(fileName)
	title = " ".join(fileName.replace(".csv","").split("-"))
	dic['columnNumber'] = len(keys)
	dic['query'] = "a random query"
	dic['tableTitle'] = title
	#dic['columnData'] = {dict(zip(keys, property)) for property in reader}
	out1 = [dic]
	out2 = [dict(zip(keys, property)) for property in reader]
	print json.dumps(out2)
	#print out
	for j in out1:
			print j
			break


	print '</body>'
	print '</html>'

returnjson()
