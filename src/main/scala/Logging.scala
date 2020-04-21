import org.apache.log4j.{LogManager, Logger}

trait Logging {
  @transient protected lazy val log: Logger = LogManager.getLogger(getClass)
}
