# Diabetic.io
(Big Data pipeline Recommender to suggest insulin intake for diabetic patients)

- Introduction
- Technology used
- Architecture
- Data Collection and cleanup
- Kafka Streaming from sensor device
- Random Forest Model generation
- RESTfull Services
- Mobile App
- ScreenShots


#Introduction:
American Diabetic association states that 29.1 million Americans, 300+ million people all over had diabetes. Diabetic medication management is always challenging.

Patients take insulin dosage one hour before lunch or dinner or breakfast based on Dr's prescription. But the real world scenario the insulin intake can be changed based on the blood glucose level,  calorie intake on specific day. 

Based on Calorie intake and Blood glucose level from public as well as generated dataset recommend insulin dosage which will help patients to avoid over/under dosage. 

#Technology Used:
The entire big data pipeline will be implemented using Lambda Architecture.

 - Kafka for streaming:Get data from socket streaming. Data is generated from wearable sensor monitor
 - Spark Streaming:  Get data from kafka use spark streaming save it to S3
 - Machine learning: Use Random Forest / Linear regression and generate models for recommendation
 - REST Service(Scala) : Use Scala and generate RESTfull service 
 - Mobile or Web App: Consume the RESTfull service and show recommendation in mobile
 - D3 or Tableau: for showing individual chart.
 - EMR : Deploy model generation in EMR Cluster
 - EC2 : Deploy Scala REST Services in EC2
 - EC2 : Kafka and Spark streaming in EC2

#Lambda Architecture:
![alt tag](https://github.com/Jayaradha/pipeline/blob/master/images/Architecture.png)

#Data Collection and cleanup:

##Data from sources
Data from Baba Medicals (india)
Data from Dr Catherine Croft Phd from university of Auckland

##Generated Data
Use python script to generate data using array of medications and range of blood glucose level
![alt tag](https://github.com/Jayaradha/pipeline/blob/master/images/input.png)



#Algorithm to use:                      
Random Forest, Regression

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







