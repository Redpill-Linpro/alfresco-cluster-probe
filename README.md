Alfresco Cluster Probe
=============================================

This module is sponsored by Redpill Linpro AB - http://www.redpill-linpro.com.

Description
-----------
This module contains probing functionality used in clustered setups of Alfresco. 

Structure
------------

The project consists of one module which can be used on both repository side and on share.

Building & Installation
------------
The build produces one jar file which can be included in your maven project using the following declaration in your pom.xml file.

Repository dependency:
```xml
<dependency>
  <groupId>org.redpill-linpro.alfresco</groupId>
  <artifactId>alfresco-cluster-probe</artifactId>
  <version>1.1.8</version>
</dependency>
`

Maven repository:
```xml
<repository>
  <id>redpill-public</id>
  <url>http://maven.redpill-linpro.com/nexus/content/groups/public</url>
</repository>
```

The jar files are also downloadable from: https://maven.redpill-linpro.com/nexus/index.html#nexus-search;quick~alfresco-cluster-probe

Usage
-----

The tool allows an administrator to set whether or not a repository or share cluster node should be considered to be online in a cluster.

An administrator can go to the Share admin console to control the status of a share node or the enterprise admin console on the repository side to control the status of a repository node.

The user can control what text and HTTP status a "probe"-page will return. This can be used in a load balancer to read node status and control whether or not traffic should be directed to the node.

The following endpoints are available out of the box:

Share:
* http://localhost:8081/share/service/org/redpill/alfresco/clusterprobe/probe - Will return the probe status and text for share
* http://localhost:8081/share/service/org/redpill/alfresco/clusterprobe/proberepo - Will return the probe status and text for the repository node that this share node is currently connected to

Repository:
* http://localhost:8080/alfresco/service/org/redpill/alfresco/clusterprobe/probe - Will return the probe status of the targeted repository node

Example response:

```
HTTP Status code: 200
host.domain.tld-STATUS
```

For repository nodes the following configuration should be made in ```alfresco-global.properties```

```
#The hostname of the current server to be used to differentiate different cluster nodes
alfresco.probe.host=host.domain.tld
```

For share nodes the following configuration should be made in ```share-config-custom.xml```

```
<!-- Configuration for cluster-probe -->
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
