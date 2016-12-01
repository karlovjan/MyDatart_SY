<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="UTF-8" omit-xml-declaration="yes" />
<xsl:template match="/">

	<xsl:param name="entityId"/>

    <SendDetailOffer xmlns="http://ws.mydatart.axapta.datart.cz">
      <offerId><xsl:copy-of select="$entityId" /></offerId>
    </SendDetailOffer>

</xsl:template>
</xsl:stylesheet>
