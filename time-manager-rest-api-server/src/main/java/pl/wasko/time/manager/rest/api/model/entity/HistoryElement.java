/*
 * Created on 2020-07-10 ( Time 09:27:29 )
 * Generated by Telosys Tools Generator ( version 3.1.2 )
 */
// This Bean has a basic Primary Key (not composite) 

package pl.wasko.time.manager.rest.api.model.entity;

import pl.wasko.time.manager.rest.api.model.enumeration.HistoryElementEnum;

import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;

import java.util.List;

import javax.persistence.*;

/**
 * Persistent class for entity stored in table "history_elements"
 *
 * @author Telosys Tools Generator
 *
 */

@Entity
@Table(name="history_elements")
// Define named queries here
@NamedQueries ( {
  @NamedQuery ( name="HistoryElements.countAll", query="SELECT COUNT(x) FROM HistoryElement x" )
} )
public class HistoryElement implements Serializable
{
    private static final long serialVersionUID = 1L;

    //----------------------------------------------------------------------
    // ENTITY PRIMARY KEY ( BASED ON A SINGLE FIELD )
    //----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id_element", nullable=false)
    private Integer    idElement    ;


    //----------------------------------------------------------------------
    // ENTITY DATA FIELDS 
    //----------------------------------------------------------------------    
    @Column(name="element_name", nullable=false, length=255)
    @Enumerated(EnumType.STRING)
    private HistoryElementEnum elementName  ;



    //----------------------------------------------------------------------
    // ENTITY LINKS ( RELATIONSHIP )
    //----------------------------------------------------------------------
    @OneToMany(mappedBy="historyElement", targetEntity=History.class)
    private List<History> listOfHistory;


    //----------------------------------------------------------------------
    // CONSTRUCTOR(S)
    //----------------------------------------------------------------------
    public HistoryElement()
    {
		super();
    }
    
    //----------------------------------------------------------------------
    // GETTER & SETTER FOR THE KEY FIELD
    //----------------------------------------------------------------------
    public void setIdElement( Integer idElement )
    {
        this.idElement = idElement ;
    }
    public Integer getIdElement()
    {
        return this.idElement;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    //--- DATABASE MAPPING : element_name ( VARCHAR ) 
    public void setElementName( HistoryElementEnum elementName )
    {
        this.elementName = elementName;
    }
    public HistoryElementEnum getElementName()
    {
        return this.elementName;
    }


    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR LINKS
    //----------------------------------------------------------------------
    public void setListOfHistory( List<History> listOfHistory )
    {
        this.listOfHistory = listOfHistory;
    }
    public List<History> getListOfHistory()
    {
        return this.listOfHistory;
    }


    //----------------------------------------------------------------------
    // toString METHOD
    //----------------------------------------------------------------------
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append("["); 
        sb.append(idElement);
        sb.append("]:"); 
        sb.append(elementName);
        return sb.toString(); 
    } 

}