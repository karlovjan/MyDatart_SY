<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="UTF-8" omit-xml-declaration="yes" />



<xsl:param name="entityId"/>

<xsl:template match="/">

    <SendDetailOrder xmlns="http://ws.mydatart.axapta.datart.cz">
      <orderId><xsl:value-of select="$entityId" /></orderId>
<!-- 		<orderId><xsl:value-of select="//*[local-name() = 'orderId']/text()" /></orderId> -->
    </SendDetailOrder>

</xsl:template>
  
</xsl:stylesheet>
