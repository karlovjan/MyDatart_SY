<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="UTF-8" omit-xml-declaration="yes" />

	<xsl:param name="entityId"/>

	<xsl:template match="/">
	
	    <SendDetailCust xmlns="http://ws.mydatart.axapta.datart.cz">
	      <customerId><xsl:copy-of select="$entityId" /></customerId>
	    </SendDetailCust>
	
	</xsl:template>
</xsl:stylesheet>
