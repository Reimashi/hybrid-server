<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:tns="http://www.esei.uvigo.es/dai/hybridserver">

	<xsl:output method="html" encoding="utf-8" />

	<xsl:template match="/">
	  <html>
	  	<body>
	  		<div>
	  			<h2>Conexiones</h2>
	  			<table border="1">
	  				<tr>
	  					<td>Puerto HTTP</td>
	  					<td><xsl:value-of select="/tns:configuration/tns:connections/tns:http"/></td>
	  				</tr>
	  				<tr>
	  					<td>N. maximo de clientes</td>
	  					<td><xsl:value-of select="/tns:configuration/tns:connections/tns:numClients"/></td>
	  				</tr>
	  				<tr>
	  					<td>Servicio web</td>
	  					<td><xsl:value-of select="/tns:configuration/tns:connections/tns:webservice"/></td>
	  				</tr>
	  			</table>
	  		</div>
	  		<div>
	  			<h2>Base de datos</h2>
	  			<table border="1">
	  				<tr>
	  					<td>Dirección del servidor</td>
	  					<td><xsl:value-of select="/tns:configuration/tns:database/tns:url"/></td>
	  				</tr>
	  				<tr>
	  					<td>Usuario</td>
	  					<td><xsl:value-of select="/tns:configuration/tns:database/tns:user"/></td>
	  				</tr>
	  				<tr>
	  					<td>Contraseña</td>
	  					<td><xsl:value-of select="/tns:configuration/tns:database/tns:password"/></td>
	  				</tr>
	  			</table>
	  		</div>
	  		<div>
	  			<h2>Servidores Remotos</h2>
	  			<table border="1">
	  				<tr>
	  					<th>Nombre</th>
	  					<th>Dirección HTTP</th>
	  					<th>Dirección WSDL</th>
	  					<th>Servicio</th>
	  					<th>Namespace</th>
	  				</tr>
	  				<xsl:for-each select="tns:configuration/tns:servers/tns:server">
	  					<tr>
	  						<td><xsl:value-of select="attribute::name"></xsl:value-of></td>
	  						<td><xsl:value-of select="attribute::httpAddress"></xsl:value-of></td>
	  						<td><xsl:value-of select="attribute::wsdl"></xsl:value-of></td>
	  						<td><xsl:value-of select="attribute::service"></xsl:value-of></td>
	  						<td><xsl:value-of select="attribute::namespace"></xsl:value-of></td>
	  					</tr>
	  				</xsl:for-each>
	  			</table>
	  		</div>
	  </body>
	  </html>
	</xsl:template>

</xsl:stylesheet>