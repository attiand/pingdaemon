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

def config = new Config(new File(opt.c ?: 'config.cfg'))
def cfg = config.load()
def mail = new Mail(config: cfg)

def state = [:]

use (TimerMethods) {
    def timer = new Timer()

    def task = timer.runEvery(opt.d ? opt.d.toInteger() : cfg.startupDelay, opt.p ? opt.p.toInteger() : cfg.pollingPeriod) {
      cfg = config.load()
      def tmp = [:]
      cfg.hosts.each {
        def proc = "ping -c 3 ${it}".execute()
        proc.waitFor()
        tmp[it] = proc.exitValue()
        if(cfg.failed == it) {
          tmp[it] = 1
        }

        def host = new Host(address: it)
        println "${it} (${host.name}) " + (tmp[it] == 0 ? 'success' : 'failed')
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
