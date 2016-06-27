package DiaModel

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.tree.model.RandomForestModel
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.sql.types._
/**
  * Created by Jayaradha on 6/17/16.
  */
object DiabeticRandomForestML {

  def main(args: Array[String]) = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("DiaModel")
    val sc = new SparkContext(conf)

    val sqlContext = new SQLContext(sc)

    val customSchema = StructType(Array(
      StructField("PatientID", IntegerType, true),
      StructField("PatientWeight", IntegerType, true),
      StructField("Age", IntegerType, true),
      StructField("Sex", StringType, true),
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
      Map("path" -> "/Users/arivolit/Gavanize/capstone/pipeline/data/PatientDataSample2.csv", "header" -> "true"))


    val dfam = df.select("PatientID","PatientWeight","Age","Sex","MorningBloodGlucose","MorningMedication","AM","MorningAfterMedicationBloodGlucose")
    val dfpm = df.select("PatientID","PatientWeight","Age","Sex","EveningBloodGlucose","EveningMedication","PM","EveningAfterMedicationBloodGlucose")


    val dfall = dfam unionAll(dfpm)
    val dfdata = dfall.selectExpr("(MorningMedication-1) AS MorningMedication","AM","MorningAfterMedicationBloodGlucose","IF(Sex='Male',0,1) AS Gender")

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

    model.save(sc, "/Users/arivolit/Gavanize/capstone/pipeline/data/Diabetic-rf-model")
    println("Saved..")
  //Prediction
   // val predict = model.predict(Vectors.dense(Array(1, 250.0, 0)))
    //val predict = model.predict(Vectors.dense(Array(0, 250.0, 0)))
  }

}

//Make meidcation unit homogenious 1,2,3 4,5,6 1-metformin 250 ml, 2->metformin->500ml, 3->insulin 1 unit
//Union Morning and Evening
//Medicine Type (Injection or Tablet or insulin or galgrine or metformin 0,1,2,3,4 catigorical feature mecication cloum has 16 )