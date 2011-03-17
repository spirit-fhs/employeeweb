package de.codecarving.employeeweb
package model

import com.googlecode.charts4j._
import com.googlecode.charts4j.Color._

import scala.collection.JavaConversions._

trait GraphCreators extends SpiritHelpers {

  /**
   * Creating a BarChart.
   * @param title The Title which will be located above the BarChart.
   * @param currentAnswers A Tuple containing the Answers with Votecount.
   * @return String The URL to the Graph for Google Chart API.
   */
  def createBarChart(title: String, data: List[(String, Int)]): String = {

    lazy val colorList =
      List(BLUE, RED,
        BLACK, YELLOWGREEN,
        VIOLET, STEELBLUE,
        SLATEGRAY, SILVER,
        ORANGE, SPRINGGREEN,
        BROWN, SKYBLUE,
        CHOCOLATE, ROSYBROWN,
        ROYALBLUE, DARKBLUE,
        DARKCYAN, OLIVE)

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

  //TODO createPieChart
}
