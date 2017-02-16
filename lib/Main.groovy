def cli = new CliBuilder(usage: 'ping.groovy [OPTIONS]')

cli.with {
  h longOpt:'help', 'usage information'
  c longOpt:'config', 'config file', args:1
  p longOpt:'period', 'period to sleep between checks in sec', args:1
  d longOpt:'delay', 'startup sleep time in sec', args:1
}

def opt = cli.parse(args)

if(opt.arguments().size() != 0 || opt.h) {
  cli.usage()
  return
}

def config = new Config(new File(opt.c ?: 'config.json'))
def mail = new Mail(config: config.load())

def state = [:]

use (TimerMethods) {
    def timer = new Timer()
    def task = timer.runEvery(opt.d ? opt.d.toInteger() * 1000 : 1000, opt.p ? opt.p.toInteger() * 1000 : 60000) {
      cfg = config.load()
      def tmp = [:]
      cfg.hosts.each {
        def proc = "ping -c 3 ${it}".execute()
        proc.waitFor()
        tmp[it] = proc.exitValue()
        if(cfg.failed == it) {
          tmp[it] = 1
        }
        println "${it} " + (tmp[it] == 0 ? 'success' : 'failed')
      }

      tmp.each { k, v ->
        if(state[k] != null && state[k] != v){
          mail.send('Ping state change', tmp)
          println "send mail to: " + cfg.mail.to
        }
      }
      state = tmp
    }
}
