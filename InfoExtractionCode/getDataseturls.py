#!/usr/bin/env python
# Selenium stuff
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import ElementNotVisibleException
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains

import time

browser = webdriver.Firefox()
site = "http://catalog.data.gov/dataset?q=health&sort=score+desc%2C+name+asc&page="
storeLinks = open("links","w")
for i in range(102):
	browser.get(site+str(i+1))
	datasets = browser.find_elements_by_css_selector(".dataset-content")
	#datasets = browser.find_elements_by_class_name("dataset-item has-or
	for j in range(len(datasets)):
		heading = datasets[j].find_element_by_css_selector(".dataset-heading")
		source = heading.find_element_by_partial_link_text(heading.text)
		linktext = source.get_attribute("href")
		storeLinks.write(linktext+"\n")
	time.sleep(5)

browser.close()
	

