package de.codecarving.employeeweb
package model

import bootstrap.liftweb.Boot
import net.liftweb.sitemap._
import Loc._
import net.liftweb.http.RedirectResponse
import de.codecarving.fhsldap.model.User

trait MenuBuilder[T <: Boot] {

  lazy val loggedInEmployeeorAdmin = If(() => User.isEmployee || User.isAdmin,
                                   () => RedirectResponse("/index"))

  lazy val loggedInEmployee = If(() => User.isEmployee,
                            () => RedirectResponse("/index"))

  lazy val loggedInStudent = If(() => User.isStudent,
                           () => RedirectResponse("/index"))

  lazy val loggedInAdmin = If(() => User.isAdmin,
                         () => RedirectResponse("/index"))

  // Build SiteMap
  val sitemap = List(
    Menu.i("Home") / "index",
    Menu.i("News") / "news" / "news" >> loggedInEmployeeorAdmin submenus (
      Menu.i("News Anlegen") / "news" / "write" >> loggedInEmployeeorAdmin,
      Menu.i("Editieren") / "news" / "edit" >> loggedInEmployeeorAdmin >> Hidden
      ),
    Menu.i("Talk Allocator") / "talkallocator" / "index" >> loggedInEmployeeorAdmin submenus (
      Menu.i("Talk Allocator Anlegen") / "talkallocator" / "newTalkAllocator" >> loggedInEmployeeorAdmin,
      Menu.i("Add TalkAllocator Talks") / "talkallocator" / "addTalkAllocatorTalks" >> loggedInEmployeeorAdmin >> Hidden,
      Menu.i("Edit TalkAllocator Talks") / "talkallocator" / "edit" >> loggedInEmployeeorAdmin >> Hidden,
      Menu.i("Evaluate TalkAllocator Talks") / "talkallocator" / "evaluate" >> loggedInEmployeeorAdmin >> Hidden,
      Menu.i("Edit TalkAllocator Talk") / "talkallocator" / "editTalk" >> loggedInEmployeeorAdmin >> Hidden
      ),
    Menu.i("Poll Pal") / "pollpal" / "index" >> loggedInEmployeeorAdmin submenus (
      Menu.i("Umfrage Anlegen") / "pollpal" / "newpoll" >> loggedInEmployeeorAdmin,
      Menu.i("PollGraph") / "pollpal" / "pollgraph" >> loggedInEmployeeorAdmin >> Hidden
      ),
    Menu.i("Administration") / "administration" >> loggedInAdmin

    // Examples -----------------------------------------
    //Menu.i("Student") / "test1" >> loggedInStudent,
    //Menu.i("Admin") / "test2" >> loggedInAdmin,
    //Menu.i("Employee and Admin") / "test3" >> loggedInEmployeeorAdmin
    // more complex because this menu allows anything in the
    // /static path to be visible
    //Menu(Loc("Static", Link(List("static"), true, "/static/index"),
	  //   "Static Content"))
	)
}
