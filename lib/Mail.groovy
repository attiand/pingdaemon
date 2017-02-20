@Grab(group='org.apache.ant', module='ant-javamail', version='1.9.4')
@Grab(group='javax.activation', module='activation', version='1.1.1')
@Grab(group='javax.mail', module='mail', version='1.4.7')
@GrabConfig(systemClassLoader=true)

class Mail {
  def config

  void send(def subject, def iptable){
    def ant = new AntBuilder()
    def body = "nodes:\n------\n"
    iptable.each { k, v ->
      def host = new Host(address: k)
      body <<= "$k (${host.name}): " + (v == 0 ? 'success' : 'failed') + "\n"
    }

    ant.mail (from: config.mail.from,
              tolist: config.mail.to instanceof List ? config.mail.to : [config.mail.to],
              message: body,
              subject : subject,
              mailhost: 'localhost',
              messagemimetype: 'text/plain')
  }
}
