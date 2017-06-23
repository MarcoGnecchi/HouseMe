# WebMonitor
A crawler to monitor websites updates

Add resource to be monitored:
curl -vX POST http://178.62.109.195:9000/api/monitor -d @resource.json --header "Content-type: application/json"


Scan resource:
curl http://178.62.109.195:9000/api/check
