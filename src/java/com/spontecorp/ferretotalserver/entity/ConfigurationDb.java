/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sponte03
 */
@Entity
@Table(name = "configuration_db")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfigurationDb.findAll", query = "SELECT c FROM ConfigurationDb c"),
    @NamedQuery(name = "ConfigurationDb.findById", query = "SELECT c FROM ConfigurationDb c WHERE c.id = :id"),
    @NamedQuery(name = "ConfigurationDb.findByDbName", query = "SELECT c FROM ConfigurationDb c WHERE c.dbName = :dbName"),
    @NamedQuery(name = "ConfigurationDb.findByDbuserName", query = "SELECT c FROM ConfigurationDb c WHERE c.dbuserName = :dbuserName"),
    @NamedQuery(name = "ConfigurationDb.findByDbPassword", query = "SELECT c FROM ConfigurationDb c WHERE c.dbPassword = :dbPassword"),
    @NamedQuery(name = "ConfigurationDb.findByPathMysqldump", query = "SELECT c FROM ConfigurationDb c WHERE c.pathMysqldump = :pathMysqldump"),
    @NamedQuery(name = "ConfigurationDb.findByPathMysql", query = "SELECT c FROM ConfigurationDb c WHERE c.pathMysql = :pathMysql")})

public class ConfigurationDb implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "db_name")
    private String dbName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "db_userName")
    private String dbuserName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "db_password")
    private String dbPassword;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "path_mysqldump")
    private String pathMysqldump;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "path_mysql")
    private String pathMysql;

    public ConfigurationDb() {
    }

    public ConfigurationDb(Integer id) {
        this.id = id;
    }

    public ConfigurationDb(Integer id, String dbName, String dbuserName, String dbPassword, String pathMysqldump, String pathMysql) {
        this.id = id;
        this.dbName = dbName;
        this.dbuserName = dbuserName;
        this.dbPassword = dbPassword;
        this.pathMysqldump = pathMysqldump;
        this.pathMysql = pathMysql;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbuserName() {
        return dbuserName;
    }

    public void setDbuserName(String dbuserName) {
        this.dbuserName = dbuserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getPathMysqldump() {
        return pathMysqldump;
    }

    public void setPathMysqldump(String pathMysqldump) {
        this.pathMysqldump = pathMysqldump;
    }

    public String getPathMysql() {
        return pathMysql;
    }

    public void setPathMysql(String pathMysql) {
        this.pathMysql = pathMysql;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConfigurationDb)) {
            return false;
        }
        ConfigurationDb other = (ConfigurationDb) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.ferreasesor.entity.ConfigurationDb[ id=" + id + " ]";
    }
    
}
