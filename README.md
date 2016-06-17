# Diabetic.io
(Big Data pipeline Recommender to suggest insulin intake for diabetic patients)

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


#STEPS:
Generate Data
Random Forest (Accuracy - validate)
Web UI
Kafka - spark -S3 
Notification (Mobile)
Deploy using Docker or Ambari or AMR

#Algorithm to use:                      
Random Forest, Regression module, Naive bayes - Continuous training - spark documentation. (NO)







