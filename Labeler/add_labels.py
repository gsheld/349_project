import sys

class QueryScore:
	def __init__(self, query, score):
		self.query = query
		self.score = score

def findRelevantScores(query, qsList):
	scores = []
	for item in qsList:
		if query == item.query:
			scores.append(int(item.score))
	return scores

def listProduct(l):
	product = 1
	for item in l:
		product *= item
	return product

labelMap = {} # Mapping from table name -> query and score
labelDatabase = open('label_database.txt', 'rb')
currentQueryFile = open(str(sys.argv[1]), 'r+')

for label in labelDatabase:
	labelArray = label.split(', ')
	labelTable = labelArray[1]

	if not labelMap.has_key(labelTable):
		labelMap[labelTable] = [QueryScore(labelArray[0], labelArray[2].strip())]
	else:
		currentList = labelMap[labelTable]
		currentList.append(QueryScore(labelArray[0], labelArray[2].strip()))
		labelMap[labelTable] = currentList

relevantScoreMap = {} # Mapping from table name -> sum of scores
for item in currentQueryFile:
	table = item.split(' # ')[1].strip()
	if labelMap.has_key(table):
		queries = labelMap[table]
		relevantScores = findRelevantScores(str(sys.argv[2]), queries) # Relevant scores for table and query
		relevantScoreMap[table] = listProduct(relevantScores)

for w in sorted(relevantScoreMap, key=relevantScoreMap.get, reverse=True):
	print w, relevantScoreMap[w]

currentQueryFile.close()
labelDatabase.close()