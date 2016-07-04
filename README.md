# Diabetic.io
(Big Data pipeline Recommender to suggest insulin intake for diabetic patients)

Introduction
Technology used
Architecture
Kafka Streaming from sensor device
Random Forest Model generation
RESTfull Services
Mobile App
ScreenShots


#Project Description:
American Diabetic association states that 29.1 million Americans, 300+ million people all over had diabetes. Diabetic medication management is always challenging.

Patients take insulin dosage one hour before lunch or dinner or breakfast based on Dr's prescription. But the real world scenario the insulin intake can be changed based on the blood glucose level,  calorie intake on specific day. 

Based on Calorie intake and Blood glucose level from public as well as generated dataset recommend insulin dosage which will help patients to avoid over/under dosage. 

#Technology Used:
The entire big data pipeline will be implemented using Lambda Architecture.

 - Mobile or web app: For a simple batch process to get Blood Glucose level for AM or PM  - Simple html page to read data from text file and show it. If time permits can do it with Swift as ios app and show it through simulator.
 - Kafka for streaming:Get data from socket streaming. The assumption is the data is generated from wearable. Since I don’t have wearable I will try to use socket streaming
 - Flink: for Low Latency data ingest
 - Spark Streaming: - Get data from Flink and save it to S3
 - Spark: Data stored in the s3 for processing can be consumed by machine learning libraries and 
recommend the insulin intake
 - Mobile or Web App: to send notification to the patient to recommend medication
 - D3 or Tableau: for showing individual chart.
 - Docker : Deploy the entier deployment artifact into docker container. 

#Lambda Architecture:
![alt tag](https://github.com/Jayaradha/pipeline/blob/master/images/lambda.png)

#Data Input Format:
![alt tag](https://github.com/Jayaradha/pipeline/blob/master/images/input.png)

#STEPS:
- Generate Data
- Random Forest (Accuracy - validate)
- Web UI
- Kafka - spark -S3 
- Notification (Mobile)
- Deploy using Docker or Ambari or AMR

#Algorithm to use:                      
Random Forest, Regression module, Naive bayes - Continuous training - spark documentation. 

#Issues and Resolutions
- Can you anticipate problems, what are they, do you need to overcome them now? How do you overcome them?
  Kafka -> Flink (I want to try and if it didn’t work i will move to some other technique)
  Docker -> Never tried before and need to set it up.

- How far do you anticipate to take the project in the allotted time frame? 
Can spend one or 2 days and keep moving if things didn’t work as expected.

- Any other repos, libraries and other tools that you're considering using? Are you citing them? Are you acknowledging them for their contribution?
     * Catherine Croft Ph.d research scientist from University of Auckland, NewZealand will assist in trying this with real time use case. I will add necessary links in my project.

     * Trying to get data from a pharmacist in India as well.

     * Dan Moris from Radius said he can also help me through his sources to get data.

- Data
Working on generating data for now. This week end I will try to have some data.

##References:
- http://reference.medscape.com/drug/humalog-insulin-lispro-999005







