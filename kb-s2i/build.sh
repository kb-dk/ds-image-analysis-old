
cd /tmp/src

cp -rp /tmp/src/target/ds-pictureHash-*.war $TOMCAT_APPS/ds-pictureHash.war
cp /tmp/src/conf/ocp/ds-pictureHash.xml $TOMCAT_APPS/ds-pictureHash.xml

export WAR_FILE=$(readlink -f $TOMCAT_APPS/ds-pictureHash.war)
