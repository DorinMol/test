package controllers

//import java.io.File

import java.sql.{DriverManager, Connection, Blob}

import dao.{EmailTemplates, Inventories, Players}
import models.{PlayerBag, Item, Inventory, Player}
import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick.DBAction
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.Json
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc._


object Application extends Controller {

  implicit val playerJson = Json.format[Player]
  implicit val itemJson = Json.format[Item]
  implicit val inventoryJson = Json.format[Inventory]
  implicit val playerBagJson = Json.format[PlayerBag]

  val playerForm = Form(
    mapping (
      "idPlayer" -> optional(number),
      "name" -> text,
      "level" -> number
    )(Player.apply)(Player.unapply)
  )

  val itemForm = Form(
    mapping(
      "idItem" -> optional(number),
      "name" -> text,
      "damage" -> optional(number),
      "armor" -> optional(number)
    )(Item.apply)(Item.unapply)
  )

  val inventoryForm = Form(
    mapping(
      "idPlayer" -> number,
      "idItem" -> number
    )(Inventory.apply)(Inventory.unapply)
  )

  val playerBagForm = Form(
    mapping(
      "idPlayer" -> number,
      "name" -> text,
      "level" -> number,
      "items" -> list(mapping(
        "idItem" -> optional(number),
        "name" -> text,
        "damage" -> optional(number),
        "armor" -> optional(number)
      )(Item.apply)(Item.unapply)
      )
    )(PlayerBag.apply)(PlayerBag.unapply)
  )

  def index = Action {
    Ok(views.html.index(Players.findAll))
  }

//  def getMyConnection:Connection =
//  {
//    val connectionURL:String = "jdbc:mysql://localhost:5432/postgres";
//    Class.forName("com.mysql.jdbc.Driver");
//    val con:Connection = DriverManager.getConnection(connectionURL, "postgres", "postgres");
//    return con;
//  }

  def upload(id: Int) = Action(parse.temporaryFile) { implicit rs =>
    Logger.debug("Uploading template ")
        //filePart: FilePart[TemporaryFile] =>


        try {
          Logger.debug("Fisier gasit")
          import java.io.File
          //MultipartFormData mfd = rs.body.file
          Logger.debug("Filename: " + rs.body.file.getName + " ---- ContentType: FARA")
          val source = scala.io.Source.fromFile(rs.body.file)
          Logger.debug("Source: " + source.getLines.foreach(x => println(x)))
          //var myBlob: Blob = getMyConnection.createBlob()
          //myBlob.setBytes(1,)

          val fileToByteArray = source.map(_.toByte).toArray
          source.close()
         // myBlob.setBytes(1,fileToByteArray)
          EmailTemplates.update(fileToByteArray, id)
          //file.ref.moveTo(new File(Play.application.path().getAbsolutePath + file.filename))
          Ok("Upload reusit")
        } catch {
          case e: Exception => Logger.error("Eroare - inserare nereusita: " + e.getMessage)
            BadRequest("Error - File couldn't be uploaded")
        }

  }


  def addSinglePlayer = DBAction { implicit rs =>
    val player = playerForm.bindFromRequest.get
    Players.savePlayer(player)

    Redirect(routes.Application.index)
  }

  def findAll = DBAction{ implicit rs =>
    Ok(Json.toJson(Players.findAll))
  }

  def addMultiPlayer = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[Player].map { player =>
      Players.savePlayer(player)

      Ok(Json.toJson(player))
    }.recoverTotal{
      e => BadRequest("Invalid Json")
    }
  }

  def buySingleItem = DBAction(parse.json){ implicit rs =>
    rs.request.body.validate[List[Inventory]].map { inventory =>
      Inventories.save(inventory)

      Ok("Added items")
    }.recoverTotal{
      e => BadRequest("Invalid Json")
    }
  }

  def getSinglePlayerBag = DBAction { implicit rs =>
    Ok(views.html.player(Inventories.getPlayersWithItems))
  }

  def getMultiPlayerBag = DBAction{ implicit rs =>
    Ok(Json.toJson(Inventories.getPlayersWithItems))
  }
}