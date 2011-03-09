package de.codecarving.employeeweb
package model

import net.liftweb.http.RequestVar
import net.liftweb.common.{Box, Empty}

trait GlobalRequests {

  object CurrentEntry extends RequestVar[Box[SpiritEntry]](Empty)
}
