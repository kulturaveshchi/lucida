package training

import java.io.FileOutputStream
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}

import com.intel.imllib.crf.nlp.{CRF, CRFModel}
import model._
import org.apache.spark.{SparkConf, SparkContext}

/**
  * A driver application to train a model and save it to a file
  */
object Train extends TrainBase {
  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      println("Supply path to training data")
      System.exit(1)
    }

    val trainingPath = args(0)

    if (args.length < 2) {
      println("Supply a path to write to model to")
      System.exit(1)
    }

    val outputPath = args(1)

    val conf = (new SparkConf).setAppName("Train")
    val sc = new SparkContext(conf)

    val trainingData = loadTrainingData(sc, trainingPath)

    val templates = FeatureTemplate.templatesAsStrings(Features.templates, unqualifiedBigram=true)

    val start = System.currentTimeMillis()
    val model = CRF.train(templates, trainingData)
    val end = System.currentTimeMillis()

    val os = new FileOutputStream(outputPath)
    CRFModel.saveStream(model, os)
    os.close()

    new PrintWriter(new FileOutputStream("train.log", true)) {
      println(s"Time for training: ${end - start} ms")
      close()
    }
  }
}
