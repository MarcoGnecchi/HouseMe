# WebMonitor
A crawler to monitor websites updates

Add resource to be monitored:
curl -vX POST http://{host}:9000/api/monitor -d @resource.json --header "Content-type: application/json"


Scan resource:
curl http://{host}:9000/api/check
