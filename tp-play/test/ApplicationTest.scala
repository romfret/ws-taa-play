import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class ApplicationTest extends Specification {

  "OPower" should {
    "Sign a user in" in {
      running(TestServer(3333), HTMLUNIT) { browser =>

        // Try to go to the index page
        browser goTo "http://localhost:3333"
        // User is redirected to the login page
        browser.url must equalTo ("http://localhost:3333/login")

        // Fill the signing in form
        browser fill "[name=name]" `with` "user@mail.com"
        browser fill "[name=pwd]" `with` "passa"
        browser submit "form"
        // User is logged in and redirected to the index page
        browser.url must equalTo ("http://localhost:3333/")     //  <== Bug : caused by org.specs2.execute.FailureException: 'http://localhost:3333/login' is not equal to 'http://localhost:3333/'

        // Logout
        // TODO

        // Logout
        browser.$("Sign off").click()
        browser.url() must equalTo ("http://localhost:3333/logout")
        browser.pageSource() must contain ("You’ve been signed out")
      }
    }
    "Reject an invalid login submission" in {
      running(TestServer(3333), HTMLUNIT) { browser =>
        browser goTo "http://localhost:3333"
        // Submit an empty form
        browser submit "form"
        // Validation error
        browser.pageSource must contain ("Invalid user name or password")

        // Submit a invalid form
        browser fill "[name=name]" `with` "userInvalid@mail.com"
        browser fill "[name=pwd]" `with` "passa"
        browser submit "form"
        browser.pageSource must contain ("Invalid user name or password")
      }
    }
  }

}
