package DisStream
import kafka.serializer.{DefaultDecoder, StringDecoder}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{Logging, SparkConf}

/**
  * Created by Jayaradha on 6/23/16.
  */
object DiaKafkaSpark {

  val windowDuration = Seconds(500)
  val slideDuration = Seconds(250)

  def updateFunction(newValues: Seq[Int], runningCount: Option[Int]): Option[Int] = {
    Some(newValues.sum + runningCount.getOrElse(0))
  }

  object Util extends Logging {
    def setStreamingLogLevels() {
      // Set reasonable logging levels for streaming if the user has not configured log4j.
      val log4jInitialized = Logger.getRootLogger.getAllAppenders.hasMoreElements
      if (!log4jInitialized) {
        // Force initialization.
        logInfo("Setting log level to [WARN] for streaming example." +
          " To override add a custom log4j.properties to the classpath.")
        Logger.getRootLogger.setLevel(Level.ERROR)
      }
    }
  }

  def main(args:Array[String]) = {

    // Disable chatty logging.
    Util.setStreamingLogLevels()

    // Create Spark context.
    val conf = new SparkConf().
      setAppName("Test").
      setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(1))
    ssc.checkpoint("checkpoint")
    val sc = ssc.sparkContext

    // Brokers.
    val brokers = Set("localhost:9092")
    val kafkaParams = Map("metadata.broker.list" -> brokers.mkString(","))

    // Topics.
    val topics = Set("sensorData")

    // Create DStream.
    val kafkaStream = KafkaUtils.createDirectStream[Array[Byte], String, DefaultDecoder, StringDecoder](
      ssc, kafkaParams, topics)


    val ds = kafkaStream.transform(rdd =>rdd
      .map{case(key,value) => value}
      .map(line => line.split(","))
      .map{case Array(patientID,patientWeight,age,bg,medication,bgbefore,ampm)=>(patientID,patientWeight,age,bg,medication,bgbefore,ampm)})
     //ds.foreachRDD({rdd => rdd.toString()})
    ds.print
    ds.saveAsTextFiles("data")
      //.map{case Array(time,x1,x2,user,op,status)=>(status,1)})
    //ds.print


    /*val level2 = ds.reduceByKeyAndWindow((c1: Int, c2: Int) => (c1 + c2), windowDuration, slideDuration)

    val level3 = level2.updateStateByKey(updateFunction)

    level3.foreachRDD(rdd => {
      val total = (rdd.map({ case (status, count) => ("key", count.toInt) }).reduceByKey(_+_).map { case (c1, c2) => c2.toInt }.take(1))(0)
      rdd.foreach{ case(status, count) => println(status + " " + (count.toInt * 1.0 / total) ) }
    })

    val l1 = kafkaStream.
      map{ case(key, value) => value.split(":") }.map{ case(arr) => (arr(3), 1)}.reduceByKeyAndWindow((c1:Int,c2:Int) => (c1 + c2), windowDuration,slideDuration)

    val l2 = l1.updateStateByKey(updateFunction)
    l2.foreachRDD(rdd => {val usr = rdd.reduceByKey(_+_).sortBy({case (user,cnt) => cnt},false).take(1)(0)
      println(usr)})*/
//s3n://s3-us-west-2.amazonaws.com/diameddata/PatientDataSample3.csv
    ssc.start()
    ssc.awaitTermination()

  }

}
