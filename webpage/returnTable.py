#!/usr/bin/python
import os
import random
import csv
import json
import cgi
import cgitb; cgitb.enable()
#from flask import Flask

#app = Flask("__main__")
#@app.route('/returnjson', methods=['GET', 'OPTIONS'])
#@crossdomain(origin='*')

def returnjson():
	#if request!= None:
	#	callback = request.GET.get( 'callback', None )
	print "Content-type:text/html\r\n\r\n"
	print '<html>'
	print '<head>'
	print '<title>Sending stuff in json</title>'
	print '</head>'
	print '<body>'

	for i in range(1):
        	fileName = random.choice(os.listdir("/home/apx748/DataFiles"))
		path = "/home/apx748/DataFiles/"+str(fileName)
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
		out = [dic]
		#out = [dict(zip(keys, property)) for property in reader]
		#print json.dumps(out)
		#print out
		for j in out:
			print j
			break


	print '</body>'
	print '</html>'

returnjson()
