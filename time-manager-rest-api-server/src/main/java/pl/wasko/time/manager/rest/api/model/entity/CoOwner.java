/*
 * Created on 2020-07-10 ( Time 09:27:28 )
 * Generated by Telosys Tools Generator ( version 3.1.2 )
 */
// This Bean has a composite Primary Key  


package pl.wasko.time.manager.rest.api.model.entity;

import org.hibernate.annotations.Where;

import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;


import javax.persistence.*;

/**
 * Persistent class for entity stored in table "co_owners"
 *
 * @author Telosys Tools Generator
 *
 */

@Entity
@Table(name="co_owners")
// Define named queries here
@NamedQueries ( {
  @NamedQuery ( name="CoOwners.countAll", query="SELECT COUNT(x) FROM CoOwner x" )
} )
public class CoOwner implements Serializable
{
    private static final long serialVersionUID = 1L;

    //----------------------------------------------------------------------
    // ENTITY PRIMARY KEY ( EMBEDDED IN AN EXTERNAL CLASS )  
    //----------------------------------------------------------------------
	@EmbeddedId
    private CoOwnerKey compositePrimaryKey ;


    //----------------------------------------------------------------------
    // ENTITY DATA FIELDS 
    //----------------------------------------------------------------------    
    @Column(name="deleted", nullable=false)
    private Boolean    deleted      ;



    //----------------------------------------------------------------------
    // ENTITY LINKS ( RELATIONSHIP )
    //----------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name="bucket_id", referencedColumnName="id_bucket", insertable=false, updatable=false)
    private Bucket bucket;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id_user", insertable=false, updatable=false)
    private User user;


    //----------------------------------------------------------------------
    // CONSTRUCTOR(S)
    //----------------------------------------------------------------------
    public CoOwner()
    {
		super();
		this.compositePrimaryKey = new CoOwnerKey();
    }
    
    //----------------------------------------------------------------------
    // GETTER & SETTER FOR THE COMPOSITE KEY 
    //----------------------------------------------------------------------

    public Integer getUserId()
    {
        return this.compositePrimaryKey.getUserId() ;
    }

    public Integer getBucketId()
    {
        return this.compositePrimaryKey.getBucketId() ;
    }


    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    //--- DATABASE MAPPING : deleted ( BIT ) 
    public void setDeleted( Boolean deleted )
    {
        this.deleted = deleted;
    }
    public Boolean getDeleted()
    {
        return this.deleted;
    }


    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR LINKS
    //----------------------------------------------------------------------
    public void setBucket(Bucket bucket)
    {
        this.bucket = bucket;
        this.compositePrimaryKey.setBucketId(bucket.getIdBucket());
    }
    public Bucket getBucket()
    {
        return this.bucket;
    }

    public void setUser(User user)
    {
        this.user = user;
        this.compositePrimaryKey.setUserId(user.getIdUser());
    }
    public User getUser()
    {
        return this.user;
    }


    //----------------------------------------------------------------------
    // toString METHOD
    //----------------------------------------------------------------------
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append("["); 
        if ( compositePrimaryKey != null ) {  
            sb.append(compositePrimaryKey.toString());  
        }  
        else {  
            sb.append( "(null-key)" ); 
        }  
        sb.append("]:"); 
        sb.append(deleted);
        return sb.toString(); 
    } 

}
