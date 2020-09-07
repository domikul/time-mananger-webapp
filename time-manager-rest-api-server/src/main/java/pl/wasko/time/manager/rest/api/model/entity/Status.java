/*
 * Created on 2020-07-10 ( Time 09:27:29 )
 * Generated by Telosys Tools Generator ( version 3.1.2 )
 */
// This Bean has a basic Primary Key (not composite) 

package pl.wasko.time.manager.rest.api.model.entity;

import pl.wasko.time.manager.rest.api.model.enumeration.StatusEnum;

import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;

import java.util.List;

import javax.persistence.*;


/**
 * Persistent class for entity stored in table "statuses"
 *
 * @author Telosys Tools Generator
 *
 */

@Entity
@Table(name="statuses")
// Define named queries here
@NamedQueries ( {
  @NamedQuery ( name="Statuses.countAll", query="SELECT COUNT(x) FROM Status x" )
} )
public class Status implements Serializable
{
    private static final long serialVersionUID = 1L;

    //----------------------------------------------------------------------
    // ENTITY PRIMARY KEY ( BASED ON A SINGLE FIELD )
    //----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id_status", nullable=false)
    private Integer    idStatus     ;


    //----------------------------------------------------------------------
    // ENTITY DATA FIELDS 
    //----------------------------------------------------------------------    
    @Column(name="status_name", nullable=false, length=255)
    @Enumerated(EnumType.STRING)
    private StatusEnum statusName   ;



    //----------------------------------------------------------------------
    // ENTITY LINKS ( RELATIONSHIP )
    //----------------------------------------------------------------------
    @OneToMany(mappedBy="status", targetEntity= Task.class)
    private List<Task> listOfTasks ;


    //----------------------------------------------------------------------
    // CONSTRUCTOR(S)
    //----------------------------------------------------------------------
    public Status()
    {
		super();
    }
    
    //----------------------------------------------------------------------
    // GETTER & SETTER FOR THE KEY FIELD
    //----------------------------------------------------------------------
    public void setIdStatus( Integer idStatus )
    {
        this.idStatus = idStatus ;
    }
    public Integer getIdStatus()
    {
        return this.idStatus;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    //--- DATABASE MAPPING : status_name ( VARCHAR ) 
    public void setStatusName( StatusEnum statusName )
    {
        this.statusName = statusName;
    }
    public StatusEnum getStatusName()
    {
        return this.statusName;
    }


    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR LINKS
    //----------------------------------------------------------------------
    public void setListOfTasks( List<Task> listOfTasks )
    {
        this.listOfTasks = listOfTasks;
    }
    public List<Task> getListOfTasks()
    {
        return this.listOfTasks;
    }


    //----------------------------------------------------------------------
    // toString METHOD
    //----------------------------------------------------------------------
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append("["); 
        sb.append(idStatus);
        sb.append("]:"); 
        sb.append(statusName);
        return sb.toString(); 
    } 

}
