package problem
import zio._

import problem.log._

package object store {
  type Store = Has[StoreService]

  // Ugly part 3.2 (ok try shortcuts)
  // Ugly part 3.2.1 - ugh... still [Store,Store] ((
  val any: RLayer[Store, Store] = ZLayer.requires

  // Ugly part 3.2.2 - looks little better
  type SelfLayer[R] = RLayer[R, R]
  val any1: SelfLayer[Store] = ZLayer.requires

  /*
   * Ugly part 3.2.3 - looks like a way to ZLayer.self
   */
  type SelfLayer2[R] = RLayer[R, R]
  object SelfLayer2 {
    def apply[R <: Has[_]]: SelfLayer2[R] = ZLayer.requires[R]
  }
  def anyT[T]: SelfLayer2[Store] = SelfLayer2[Store]
  val any2: SelfLayer2[Store]    = anyT[Store]

  val live: RLayer[Log, Store] = ZLayer.fromService { l: LogService =>
    new StoreService {

      /**
        * Uses no log
        */
      override def storeEntityOne(data: String): Task[Boolean] = ZIO.succeed(true)

      // Ugly part 4 - without solving `Ugly part 1` i cant use all of service abilities
      /**
        * Uses "module alike" log interface (cant use `trace` this way)
        */
      override def storeEntityTwo(data: String): RIO[Log, Unit] = debug("DO WITH TRACE")

      // Ugly part 5 - this fills better, but this is ugliest part for npw (you see later)
      /**
        * Uses direct module access
        */
      override def storeEntityThree(data: String): Task[Unit] = l.trace(data)
    }
  }
}

trait StoreService {
  def storeEntityOne(data: String): Task[Boolean]
  def storeEntityTwo(data: String): RIO[Log, Unit]
  def storeEntityThree(data: String): Task[Unit]
}
