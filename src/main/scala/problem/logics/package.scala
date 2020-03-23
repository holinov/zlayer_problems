package problem

import problem.log._
import problem.store._
import zio._

package object logics {
  type Logics = Has[LogicsService]

  /*
   *  Ugly part 7 - this does not compiles with:
   *  [error] /Users/alberthofmann/src/scala/marvin_new/zlayer_problem/src/main/scala/problem/logics/package.scala:11:44: type mismatch;
   *  [error]  found   : (problem.LogService, problem.StoreService) => problem.LogicsService
   *  [error]  required: (problem.LogService, problem.LogService) => problem.LogicsService
   *  [error]     (log: LogService, store: StoreService) =>
   *
   * WTF????
   */
  val liveFromServices: ZLayer[Log with Store, Nothing, Logics] = ZLayer.fromServices {
    (log: LogService, store: StoreService) =>
      new LogicsService {

        override def useLogDirect: RIO[Log, Unit]     = debug("Direct")
        override def useLogFromService: Task[Unit]    = log.debug("Direct")
        override def useStoreEntityOne: Task[Boolean] = store.storeEntityOne("DATA 1")

        // Ugly part 6 - ok for `useLogDirect` is quite clear
        // but for useStoreEntityTwo `Log` in env? why????
        override def useStoreEntityTwo: RIO[Log, Unit] = store.storeEntityTwo("DATA 2")

        // Ugly part 7 - `useStoreEntityThree` has no signature of using `Log` and `Store`
        override def useStoreEntityThree: Task[Unit] = store.storeEntityThree("DATA 3")
      }
  }
}

trait LogicsService {

  def useLogDirect: RIO[Log, Unit]
  def useStoreEntityTwo: RIO[Log, Unit]

  def useLogFromService: Task[Unit]
  def useStoreEntityThree: Task[Unit]
  def useStoreEntityOne: Task[Boolean]
}
