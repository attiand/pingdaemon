#!/usr/bin/env groovy

@Grab(group='org.apache.ant', module='ant-javamail', version='1.9.4')
@Grab(group='javax.activation', module='activation', version='1.1.1')
@Grab(group='javax.mail', module='mail', version='1.4.7')
@GrabConfig(systemClassLoader=true)

import groovy.json.JsonSlurper

class GroovyTimerTask extends TimerTask {
    Closure closure
    void run() {
        closure()
    }
}

class TimerMethods {
    static TimerTask runEvery(Timer timer, long delay, long period, Closure codeToRun) {
        TimerTask task = new GroovyTimerTask(closure: codeToRun)
        timer.schedule task, delay, period
        task
    }
}

def sendMail(def to, def from, def subject, def iptable) {
  def ant = new AntBuilder()
  def body = "ip table\n"
  iptable.each {
    k, v -> body <<= "$k: " + (v == 0 ? 'success' : 'failed') + "\n"
  }

  ant.mail (from: from,
            tolist: to,
            message: body,
            subject : subject,
            mailhost: 'localhost',
            messagemimetype: 'text/html')
}

def readConfig () {
  File f = new File('config.json')
  if(f.exists()) {
    return f.withReader { r ->
        new JsonSlurper().parse( r )
    }
  }
}

def cfg = readConfig()

if(! cfg) {
    System.err << "Could not find configuration file\n"
    return -1
}

enum State {OK, FAILED}
State state = State.OK
def iptable = [:]

use (TimerMethods) {
    def timer = new Timer()
    def task = timer.runEvery(10_000, 60_000) {
      cfg = readConfig()
      cfg.hosts.each {
        def proc = "ping -c 3 ${it}".execute()
        proc.waitFor()
        iptable[it] = proc.exitValue()

        println "${it} " + (iptable[it] == 0 ? 'success' : 'failed')

        if(iptable[it] != 0 && state == State.OK) {
          state = State.FAILED
          sendMail(cfg.mail.to, cfg.mail.from, 'Ping failed', iptable)
        }
      }

      if(state == State.FAILED){
        if(iptable.values().findAll { it == 0 }.size() == iptable.size()) {
          state = State.OK
          sendMail(cfg.mail.to, cfg.mail.from, 'Ping OK', iptable)
        }
      }
    }
}
