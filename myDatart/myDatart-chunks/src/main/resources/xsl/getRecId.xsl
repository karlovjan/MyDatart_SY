<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output encoding="UTF-8" omit-xml-declaration="yes"/>

    <xsl:output method="xml" />

    <xsl:template match="/">
        <xsl:element name="list">
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match = "recId|recid">
        <xsl:element name="recId"><xsl:value-of select="text()" /></xsl:element>
    </xsl:template>

    <xsl:template match="*|@*">
        <xsl:apply-templates select="@*|node()"/>
    </xsl:template>

    <xsl:template match="text()" />
</xsl:stylesheet>