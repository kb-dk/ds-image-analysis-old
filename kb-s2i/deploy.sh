
cp /tmp/src/conf/ocp/logback.xml $CONF_DIR/logback.xml
cp /tmp/src/conf/ds-image-analysis.yaml $CONF_DIR/ds-image-analysis.yaml
 
ln -s  $TOMCAT_APPS/ds-image-analysis.xml $DEPLOYMENT_DESC_DIR/ds-image-analysis.xml
