# pingdaemon

Pings multiple host and send email on failures

## Configuration file syntax

```json
{
  "mail": {
    "to": "to@gmail.com",
    "from": "from@gmail.com"
  },
  "hosts": [
    "85.30.23.232",
    "85.30.23.233",
    "85.30.23.234"
  ]
}
```
## Usage
```
usage: ping.groovy [OPTIONS]
 -c,--config <arg>   config file
 -d,--delay <arg>    startup sleep time in sec
 -h,--help           usage information
 -p,--period <arg>   period to sleep between checks in sec
```
### Example

```
bin/pingdaemon --config etc/example/config.json
```
## Installation

```
sudo mkdir -p /usr/share && cd /usr/share
sudo git clone https://github.com/attiand/pingdaemon.git
sudo mkdir -p /etc/pingdaemon && sudo cp pingdaemon/etc/example/config.json /etc/pingdaemon/
sudo mkdir -p /usr/lib/systemd/system && sudo cp pingdaemon/etc/pingdaemon.service /usr/lib/systemd/system/pingdaemon.service
systemctl enable pingdaemon
```
### Prerequisite

* Groovy
