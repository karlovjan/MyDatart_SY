<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="UTF-8" omit-xml-declaration="yes" />



<xsl:param name="entityId"/>

<xsl:template match="/">

    <SendDetailManual xmlns="http://ws.mydatart.axapta.datart.cz">
      <manualId><xsl:value-of select="$entityId" /></manualId>
<!-- 		<orderId><xsl:value-of select="//*[local-name() = 'orderId']/text()" /></orderId> -->
    </SendDetailManual>

</xsl:template>
  
</xsl:stylesheet>
