package de.codecarving.employeeweb
package model

import com.googlecode.charts4j._
import com.googlecode.charts4j.Color._

import scala.collection.JavaConversions._

trait GraphCreators {

  /**
   * Creating a BarChart from a Poll.
   */
  def createBarChart(title: String, currentAnswers: List[SpiritPollAnswers]): String = {

    //TODO random colors
    //TODO Votes have to be between 0 and 100, what if we have more than 100 votes?!
    val plotList = currentAnswers map { cur =>
      Plots.newBarChartPlot(Data.newData(if(cur.votes.value < 101) cur.votes.value else 100), BLUE, cur.answer.value)
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
