package io.ino.bench
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import java.util.concurrent.TimeUnit

import io.ino.bench.MathBenchmark.Data
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

/* Default settings for benchmarks in this class */
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Array(Mode.Throughput))
class MathBenchmark {

  @Benchmark
  def baseline(): Unit = ()

  @Benchmark
  def measureWrong(): Unit = math.log(Math.PI)

  @Benchmark
  def missingReturnValue(data: Data): Unit = math.log(data.x)

  @Benchmark
  def right(data: Data): Double = math.log(data.x)

}

object MathBenchmark {

  @State(Scope.Thread)
  class Data {
    val x: Double = Math.PI
  }

}
