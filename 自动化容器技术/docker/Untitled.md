```
# sed -i "s/SELINUX=enforcing/SELINUX=disabled/g" /etc/selinux/config
# systemctl stop firewalld
# systemctl disable firewalld
```

```
# yum install -y yum-utils device-mapper-persistent-data lvm2
# yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
# yum list docker-ce --showduplicates | sort -r
# yum -y install docker-ce
# docker --version
Docker version 17.06.2-ce, build cec0b72
# systemctl start docker
# systemctl status docker
# systemctl enable docker
```

```
# curl -L https://storage.googleapis.com/etcd/v3.2.9/etcd-v3.2.9-linux-amd64.tar.gz -o /root/etcd-v3.2.9-linux-amd64.tar.gz
# tar -zxvf etcd-v3.2.9-linux-amd64.tar.gz
# cp etcd-v3.2.9-linux-amd64/etcd* /usr/bin/
# etcd --version
etcd Version: 3.2.9
Git SHA: f1d7dd8
Go Version: go1.8.4
Go OS``/Arch``: linux``/amd64
# etcdctl --version
etcdctl version: 3.2.9
API version: 2
```

```
# wget https://storage.googleapis.com/kubernetes-release/release/v1.8.1/kubernetes-server-linux-amd64.tar.gz
# tar -zxvf kubernetes-server-linux-amd64.tar.gz
# cd kubernetes/server/bin/
# cp kubectl kube-apiserver kube-scheduler kube-controller-manager kubelet kube-proxy /usr/bin/       
# kube-apiserver --version
Kubernetes v1.8.1
```

```
# curl -L https://github.com/coreos/flannel/releases/download/v0.9.0/flannel-v0.9.0-linux-amd64.tar.gz -o flannel-v0.9.0-linux-amd64.tar.gz
# tar -zxvf flannel-v0.9.0-linux-amd64.tar.gz
# mv flanneld /usr/bin/
# mkdir /usr/libexec/flannel/
# mv mk-docker-opts.sh /usr/libexec/flannel/
# flanneld --version
v0.9.0
```

```
# vim /usr/lib/systemd/system/etcd.service
[Unit]
Description=etcd
After=network.target
After=network-online.target
Wants=network-online.target
Documentation=https:``//github``.com``/coreos/etcd
[Service]
Type=notify
WorkingDirectory=``/var/lib/etcd
EnvironmentFile=-``/etc/etcd/etcd``.conf
ExecStart=``/usr/bin/etcd` `--config-``file` `/etc/etcd/etcd``.conf
Restart=on-failure
LimitNOFILE=65536
Restart=on-failure
RestartSec=5
LimitNOFILE=65536
[Install]
WantedBy=multi-user.target
```



```
# mkdir -p /var/lib/etcd/
# mkdir -p /etc/etcd/
# export ETCD_NAME=etcd
# export INTERNAL_IP=192.168.100.104
# cat << EOF > /etc/etcd/etcd.conf 
name: ``'${ETCD_NAME}'
data-``dir``: ``"/var/lib/etcd/"
listen-peer-urls: http:``//``${INTERNAL_IP}:2380
listen-client-urls: http:``//``${INTERNAL_IP}:2379,http:``//127``.0.0.1:2379
initial-advertise-peer-urls: http:``//``${INTERNAL_IP}:2380
advertise-client-urls: http:``//``${INTERNAL_IP}:2379
initial-cluster: ``"etcd=http://${INTERNAL_IP}:2380"
initial-cluster-token: ``'etcd-cluster'
initial-cluster-state: ``'new'
EOF
```



```
KUBE_API_ADDRESS=``"--advertise-address=172.24.5.22 --bind-address=192.168.100.104 --insecure-bind-address=0.0.0.0"
KUBE_ETCD_SERVERS=``"--etcd-servers=http://172.24.5.22:2379"
KUBE_SERVICE_ADDRESSES=``"--service-cluster-ip-range=10.254.0.0/16"
KUBE_ADMISSION_CONTROL=``"--admission-control=NamespaceLifecycle,NamespaceExists,LimitRanger,SecurityContextDeny,ResourceQuota"
KUBE_API_ARGS=``"--enable-swagger-ui=true --apiserver-count=3 --audit-lo
```







```
grep ^[A-Z] /etc/sysconfig/flanneld FLANNEL_ETCD_ENDPOINTS=``"http://192.168.100.104:2379"
FLANNEL_ETCD_PREFIX=``"/kube/network"
```

```
etcdctl -o extended  get /kube/network/subnets/10.254.26.0-24
Key: ``/kube/network/subnets/10``.254.26.0-24
Created-Index: 6
Modified-Index: 6
TTL: 85638
Index: 6
{``"PublicIP"``:``"192.168.100.104"``}
```

```
`# sed -i "s/SELINUX=enforcing/SELINUX=disabled/g" /etc/selinux/config``# systemctl stop firewalld``# systemctl disable firewalld`
```

<https://yq.aliyun.com/articles/523821?spm=5176.11065265.1996646101.searchclickresult.68fd63f2hhhjdM>







```
# wget https://storage.googleapis.com/kubernetes-release/release/v1.8.1/kubernetes-server-linux-amd64.tar.gz
# tar -zxvf kubernetes-server-linux-amd64.tar.gz
# cd kubernetes/server/bin/
# cp kubectl kube-apiserver kube-scheduler kube-controller-manager kubelet kube-proxy /usr/bin/       
# kube-apiserver --version
Kubernetes v1.8.1
```



```
# curl -L https://github.com/coreos/flannel/releases/download/v0.9.0/flannel-v0.9.0-linux-amd64.tar.gz -o flannel-v0.9.0-linux-amd64.tar.gz
# tar -zxvf flannel-v0.9.0-linux-amd64.tar.gz
# mv flanneld /usr/bin/
# mkdir /usr/libexec/flannel/
# mv mk-docker-opts.sh /usr/libexec/flannel/
# flanneld --version
v0.9.0
```

```
apiVersion: v1
kind: Service
metadata:
  name: kube-dns
  namespace: kube-system
  labels:
    k8s-app: kube-dns
    kubernetes.io/cluster-service: "true"
    addonmanager.kubernetes.io/mode: Reconcile
    kubernetes.io/name: "KubeDNS"
spec:
  selector:
    k8s-app: kube-dns
  clusterIP: __PILLAR__DNS__SERVER__
  ports:
  - name: dns
    port: 53
    protocol: UDP
  - name: dns-tcp
    port: 53
    protocol: TCP
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: kube-dns
  namespace: kube-system
  labels:
    kubernetes.io/cluster-service: "true"
    addonmanager.kubernetes.io/mode: Reconcile
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: kube-dns
  namespace: kube-system
  labels:
    addonmanager.kubernetes.io/mode: EnsureExists
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kube-dns
  namespace: kube-system
  labels:
    k8s-app: kube-dns
    kubernetes.io/cluster-service: "true"
    addonmanager.kubernetes.io/mode: Reconcile
spec:
  strategy:
    rollingUpdate:
      maxSurge: 10%
      maxUnavailable: 0
  selector:
    matchLabels:
      k8s-app: kube-dns
  template:
    metadata:
      labels:
        k8s-app: kube-dns
      annotations:
        seccomp.security.alpha.kubernetes.io/pod: 'runtime/default'
        prometheus.io/port: "10054"
        prometheus.io/scrape: "true"
    spec:
      priorityClassName: system-cluster-critical
      securityContext:
        supplementalGroups: [ 65534 ]
        fsGroup: 65534
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
          - weight: 100
            podAffinityTerm:
              labelSelector:
                matchExpressions:
                  - key: k8s-app
                    operator: In
                    values: ["kube-dns"]
              topologyKey: kubernetes.io/hostname
      tolerations:
      - key: "CriticalAddonsOnly"
        operator: "Exists"
      nodeSelector:
        kubernetes.io/os: linux
      volumes:
      - name: kube-dns-config
        configMap:
          name: kube-dns
          optional: true
      nodeSelector:
        kubernetes.io/os: linux
      containers:
      - name: kubedns
        image: k8s.gcr.io/k8s-dns-kube-dns:1.15.10
        resources:
          limits:
            memory: __PILLAR__DNS__MEMORY__LIMIT__
          requests:
            cpu: 100m
            memory: 70Mi
        livenessProbe:
          httpGet:
            path: /healthcheck/kubedns
            port: 10054
            scheme: HTTP
          initialDelaySeconds: 60
          timeoutSeconds: 5
          successThreshold: 1
          failureThreshold: 5
        readinessProbe:
          httpGet:
            path: /readiness
            port: 8081
            scheme: HTTP
          initialDelaySeconds: 3
          timeoutSeconds: 5
        args:
        - --domain=__PILLAR__DNS__DOMAIN__.
        - --dns-port=10053
        - --config-dir=/kube-dns-config
        - --v=2
        env:
        - name: PROMETHEUS_PORT
          value: "10055"
        ports:
        - containerPort: 10053
          name: dns-local
          protocol: UDP
        - containerPort: 10053
          name: dns-tcp-local
          protocol: TCP
        - containerPort: 10055
          name: metrics
          protocol: TCP
        volumeMounts:
        - name: kube-dns-config
          mountPath: /kube-dns-config
        securityContext:
          allowPrivilegeEscalation: false
          readOnlyRootFilesystem: true
          runAsUser: 1001
          runAsGroup: 1001
      - name: dnsmasq
        image: k8s.gcr.io/k8s-dns-dnsmasq-nanny:1.15.10
        livenessProbe:
          httpGet:
            path: /healthcheck/dnsmasq
            port: 10054
            scheme: HTTP
          initialDelaySeconds: 60
          timeoutSeconds: 5
          successThreshold: 1
          failureThreshold: 5
        args:
        - -v=2
        - -logtostderr
        - -configDir=/etc/k8s/dns/dnsmasq-nanny
        - -restartDnsmasq=true
        - --
        - -k
        - --cache-size=1000
        - --no-negcache
        - --dns-loop-detect
        - --log-facility=-
        - --server=/__PILLAR__DNS__DOMAIN__/127.0.0.1#10053
        - --server=/in-addr.arpa/127.0.0.1#10053
        - --server=/ip6.arpa/127.0.0.1#10053
        ports:
        - containerPort: 53
          name: dns
          protocol: UDP
        - containerPort: 53
          name: dns-tcp
          protocol: TCP
        resources:
          requests:
            cpu: 150m
            memory: 20Mi
        volumeMounts:
        - name: kube-dns-config
          mountPath: /etc/k8s/dns/dnsmasq-nanny
        securityContext:
          capabilities:
            drop:
              - all
            add:
              - NET_BIND_SERVICE
              - SETGID
      - name: sidecar
        image: k8s.gcr.io/k8s-dns-sidecar:1.15.10
        livenessProbe:
          httpGet:
            path: /metrics
            port: 10054
            scheme: HTTP
          initialDelaySeconds: 60
          timeoutSeconds: 5
          successThreshold: 1
          failureThreshold: 5
        args:
        - --v=2
        - --logtostderr
        - --probe=kubedns,127.0.0.1:10053,kubernetes.default.svc.__PILLAR__DNS__DOMAIN__,5,SRV
        - --probe=dnsmasq,127.0.0.1:53,kubernetes.default.svc.__PILLAR__DNS__DOMAIN__,5,SRV
        ports:
        - containerPort: 10054
          name: metrics
          protocol: TCP
        resources:
          requests:
            memory: 20Mi
            cpu: 10m
        securityContext:
          allowPrivilegeEscalation: false
          readOnlyRootFilesystem: true
          runAsUser: 1001
          runAsGroup: 1001
      dnsPolicy: Default  # Don't use cluster DNS.
      serviceAccountName: kube-dns
```

curl -X POST "<http://10.1.0.82:10168/bizconfig/addConfig>" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"configData\": \"eyJzZXJ2aWNlRGVmIjoiU0lDS01BTkFHRSIsInNlcnZpY2VDaGlsZERlZiI6IlNpY2tOb3RpY2UiLCJzZXJ2aWNlRGVmTXNnIjoi5L2P6Zmi566h55CGIn0=", \"configType\": 20001, \"currentUserId\": 18814}"



relatedObjId/relatedObjType/sourceId/sourceType







```
CREATE TABLE `medicalrecordindexs` (
    `id` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
    `providerid` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
    `relationshipid` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
    `patientid` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
    `relatedobjid` bigint(20) UNSIGNED NOT NULL DEFAULT '0',
    `relatedobjtype` varchar(50) NOT NULL DEFAULT '',
    `relationshiptype` varchar(50) NOT NULL DEFAULT '',
    `status` tinyint(2) unsigned NOT NULL DEFAULT 0,
    `isprivate` tinyint(2) unsigned NOT NULL DEFAULT 0,
    `isdelete` tinyint(2) unsigned NOT NULL DEFAULT 0,
    `starttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `ver` mediumint(8) unsigned NOT NULL DEFAULT '0',
    `ctime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    `utime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unidx_provider_patient_relatedobj` (`providerid`,`patientid`,`relatedobjid`,`relatedobjtype`),
    KEY `index_relationshipid_status_starttime` (`relationshipid`, `status`, `starttime`),
    KEY `index_patientid_status_starttime` (`patientid`, `status`, `starttime`),
    KEY `index_relatedobjid_relatedobjtype_status_isprivate` (`relatedobjid`, `relatedobjtype`, `status`, `isprivate`)
) ENGINE = InnoDB CHARSET = utf8mb4;
```



```php
Index: medicalcasesvc.php
===================================================================
--- medicalcasesvc.php	(revision 153335)
+++ medicalcasesvc.php	(working copy)
@@ -142,7 +142,8 @@
             $addStandardDtos = $this->addBingLisToMongo($userId, $patientId, $standardDtos, $bingLiSource, $isDistinct);
             $mongoBingLiIds = $this->getBingLiIdsByDtos($addStandardDtos);
             $bingLiIds = array_merge($bingLiIds, $mongoBingLiIds);
-            $this->bindBingLisByIdAndObjToMongo($mongoBingLiIds, $relatedObjId, $relatedObjType, $userId, $patientId);
+            $res = $this->bindBingLisByIdAndObjToMongo($mongoBingLiIds, $relatedObjId, $relatedObjType, $userId, $patientId);
+			$diseaseCourseId = $res['diseaseCourseId'];
             //同步到mysql
             $this->syncAddBingLisAndRefsToMysql($addStandardDtos, $relatedObjId, $relatedObjType, $bingLiSource);
         }
@@ -154,6 +155,27 @@
         }
         return $bingLiIds;
     }/*}}}*/
+	    // 执行新增病历、病程、病程病历
+    public function addBingLisAndRefsForJava($userId, $patientId, array $bingLiDtos, $relatedObjId, $relatedObjType, $bingLiSource=NodeObjConst::SOURCE_PATIENT, $isDistinct = false)
+    {/*{{{*/
+        if (empty($bingLiDtos)) return null;
+        $relatedObjType = strtolower($relatedObjType);
+        list($curUserId, $curPatientId, $standardDtos, $retainDtos) = $this->checkBingLiDosAndSplit($bingLiDtos);
+        DBC::requireTrue($userId == $curUserId && $patientId == $curPatientId, '你不能绑定他人的病历');
+        $bingLiIds = array();
+		$diseaseCourseId = 0;
+        if (false == empty($standardDtos))
+        {
+            $addStandardDtos = $this->addBingLisToMongo($userId, $patientId, $standardDtos, $bingLiSource, $isDistinct);
+            $mongoBingLiIds = $this->getBingLiIdsByDtos($addStandardDtos);
+            $bingLiIds = array_merge($bingLiIds, $mongoBingLiIds);
+            $res = $this->bindBingLisByIdAndObjToMongo($mongoBingLiIds, $relatedObjId, $relatedObjType, $userId, $patientId);
+            $diseaseCourseId = $res['diseaseCourseId'];
+            //同步到mysql
+            $this->syncAddBingLisAndRefsToMysql($addStandardDtos, $relatedObjId, $relatedObjType, $bingLiSource);
+        }
+        return $diseaseCourseId;
+    }/*}}}*/

     private function syncAddBingLisAndRefsToMysql($addStandardDtos, $relatedObjId, $relatedObjType, $bingLiSource)
     {/*{{{*/
@@ -198,7 +220,8 @@
         $refIds = array();
         if (false == empty($medicalCaseIds))
         {
-            $courseMedicalIds = $this->bindBingLisByIdAndObjToMongo($medicalCaseIds, $relatedObjId, $relatedObjType, $userId, $patientId);
+            $res = $this->bindBingLisByIdAndObjToMongo($medicalCaseIds, $relatedObjId, $relatedObjType, $userId, $patientId);
+			$courseMedicalIds = $res['courseMedicalIds'];
             $refIds = array_merge($refIds, $courseMedicalIds);
             $this->syncBindBingLisByIdAndObjToMysql($medicalCaseIds, $relatedObjId, $relatedObjType);
         }
@@ -282,7 +305,7 @@
             $msg['nodeObjIds'] = $attachmentNodeIds;
             Producer::getInstance('bingli')->publish($msg, 'ImportMedicalImageAsync');
         }
-        return $courseMedicalIds;
+        return array("courseMedicalIds"=>$courseMedicalIds,"diseaseCourseId"=>$diseaseCourse->id);
     }/*}}}*/
```



