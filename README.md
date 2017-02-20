# Pingdaemon

Pings multiple hosts and sends email on status change.

## Configuration file syntax (Groovy)

```groovy
mail {
  to = 'to@gmail.com'
  from = 'from@gmail.com'
}
hosts = [
  '85.30.23.232',
  '85.30.23.233',
  '85.30.23.234'
]
```

### Multiple mail recipients
```groovy
  to = ['m1@gmail.com', 'm2@gmail.com']
```
### Change the polling period
Default is 60 seconds.
```groovy
  pollingPeriod = 60
```
## Usage
```
usage: ping.groovy [OPTIONS]
 -c,--config <arg>   config file (default is config.cfg)
 -d,--delay <arg>    startup sleep time in sec
 -h,--help           usage information
 -p,--period <arg>   period to sleep between checks in sec
```
### Example

```
bin/pingdaemon --config etc/example.cfg
```
## Installation

```
sudo mkdir -p /usr/share && cd /usr/share
sudo git clone https://github.com/attiand/pingdaemon.git
sudo mkdir -p /etc/pingdaemon && sudo cp pingdaemon/etc/example.cfg /etc/pingdaemon/pingdaemon.cfg
sudo mkdir -p /usr/lib/systemd/system && sudo cp pingdaemon/etc/pingdaemon.service /usr/lib/systemd/system/pingdaemon.service
systemctl enable pingdaemon
```
### Prerequisites

* Groovy 2
