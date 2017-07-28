/*
 *      File: SmppGovernable.java
 *    Author: Orlando Ramos <orlando.ramos@amk-technologies.com>
 *      Date: Jul 21, 2017
 * Copyright: AMK Technologies, S.A. de C.V. 2017
 */

package com.amk.smpp.rules;

/**
 * Contract that defines the behavior for the classes of type Rule that help to validate some characteristics of the
 * operations SMPP before being executed.
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @param <E> Type of object expected to evaluate.
 * @since 1.0.0
 */
public interface Ruleable< E > {
    /**
     * Execute object validation.
     * @param object expected.
     */
    void validate(E object);
}
