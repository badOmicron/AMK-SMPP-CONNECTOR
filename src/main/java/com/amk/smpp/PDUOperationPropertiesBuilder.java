/*
 *      File: PDUOperationPropertiesBuilder.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp;

import java.util.Date;

import org.smpp.Data;
import org.smpp.pdu.Address;

/**
 * PDU Operation Property Builder.<br/><br/>
 * <b>Pattern:  </b> <code>Builder</code>.
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @see  PDUOperationProperties
 * @since 1.0.0
 */
public class PDUOperationPropertiesBuilder {

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
    private byte esmClass             = Data.DFLT_ESM_CLASS;
    /**
     * Protocol Identifier. Network specific field;<br/>
     *  - GSM  : 0 <br/>
     *  - TDMA : 1 <br/>
     *  - CDMA : 2 <br/>
     */
    private byte protocolId           = Data.DFLT_PROTOCOLID;
    /**
     *  Designates the priority level of the message.
     *  0 = Level 0 (lowest) priority <br/>
     *  1 = Level 1 priority <br/>
     *  2 = Level 2 priority <br/>
     *  3 = Level 3 (highest) priority <br/>
     * >3=Reserved <br/>
     */
    private byte priorityFlag         = Data.DFLT_PRIORITY_FLAG;
    /**
     * Indicates if an ESMEacknowledgement is required. <br/>
     * Note: A delivery receipt is returned only when the message has reached a non-delivered
     * final state such as cancelled or undeliverable, etc.
     */
    private byte registeredDelivery   = Data.DFLT_REG_DELIVERY;
    /**
     * used to request the SMSC to replace a previously submitted message, that is still pending delivery.<br/>
     * The SMSC will replace an existing message provided that the source address, destination address and
     * service_type match the same fields in the new message. <br/>
     * - 0 Don’t replace (default) <br/>
     * - 1 Replace <br/>
     * - 2 - 255 reserved <br/>
     */
    private byte replaceIfPresentFlag = Data.DFTL_REPLACE_IFP;
    /**
     * Defines the encoding scheme of the short message user data. <br/>
     */
    private byte dataCoding           = Data.DFLT_DATA_CODING;
    /**
     * Indicates the short message to send from a list of predefined (‘canned’) short messages stored on the SMSC.
     * If not using an SMSC canned message, set to NULL.
     */
    private byte smDefaultMsgId       = Data.DFLT_DFLTMSGID;

    private long receiveTimeout = Data.RECEIVE_BLOCKING;

    /**
     * Getter for systemType.
     * @return systemType.
     **/
    String getSystemType() {
        return systemType;
    }

    /**
     * Getter for serviceType.
     * @return serviceType.
     **/
    String getServiceType() {
        return serviceType;
    }

    /**
     * Getter for sourceAddress.
     * @return sourceAddress.
     **/
    Address getSourceAddress() {
        return sourceAddress;
    }

    /**
     * Getter for destAddress.
     * @return destAddress.
     **/
    Address[] getDestAddress() {
        return destAddress;
    }

    /**
     * Getter for scheduleDeliveryTime.
     * @return scheduleDeliveryTime.
     **/
    Date getScheduleDeliveryTime() {
        return scheduleDeliveryTime;
    }

    /**
     * Getter for validityPeriod.
     * @return validityPeriod.
     **/
    String getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * Getter for numberOfDestination.
     * @return numberOfDestination.
     **/
    int getNumberOfDestination() {
        return numberOfDestination;
    }


    /**
     * Getter for esmClass.
     * @return esmClass.
     **/
    byte getEsmClass() {
        return esmClass;
    }

    /**
     * Getter for protocolId.
     * @return protocolId.
     **/
    byte getProtocolId() {
        return protocolId;
    }

    /**
     * Getter for priorityFlag.
     * @return priorityFlag.
     **/
    byte getPriorityFlag() {
        return priorityFlag;
    }

    /**
     * Getter for registeredDelivery.
     * @return registeredDelivery.
     **/
    byte getRegisteredDelivery() {
        return registeredDelivery;
    }

    /**
     * Getter for replaceIfPresentFlag.
     * @return replaceIfPresentFlag.
     **/
    byte getReplaceIfPresentFlag() {
        return replaceIfPresentFlag;
    }

    /**
     * Getter for dataCoding.
     * @return dataCoding.
     **/
    byte getDataCoding() {
        return dataCoding;
    }

    /**
     * Getter for smDefaultMsgId.
     * @return smDefaultMsgId.
     **/
    public byte getSmDefaultMsgId() {
        return smDefaultMsgId;
    }

    /**
     * Getter for receiveTimeout.
     * @return receiveTimeout.
     **/
    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    /**
     * Setter for systemType.
     * @param systemType expected.
     **/
    public PDUOperationPropertiesBuilder setSystemType(final String systemType) {
        this.systemType = systemType;
        return this;
    }

    /**
     * Setter for serviceType.
     * @param serviceType expected.
     **/
    public PDUOperationPropertiesBuilder setServiceType(final String serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    /**
     * Setter for sourceAddress.
     * @param sourceAddress expected.
     **/
    public PDUOperationPropertiesBuilder setSourceAddress(final Address sourceAddress) {
        this.sourceAddress = sourceAddress;
        return this;
    }

    /**
     * Setter for destAddress.
     * @param destAddress expected.
     **/
    public PDUOperationPropertiesBuilder setDestAddress(final Address[] destAddress) {
        this.destAddress = destAddress;
        return this;
    }

    /**
     * Setter for scheduleDeliveryTime.
     * @param scheduleDeliveryTime expected.
     **/
    public PDUOperationPropertiesBuilder setScheduleDeliveryTime(final Date scheduleDeliveryTime) {
        this.scheduleDeliveryTime = scheduleDeliveryTime;
        return this;
    }

    /**
     * Setter for validityPeriod.
     * @param validityPeriod expected.
     **/
    public PDUOperationPropertiesBuilder setValidityPeriod(final String validityPeriod) {
        this.validityPeriod = validityPeriod;
        return this;
    }

    /**
     * Setter for numberOfDestination.
     * @param numberOfDestination expected.
     **/
    public PDUOperationPropertiesBuilder setNumberOfDestination(final int numberOfDestination) {
        this.numberOfDestination = numberOfDestination;
        return this;
    }

    /**
     * Setter for esmClass.
     * @param esmClass expected.
     **/
    public PDUOperationPropertiesBuilder setEsmClass(final byte esmClass) {
        this.esmClass = esmClass;
        return this;
    }

    /**
     * Setter for protocolId.
     * @param protocolId expected.
     **/
    public PDUOperationPropertiesBuilder setProtocolId(final byte protocolId) {
        this.protocolId = protocolId;
        return this;
    }

    /**
     * Setter for priorityFlag.
     * @param priorityFlag expected.
     **/
    public PDUOperationPropertiesBuilder setPriorityFlag(final byte priorityFlag) {
        this.priorityFlag = priorityFlag;
        return this;
    }

    /**
     * Setter for registeredDelivery.
     * @param registeredDelivery expected.
     **/
    public PDUOperationPropertiesBuilder setRegisteredDelivery(final byte registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
        return this;
    }

    /**
     * Setter for replaceIfPresentFlag.
     * @param replaceIfPresentFlag expected.
     **/
    public PDUOperationPropertiesBuilder setReplaceIfPresentFlag(final byte replaceIfPresentFlag) {
        this.replaceIfPresentFlag = replaceIfPresentFlag;
        return this;
    }

    /**
     * Setter for dataCoding.
     * @param dataCoding expected.
     **/
    public PDUOperationPropertiesBuilder setDataCoding(final byte dataCoding) {
        this.dataCoding = dataCoding;
        return this;
    }

    /**
     * Setter for smDefaultMsgId.
     * @param smDefaultMsgId expected.
     **/
    public PDUOperationPropertiesBuilder setSmDefaultMsgId(final byte smDefaultMsgId) {
        this.smDefaultMsgId = smDefaultMsgId;
        return this;
    }


    /**
     *  * Setter for receiveTimeout.
     * @param receiveTimeout expected.
     **/
    public PDUOperationPropertiesBuilder setReceiveTimeout(final long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
        return this;
    }


    /**
     * Create a {@link PDUOperationProperties} intance.
     * @return new {@link PDUOperationProperties}.
     */
    public PDUOperationProperties build() {
        return new PDUOperationProperties(this);
    }

}