@Grab(group='org.apache.ant', module='ant-javamail', version='1.9.4')
@Grab(group='javax.activation', module='activation', version='1.1.1')
@Grab(group='javax.mail', module='mail', version='1.4.7')
@GrabConfig(systemClassLoader=true)

class Mail {
  def config

  void send(def subject, def iptable){
    def ant = new AntBuilder()
    def body = "ip table\n"
    iptable.each {
      k, v -> body <<= "$k: " + (v == 0 ? 'success' : 'failed') + "\n"
    }
    ant.mail (from: config.mail.from,
              tolist: config.mail.to,
              message: body,
              subject : subject,
              mailhost: 'localhost',
              messagemimetype: 'text')
  }
}
