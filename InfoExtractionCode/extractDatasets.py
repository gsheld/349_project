#!/usr/bin/env python
# Selenium stuff
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import ElementNotVisibleException
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains

import time
import StringIO

count = 0
browser = webdriver.Firefox()
dataUrls = open("links","r")
lines = dataUrls.readlines()
for line in lines:
	count = count + 1
	if count<989 or count>1410:
		continue
	browser.get(line)
        time.sleep(3)
	fileName = "Datasets/health"+str(count)
	print fileName
	output = open(fileName,"w")
	dataclass =  browser.find_elements_by_css_selector(".btn-group")
	for i in range(len(dataclass)):
		html = dataclass[i].get_attribute("innerHTML").encode("utf-8")
		for href in StringIO.StringIO(html):
			if "href" in href:
				url = href.split('href="').pop()
				url, dump = url.split('" class')
				output.write("Data : "+url+"\n")
				break		
	continue_link = browser.find_elements_by_partial_link_text('Preview')
	for i in range(len(continue_link)):
		link = continue_link[i].get_attribute("href")
		if link == url:
			continue
		output.write("Metadata : "+link+"\n")
		print "\n"
		print link
	output.close()

		
browser.close()	
