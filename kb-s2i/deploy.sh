
cp /tmp/src/conf/ocp/logback.xml $CONF_DIR/logback.xml
cp /tmp/src/conf/ds-pictureHash.yaml $CONF_DIR/ds-pictureHash.yaml
 
ln -s  $TOMCAT_APPS/ds-pictureHash.xml $DEPLOYMENT_DESC_DIR/ds-pictureHash.xml
