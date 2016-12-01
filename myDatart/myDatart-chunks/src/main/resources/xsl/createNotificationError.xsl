<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
    <xsl:output encoding="UTF-8" omit-xml-declaration="yes" />

<!--     <xsl:variable name="error" select="list/error" /> -->
	<xsl:param name="errorText"/>
	<xsl:param name="error"/>

    <xsl:template match="/">
        <xsl:element name="list">
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>

    <xsl:template match="recId|recid">
        <xsl:element name="notification">
            <xsl:element name="recId"><xsl:value-of select="text()" /></xsl:element>
            <xsl:element name="result"><xsl:value-of select="$error" /></xsl:element>
            <xsl:element name="resultText"><xsl:value-of select="$errorText" /></xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="text()" />

</xsl:stylesheet>