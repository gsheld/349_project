from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import ElementNotVisibleException
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains

import time
import StringIO
import os.path
import urllib2


path = 'Datasets'
num_files = len([f for f in os.listdir(path)
                if os.path.isfile(os.path.join(path, f))])
for i in range(1274, 1274 + num_files):
	firstPath = "/home/arindam/ML/ML-Project/"
	lastPath ="/geospatial"+str(i+1)
	dataPath = firstPath + "DataFiles"+ lastPath
	fileName = "Datasets/health"+str(i+1)
	f = open(fileName,"r")
	lines = f.readlines()
	for line in lines:
		if "Data" in line:
			if not os.path.exists(dataPath):
    				os.makedirs(dataPath)
			line = line.split("Data : ").pop().strip()
			try:
				browser.get(line)
				time.sleep(3)
				urls = []
				links = browser.find_elements_by_tag_name("a")
				for i in range(len(links)):
					l = links[i]
					link = str(l.get_attribute("href"))
					if "csv"in link and "http" in link:
						urls.append(link)
				for url in urls:
					response = urllib2.urlopen(url)
					name, extension = os.path.splitext(data)
                			if extension == ".csv":
						line = line.split("/").pop()
						dataFile = dataPath +"/"+str(line)
						with open(dataFile, 'w') as dl:
    							dl.write(response.read())
			except Exception,e:
				pass

	
	f.close()
