package io.ino.bench

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import com.typesafe.scalalogging.Logger
import io.ino.bench.LoggerBenchmark.ActorSystemState
import io.ino.bench.LoggerBenchmark.Data
import io.ino.bench.LoggerBenchmark.Log4sLogger
import io.ino.bench.LoggerBenchmark.ScalaLoggingLogger
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/* Default settings for benchmarks in this class */
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Array(Mode.Throughput))
class LoggerBenchmark {

  @Benchmark
  @Warmup(iterations = 1)
  @Measurement(iterations = 1)
  def baseline(): Unit = ()

  @Benchmark
  def akkaLogging(
      state: ActorSystemState,
      data: Data,
      blackhole: Blackhole
  ): Unit = blackhole.consume(state.system.log.debug("Sum: {}", data.items.sum))

  @Benchmark
  def scalaLogging(
      state: ScalaLoggingLogger,
      data: Data,
      blackhole: Blackhole
  ): Unit = blackhole.consume(state.logger.debug(s"Sum: ${data.items.sum}"))

  @Benchmark
  def log4sLogging(
      state: Log4sLogger,
      data: Data,
      blackhole: Blackhole
  ): Unit = blackhole.consume(state.logger.debug(s"Sum: ${data.items.sum}"))

}

//noinspection TypeAnnotation
object LoggerBenchmark {

  @State(Scope.Benchmark)
  class ActorSystemState {
    val system = ActorSystem()

    @TearDown(Level.Trial)
    def doTearDown(): Unit = Await.ready(system.terminate(), Duration.Inf)
    
  }

  @State(Scope.Benchmark)
  class ScalaLoggingLogger {
    val logger = Logger[LoggerBenchmark]
  }

  @State(Scope.Benchmark)
  class Log4sLogger {
    val logger = org.log4s.getLogger
  }

  @State(Scope.Benchmark)
  class Data {
    val items: List[Int] = (1 to 1000).toList
  }

}
