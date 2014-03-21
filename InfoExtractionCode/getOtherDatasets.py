#!/usr/bin/env python
from bs4 import BeautifulSoup
import urllib2
import time

site = "http://catalog.data.gov/organization/3609164c-4c49-4a44-9179-23163027dc0a?res_format=CSV&_res_format_limit=0&page="
for i in range(1):
	path = site+str(i+1)
	data = urllib2.urlopen(path)
	Soup=BeautifulSoup(data)
	for a in Soup.findAll('a', href = True):
		if "http" not in a['href'] and "/dataset/" in a['href']:
			fileName =  (a.text).replace(" ","-")

		if  "JSON" in a.text :
			print a['href']
			response = urllib2.urlopen(a['href'])
			if "json" in a['href']:
				extension = ".csv"
			elif "zip" in a['href']:
				extension = ".zip"
			extension = "."+extension.split(".")[-1]
			dataFile = "/Users/arindam/ML/ML-Project/CSVFiles/"+fileName+extension
			with open(dataFile, 'w') as dl:
				dl.write(response.read())

			
