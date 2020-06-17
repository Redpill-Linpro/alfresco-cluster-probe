Alfresco Cluster Probe
=============================================

This module is sponsored by Redpill Linpro AB - http://www.redpill-linpro.com.

Description
-----------
This module contains probing functionality used in clustered setups of Alfresco. 

Structure
------------

The project consists of two modules, one for the repository side and one for share.

Building & Installation
------------
The build produces jar filew which can be included in your maven project using the following declaration in your pom.xml files.

Repository dependency:

```xml
<dependency>
  <groupId>org.redpill-linpro.alfresco</groupId>
  <artifactId>alfresco-cluster-probe-platform</artifactId>
  <version>2.0.0</version>
</dependency>
```
Share dependency:

```xml
<dependency>
  <groupId>org.redpill-linpro.alfresco</groupId>
  <artifactId>alfresco-cluster-probe-share</artifactId>
  <version>2.0.0</version>
</dependency>
```

Maven repository:

```xml
<repository>
  <id>redpill-public</id>
  <url>http://maven.redpill-linpro.com/nexus/content/groups/public</url>
</repository>
```

Usage
-----

The tool allows an administrator to set whether or not a repository or share cluster node should be considered to be online in a cluster.

An administrator can go to the Share admin console or the enterprise admin console on the repository side to control the status of all servers in the cluster that have the same configuration.


The following endpoints are available out of the box:

Share:
* http://localhost:8081/share/noauth/org/redpill/alfresco/clusterprobe/probe/repo - Will return the probe status and text for share
* http://localhost:8081/share/noauth/org/redpill/alfresco/clusterprobe/probe/repo - Will return the probe status and text for the repository node that this share node is currently connected to
* http://localhost:8081/share/noauth/org/redpill/alfresco/clusterprobe/probe/search - Will return the probe status of the search engine that the current repo node uses
* http://localhost:8081/share/noauth/org/redpill/alfresco/clusterprobe/probe/transform/docx/pdf - Will return the probe status of transforms between docx and pdf on the repo node that this share node is currently connected to
* http://localhost:8081/share/noauth/org/redpill/alfresco/clusterprobe/probe/transform/docx/png - Will return the probe status of transforms between docx and png on the repo node that this share node is currently connected to

Repository:
* http://localhost:8080/alfresco/service/org/redpill/alfresco/clusterprobe/probe/repo - Will return the probe status of the targeted repository node
* http://localhost:8080/alfresco/service/org/redpill/alfresco/clusterprobe/probe/search - Will return the probe status of the search engine that the current repo node uses
* http://localhost:8080/alfresco/service/org/redpill/alfresco/clusterprobe/probe/share - Will return the probe status for share on the current server
* http://localhost:8080/alfresco/service/org/redpill/alfresco/clusterprobe/probe/transform/docx/pdf - Will return the probe status of transforms between docx and pdf
* http://localhost:8080/alfresco/service/org/redpill/alfresco/clusterprobe/probe/transform/docx/png - Will return the probe status of transforms between docx and png

The urls for repo, share and search can also be appended with a hostname, to get the probe-setting of a specific node. Ex:
```
http://localhost:8080/alfresco/service/org/redpill/alfresco/clusterprobe/probe/repo/localhost2
```
Will give the configured repo-status for the server localhost2

Example responses for repo, share and search probes:

```
HTTP Status code: 
200
Response text:
localhost-ONLINE
```
```
HTTP Status code: 
404
Response text:
localhost-OFFLINE
```

Example response for transformation probes:

```
HTTP Status code: 200
Transformation is working on server localhost
```

```
HTTP Status code: 500
Transformation not available. An occured error on server localhost: Test transformation from docx to pdf not supported
```


Configuration
-------------


Two things are needed to configure the clusterprobe:

* One json file, which will contain the state of each server. This file could be shared over nfs to be able to control all nodes from one node. Below is an example of a json-configuration.
```
[
  {
    "serverName": "localhost",
    "repo": true,
    "share": true,
    "search": true,
    "description": "localhost server 1"
  },
  {
    "serverName": "localhost2",
    "repo": true,
    "share": true,
    "search": true,
    "description": "localhost server 2"
  },
  {
    "serverName": "localhost3",
    "share": true,
    "description": "localhost share server"
  },
  {
    "serverName": "localhost4",
    "search": true,
    "description": "localhost search server"
  },
  {
    "serverName": "localhost5",
    "repo": true,
    "description": "locahost repo server"
  }
]
```
* Configuration in alfresco-global.properties. There are a number of properties that need to be set on the platform side for this to work
    * 
    *  cluster.probe.online.httpcode=200, The code that will be returned for an active node
    *  cluster.probe.offline.httpcode=404, The code that will be returned for an inactive node
    *  cluster.probe.online.text=ONLINE, The text that will be returned for an active node
    *  cluster.probe.offline.text=OFFLINE, The text that will be returned for an inactive node
    *  cluster.probe.hosts=localhost,localhost3,localhost4,localhost5, A list of configured servers. Specifies which servers are configured in the json-config file. Only server entries that are in both places will be valid
    *  cluster.probe.host=localhost, The current server, will be used of no explicit server is provided in probe call
    *  cluster.probe.discpath=/clusterprobesettings/clusterProbeSettings.json, the path to the json configuration file on disk


The recommended way of configuring the cluster probe is to use either configuration file or a custom environment variable. The lookup of server hostname might be expensive if the probe is used continuously.

For repository nodes the following configuration should be made in ```alfresco-global.properties```

```
#The hostname of the current server to be used to differentiate different cluster nodes
cluster.probe.host=host.domain.tld
```

For share nodes the following configuration should be made in ```share-config-custom.xml```

```
  <!-- The hostname of this cluster node-->
  <config>
    <probe-host>host.domain.tld</probe-host>
  </config>
```

License
-------

This application is licensed under the LGPLv3 License. See the [LICENSE file](LICENSE) for details.

Authors
-------

Niklas Ekman - Redpill Linpro AB

Marcus Svartmark - Redpill Linpro AB
