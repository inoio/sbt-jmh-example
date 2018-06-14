package io.ino.sample

import java.lang.System.nanoTime
import java.util.concurrent.TimeUnit.MILLISECONDS

import scala.annotation.tailrec
import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationLong
import scala.concurrent.duration.FiniteDuration
import scala.language.postfixOps

object CustomBench extends App {

  def bench(
      name: String,
      runMillis: Long,
      warmupIterations: Int,
      iterations: Int
  )(fun: => Unit): Unit = {
    println(s"Running: $name")
    val max = iterations + warmupIterations
    val runNanos = MILLISECONDS.toNanos(runMillis)
    val totalThroughput = (0 until max).foldLeft(0L) { (res, i) =>
      val (nops, duration) = run(runNanos, fun)
      val throughput = nops / duration.toMillis
      val benchRun = i >= warmupIterations
      print(f"$throughput%,8d ops/ms ${if (benchRun) "| " else s"(warmup) | "}")
      if (benchRun) res + throughput else res
    }
    val averageThroughput = totalThroughput / iterations
    println(f"\n[ ~$averageThroughput%,8d ops/ms ]\n")
  }

  @tailrec
  private def run(
      nanos: Long,
      fun: => Unit,
      totalOps: Long = 0L,
      totalDuration: FiniteDuration = Duration.Zero
  ): (Long, FiniteDuration) = {
    if (totalDuration.toNanos >= nanos) totalOps -> totalDuration
    else {
      // use internal loop to not suffer from timing overhead (nanoTime) per function invocation

      // you think we can just use one of ops or j?
      // see what happens if you do this... (long comparison too expensive, with int loop optimization kicks in)
      var ops = 0L
      var j = 0
      val start = nanoTime()
      // use while loop because it's much faster than using a range (i.e. a range would negatively affect results)
      // and e.g. an internal tail recursive loop was optimized away completely
      while (j < 10000) {
        fun
        ops += 1
        j += 1
      }
      val duration = nanoTime() - start
      run(nanos, fun, totalOps + ops, totalDuration + duration.nanos)
    }
  }

  val RunMillis = 4000
  val WarmupIterations = 3 // 10
  val Iterations = 2 // 5

  bench("baseline", RunMillis, WarmupIterations, Iterations) {
    ()
  }

  bench("measureWrong", RunMillis, WarmupIterations, Iterations) {
    // This is wrong: the result is provably the same, optimized out (constant folding).
    math.log(Math.PI)
  }

  private val x = Math.PI

  bench("missingReturnValue", RunMillis, WarmupIterations, Iterations) {
    // This is better: the result is computed.
    math.log(x)
  }

}
