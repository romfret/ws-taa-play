package controllers

import util.Commons._
import play.api.mvc._
import models.OPowerService
import play.api.data._
import play.api.data.Forms._
import play.api.libs.ws.WS

object Application extends Controller with Authentication {

  // Reference to the OPower service
  lazy val opower = OPowerService()

  /**
   * Show the dashboard of a user
   */
  val index = Authenticated { username => request =>
    var person = opower.getPersonById(opower.getPersonIdByMailAddress(username))

    Ok(views.html.index(username, person.toString()))
  }

  def loginForm: Form[(String, String)] = Form(tuple("mailAddress" -> text, "pwd" -> text))

  /**
   * Authenticate the user
   */
  val authenticate = Action { implicit request =>
  /*
   * Check if data from the form submission can authenticate the user
   * TODO Modify this function to call your service
   */
    def checkAuthentication(name: String, pwd: String): Boolean = return "florent@mail.com".equals(name) && "passa".equals(pwd) && (pwd.size > 4)

    var mailAddress = ""
    var password = ""

    loginForm.bindFromRequest.fold(
      failure => (),//do smthg with the failure info
      {
        case (p1, p2) =>
          mailAddress = p1 ;
          password = p2
      }
    )

    if (checkAuthentication(mailAddress, password)) {
      println(mailAddress + ", " + password)

      index // Set du nom dans la form index.html

      // Redirect("/") => redirection vers la racine du site web
      signingIn(mailAddress)(Redirect("/"))

    } else {
      BadRequest(views.html.login(loginForm.withGlobalError("Invalid user name or password")))
    }
  }

  /**
   * Show the login page
   */
  val login = Action { request =>
    Ok(views.html.login(loginForm))
  }

  /**
   * Sign out the user
   */
  val logout = Action { implicit request =>
    signingOut(Ok("You’ve been signed out"))
  }

  val onUnauthenticated = Redirect("/login")

  /**
   * Work with PVWatts (http://developer.nrel.gov/doc/pvwatts)
   */
  val askPvwatts = Authenticated { username => request =>
    val userAddress = "???" // TODO retrieve the user address using your OPower service
    val pvwattsP = WS.url("http://developer.nrel.gov/api/georeserv/app/sam/pvwatts.json")
      .withQueryString(
        "api_key" -> ???,
        "address" -> userAddress,
        "system_size" -> "42",
        "timeframe" -> "monthly"
      ).get()
    NotImplemented
  }
}