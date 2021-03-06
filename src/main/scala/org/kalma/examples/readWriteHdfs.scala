
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, lit}

object readWriteHdfs {
  def main(args: Array[String]) {

    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("readWriteHdfs")
      .getOrCreate();

    import spark.implicits._

    println()
    println()
    println()
    val df = Seq((1, 2, "APP Name :" + spark.sparkContext.appName), (3, 4, "Deploy Mode :" + spark.sparkContext.deployMode), (5, 6, "Master :" + spark.sparkContext.master)).toDF("Col_0", "Col_1", "col3")
    df.show(false)

    df.write.mode("overwrite").format("csv").save("hdfs://hdfs-namenode-sts-0.hdfs-namenode-service.default.svc.cluster.local/datosSc.csv")

    val df2 = spark
      .read
      .format("csv")
      .option("inferSchema", true)
      .load("hdfs://hdfs-namenode-sts-0.hdfs-namenode-service.default.svc.cluster.local/incrementar_datos.parquet")

    df2.show(false)
    df2.withColumn("Col_0", col("Col_0") + lit(1))
      .withColumn("Col_1", col("Col_1") + lit(2))
      .write
      .mode("append")
      .format("parquet")
      .save("hdfs://hdfs-namenode-sts-0.hdfs-namenode-service.default.svc.cluster.local/incrementar_datos.parquet")
  }

}
