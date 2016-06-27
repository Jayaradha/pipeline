package DiaModel

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types._
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by arivolit on 6/25/16.
  */
object s3test {
  def main(args: Array[String]) = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("DiabeticRandomForest")
    val sc = new SparkContext(conf)
    val path = System.getenv("HOME") + "/.ssh/aws-hadoop-conf.xml"
    sc.hadoopConfiguration.addResource((new java.io.File(path)).toURI().toURL())
    sc.hadoopConfiguration.get("fs.s3n.awsAccessKeyId")

    val hadoopConf = sc.hadoopConfiguration
    hadoopConf.set("fs.s3.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem")
    val awsAccessKey = "xxx"
    val awsSecretKey = "yyy"

    hadoopConf.set("fs.s3.awsAccessKeyId", awsAccessKey)
    hadoopConf.set("fs.s3.awsSecretAccessKey", awsSecretKey)
   // val textFile = sc.textFile("s3://diameddata/PatientDataSample3.csv")

    //print("Hello World");
    sc.parallelize(Seq(1,2,3)).saveAsTextFile("s3://diameddata/one.txt")
  }


}
