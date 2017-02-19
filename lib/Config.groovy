import groovy.util.ConfigSlurper

class Config {
  private ConfigSlurper slurper = new ConfigSlurper()
  private File file

  def defaults = '''
  startupDelay = 1
  pollingPeriod = 60
  '''

  Config(File file) {
    this.file = file
    load()
  }

  def boolean exists() {
    file.exists() && file.canRead()
  }

  private load() {
    def d = slurper.parse(defaults)
    d.merge(slurper.parse(file.toURI().toURL()))
  }
}
