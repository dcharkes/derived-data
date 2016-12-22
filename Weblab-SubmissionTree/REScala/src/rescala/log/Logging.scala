package rescala.log

import rescala.Reactive
import rescala.Stamp

/**
 * Interface for logging.
 */
trait Logging {
  def nodeCreated(r: Reactive): Unit
  def nodeAttached(dependent: Reactive, r: Reactive): Unit
  def nodePulsed(r: Reactive): Unit
  def nodeScheduled(r: Reactive): Unit
  def nodeEvaluationStarted(r: Reactive): Unit
  def nodeEvaluationEnded(r: Reactive): Unit
  /** called when propagation inside a signal is stopped because the new value equals the old value */
  def nodeEvaluationEndedWithException(r: Reactive, e: Exception): Unit
  def nodeValueSet(r: Reactive): Unit
  def nodePropagationStopped(r: Reactive): Unit
  def logRound(ts: Stamp): Unit
  def logMessage(s: String): Unit
}

/** A Logging stub, that performs no logging */
class NoLogging extends Logging {
  def nodeCreated(r: Reactive): Unit = {}
  def nodeAttached(dependent: Reactive, r: Reactive): Unit = {}
  def nodePulsed(r: Reactive): Unit = {}
  def nodeScheduled(r: Reactive): Unit = {}
  def nodeEvaluationStarted(r: Reactive): Unit = {}
  def nodeEvaluationEnded(r: Reactive): Unit = {}
  def nodeEvaluationEndedWithException(r: Reactive, e: Exception): Unit = {}
  def nodeValueSet(r: Reactive): Unit = {}
  def nodePropagationStopped(r: Reactive): Unit = {}
  def logRound(ts: Stamp): Unit = {}
  def logMessage(s: String): Unit = {}
}
