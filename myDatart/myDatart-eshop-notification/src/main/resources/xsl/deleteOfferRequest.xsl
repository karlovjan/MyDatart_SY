<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output encoding="UTF-8" omit-xml-declaration="yes"/>
    <xsl:template match="/">
        
                <bam:updateMarketingOffers xmlns:bam="http://etnetera.com/projects/datart/bambino">
                    <scope>
                       <segment></segment>
                       <oldVersion></oldVersion>
                       <newVersion></newVersion>
                    </scope>

                    <list>
                        <eUser deleted="true">
                            <id>
                                <xsl:value-of select="//id"/>
                            </id>
                        </eUser>
                    </list>
                </bam:updateMarketingOffers>
          
    </xsl:template>
</xsl:stylesheet>