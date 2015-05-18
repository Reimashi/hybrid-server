package es.uvigo.esei.dai.hybridserver.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.uvigo.esei.dai.hybridserver.document.DocumentBean;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanInfo;
import es.uvigo.esei.dai.hybridserver.document.DocumentBeanType;
import es.uvigo.esei.dai.hybridserver.services.DBService;

public class DocumentDAO {
	private final DBService db;
	
	public DocumentDAO(DBService database) {
		this.db = database;
	}
	
	public DocumentBean get (DocumentBeanType type, UUID id) throws SQLException {
        try (PreparedStatement cons = this.db.getConnection().
                prepareStatement("SELECT * FROM " + type.getName() + " where uuid = ? ")) {

            cons.setString(1, id.toString());

            ResultSet res = cons.executeQuery();

            if(res.next()){
            	DocumentBeanInfo info = new DocumentBeanInfo();
            	info.setID(id);
            	info.setType(type);
            	
            	if (type == DocumentBeanType.XSLT) {
            		info.setXsd(UUID.fromString(res.getString("xsd")));
            	}
            	
            	DocumentBean docb = new DocumentBean();
            	docb.setInfo(info);
            	docb.setContent(res.getString("content"));
            	
                return docb;
            }
        }

        return null;
    }

    public boolean add (DocumentBeanType type, DocumentBean elem) throws SQLException {
    	String query = "";
        if (type == DocumentBeanType.XSLT) {
            query = "INSERT INTO " + type.getName() + " (uuid, content, xsd) VALUES ( ? , ? , ? )";
        }
        else {
        	query = "INSERT INTO " + type.getName() + " (uuid, content) VALUES ( ? , ? )";
        }

        try (PreparedStatement cons = this.db.getConnection().
                prepareStatement(query)) {

            cons.setString(1, elem.getInfo().getID().toString());
            cons.setString(2, elem.getContent());

            if (type == DocumentBeanType.XSLT) {
                cons.setString(3, elem.getInfo().getXsd().toString());
            }

            cons.executeUpdate();

            return true;
        }
    }

    public boolean delete(DocumentBeanType type, UUID id) throws SQLException {
    	// Borramos el documento
        try (PreparedStatement cons = this.db.getConnection().
                prepareStatement("DELETE FROM " + type.getName() + " WHERE uuid = ? ")) {

            cons.setString(1, id.toString());

            cons.executeUpdate();
        }
        
        // Si es un documento XSD, borramos los XSLT asociados
        try {
	        if (type == DocumentBeanType.XSD) {
	        	for (DocumentBeanInfo infoxslt : this.getList(DocumentBeanType.XSLT)) {
	        		if (infoxslt.getXsd() == id) {
	        			this.delete(DocumentBeanType.XSLT, infoxslt.getID());
        			}
        		}
        	}
        }
		catch (SQLException e) {
			// El borrado es opcional, si falla no pasa nada.
		}
        
        return true;
    }

    public List<DocumentBeanInfo> getList(DocumentBeanType type) throws SQLException {
        String query = "";
        if (type == DocumentBeanType.XSLT) {
            query = "SELECT uuid, xsd FROM " + type.getName();
        }
        else {
            query = "SELECT uuid FROM " + type.getName();
        }

        try (PreparedStatement cons = this.db.getConnection().
                prepareStatement(query)) {

            ResultSet res = cons.executeQuery();

            List<DocumentBeanInfo> toret = new ArrayList<>();
            while (res.next()) {
            	DocumentBeanInfo info = new DocumentBeanInfo();
            	
            	info.setID(UUID.fromString(res.getString("uuid")));
            	info.setType(type);
        		if (type == DocumentBeanType.XSLT) {
        			info.setXsd(UUID.fromString(res.getString("xsd")));
        		}

                toret.add(info);
            }

            return toret;
        }
    }

    public int length(DocumentBeanType type) throws SQLException {
        try (PreparedStatement cons = this.db.getConnection().
                prepareStatement("SELECT COUNT(*) FROM " + type.getName())) {

            ResultSet res = cons.executeQuery();

            while (res.next()) {
                return (res.getInt(1));
            }
        }

        return 0;
    }

    public boolean contains(DocumentBeanType type, UUID id) throws SQLException {
        return this.get(type, id) != null ? true : false;
    }
}
