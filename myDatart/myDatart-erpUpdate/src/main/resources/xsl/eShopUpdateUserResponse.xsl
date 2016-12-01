<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
    <xsl:output encoding="UTF-8" omit-xml-declaration="yes" />

    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="//*[local-name()='Fault']">
                <xsl:copy-of select="." />
            </xsl:when>
            <xsl:when test="//*//*[string-length() = 0]">
				<bam:updateUserResponse xmlns:bam="http://etnetera.com/projects/datart/bambino"/>
            </xsl:when>
            <xsl:otherwise>
				<soapenv:Fault><faultcode>UpdateUserError</faultcode><faultstring>Axapta fails on updating user!</faultstring><detail><ns2:requestError xmlns:ns2="http://etnetera.com/projects/datart/bambino"><errorCode>UpdateUserError</errorCode><errorText><xsl:value-of select="text()"/></errorText></ns2:requestError></detail></soapenv:Fault>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- wipe out outgoing text -->
    <xsl:template match="text()" />
</xsl:stylesheet>