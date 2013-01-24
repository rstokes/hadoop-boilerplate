import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable,Text}
import org.apache.hadoop.mapreduce.{Job,Mapper,Reducer}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import scala.collection.JavaConversions._

class TokenizerMapper extends Mapper[Object, Text, Text, IntWritable] {

  val one = new IntWritable(1)
  var word = new Text

  override
  def map (key: Object, value: Text, context: Mapper[Object,Text,Text,IntWritable]#Context) {
    value.toString.split("\\s").foreach { token => word.set(token); context.write(word, one) }
  }
}

class IntSumReducer extends Reducer[Text,IntWritable,Text,IntWritable] {

  val result = new IntWritable()

  override
  def reduce (key: Text, values: java.lang.Iterable[IntWritable], context: Reducer[Text,IntWritable,Text,IntWritable]#Context) {
    result set(values.foldLeft(0) { _ + _.get })
    context write(key, result)
  }
}

object WordCount {
  def main (args: Array[String]) {
    val conf = new Configuration()
    val job = new Job(conf, "word count")
    job.setJarByClass(classOf[TokenizerMapper])
    job.setMapperClass(classOf[TokenizerMapper])
    job.setCombinerClass(classOf[IntSumReducer])
    job.setReducerClass(classOf[IntSumReducer])
    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[IntWritable])
    FileInputFormat.addInputPath(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))
    System.exit(if(job.waitForCompletion(true)) 0 else 1)
  }

}