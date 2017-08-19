/*
 *      File: SmppPDUOperations.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.operation;

/**
 * Available operations for the user to execute in the SMPP protocol.
 *
 *  <ul>
 *  <li>{@link #SUBMIT_SMS}</li>
 *  <li>{@link #SUBMIT_SMS_MULTI}</li>
 *  <li>{@link #DATA}</li>
 *  <li>{@link #QUERY}</li>
 *  <li>{@link #REPLACE}</li>
 *  <li>{@link #CANCEL}</li>
 *  <li>{@link #ENQUIRE}</li>
 *  <li>{@link #RECEIVE}</li>
 *  </ul>
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public enum PDUOperationTypes {
    /**
     * Used to send SMS message to a device.
     */
    SUBMIT_SMS(1, "send SMS message to a device"),
    /**
     * Used to send SMS message.
     */
    SUBMIT_SMS_MULTI(2, ""),
    /**
     * Is an alternative to the <code>SUBMIT_SMS</code>.
     */
    DATA(3, "send Data message to a device"),
    /**
     * Used to fetch information about status of already submitted message.
     */
    QUERY(4, "fetch information about status of already submitted message"),
    /**
     * Used to replace certain attributes of already submitted message.
     */
    REPLACE(5, "replace certain attributes of already submitted message"),
    /**
     * Used to cancel an already submitted message.
     */
    CANCEL(6, "cancel an already submitted message."),
    /**
     * Is used to check if the SMCS application of the other party is alive.
     */
    ENQUIRE(7, "check if the SMCS application of the other party is alive."),
    /**
     * Receives one PDU of any type from SMSC.
     */
    RECEIVE(8, "receives one PDU of any type from SMSC.");

    /**
     * Operation Id.
     */
    private Integer operationId;
    /**
     * Operation description.
     */
    private String  description;

    /**  Creates an instance of AvailableOperations.
     *
     * @param operationId - operation identifier.
     * @param description - operation description.
     */
    PDUOperationTypes(final Integer operationId, final String description) {
        this.operationId = operationId;
        this.description = description;
    }

    /**
     * Return the SMPP Operation of the argument <code>operationId</code>.
     * @param operationId a <code>Integer</code> representing the SMPP Operation {@link PDUOperationTypes}
     * @return {@link PDUOperationTypes}
     */
    public static PDUOperationTypes valueOf(final Integer operationId) {
        for (PDUOperationTypes availableOperations : values()) {
            if (availableOperations.getOperationId().equals(operationId)) {
                return availableOperations;
            }
        }
        throw new IllegalArgumentException(
                "operation id '" + operationId + "' is invalid to get enum PDUOperationTypes");
    }


    /**
     * Getter for operationId.
     * @return operationId.
     **/
    public Integer getOperationId() {
        return operationId;
    }


    /**
     * Getter for description.
     * @return description.
     **/
    public String getDescription() {
        return description;
    }

}
