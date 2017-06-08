<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:imscp="http://www.imsglobal.org/xsd/imscp_v1p1"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:adlcp="http://www.adlnet.org/xsd/adlcp_v1p3"
                version="2.0"
                xsi:schemaLocation="http://www.imsglobal.org/xsd/imscp_v1p1 imscp_v1p1.xsd
                              http://www.adlnet.org/xsd/adlcp_v1p3 adlcp_v1p3.xsd"


>

    <xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>
    <xsl:strip-space elements="*"/>
    <xsl:key name="resourceUrl" match="//imscp:resource" use="@identifier"/>

    <xsl:template match="/">
        <xsl:text>{</xsl:text>

        <xsl:apply-templates select="imscp:manifest/imscp:organizations"/>


        <xsl:text>}</xsl:text>
    </xsl:template>
    <xsl:template match="imscp:organizations">
        <xsl:if test="@default !=''">
            &quot;default&quot;:&quot;<xsl:value-of select="@default"/>&quot;,
        </xsl:if>
        <xsl:text>&quot;bundles&quot;:[</xsl:text>
        <xsl:apply-templates select="imscp:organization"/>
        <xsl:text>]</xsl:text>
    </xsl:template>
    <xsl:template match="imscp:organization">
        <xsl:text>{</xsl:text>
        <xsl:text>&quot;id&quot;:</xsl:text>&quot;<xsl:value-of select="@identifier"/>&quot;,
        <xsl:text>&quot;title&quot;:</xsl:text>&quot;<xsl:value-of select="imscp:title"/>&quot;,
        <xsl:text>&quot;items&quot;:[</xsl:text>
        <xsl:apply-templates select="imscp:item"/>
        <xsl:text>]</xsl:text>
        <xsl:text>}</xsl:text>
        <xsl:if test="position() != last()">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="imscp:item">
        <xsl:text>{</xsl:text>
        <xsl:text>&quot;id&quot;:</xsl:text>&quot;<xsl:value-of select="@identifier"/>&quot;
        <xsl:if test="@identifierref !=''">
            <xsl:text>,&quot;url&quot;:</xsl:text>&quot;<xsl:value-of select="key('resourceUrl',@identifierref)/@href"/>&quot;
        </xsl:if>

        <xsl:if test="@isvisible !=''">
            <xsl:text>,&quot;isVisible&quot;:</xsl:text>&quot;<xsl:value-of select="@isvisible"/>&quot;
        </xsl:if>


        <xsl:text>,&quot;title&quot;:</xsl:text>&quot;<xsl:value-of select="imscp:title"/>&quot;

        <xsl:apply-templates select="adlcp:timeLimitaction"/>
        <xsl:apply-templates select="adlcp:dataFromLMS"/>
        <xsl:apply-templates select="adlcp:completionThreshold"/>

        <xsl:if test="imscp:item !=''">
            <xsl:text>,&quot;items&quot;:[</xsl:text>
            <xsl:apply-templates select="imscp:item"/>
            <xsl:text>]</xsl:text>
        </xsl:if>
        <xsl:text>}</xsl:text>
        <xsl:if test="position() != last()">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template match="adlcp:timelimitaction">
        <xsl:text>,&quot;actionOnTimeLimit&quot;:&quot;</xsl:text><xsl:value-of select="."/>&quot;
    </xsl:template>

    <xsl:template match="adlcp:datafromlms">
        <xsl:text>,&quot;dataFromLMS&quot;:&quot;</xsl:text><xsl:value-of select="."/>&quot;
    </xsl:template>

    <xsl:template match="adlcp:completionThreshold">
        <xsl:text>,&quot;completionThreshold;:&quot;</xsl:text><xsl:text>{&quot;completedByMeasure:&quot;</xsl:text>
        <xsl:value-of select="@completedByMeasure"/>&quot;,
        <xsl:text>&quot;minProgressMeasure:&quot;</xsl:text><xsl:value-of select="@minProgressMeasure"/>&quot;,
        <xsl:text>&quot;value&quot;:&quot;</xsl:text><xsl:value-of select="."/>&quot;
        <xsl:text>}</xsl:text>
    </xsl:template>


    <xsl:template match="text()"/>

</xsl:stylesheet>