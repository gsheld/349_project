import os.path
import urllib2


path = 'Datasets'
num_files = len([f for f in os.listdir(path)
                if os.path.isfile(os.path.join(path, f))])
for i in range(1257,2015):
	firstPath = "/home/arindam/ML/ML-Project/"
	lastPath ="/health"+str(i+1)
	dataPath = firstPath + "DataFiles"+ lastPath
	metaPath = firstPath + "MetadataFiles"+lastPath
	fileName = "Datasets/health"+str(i+1)
	f = open(fileName,"r")
	lines = f.readlines()
	for line in lines:
		print line
		if "Data" in line:
			if not os.path.exists(dataPath):
    				os.makedirs(dataPath)
			line = line.split("Data : ").pop().strip()
			try:
				response = urllib2.urlopen(line)
				line = line.split("/").pop()
				dataFile = dataPath +"/"+str(line)
				with open(dataFile, 'w') as dl:
    					dl.write(response.read())
			except Exception,e:
				pass

		elif "Metadata" in line:
			if not os.path.exists(metaPath):
                                os.makedirs(metaPath)
                        line = line.split("Metadata : ").pop().strip()
			try:
                        	response = urllib2.urlopen(line)
				line = line.split("/").pop()
				metaFile = metaPath +"/"+str(line)
                        	with open(metaFile, 'w') as dl:
                                	dl.write(response.read())
			except Exception,e:
                                pass

	
	f.close()
		
				

