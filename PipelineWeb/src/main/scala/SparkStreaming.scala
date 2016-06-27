import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by arivolit on 6/20/16.
  */

object SparkStreaming {

  def main(args : Array[String]) {
    println( "Hello World!" )

    val conf = new SparkConf().
      setAppName("USAGov").
      setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(1))
    ssc.checkpoint("checkpoint")
    val sc = ssc.sparkContext


    // Create DStream to listen to hostname:port
    val lines = ssc.socketTextStream("127.0.0.1", 9999)

    lines.print()

    // Start computation
    ssc.start()

    // Wait streaming to terminate
    ssc.awaitTermination()
  }

}
