<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
    <xsl:output encoding="UTF-8" omit-xml-declaration="yes"/>

    <xsl:template match="/">
        <xsl:choose>
        	<xsl:when test="/*[local-name()='Fault']">
                <xsl:copy-of select="." />
            </xsl:when>
            <!-- good response -->
            <xsl:when test="//*//*[string-length() = 0]" >
				<bam:changeOrderOwnerResponse xmlns:bam="http://etnetera.com/projects/datart/bambino"/>
            </xsl:when>

            <!-- known Axapta error-->
<!--             test="count(axaptaResponse[text() = 'bad amount' or text() = 'incorrect order' or text() = 'incorrect user']) > 0 "> -->
				

            <!-- other errors -->
            <xsl:otherwise>
				<soapenv:Fault><faultcode>ChangeOrderOwnerError</faultcode><faultstring>Invalid return code!</faultstring><detail><ns2:requestError xmlns:ns2="http://etnetera.com/projects/datart/bambino"><errorCode>InvalidReturnCode</errorCode><errorText lang="en"><xsl:value-of select="text()"/></errorText></ns2:requestError></detail></soapenv:Fault>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="text()" />
</xsl:stylesheet>