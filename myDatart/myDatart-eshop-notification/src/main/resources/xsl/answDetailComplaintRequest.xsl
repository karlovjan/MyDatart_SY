<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
    <xsl:output encoding="UTF-8" omit-xml-declaration="yes" />

	<xsl:param name="entityId"/>
	<xsl:param name="newItemVersion"/>
	<xsl:param name="axaResultOk"/>
	<xsl:param name="axaErrorText"/>

    <xsl:template match="/">
        <AnswDetailComplaint xmlns="http://ws.mydatart.axapta.datart.cz">
		   <repNr><xsl:value-of select="$entityId"/></repNr>
		   <version><xsl:value-of select="$newItemVersion"/></version>
		   <result><xsl:value-of select="$axaResultOk"/></result>
		   <resultText><xsl:value-of select="$axaErrorText"/></resultText>
		</AnswDetailComplaint>
    </xsl:template>

    <!-- wipe out outgoing text -->
    <xsl:template match="text()" />
</xsl:stylesheet>