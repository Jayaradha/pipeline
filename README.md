# Diabetics.io
######(Big Data pipeline Recommender to suggest insulin intake for diabetic patients)

- Introduction
- Technology used
- Architecture
- Data Collection and cleanup
   - Data from sources
   - Generated Data
   - Data formate after cleanup
- Kafka Streaming from Sensor Device
- Random Forest Model generation
- RESTfull Services
- Deployment
- Mobile Application
- Screen Shots
- Future Scope
- Credits


#Introduction:
American Diabetic association states that 29.1 million Americans, 300+ million people all over had diabetes. Diabetic medication management is always challenging.

Patients take insulin dosage one hour before lunch or dinner or breakfast based on Dr's prescription. But the real world scenario the insulin intake can be changed based on the blood glucose level,  calorie intake on specific day. 

Based on Calorie intake and Blood glucose level from public as well as generated dataset recommend insulin dosage which will help patients to avoid over/under dosage. 

#Technology Stack:
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
Collected data from medical facilities as well as universities where similar kind of research is going on
Below are the 2 are the 2 major data sources
 - Data from Baba Medicals (india)
 - Data from Dr Catherine Croft Phd from university of Auckland

##Generated Data
Use python script to generate data using array of medications and range of blood glucose level

##Data formate after cleanup
![alt tag](https://github.com/Jayaradha/pipeline/blob/master/images/input.png)

#Kafka Streaming from sensor device
Companies like abott are using wearable glucose monitors (http://www.imedicalapps.com/2014/09/abbotts-wearable-glucose-monitor/). Collect sensor data into s3 using spark streaming
```
  val ds = kafkaStream.transform(rdd =>rdd
      .map{case(key,value) => value}
      .map(line => line.split(","))
      .map{case Array(patientID,patientWeight,age,sex,mdate,bgbefore,medication,bgafter,ampm)
      =>(patientID,patientWeight,age,sex,mdate,bgbefore,medication,bgafter,ampm)})
```

#Random Forest Model generation
 From Stream and Batch data generate the model using Random Forest and Regression
 ```
     val trainingData = dfdata.map(row => {
      val AM = row.getAs[Int]("AM") // 0 or 1 AM or PM
      val MorningAfterMedicationBloodGlucose = row.getAs[Double]("MorningAfterMedicationBloodGlucose") // blood Glucose Level
      val Gender = row.getAs[Int]("Gender") // Male of Female 0 or 1
      val features = Vectors.dense(Array(AM,MorningAfterMedicationBloodGlucose,Gender))
      val label = row.getAs[Double]("MorningMedication") // 1 to 3
      val labeledPoint = new LabeledPoint(label, features)
      labeledPoint
    })
 ```

#RESTfull Services
The model is generated in S3 bucket. Predict the model and use scalatra and generate RESTful service

```
    val MorningMedication = model.predict(Vectors.dense(Array(ampm, MorningAfterMedicationBloodGlucose, Gender)))
```

#Deployment
 Model generation from spark to s3 is deployed in EMR cluster and a scheduled to run every day. Kafka is deployed in EC2 m4Xlarge instance. Scalatra is deployed in EC2 m4Xlarge instance.

#Mobile App
Scalatra genrated services are consumed by mobile app and the morning and evening medication is sent to customers mobile

#ScreenShots


##References:
- http://reference.medscape.com/drug/humalog-insulin-lispro-999005
