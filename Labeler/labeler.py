import sys, os, random, csv, subprocess

labelDatabase = open('label_database.txt', 'a')

def runProgram():
	subprocess.call(['clear'])

	# This is subject to change #
	dataSetDirectory = '/home/arindam/ML/ML-Project/DataFiles/'

	# Randomly open a table csv file #
	tableFileName = random.choice(os.listdir(dataSetDirectory))
	tableFile = open(dataSetDirectory + tableFileName, 'rb')

	# Randomly select a query #
	queryFile = open('QueryList.txt', 'r')
	query = random.choice(queryFile.readlines())

	# Read CSV file #
	csvReader = csv.reader(tableFile, delimiter=',', quotechar='"')

	# Print table #
	count = 0
	print '\n***************************************\n' + 'QUERY:\t' + query + '\n***************************************\n'
	print 'Table Title: ' + tableFileName.replace('.csv', '').replace('-', ' ') + '\n'
	for row in csvReader:
		if count == 0:
			print '--Columns--\n' + ', '.join(row) + '\n'
		elif count < 6:
			print '-Row ' + str(count) + '-\n' + ', '.join(row) + '\n'
		else:
			break
		count += 1

	# Collect user input #
	noExit = True
	while noExit:
		try:
			userInput = raw_input('One a scale of 1-5 (5 being most relevant), how relevant is this table is to the query? Enter q to exit.\n >> ')
			if userInput == 'q':
				tableFile.close()
				queryFile.close()
				labelDatabase.close()
				noExit = False
			elif int(userInput) in range(1,6):
				labelDatabase.write(query.strip() + ', ' + tableFileName + ', ' + userInput + '\n')
				tableFile.close()
				queryFile.close()
				runProgram()
			else:
				print 'Out of range. Try again.'
		except:
			print 'Enter a valid rating.'

		os._exit(0)

runProgram()