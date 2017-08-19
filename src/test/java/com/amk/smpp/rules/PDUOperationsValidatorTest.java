package com.amk.smpp.rules;

import org.junit.Assert;
import org.junit.Test;

import com.amk.smpp.core.BindingType;
import com.amk.smpp.operation.PDUOperation;
import com.amk.smpp.operation.PDUOperationTypes;

/** TODO Descripción de las responsabilidades de la clase, patrones utilizados, algoritmos utilizados.
 *
 * @author Orlando Ramos &lt;orlando.ramos@amk-technologies.com&gt;
 * @version 1.0.0
 * @since 1.0.0
 */
public class PDUOperationsValidatorTest {
    @Test
    public void validNotNull() throws Exception {
        try {
            PDUOperationsValidator.validNotNull(null);
            Assert.fail();
        } catch (Exception e) {
            //error expected
        }
    }

    @Test
    public void validNotEmpty() throws Exception {
        PDUOperation send = PDUOperation.newBuilder()
                .withAsynchronous(true)
                .withSmsMessage(null)
                .withBindingType(BindingType.RX)
                .withOperationType(PDUOperationTypes.DATA)
                .build();
        try {
            PDUOperationsValidator.validNotEmpty(send);
            Assert.fail();
        } catch (Exception e) {
            //error expected
        }

        PDUOperation receive = PDUOperation.newBuilder()
                .withAsynchronous(true)
                .withSmsMessage(null)
                .withBindingType(null)
                .withOperationType(PDUOperationTypes.RECEIVE)
                .build();
        try {
            PDUOperationsValidator.validNotEmpty(receive);
            Assert.fail();
        } catch (Exception e) {
            //error expected
        }

    }

}