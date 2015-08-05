package models

import java.sql.Blob

/**
 * Created by dorin on 04.08.2015.
 */
case class EmailTemplate (
   idTemplate: Option[Int],
   idUserType: Option[Int],
   emailTemplate: Option[Blob]
     )
