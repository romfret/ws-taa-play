package models

import javax.naming.InitialContext
import javax.naming.Context._
import util.Commons._

/**
 * Provide access to the service layer implemented as a bean deployed in EasyBeans
 */
object OPowerService {

  // TODO replace these values with your settings
  // Name and type of the bean
  val SERVICE_NAME = "businessImpl"     // @Stateless(mappedName = "businessImpl") de l'implementtation
  type SERVICE_TYPE = business.BusinessInterface  // package et nom de l'interface

  // Return a reference to the OPower service
  def apply() = {
    import collection.JavaConverters._
    import javax.naming
    val env = Map(
      Context.INITIAL_CONTEXT_FACTORY -> "org.ow2.easybeans.component.smartclient.spi.SmartContextFactory",
      Context.PROVIDER_URL -> "smart://localhost:2503"
    )
    val ctx = new InitialContext(new java.util.Hashtable[String, String](env.asJava))
    ctx.lookup(SERVICE_NAME).asInstanceOf[SERVICE_TYPE]
  }

}
