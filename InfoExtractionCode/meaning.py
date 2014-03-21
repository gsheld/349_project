import urllib
import operator
import random
from BeautifulSoup import BeautifulSoup
queries = open("QueryList.txt").readlines()
for query in queries: 
#= raw_input("Enter a query: ")
#sentence = "environmental-radiation-surveilance"
	words = query.split(" ")
	for word in words:

		syns = []
		#word = raw_input("Enter a word: ")
		url = "http://thesaurus.com/browse/" +word

		try:
			data = urllib.urlopen(url)
			Soup=BeautifulSoup(data)


			syn = Soup.find('div', { "class" : "relevancy-list" })
			ant = Soup.find('div', { "class" : "list-holder" })

			synonyms = []
			antonyms = []

			for element in syn.findAll("span",{ "class" : "text" }):
				synonyms.append(str(element.text))
			#print "\n"+ "Synonyms :" + str(synonyms[0])+"\n"
		
			syns.append(synonyms[0])


		except Exception,e:
			syns.append(word)
	for s in syns:
		print s,
	print
	

	#except Exception,e:
	#print "Synonym not found in our dictionary"
	'''
	for element in ant.findAll("span",{ "class" : "text" }):
		antonyms.append(str(element.text))
	'''
	#print "\n"+ "Synonyms :" + str(synonyms)+"\n"
	#print "Antonyms :" + str(antonyms)+"\n"

