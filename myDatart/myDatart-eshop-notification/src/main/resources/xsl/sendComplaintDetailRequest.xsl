<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="UTF-8" omit-xml-declaration="yes" />
<xsl:template match="/">
<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
  <s:Header/>
  <s:Body>
    <SendDetailComplaint xmlns="http://ws.mydatart.axapta.datart.cz">
      <repNr><xsl:value-of select="//id" /></repNr>
    </SendDetailComplaint>
  </s:Body>
</s:Envelope>
</xsl:template>
</xsl:stylesheet>
