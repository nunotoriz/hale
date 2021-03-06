//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.16 at 10:55:30 AM CEST 
//


package eu.esdihumboldt.hale.common.align.io.impl.internal.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for CellType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CellType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="source" type="{http://www.esdi-humboldt.eu/hale/alignment}NamedEntityType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="target" type="{http://www.esdi-humboldt.eu/hale/alignment}NamedEntityType" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.esdi-humboldt.eu/hale/alignment}AbstractParameter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="documentation" type="{http://www.esdi-humboldt.eu/hale/alignment}DocumentationType"/>
 *           &lt;element name="annotation" type="{http://www.esdi-humboldt.eu/hale/alignment}AnnotationType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="relation" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="priority" type="{http://www.esdi-humboldt.eu/hale/alignment}PriorityType" default="normal" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CellType", propOrder = {
    "source",
    "target",
    "abstractParameter",
    "documentationOrAnnotation"
})
public class CellType {

    protected List<NamedEntityType> source;
    @XmlElement(required = true)
    protected List<NamedEntityType> target;
    @XmlElementRef(name = "AbstractParameter", namespace = "http://www.esdi-humboldt.eu/hale/alignment", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractParameterType>> abstractParameter;
    @XmlElements({
        @XmlElement(name = "annotation", type = AnnotationType.class),
        @XmlElement(name = "documentation", type = DocumentationType.class)
    })
    protected List<Object> documentationOrAnnotation;
    @XmlAttribute(required = true)
    protected String relation;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute
    protected PriorityType priority;

    /**
     * Gets the value of the source property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the source property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NamedEntityType }
     * 
     * 
     */
    public List<NamedEntityType> getSource() {
        if (source == null) {
            source = new ArrayList<NamedEntityType>();
        }
        return this.source;
    }

    /**
     * Gets the value of the target property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the target property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTarget().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NamedEntityType }
     * 
     * 
     */
    public List<NamedEntityType> getTarget() {
        if (target == null) {
            target = new ArrayList<NamedEntityType>();
        }
        return this.target;
    }

    /**
     * Gets the value of the abstractParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ComplexParameterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractParameterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ParameterType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractParameterType>> getAbstractParameter() {
        if (abstractParameter == null) {
            abstractParameter = new ArrayList<JAXBElement<? extends AbstractParameterType>>();
        }
        return this.abstractParameter;
    }

    /**
     * Gets the value of the documentationOrAnnotation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentationOrAnnotation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentationOrAnnotation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AnnotationType }
     * {@link DocumentationType }
     * 
     * 
     */
    public List<Object> getDocumentationOrAnnotation() {
        if (documentationOrAnnotation == null) {
            documentationOrAnnotation = new ArrayList<Object>();
        }
        return this.documentationOrAnnotation;
    }

    /**
     * Gets the value of the relation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelation() {
        return relation;
    }

    /**
     * Sets the value of the relation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelation(String value) {
        this.relation = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link PriorityType }
     *     
     */
    public PriorityType getPriority() {
        if (priority == null) {
            return PriorityType.NORMAL;
        } else {
            return priority;
        }
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link PriorityType }
     *     
     */
    public void setPriority(PriorityType value) {
        this.priority = value;
    }

}
