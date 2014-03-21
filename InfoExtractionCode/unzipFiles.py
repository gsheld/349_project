import zipfile
import os

#for i in range(0,2015):
count = len(os.listdir("DataFiles"))
print count


'''
for i in range(0,2015):	
	dirPath = "DataFiles/health"+str(i+1)
	for data in os.listdir(dirPath):
		name, extension = os.path.splitext(data)
		if extension == ".zip":
			unzip = zipfile.ZipFile(data)
			unzip.extractall(".")
'''
		

