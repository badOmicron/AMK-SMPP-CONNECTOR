/*
 *      File: PDUOperation.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp;

import java.io.Serializable;

/**
 * A class that represents a PDU operationType over an SMMP protocol.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class PDUOperation implements Serializable {

    /**
     * Type of operationType to be performed with the SMSC.
     */
    private PDUOperationTypes      operationType;
    /**
     * Some of the variables necessary to be able to communicate through the SMPP protocol.
     */
    private PDUOperationProperties operationProps;
    /**
     * Type of bind required for operationType.
     */
    private SmppBindTypes          bindType;
    /**
     * ID of the submitted message. It may be used at a later stage to perform subsequent operations on the message.
     */
    private String                 messageId;
    /**
     * Provides encapsulation of message data with optional message encoding.
     */
    private String                 smsMessage;
    /**
     * # This is receiving mode. If set to sync, then the application
     # waits for response after sending a request pdu. If set to sync,
     # the application doesn't wait for responses, rather they are passed to
     # and implementation of ServerPDUListener by the Receiver.
     # The listener is also passed every request pdu received from the smsc.
     # Possible values are "sync" and "async"
     */
    private boolean asynchronous = false;


    /**
     * Getter for operationType.
     * @return operationType.
     **/
    public PDUOperationTypes getOperationType() {
        return operationType;
    }

    /**
     * Setter for operationType.
     * @param operationType expected.
     **/
    public void setOperationType(final PDUOperationTypes operationType) {
        this.operationType = operationType;
    }

    /**
     * Getter for operationProps.
     * @return operationProps.
     **/
    public PDUOperationProperties getOperationProps() {
        return operationProps;
    }

    /**
     * Setter for operationProps.
     * @param operationProps expected.
     **/
    public void setOperationProps(final PDUOperationProperties operationProps) {
        this.operationProps = operationProps;
    }

    /**
     * Getter for bindType.
     * @return bindType.
     **/
    public SmppBindTypes getBindType() {
        return bindType;
    }

    /**
     * Setter for bindType.
     * @param bindType expected.
     **/
    public void setBindType(final SmppBindTypes bindType) {
        this.bindType = bindType;
    }


    /**
     * Getter for messageId.
     * @return messageId.
     **/
    public String getMessageId() {
        return messageId;
    }

    /**
     * Setter for messageId.
     * @param messageId expected.
     **/
    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    /**
     * Getter for smsMessage.
     * @return smsMessage.
     **/
    public String getSmsMessage() {
        return smsMessage;
    }

    /**
     * Setter for smsMessage.
     * @param smsMessage expected.
     **/
    public void setSmsMessage(final String smsMessage) {
        this.smsMessage = smsMessage;
    }

    /**
     * Getter for asynchronous.
     * @return asynchronous.
     **/
    public boolean isAsynchronous() {
        return asynchronous;
    }

    /**
     * Setter for asynchronous.
     * @param asynchronous expected.
     **/
    public void setAsynchronous(final boolean asynchronous) {
        this.asynchronous = asynchronous;
    }
}
