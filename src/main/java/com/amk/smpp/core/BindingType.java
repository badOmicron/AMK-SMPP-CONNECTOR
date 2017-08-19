/*
 *      File: SmppBindTypes.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.core;


/**
 * An ESME needs to be bounded to an SMCS to be able to initiate communication, so it is necessary to specify what type
 * of link you want to perform: TX, RX or TRX.
 *
 *  <ul>
 *  <li>{@link #TX}</li>
 *  <li>{@link #RX}</li>
 *  <li>{@link #TRX}</li>
 *  </ul>
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public enum BindingType {
    /**
     * A connected ESME has requested to bind as a Transmitter.
     */
    TX(1, "An ESME bound as a transmitter may send short messages to a Message Center"),
    /**
     * A connected ESME has requested to bind as a Receive.
     */
    RX(2, "An ESME bound as a receiver may receive short messages from a Message Center."),
    /**
     * A connected ESME has requested to bind as a Transceiver.
     */
    TRX(3,
            "An ESME bound as a Transceiver is authorised to use all operations supported by a Transmitter ESME and a Receiver ESME.");

    /**
     * Bind Type Id.
     */
    private int    typeId;
    /**
     * Bind description.
     */
    private String description;

    /**  Creates an instance of AvailableOperations.
     *
     * @param typeId - bind identifier.
     * @param description - operation description.
     */
    BindingType(final Integer typeId, final String description) {
        this.typeId = typeId;
        this.description = description;
    }

    /**
     * Return the SMPP Operation of the argument <code>operationId</code>.
     * @param bindTypeId a <code>Integer</code> representing the SMPP Operation {@link com.amk.smpp.operation.PDUOperationTypes}
     * @return {@link com.amk.smpp.operation.PDUOperationTypes}
     */
    public static BindingType valueOf(final Integer bindTypeId) {
        for (BindingType availableBindingTypes : values()) {
            if (availableBindingTypes.getBindId() == bindTypeId) {
                return availableBindingTypes;
            }
        }
        throw new IllegalArgumentException(
                "type id '" + bindTypeId + "' is invalid to get enum BindingType");
    }


    /**
     * Getter for bindTypeId.
     * @return operationId.
     **/
    public int getBindId() {
        return typeId;
    }


    /**
     * Getter for description.
     * @return description.
     **/
    public String getDescription() {
        return description;
    }
}
