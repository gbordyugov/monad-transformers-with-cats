import cats.data.{EitherT, ReaderT}
import cats.implicits._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global



object Service {
  case class Config()
  type Status = String

  type MyEither[A] = EitherT[Future,   Error,  A]
  type MyReader[A] = ReaderT[MyEither, Config, A]

  def userName(id: Long): MyReader[String] = ???
  def sendEmail(userName: String): MyReader[Status] = ???

  def sendEmailToUser(id: Long): MyReader[Status] = for {
    name <- userName(id)
    status <- sendEmail(name)
  } yield(status)
}
