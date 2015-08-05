package dao

import java.sql.Blob
import javax.sql.rowset.serial.SerialBlob

import helpers.PostgresSupport
import models.Inventory
import net.sf.ehcache.hibernate.HibernateUtil
import play.api.Logger

import scala.slick.direct.AnnotationMapper.column
import scala.slick.lifted.{Tag, TableQuery}
import play.api.db.slick.Config.driver.simple._
import models._

//import scalaz.std.effect.sql.connection

/**
 * Created by dorin on 04.08.2015.
 */
object EmailTemplates extends PostgresSupport {
  val emailTemplates = TableQuery[EmailTemplateTable]

  class EmailTemplateTable(tag: Tag) extends Table[EmailTemplate](tag, "user_type_templates") {
    def idTemplate = column[Int]("id_template", O.AutoInc,O.NotNull)
    def idUserType = column[Int]("id_user_type", O.Nullable)
    def emailTemplate = column[Blob]("email_template", O.Nullable)

    def * = (idTemplate.?, idUserType.?, emailTemplate.?) <> (EmailTemplate.tupled, EmailTemplate.unapply _)

//    def ? = (idTemplate.?, idUserType.?, emailTemplate.?)

    //def idUserFK = foreignKey("id_user_type", idUserType, Players.players)(_.idPlayer)
    //def itemFK = foreignKey("inventory_idItem", idItem, Items.items)(_.idItem)
  }

  def update(myArray: Array[Byte], id: Int) = {
    Logger.debug("Am intrat in metoda update din dao")
//    emailTemplates.filter(_.idUserType === id)
//      .map(x => x.emailTemplate)
//      .update(new SerialBlob(byteArr))
    try {
      emailTemplates.update(new EmailTemplate(Some(1), Some(id), Some(new SerialBlob(myArray).asInstanceOf[Blob])))
    }catch {
      case e: Exception => Logger.debug("Exceptie " + e.printStackTrace)
    }

  }

}
