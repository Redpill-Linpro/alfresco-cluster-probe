package org.redpill.alfresco.clusterprobe;

import org.alfresco.service.namespace.QName;

public interface ClusterProbeModel {

  public static final String CP_URI = "http://www.redpill-linpro.com/cluster-probe/model/1.0";

  public static final QName PROP_STATUS = QName.createQName(CP_URI, "status");
  public static final QName PROP_LAST_UPDATED = QName.createQName(CP_URI, "lastUpdated");
  public static final QName PROP_TEXT = QName.createQName(CP_URI, "text");
  public static final QName PROP_CODE = QName.createQName(CP_URI, "code");

}
