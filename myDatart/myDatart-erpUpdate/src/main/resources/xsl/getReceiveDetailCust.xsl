<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="UTF-8" omit-xml-declaration="yes" />
	<xsl:template match="/">

		<vid:ReceiveDetailCust xmlns:vid="http://ws.mydatart.axapta.datart.cz">
			<vid:xmlInput>
				<xsl:copy>
					<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
					<xsl:copy-of select="*" />
					<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
				</xsl:copy>
			</vid:xmlInput>
		</vid:ReceiveDetailCust>

	</xsl:template>
</xsl:stylesheet>