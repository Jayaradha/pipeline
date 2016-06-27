package dei

import org.apache.spark
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.tree.model.RandomForestModel
import org.apache.spark.{SparkConf, SparkContext}
import org.json4s.{DefaultFormats, Formats, JValue}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

class TitanicServlet extends ScalatraServlet with JacksonJsonSupport {
  protected implicit val jsonFormats: Formats = DefaultFormats
  case class DataPoint(dataKey:String,dataValue:Double)
  val conf = new SparkConf().setMaster("local[*]").setAppName("Titanic")
  val sc = new SparkContext(conf)
  val model = RandomForestModel.load(sc, "/Users/arivolit/Gavanize/dei-student-2/case-study1/lab-answer/lab1/titanic-model")

  before() {
    //contentType = "text/json"
    contentType = formats("json")

  }

  get ("/") {

  }

  get("/getResult.json") {

    val pclass = params.get("pclass").get.toInt
    val age = params.get("age").get.toDouble
    val gender = params.get("gender").get.toInt

    println(pclass)
    println(age)
    println(gender)

    val survived = model.predict(Vectors.dense(Array(pclass, age, gender)))

    val dp = new DataPoint("survived", survived)
    dp
  }

  override def render(value: JValue)(implicit formats: Formats): JValue = value.camelizeKeys
}