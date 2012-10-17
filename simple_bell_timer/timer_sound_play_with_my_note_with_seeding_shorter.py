import subprocess
audio_file = "./Doorbell-SoundBible.com-516741062.mp3"
import random
import time
import sys

if __name__ == '__main__':
	random.seed()
	sleepTime = (5+random.randint(1,3))
	print "Sleeping for %d minutes" % sleepTime
	for i in xrange(1,sleepTime):
		if i == sleepTime-1:
			print "Almost there!"
		else:
			sys.stdout.write('.')
		sys.stdout.flush()
		time.sleep(60)
	#print "Ding-dong"
	return_code = subprocess.call(["afplay", audio_file])