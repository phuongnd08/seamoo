<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en-US" xml:lang="en">
    <head>
        <!--
        Created by Artisteer v2.2.0.17376
        Base template (without user's data) checked by http://validator.w3.org : "This page is valid XHTML 1.0 Transitional"
        -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
        <title>${title}</title>
        <c:set var="templateName" value="x2-consolas"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/templates/${templateName}/script.js"></script>

        <link rel="stylesheet" href="<%=request.getContextPath()%>/templates/${templateName}/style.css" type="text/css" media="screen" />
        <!--[if IE 6]><link rel="stylesheet" href="<%=request.getContextPath()%>/templates/${templateName}/style.ie6.css" type="text/css" media="screen" /><![endif]-->
        <!--[if IE 7]><link rel="stylesheet" href="<%=request.getContextPath()%>/templates/${templateName}/style.ie7.css" type="text/css" media="screen" /><![endif]-->
        <style type="text/css">
            /*for-category*/
            .selected {
                background: yellow;
            }
        </style>
    </head>
    <body>
        <div id="art-page-background-gradient"></div>
        <div id="art-page-background-glare">
            <div id="art-page-background-glare-image"></div>
        </div>
        <div id="art-main">
            <div class="art-Sheet">
                <div class="art-Sheet-tl"></div>
                <div class="art-Sheet-tr"></div>
                <div class="art-Sheet-bl"></div>
                <div class="art-Sheet-br"></div>
                <div class="art-Sheet-tc"></div>
                <div class="art-Sheet-bc"></div>
                <div class="art-Sheet-cl"></div>
                <div class="art-Sheet-cr"></div>
                <div class="art-Sheet-cc"></div>
                <div class="art-Sheet-body">
                    <div class="art-Header">
                        <div class="art-Header-png"></div>
                        <div class="art-Header-jpeg"></div>
                        <div class="art-Logo">
                            <h1 id="name-text" class="art-Logo-name"><a href="#">Super Market Management System</a></h1>
                        </div>
                    </div>
                    <div class="art-contentLayout">
                        <div class="art-sidebar1">
                            <tiles:insertAttribute name="menu" />
                        </div>
                        <div class="art-content">
                            <div class="art-Post">
                                <div class="art-Post-tl"></div>
                                <div class="art-Post-tr"></div>
                                <div class="art-Post-bl"></div>
                                <div class="art-Post-br"></div>
                                <div class="art-Post-tc"></div>
                                <div class="art-Post-bc"></div>
                                <div class="art-Post-cl"></div>
                                <div class="art-Post-cr"></div>
                                <div class="art-Post-cc"></div>
                                <div class="art-Post-body">
                                    <div class="art-Post-inner art-article">
                                        <div class="art-PostMetadataHeader">
                                            <h2 class="art-PostHeaderIcon-wrapper">
                                                <img src="<%=request.getContextPath()%>/templates/${templateName}/images/PostHeaderIcon.png" width="26" height="26" alt="PostHeaderIcon" />
                                                <span class="art-PostHeader">${title}</span>
                                            </h2>
                                        </div>
                                        <div class="art-PostHeaderIcons art-metadata-icons">
                                        </div>
                                        <div class="art-PostContent">
                                            <p>
                                                <tiles:insertAttribute name="body" />
                                            </p>
                                        </div>
                                        <div class="cleared"></div>
                                    </div>


                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="cleared"></div><div class="art-Footer">
                        <div class="art-Footer-inner">
                            <div class="art-Footer-text">
                                <p>Created by mrcold with Artisteer.</p>
                            </div>
                        </div>
                        <div class="art-Footer-background"></div>
                    </div>
                </div>
            </div>
            <div class="cleared"></div>
        </div>

    </body>
</html>