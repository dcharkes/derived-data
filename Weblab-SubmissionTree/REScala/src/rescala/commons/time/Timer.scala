package rescala.commons.time

import rescala.events._
import rescala.SignalSynt
import rescala.Var
import rescala.Signal
import scala.collection.SortedSet
import TimePostfixedValue._

/**
 * A Timer class for FRP purposes.
 * After creation, all timers need to be run with Timer.runAll
 */
class Timer(val interval: Time) extends Ordered[Timer] {

  // Timers are ordered by interval
  def compare(that: Timer) = (this.interval - that.interval).s.toInt

  /** Tick event gets triggered every at 'interval'
   *  Passes the real delta as parameter due to thread sleeping inaccuracy */
  val tick = new ForkedEvent[Time]

  /** Tick event that drops the delta for convenience */
  val tock: Event[Unit] = tick.dropParam

  /** Signal for the total time */
  val time = tick.fold(0.s) {(total: Time, delta: Time) => total + delta}

  /** Returns the integral of the Signal s over time */
  def integral(s: Signal[Time]): Signal[Time] = {
    // simple Riemann integral, could do more
    tick.fold(0.ns) {(total: Time, delta: Time) =>
      total + delta * s()
    }
  }

  /** Integrates a real Signal expression over time */
  def integrate(expr: => Double): Signal[Double] = {
    tick.fold(0d) {(total: Double, delta: Time) =>
      total + delta.s * expr
    }
  }

  /** Integrates a Signal expression over time but uses this.interval as
   *  difference between the ticks */
  def integrateWithInterval(expr: => Double): Signal[Double] = {
    tick.fold(0d) { (total, _ ) =>
      total + this.interval * expr
    }
  }

  /** Returns a new Signal that counts the local time from now */
  def localTime: Signal[Time] = {
    val now = time()
    SignalSynt(time) {s: SignalSynt[Time] => time(s) - now}
  }

  /** Returns a Signal which is true if the specified delay has passed */
  def passed(delay: Time) : Signal[Boolean] = {
    val now = time()
    SignalSynt {s: SignalSynt[Boolean] => time(s) > now + delay }
  }

  /** Returns a new event which fires exactly once after the specified delay */
  def after(delay: Time) : Event[Unit] = passed(delay).changedTo(true)

  /** Snapshots a signal for a given time window */
  def timeWindow[A](window: Time)(s : Signal[A]) : Signal[Seq[A]] = {
    if(interval.ns <= 0) throw new RuntimeException("You must use an interval > 0")
    val delta = interval.s
    val n = (window / delta).asInstanceOf[Int]
    (tick snapshot s).changed.last(n)
  }

  /** Samples this expression on each occurence of the timer */
  def sample[T](expr: => T): Signal[T] = {
    this.tock.set(None)(_ => expr)
  }

  /** The current system time, sampled by this timer */
  def currentTime: Signal[Time] = sample(Time.current)

  // globally register this timer
  Timer.register(this)
}


object Timer {

  /** Factory method to create timers */
  def apply(interval: Time): Timer = new Timer(interval)

  /** Convenience method for creating a signal holding the current time */
  def currentTime(interval: Time): Signal[Time] = new Timer(interval).currentTime

  // a global ticker which triggers as often as needed
  val globaltick = new ImperativeEvent[Time]

  case class Scheduled(remaining: Long, timer: Timer)
  extends Ordered[Scheduled] {
	def compare(that: Scheduled) = (this.remaining - that.remaining).toInt
	def postpone(offset : Int) = Scheduled(remaining - offset, timer)
	def reschedule = Scheduled(timer.interval.ms, timer)
  }

  protected var schedule: List[Scheduled] = List()
  protected def register(t : Timer) = schedule :+= Scheduled(t.interval.ms, t)
  protected def unregister(t : Timer) = schedule = schedule.filterNot(_.timer == t)


  protected var lastTick: Long = 0

  /**
   * Runs all created Timer objects in this thread (blocking).
   */
  def runAll() {
    while(schedule.nonEmpty) {
      tickNext()
    }
  }

  def tickNext() {
    val delta = schedule.min.remaining
    val (tickNow, tickLater) = schedule.partition {_.remaining == delta}

    // sleep to approximate current delta, assume a min. passed time of 1 ms
    val now = System.nanoTime()
    val tNeeded = (now - (if(lastTick == 0) now else lastTick)) / 1000 + 1
    val tSleep = math.max(1, delta - tNeeded)

    Thread.sleep(tSleep)

    // measure real delta
    val realDelta = math.max(tNeeded, delta).asInstanceOf[Int]
    val realSecs = realDelta / 1000.0

    // tick timer subset
    val tickset = tickNow.map(_.timer.tick)
    val ticker = new ImperativeForkEvent[Time](tickset: _*)
    ticker(realSecs)

    // update the schedule
    val rescheduled = tickNow.map (_.reschedule)
    val postponed = tickLater.map (_ postpone realDelta)
    schedule = rescheduled ++ postponed

    // record the time
    lastTick = System.nanoTime()
  }
}
