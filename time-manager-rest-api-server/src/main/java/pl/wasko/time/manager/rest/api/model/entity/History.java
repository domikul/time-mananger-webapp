/*
 * Created on 2020-07-10 ( Time 09:27:29 )
 * Generated by Telosys Tools Generator ( version 3.1.2 )
 */
// This Bean has a basic Primary Key (not composite) 

package pl.wasko.time.manager.rest.api.model.entity;

import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;

import java.util.Date;

import javax.persistence.*;

/**
 * Persistent class for entity stored in table "history"
 *
 * @author Telosys Tools Generator
 *
 */

@Entity
@Table(name="history")
// Define named queries here
@NamedQueries ( {
  @NamedQuery ( name="History.countAll", query="SELECT COUNT(x) FROM History x" )
} )
public class History implements Serializable
{
    private static final long serialVersionUID = 1L;

    //----------------------------------------------------------------------
    // ENTITY PRIMARY KEY ( BASED ON A SINGLE FIELD )
    //----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id_history", nullable=false)
    private Integer    idHistory    ;


    //----------------------------------------------------------------------
    // ENTITY DATA FIELDS 
    //----------------------------------------------------------------------    
    @Column(name="modified_element_id", nullable=false)
    private Integer    modifiedElementId ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modification_time", nullable=false)
    private Date       modificationTime ;

	// "modificatorId" (column "modificator_id") is not defined by itself because used as FK in a link 
	// "typeId" (column "type_id") is not defined by itself because used as FK in a link 


    //----------------------------------------------------------------------
    // ENTITY LINKS ( RELATIONSHIP )
    //----------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name="modificator_id", referencedColumnName="id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name="type_id", referencedColumnName="id_element")
    private HistoryElement historyElement;


    @ManyToOne
    @JoinColumn(name="operation_type_id", referencedColumnName="id_operation_type")
    private OperationType operationType;


    //----------------------------------------------------------------------
    // CONSTRUCTOR(S)
    //----------------------------------------------------------------------
    public History()
    {
		super();
    }
    
    //----------------------------------------------------------------------
    // GETTER & SETTER FOR THE KEY FIELD
    //----------------------------------------------------------------------
    public void setIdHistory( Integer idHistory )
    {
        this.idHistory = idHistory ;
    }
    public Integer getIdHistory()
    {
        return this.idHistory;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    //--- DATABASE MAPPING : modified_element_id ( INT ) 
    public void setModifiedElementId( Integer modifiedElementId )
    {
        this.modifiedElementId = modifiedElementId;
    }
    public Integer getModifiedElementId()
    {
        return this.modifiedElementId;
    }

    //--- DATABASE MAPPING : modification_time ( DATETIME ) 
    public void setModificationTime( Date modificationTime )
    {
        this.modificationTime = modificationTime;
    }
    public Date getModificationTime()
    {
        return this.modificationTime;
    }


    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR LINKS
    //----------------------------------------------------------------------
    public void setUser(User user)
    {
        this.user = user;
    }
    public User getUser()
    {
        return this.user;
    }

    public void setHistoryElement(HistoryElement historyElement)
    {
        this.historyElement = historyElement;
    }
    public HistoryElement getHistoryElement()
    {
        return this.historyElement;
    }


    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }


    //----------------------------------------------------------------------
    // toString METHOD
    //----------------------------------------------------------------------
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append("["); 
        sb.append(idHistory);
        sb.append("]:"); 
        sb.append(modifiedElementId);
        sb.append("|");
        sb.append(modificationTime);
        return sb.toString(); 
    } 

}
