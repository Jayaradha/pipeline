package JsonML

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.tree.model.RandomForestModel
import org.apache.spark.{SparkConf, SparkContext}
import org.json4s._
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
  * Created by Jayaradha on 6/21/16.
  */
class DiabeticRandomForestServelet extends ScalatraServlet with JacksonJsonSupport {
  protected implicit val jsonFormats: Formats = DefaultFormats

  case class DataPoint(dataKey: String, dataValue: Double)

  val conf = new SparkConf().setMaster("local[*]").setAppName("DiabeticRandomForest")
  val sc = new SparkContext(conf)
  //val model = RandomForestModel.load(sc, "/Users/arivolit/Gavanize/capstone/pipeline/data/Diabetic-rf-model")

  val model = RandomForestModel.load(sc, "s3://diamed/Diabetic-rf-model")


  before() {
    //contentType = "text/json"
    contentType = formats("json")

  }

  get("/") {

  }

  get("/getResult.json") {
// val features = Vectors.dense(Array(AM,MorningAfterMedicationBloodGlucose,Gender))
    //http://localhost:8089/app/getResult.json?ampm=1&MorningAfterMedicationBloodGlucose=140.0&Gender=1
   /*val predict = model.predict(Vectors.dense(Array(0, 145.0, 1.0)))
    scala> val predict = model.predict(Vectors.dense(Array(0, 250.0, 0)))
    predict: Double = 0.0

    scala> val predict = model.predict(Vectors.dense(Array(1, 250.0, 0)))
    predict: Double = 2.0
    */
    val ampm = params.get("ampm").get.toInt
    val MorningAfterMedicationBloodGlucose = params.get("MorningAfterMedicationBloodGlucose").get.toDouble
    val Gender = params.get("Gender").get.toInt

    println(ampm)
    println(MorningAfterMedicationBloodGlucose)
    println(Gender)

    val MorningMedication = model.predict(Vectors.dense(Array(ampm, MorningAfterMedicationBloodGlucose, Gender)))

    val dp = new DataPoint("Medication", MorningMedication)
    dp
    //List("a1","b1")
  }

  override def render(value: JValue)(implicit formats: Formats): JValue = value.camelizeKeys
}
