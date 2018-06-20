Sample project showing [sbt-jmh](https://github.com/ktoso/sbt-jmh), for the [Scala Hamburg Meetup 06/18](https://www.meetup.com/de-DE/Scala-Hamburg/events/251207492/).

## Usage

Start sbt and navigate through commits (e.g. with `groll next/prev`).

To run the manual/custom microbenchmark, in sbt execute
```
core/run
```

To run the jmh benchmark, execute
```
bench/jmh:run -wi 10 -i 5 .*Benchmark
```
