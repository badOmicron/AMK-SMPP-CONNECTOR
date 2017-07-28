/*
 *      File: PDUOperationProperties.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.smpp.Data;
import org.smpp.pdu.Address;

/**
 * Mandatory SMPP Parameters.
 * Some of the variables necessary to be able to communicate through the SMPP protocol.
 * For information about these variables have a look in SMPP 3.4 specification.
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @see <a href="http://opensmpp.org/specs/SMPP_v3_4_Issue1_2.pdf">SMPP 3.4 specification</a>
 * @since 1.0.0
 */
public class PDUOperationProperties {

    /**
     * Identifies the type of ESME system requesting to bind as a transmitter with the MC.
     */
    private String systemType  = Data.DFLT_SYSTYPE;
    /**
     * The service_type parameter can be used to indicate the SMS Application service associated with the message. Specifying the
     service_type allows the ESME to avail of enhanced messaging services such as “replace by service_type” or to control
     the teleservice used on the air interface. Set to NULL for default MC settings. <br/>
     The service type can be empty or one of the following values: <br/>
     <code>CMT, CPT, VMN, VMA, WAP or USSD</code>
     */
    private String serviceType = Data.DFLT_SRVTYPE;
    /**
     * Address of alert SME.
     */
    private Address   sourceAddress;
    /**
     * Type of Number for destination.
     */
    private Address[] destAddress;
    /**
     * The short message is to be scheduled by the MC for delivery. Set to NULL for immediate message delivery.
     */
    private Date   scheduleDeliveryTime = new Date();
    /**
     * The validity period of this message. Set to NULL to request the MC default validity period. Must be a number.
     */
    private String validityPeriod       = Data.DFLT_VALIDITY;
    /**
     * Number of destination addresses – indicates the number of destinations that are to follow.
     * A maximum of 255 destinationaddresses are allowed.
     */
    private int    numberOfDestination  = 1;
    /**
     * Indicates Message Mode and Message Type.
     * • Default Message Mode - value: 0
     * This can be any of the following three modes, but usually maps to “Store and  Forward” mode <br/>
     * • Datagram Mode - value: 1
     * Message delivery is attempted only once <br/>
     * • Transaction Mode
     * Message delivery is attempted once, with the submit_sm_resp or data_sm_resp <br/>
     * delayed until the delivery attempt has been made.
     * • Store and Forward Mode - value: 3
     * The message is stored in the MC message database. <br/>
     */
    private byte   esmClass             = Data.DFLT_ESM_CLASS;
    /**
     * Protocol Identifier. Network specific field;<br/>
     *  - GSM  : 0 <br/>
     *  - TDMA : 1 <br/>
     *  - CDMA : 2 <br/>
     */
    private byte   protocolId           = Data.DFLT_PROTOCOLID;
    /**
     *  Designates the priority level of the message.
     *  0 = Level 0 (lowest) priority <br/>
     *  1 = Level 1 priority <br/>
     *  2 = Level 2 priority <br/>
     *  3 = Level 3 (highest) priority <br/>
     * >3=Reserved <br/>
     */
    private byte   priorityFlag         = Data.DFLT_PRIORITY_FLAG;
    /**
     * Indicates if an ESMEacknowledgement is required. <br/>
     * Note: A delivery receipt is returned only when the message has reached a non-delivered
     * final state such as cancelled or undeliverable, etc.
     */
    private byte   registeredDelivery   = Data.DFLT_REG_DELIVERY;
    /**
     * used to request the SMSC to replace a previously submitted message, that is still pending delivery.<br/>
     * The SMSC will replace an existing message provided that the source address, destination address and
     * service_type match the same fields in the new message. <br/>
     * - 0 Don’t replace (default) <br/>
     * - 1 Replace <br/>
     * - 2 - 255 reserved <br/>
     */
    private byte   replaceIfPresentFlag = Data.DFTL_REPLACE_IFP;
    /**
     * Defines the encoding scheme of the short message user data. <br/>
     */
    private byte   dataCoding           = Data.DFLT_DATA_CODING;
    /**
     * Indicates the short message to send from a list of predefined (‘canned’) short messages stored on the SMSC.
     * If not using an SMSC canned message, set to NULL.
     */
    private byte   smDefaultMsgId       = Data.DFLT_DFLTMSGID;


    private long receiveTimeout = Data.RECEIVE_BLOCKING;

    /**
     * Creates an instance of PDUOperationProperties.
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     */
    public PDUOperationProperties() {
        super();
    }

    /**
     * Creates an instance of PDUOperationProperties.
     * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
     * @param propsBuilder The Property Builder.
     */
    PDUOperationProperties(final PDUOperationPropertiesBuilder propsBuilder) {
        this.systemType = propsBuilder.getSystemType();
        this.serviceType = propsBuilder.getServiceType();
        this.sourceAddress = propsBuilder.getSourceAddress();
        this.destAddress = propsBuilder.getDestAddress();
        this.scheduleDeliveryTime = propsBuilder.getScheduleDeliveryTime();
        this.validityPeriod = propsBuilder.getValidityPeriod();
        this.numberOfDestination = propsBuilder.getNumberOfDestination();
        this.esmClass = propsBuilder.getEsmClass();
        this.protocolId = propsBuilder.getProtocolId();
        this.priorityFlag = propsBuilder.getPriorityFlag();
        this.registeredDelivery = propsBuilder.getRegisteredDelivery();
        this.replaceIfPresentFlag = propsBuilder.getReplaceIfPresentFlag();
        this.dataCoding = propsBuilder.getDataCoding();
        this.smDefaultMsgId = propsBuilder.getSmDefaultMsgId();
    }

    /**
     * Getter for systemType.
     * @return systemType.
     **/
    public String getSystemType() {
        return systemType;
    }

    /**
     * Setter for systemType.
     * @param systemType expected.
     **/
    public void setSystemType(final String systemType) {
        this.systemType = systemType;
    }

    /**
     * Getter for serviceType.
     * @return serviceType.
     **/
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Setter for serviceType.
     * @param serviceType expected.
     **/
    public void setServiceType(final String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Getter for sourceAddress.
     * @return sourceAddress.
     **/
    public Address getSourceAddress() {
        return sourceAddress;
    }

    /**
     * Setter for sourceAddress.
     * @param sourceAddress expected.
     **/
    public void setSourceAddress(final Address sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    /**
     * Getter for destAddress.
     * @return destAddress.
     **/
    public Address[] getDestAddress() {
        return destAddress;
    }

    /**
     * Setter for destAddress.
     * @param destAddress expected.
     **/
    public void setDestAddress(final Address[] destAddress) {
        this.destAddress = destAddress;
    }

    /**
     * Getter for scheduleDeliveryTime.
     * @return scheduleDeliveryTime.
     **/
    public Date getScheduleDeliveryTime() {
        return scheduleDeliveryTime;
    }

    /**
     * Setter for scheduleDeliveryTime.
     * @param scheduleDeliveryTime expected.
     **/
    public void setScheduleDeliveryTime(final Date scheduleDeliveryTime) {
        this.scheduleDeliveryTime = scheduleDeliveryTime;
    }

    /**
     * Getter for validityPeriod.
     * @return validityPeriod.
     **/
    public String getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * Setter for validityPeriod.
     * @param validityPeriod expected.
     **/
    public void setValidityPeriod(final String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    /**
     * Getter for numberOfDestination.
     * @return numberOfDestination.
     **/
    public int getNumberOfDestination() {
        return numberOfDestination;
    }

    /**
     * Setter for numberOfDestination.
     * @param numberOfDestination expected.
     **/
    public void setNumberOfDestination(final int numberOfDestination) {
        this.numberOfDestination = numberOfDestination;
    }

    /**
     * Getter for esmClass.
     * @return esmClass.
     **/
    public byte getEsmClass() {
        return esmClass;
    }

    /**
     * Setter for esmClass.
     * @param esmClass expected.
     **/
    public void setEsmClass(final byte esmClass) {
        this.esmClass = esmClass;
    }

    /**
     * Getter for protocolId.
     * @return protocolId.
     **/
    public byte getProtocolId() {
        return protocolId;
    }

    /**
     * Setter for protocolId.
     * @param protocolId expected.
     **/
    public void setProtocolId(final byte protocolId) {
        this.protocolId = protocolId;
    }

    /**
     * Getter for priorityFlag.
     * @return priorityFlag.
     **/
    public byte getPriorityFlag() {
        return priorityFlag;
    }

    /**
     * Setter for priorityFlag.
     * @param priorityFlag expected.
     **/
    public void setPriorityFlag(final byte priorityFlag) {
        this.priorityFlag = priorityFlag;
    }

    /**
     * Getter for registeredDelivery.
     * @return registeredDelivery.
     **/
    public byte getRegisteredDelivery() {
        return registeredDelivery;
    }

    /**
     * Setter for registeredDelivery.
     * @param registeredDelivery expected.
     **/
    public void setRegisteredDelivery(final byte registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    /**
     * Getter for replaceIfPresentFlag.
     * @return replaceIfPresentFlag.
     **/
    public byte getReplaceIfPresentFlag() {
        return replaceIfPresentFlag;
    }

    /**
     * Setter for replaceIfPresentFlag.
     * @param replaceIfPresentFlag expected.
     **/
    public void setReplaceIfPresentFlag(final byte replaceIfPresentFlag) {
        this.replaceIfPresentFlag = replaceIfPresentFlag;
    }

    /**
     * Getter for dataCoding.
     * @return dataCoding.
     **/
    public byte getDataCoding() {
        return dataCoding;
    }

    /**
     * Setter for dataCoding.
     * @param dataCoding expected.
     **/
    public void setDataCoding(final byte dataCoding) {
        this.dataCoding = dataCoding;
    }

    /**
     * Getter for smDefaultMsgId.
     * @return smDefaultMsgId.
     **/
    public byte getSmDefaultMsgId() {
        return smDefaultMsgId;
    }

    /**
     * Setter for smDefaultMsgId.
     * @param smDefaultMsgId expected.
     **/
    public void setSmDefaultMsgId(final byte smDefaultMsgId) {
        this.smDefaultMsgId = smDefaultMsgId;
    }

    /**
     * Getter for receiveTimeout.
     * @return receiveTimeout.
     **/
    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    /**
     *  * Setter for receiveTimeout.
     * @param receiveTimeout expected.
     **/
    public void setReceiveTimeout(final long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }


    /* La documentación de este método se encuentra en la clase o interface que
    * lo declara (non-Javadoc)
    * @see java.lang.Object#toString()
    */
    @Override
    public String toString() {
        final ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
        return builder.build();
    }


}
