import groovy.json.JsonSlurper

class Config {
  private File file

  Config(File file) {
    this.file = file
    load()
  }

  def boolean exists() {
    file.exists() && file.canRead()
  }

  private load() {
    file.withReader { r ->
      new JsonSlurper().parse( r )
    }
  }
}
