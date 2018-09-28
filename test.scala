import cats.data.{ReaderT, EitherT, OptionT}
import cats.implicits._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FutureOptionExample {
  case class KPI(name: String)
  case class KpiRequest(kpiName: String)
  case class Query()

  def getKPI(kpiName: String): OptionT[Future, KPI] = ??? // defined elsewhere

  def buildQuery(kr: KpiRequest): OptionT[Future, Query] = ??? // defined elsewhere

  def addKpiRequest(kr: KpiRequest): OptionT[Future, Unit] = ??? // defined elsewhere

  def mainLogic(kpiName: String): OptionT[Future, Unit] = for {
    kpi <- getKPI(kpiName)
    kpiRequest = KpiRequest(kpi.name)
    query <- buildQuery(kpiRequest)
  } yield(addKpiRequest(kpiRequest))
}

object FutureEitherExample {
  type UserID = String
  case class User(name: String)
  case class Error(errorMessage: String)

  def getFriends(userId: UserID): EitherT[Future, Error, Set[UserID]] = ???

  def getCommonFriends(userId1: UserID, userId2: UserID): EitherT[Future, Error, Set[UserID]] = 
    for {
      friends1 <- getFriends(userId1)
      friends2 <- getFriends(userId2)
    } yield (friends1 & friends2)
}

object ReaderFutureEitherExample {
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
