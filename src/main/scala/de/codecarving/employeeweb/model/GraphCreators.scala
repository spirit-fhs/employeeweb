package de.codecarving.employeeweb
package model

import com.googlecode.charts4j._
import com.googlecode.charts4j.Color._

import scala.collection.JavaConversions._

trait GraphCreators extends SpiritHelpers {

  private[GraphCreators] val colorList =
      List(BLUE, RED,
        BLACK, YELLOWGREEN,
        VIOLET, STEELBLUE,
        SLATEGRAY, SILVER,
        ORANGE, SPRINGGREEN,
        BROWN, SKYBLUE,
        CHOCOLATE, ROSYBROWN,
        ROYALBLUE, DARKBLUE,
        DARKCYAN, OLIVE)

  /**
   * Creating a BarChart.
   * @param title The Title which will be located above the BarChart.
   * @param currentAnswers A Tuple containing the Answers with Votecount.
   * @return String The URL to the Graph for Google Chart API.
   */
  def createBarChart(title: String, data: List[(String, Int)]): String = {

    // Zipping the (String, Int) into ((String, Int), Color).
    lazy val triple4graph = data zip randomFromList(data.size, colorList)

    //TODO Votes have to be between 0 and 100, what if we have more than 100 votes?!
    //TODO Clean up the Tuple mess!
    val plotList = triple4graph map { cur =>
      Plots.newBarChartPlot(Data.newData(if(cur._1._2 < 101) cur._1._2 else 100), cur._2 , cur._1._1)
    }

    val pollChart = GCharts.newBarChart(plotList)

    val fillArea = Fills.newLinearGradientFill(0, ALICEBLUE, 100)
    fillArea.addColorAndOffset(WHITE, 0)

    pollChart.addXAxisLabels(AxisLabelsFactory.newNumericRangeAxisLabels(0, 100))
    pollChart.setSize(600, 500)
    pollChart.setBarWidth(30)
    pollChart.setSpaceWithinGroupsOfBars(20)
    pollChart.setDataStacked(false)
    pollChart.setTitle(title, BLACK, 14)
    pollChart.setGrid(100, 10, 3, 2)
    pollChart.setBackgroundFill(Fills.newSolidFill(WHITE))
    pollChart.setHorizontal(true)
    pollChart.setAreaFill(fillArea)
    pollChart.toURLForHTML
  }

  /**
   * Creating a PieChart..
   * @param title The Title which will be located above the PieChart.
   * @param currentAnswers A Tuple containing the Answers with Votecount.
   * @return String The URL to the Graph for Google Chart API.
   */
  def createPieChart(title: String, data: List[(String, Int)]): String = {

    // Folding the data._2 into totalVotes.
    val totalVotes = data.foldLeft(0)((result,tuple) => tuple._2 + result)

    // Calculating the percentage of a number for the PieChart.
    def getPercentage(in: Int, max: Int): Int = {
      val result = (in.toDouble / max.toDouble) * (100.toDouble)
      if(result <= 0) 1
      else result.toInt
    }

    // Zipping the (String, Int) into ((String, Int), Color).
    lazy val triple4graph = data zip randomFromList(data.size, colorList)

    // Creating new Slices
    val sliceList = triple4graph map { cur =>
      Slice.newSlice(getPercentage(cur._1._2, totalVotes), cur._2, cur._1._2.toString, cur._1._1)
    }

    // Creating the PieChart with a List[Slice]
    val pieChart = GCharts.newPieChart(sliceList)
    pieChart.setTitle(title, BLACK, 14)
    pieChart.setSize(600, 300)
    pieChart.setThreeD(true)
    pieChart.toURLForHTML

  }
}
