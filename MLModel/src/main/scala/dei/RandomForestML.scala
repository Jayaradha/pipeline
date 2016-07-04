package dei

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.sql.types._

/**
  * Created by Jayaradha on 7/1/16.
  */
object RandomForestML {

  def main(args: Array[String]) = {

    println("In main..")
    val conf = new SparkConf().setAppName("DiaModel")
    println("Created SparkConf..")
    val sc = new SparkContext(conf)
    println("Created SparkContext..")
    val sqlContext = new SQLContext(sc)
    println("Created SQLContext..")
    val path = System.getenv("HOME") + "/.ssh/aws-hadoop-conf.xml"
    sc.hadoopConfiguration.addResource((new java.io.File(path)).toURI().toURL())
    sc.hadoopConfiguration.get("fs.s3n.awsAccessKeyId")

    val hadoopConf = sc.hadoopConfiguration
    hadoopConf.set("fs.s3.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem")

    sc.hadoopConfiguration.get("fs.s3n.awsAccessKeyId")
    val awsAccessKey=   sc.hadoopConfiguration.get("fs.s3n.awsAccessKeyId")
    val awsSecretKey = sc.hadoopConfiguration.get("fs.s3n.awsSecretAccessKey")

    hadoopConf.set("fs.s3.awsAccessKeyId", awsAccessKey)
    hadoopConf.set("fs.s3.awsSecretAccessKey", awsSecretKey)

    val customSchema = StructType(Array(
      StructField("PatientID", IntegerType, true),
      StructField("PatientWeight", IntegerType, true),
      StructField("Age", IntegerType, true),
      StructField("Sex", IntegerType, true),
      StructField("Date", StringType, true),
      StructField("MorningBloodGlucose", DoubleType, true),
      StructField("AM", IntegerType, true),
      StructField("MorningMedication", DoubleType, true),
      StructField("MorningAfterMedicationBloodGlucose", DoubleType, true),
      StructField("EveningBloodGlucose", DoubleType, true),
      StructField("EveningMedication", DoubleType, true),
      StructField("PM", IntegerType, true),
      StructField("EveningAfterMedicationBloodGlucose", DoubleType, true)
    ))


    val df = sqlContext.load(
      "com.databricks.spark.csv",
      schema = customSchema,
      Map("path" -> "s3://diameddata/PatientDataSample3.csv", "header" -> "true"))


    val dfam = df.select("PatientID","PatientWeight","Age","Sex","MorningBloodGlucose","MorningMedication","AM","MorningAfterMedicationBloodGlucose")
    val dfpm = df.select("PatientID","PatientWeight","Age","Sex","EveningBloodGlucose","EveningMedication","PM","EveningAfterMedicationBloodGlucose")


    val dfall = dfam unionAll(dfpm)
    val dfdata = dfall.selectExpr("(MorningMedication-1) AS MorningMedication","AM","MorningAfterMedicationBloodGlucose","Sex AS Gender")

    // Convert to labeled points
    val trainingData = dfdata.map(row => {
      val AM = row.getAs[Int]("AM") // 0 or 1 AM or PM
      val MorningAfterMedicationBloodGlucose = row.getAs[Double]("MorningAfterMedicationBloodGlucose") // blood Glucose Level
      val Gender = row.getAs[Int]("Gender") // Male of Female 0 or 1
      val features = Vectors.dense(Array(AM,MorningAfterMedicationBloodGlucose,Gender))
      val label = row.getAs[Double]("MorningMedication") // 1 to 3
      val labeledPoint = new LabeledPoint(label, features)
      labeledPoint
    })

    val numClasses = 3

    val categoricalFeaturesInfo = Map[Int, Int](
      // Feature 0 - AM PM
      0 -> 2,
      // Feature 2 : Male, Female
      2 -> 2
    )

    val numTrees = 100
    val featureSubsetStrategy = "auto"
    val impurity = "gini"
    val maxDepth = 3
    val maxBins = 32

    val model = RandomForest.trainClassifier(trainingData,
      numClasses, categoricalFeaturesInfo,
      numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins)

    model.save(sc, "s3://diameddata/Diabetic-rf-model-3/")
    println("Saved Model..")
    //Prediction
    // val predict = model.predict(Vectors.dense(Array(1, 250.0, 0)))
    //val predict = model.predict(Vectors.dense(Array(0, 250.0, 0)))
  }


}