#!/usr/bin/env python

# Prints: timestamp,count,donut

medications = [
  "1",
  "2",
  "3",
]

import random, time, sys

while True:
    x = abs(random.gauss(0,0.5))
    if x >= 1.0: x = 0.0
    index = int(len(medications) * x)
    timestamp = time.strftime("%H:%M:%S")
    count = random.randint(1,5)
    patientID = random.randint(100001,200001)
    patientWeight = random.randint(140,250)
    age = random.randint(50,99)
    bgbefore = random.randint(120,300)
    medication = random.randint(1,3)
    bgbefore = random.randint(90,140)
    ampm = random.randint(0,1)
    #print "%s,%d,%s" % (timestamp, count, medications[index])
    print "%s,%s,%s,%s,%s,%s,%s" % (patientID,patientWeight,age,bgbefore,medication,bgbefore,ampm)
    sys.stdout.flush()
    time.sleep(1)

