//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.12.13 at 07:05:55 PM EET 
//


package soapServices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idActivity" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="recommendation" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "idActivity",
    "recommendation"
})
@XmlRootElement(name = "recommendationRequest")
public class RecommendationRequest {

    @XmlElement(required = true)
    protected String idActivity;
    @XmlElement(required = true)
    protected String recommendation;

    /**
     * Gets the value of the idActivity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdActivity() {
        return idActivity;
    }

    /**
     * Sets the value of the idActivity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdActivity(String value) {
        this.idActivity = value;
    }

    /**
     * Gets the value of the recommendation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecommendation() {
        return recommendation;
    }

    /**
     * Sets the value of the recommendation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecommendation(String value) {
        this.recommendation = value;
    }

}
