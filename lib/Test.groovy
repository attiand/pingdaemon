

def config = new Config(new File('example.cfg'))
def cfg = config.load()
def mail = new Mail(config: cfg)

def tmp = [:]

tmp['123.123.123.122']  = 0
tmp['123.123.123.123']  = 1

mail.send('Ping state change', tmp)
