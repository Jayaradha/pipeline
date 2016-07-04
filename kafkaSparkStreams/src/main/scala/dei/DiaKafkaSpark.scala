package dei
import kafka.serializer.{DefaultDecoder, StringDecoder}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{Logging, SparkConf, SparkContext}

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

    val path = System.getenv("HOME") + "/.ssh/aws-hadoop-conf.xml"
    sc.hadoopConfiguration.addResource((new java.io.File(path)).toURI().toURL())
    sc.hadoopConfiguration.get("fs.s3n.awsAccessKeyId")

    val hadoopConf = sc.hadoopConfiguration
    hadoopConf.set("fs.s3.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem")

    val awsAccessKey=   sc.hadoopConfiguration.get("fs.s3n.awsAccessKeyId")
    val awsSecretKey = sc.hadoopConfiguration.get("fs.s3n.awsSecretAccessKey")

    hadoopConf.set("fs.s3.awsAccessKeyId", awsAccessKey)
    hadoopConf.set("fs.s3.awsSecretAccessKey", awsSecretKey)

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
      .map{case Array(patientID,patientWeight,age,sex,mdate,bgbefore,medication,bgafter,ampm)
      =>(patientID,patientWeight,age,sex,mdate,bgbefore,medication,bgafter,ampm)})

    val ds1 = ds.map(x => x._1 + "," + x._2 + "," + x._3 + "," + x._4 + "," + x._5 + "," + x._6 + "," + x._7 + "," + x._8 + "," + x._9)
    ds1.print

    ds.print
    ds1.saveAsTextFiles("s3://diameddata/Diabetic-data-kafka")

    ssc.start()
    ssc.awaitTermination()

  }

}
