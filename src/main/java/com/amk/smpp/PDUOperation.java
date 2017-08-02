/*
 *      File: PDUOperation.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
    private BindingType            bindingType;

    /**
     * Provides encapsulation of message data with optional message encoding.
     */
    private Message smsMessage;
    /**
     * # This is receiving mode. If set to sync, then the application
     # waits for response after sending a request pdu. If set to sync,
     # the application doesn't wait for responses, rather they are passed to
     # and implementation of ServerPDUListener by the Receiver.
     # The listener is also passed every request pdu received from the smsc.
     # Possible values are "sync" and "async"
     */
    private boolean asynchronous = false;

    private PDUOperation(final Builder builder) {
        setOperationType(builder.operationType);
        setOperationProps(builder.operationProps);
        setBindingType(builder.bindingType);
        setSmsMessage(builder.smsMessage);
        setAsynchronous(builder.asynchronous);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(@NotNull final PDUOperation copy) {
        Builder builder = new Builder();
        builder.operationType = copy.operationType;
        builder.operationProps = copy.operationProps;
        builder.bindingType = copy.bindingType;
        builder.smsMessage = copy.smsMessage;
        builder.asynchronous = copy.asynchronous;
        return builder;
    }


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
     * Getter for bindingType.
     * @return bindingType.
     **/
    public BindingType getBindingType() {
        return bindingType;
    }

    /**
     * Setter for bindingType.
     * @param bindingType expected.
     **/
    public void setBindingType(final BindingType bindingType) {
        this.bindingType = bindingType;
    }


    /**
     * Getter for smsMessage.
     * @return smsMessage.
     **/
    public Message getSmsMessage() {
        return smsMessage;
    }

    /**
     * Setter for smsMessage.
     * @param smsMessage expected.
     **/
    public void setSmsMessage(final Message smsMessage) {
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


    /**
     * {@code PDUOperation} builder static inner class.
     */
    public static final class Builder {
        private PDUOperationTypes      operationType;
        private PDUOperationProperties operationProps;
        private BindingType            bindingType;
        private Message                smsMessage;
        private boolean asynchronous = false;

        private Builder() {
        }

        /**
         * Sets the {@code operationType} and returns a reference to this Builder so that the methods can be chained together.
         * @param operationType the {@code operationType} to set
         * @return a reference to this Builder
         */
        @NotNull
        public Builder withOperationType(@NotNull final PDUOperationTypes operationType) {
            this.operationType = operationType;
            return this;
        }

        /**
         * Sets the {@code operationProps} and returns a reference to this Builder so that the methods can be chained together.
         * @param operationProps the {@code operationProps} to set
         * @return a reference to this Builder
         */
        @NotNull
        public Builder withOperationProps(@NotNull final PDUOperationProperties operationProps) {
            this.operationProps = operationProps;
            return this;
        }

        /**
         * Sets the {@code bindingType} and returns a reference to this Builder so that the methods can be chained together.
         * @param bindingType the {@code bindingType} to set
         * @return a reference to this Builder
         */
        @NotNull
        public Builder withBindingType(@NotNull final BindingType bindingType) {
            this.bindingType = bindingType;
            return this;
        }

        /**
         * Sets the {@code smsMessage} and returns a reference to this Builder so that the methods can be chained together.
         * @param smsMessage the {@code smsMessage} to set
         * @return a reference to this Builder
         */
        @NotNull
        public Builder withSmsMessage(@NotNull final Message smsMessage) {
            this.smsMessage = smsMessage;
            return this;
        }

        /**
         * Sets the {@code asynchronous} and returns a reference to this Builder so that the methods can be chained together.
         * @param asynchronous the {@code asynchronous} to set
         * @return a reference to this Builder
         */
        @NotNull
        public Builder withAsynchronous(final boolean asynchronous) {
            this.asynchronous = asynchronous;
            return this;
        }

        /**
         * Returns a {@code PDUOperation} built from the parameters previously set.
         *
         * @return a {@code PDUOperation} built with parameters of this {@code PDUOperation.Builder}
         */
        @NotNull
        public PDUOperation build() {
            return new PDUOperation(this);
        }
    }
}
