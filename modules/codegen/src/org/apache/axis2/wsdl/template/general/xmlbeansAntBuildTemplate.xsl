<xsl:stylesheet version="1.0" xmlns:xalan="http://xml.apache.org/xslt"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes" xalan:indent-amount="4"/>
    <xsl:template match="/ant">
        <xsl:variable name="package"><xsl:value-of select="@package"/></xsl:variable>

        <project basedir="." default="jar.all">
            <xsl:comment>Auto generated ant build file</xsl:comment>
            <property name="name">
                <xsl:attribute name="value">myservice</xsl:attribute>
            </property>
            <property name="src">
                <xsl:attribute name="value">${basedir}\src</xsl:attribute>
            </property>
            <property name="test">
                <xsl:attribute name="value">${basedir}\test</xsl:attribute>
            </property>
            <property name="build">
                <xsl:attribute name="value">${basedir}\build</xsl:attribute>
            </property>
            <property name="classes">
                <xsl:attribute name="value">${build}\classes</xsl:attribute>
            </property>
            <property name="lib">
                <xsl:attribute name="value">${build}\lib</xsl:attribute>
            </property>
            <property name="resources">
                <xsl:attribute name="value">${basedir}\resources</xsl:attribute>
            </property>

            <property name="xbeans.packaged.jar.name" value="XBeans-packaged.jar"></property>
            <property name="jars.ok" value=""></property>

            <target name="init">
                <mkdir>
                    <xsl:attribute name="dir">${build}</xsl:attribute>
                </mkdir>
                <mkdir>
                    <xsl:attribute name="dir">${classes}</xsl:attribute>
                </mkdir>
                <mkdir>
                    <xsl:attribute name="dir">${lib}</xsl:attribute>
                </mkdir>
            </target>

            <target name="jar.xbeans">
                <!-- jar the  XMLbeans stuff to the lib folder-->
                <jar>
                    <xsl:attribute name="basedir">${resources}</xsl:attribute>
                    <xsl:attribute name="destfile">${lib}\${xbeans.packaged.jar.name}</xsl:attribute>
                    <xsl:attribute name="excludes">**/services.xml</xsl:attribute>
                </jar>
            </target>

            <target name="pre.compile.test" depends="init, jar.xbeans">
                <xsl:comment>Test the classpath for the availability of necesary classes</xsl:comment>
                <available classname="org.apache.xmlbeans.XmlObject" property="xbeans.available"/>
                <available classname="javax.xml.stream.XMLStreamReader" property="stax.available"/>
                <available classname="org.apache.axis2.engine.AxisEngine" property="axis2.available"/>
                <condition property="jars.ok" >
                    <and>
                        <isset property="xbeans.available"/>
                        <isset property="stax.available"/>
                        <isset property="axis2.available"/>
                    </and>
                </condition>

                <xsl:comment>Print out the availabilities</xsl:comment>
                <echo>
                     <xsl:attribute name="message">XmlBeans Availability = ${xbeans.available}</xsl:attribute>
                </echo>
                <echo>
                     <xsl:attribute name="message">Stax Availability= ${stax.available}</xsl:attribute>
                </echo>
                <echo>
                     <xsl:attribute name="message">Axis2 Availability= ${axis2.available}</xsl:attribute>
                </echo>

            </target>

            <target name="compile.all" depends="pre.compile.test">
                <xsl:attribute name="if">jars.ok</xsl:attribute>
                <javac debug="on">
                    <xsl:attribute name="destdir">${classes}</xsl:attribute>
                    <xsl:attribute name="srcdir">${src}</xsl:attribute>
                    <classpath>
                        <xsl:attribute name="location">${lib}\${xbeans.packaged.jar.name}</xsl:attribute>
                    </classpath>
                    <classpath>
                        <xsl:attribute name="location">${java.class.path}</xsl:attribute>
                    </classpath>
                </javac>
            </target>

            <target name="echo.classpath.problem" depends="pre.compile.test">
                <xsl:attribute name="unless">jars.ok</xsl:attribute>
                <echo message="The class path is not set right!
                               Please make sure the following classes are in the classpath
                               1. XmlBeans
                               2. Stax
                               3. Axis2
                "></echo>
            </target>
            <target name="jar.all" depends="compile.all,echo.classpath.problem">
                <xsl:attribute name="if">jars.ok</xsl:attribute>
                <copy>
                    <xsl:attribute name="toDir">${classes}/META-INF</xsl:attribute>
                    <fileset>
                        <xsl:attribute name="dir">${resources}</xsl:attribute>
                        <include><xsl:attribute name="name">*.xml</xsl:attribute></include>
                        <include><xsl:attribute name="name">*.wsdl</xsl:attribute></include>
                        <exclude><xsl:attribute name="name">**/schemaorg_apache_xmlbean/**</xsl:attribute></exclude>
                    </fileset>
                </copy>
                <copy>
                    <xsl:attribute name="file">${lib}\${xbeans.packaged.jar.name}</xsl:attribute>
                    <xsl:attribute name="toDir">${classes}/lib</xsl:attribute>
                </copy>
                <jar>
                    <xsl:attribute name="destfile">${lib}/${name}.aar</xsl:attribute>
                    <fileset>
                        <xsl:attribute name="excludes">**/Test.class</xsl:attribute>
                        <xsl:attribute name="dir">${classes}</xsl:attribute>
                    </fileset>
                </jar>
            </target>
            <target depends="jar.all" name="make.repo" if="jars.ok">
                <mkdir>
                    <xsl:attribute name="dir">${build}/repo/</xsl:attribute>
                </mkdir>
                <mkdir>
                    <xsl:attribute name="dir">${build}/repo/services</xsl:attribute>
                </mkdir>
                <copy>
                    <xsl:attribute name="file">${build}/lib/${name}.aar</xsl:attribute>
                    <xsl:attribute name="toDir">${build}/repo/services/</xsl:attribute>
                </copy>
            </target>
            <target depends="make.repo" name="start.server" if="jars.ok">
                <java classname="org.apache.axis2.transport.http.SimpleHTTPServer" fork="true">
                    <arg>
                        <xsl:attribute name="value">${build}/repo</xsl:attribute>
                    </arg>
                </java>
            </target>
            <target name="clean">
              <delete>
                <xsl:attribute name="dir">${build}</xsl:attribute>
              </delete>
            </target>
        </project>
    </xsl:template>
</xsl:stylesheet>
